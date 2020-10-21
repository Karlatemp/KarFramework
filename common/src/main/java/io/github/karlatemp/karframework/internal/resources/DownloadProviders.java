/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/10/21 05:34:19
 *
 * kar-framework/kar-framework.common.main/DownloadProviders.java
 */

package io.github.karlatemp.karframework.internal.resources;

import com.google.common.io.Files;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.karlatemp.karframework.IPluginProvider;
import io.github.karlatemp.karframework.annotation.InternalAPI;
import io.github.karlatemp.karframework.annotation.Warning;
import io.github.karlatemp.karframework.internal.Internal;
import io.github.karlatemp.karframework.io.OpenByteArrayOutputStream;
import io.github.karlatemp.karframework.io.RandomAccessFileInputStream;
import io.github.karlatemp.karframework.io.RandomAccessFileOutputStream;
import io.github.karlatemp.karframework.utils.NetworkUtils;
import ninja.leaping.configurate.ValueType;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

@Warning({
        "WARNING: This class is internal class.",
        "This class will be CHANGE/REMOVE ANYTIME!",
        "DONT use this anywhere."
})
@InternalAPI
public /* internal */ class DownloadProviders {
    static final LinkedList<DownloadProvider> providers = new LinkedList<>();
    static final JsonParser JSON_PARSER = new JsonParser();
    static final Map<String, String> hashMapping = new HashMap<>();
    static final Map<String, Map<String, String>> languages = new HashMap<>();
    static String language, systemLanguage;
    static Map<String, String> selectLanguage;
    static File objects;


    public /* internal */ static void preInit() {
        providers.clear();
        hashMapping.clear();
        languages.clear();
        selectLanguage = null;
        language = null;
        systemLanguage = null;

        DownloadProvider
                mojang = new MojangDownloadProvider(),
                mcbbs = new BMCLAPIDownloadProvider("https://download.mcbbs.net"),
                bmclapi = new BMCLAPIDownloadProvider("https://bmclapi2.bangbang93.com");
        final CommentedConfigurationNode config = Internal.CONFIG.get();
        final ConfigurationLoader<? extends CommentedConfigurationNode> loader = Internal.CONFIG_LOADER.get();
        final CommentedConfigurationNode options = config.getNode("resource-downloading");
        boolean zh = Locale.getDefault().getLanguage().equalsIgnoreCase("zh");
        Logger logger = Internal.PLUGIN_PROVIDER.get().getLogger();

        if (options.isVirtual()) {
            options.setComment(zh ?
                    "这里是下载资源的设置, 在这里设置下载的源优先级, 代理等..." :
                    "Here is the settings for resource downloading\n" +
                            "You can set proxy or downloading priority."
            );
            options.setValue(new LinkedHashMap<>());
        }
        final CommentedConfigurationNode priority = options.getNode("priority");
        if (priority.isVirtual() || priority.getValueType() != ValueType.LIST) {
            priority.setValue(new LinkedList<>(zh ?
                    Arrays.asList("mcbbs", "bmclapi", "mojang") :
                    Collections.singletonList("mojang")
            ));
            priority.setComment(zh ?
                    "这里是资源下载优先级设置\n" +
                            "可用值: mcbbs, bmclapi, mojang" :
                    "Here is resource downloading priority.\n" +
                            "Allowed: mojang, mcbbs, bmclapi (For Chinese)"
            );
        }
        final List<String> list = priority.getList(Helper.transformer);
        for (String s : list) {
            switch (s.toLowerCase()) {
                case "mojang": {
                    providers.add(mojang);
                    break;
                }
                case "mcbbs": {
                    providers.add(mcbbs);
                    break;
                }
                case "bmclapi": {
                    providers.add(bmclapi);
                    break;
                }
                default: {
                    logger.warning("[ResourceProvider] Unknown provider: " + s + ", skipped");
                    break;
                }
            }
        }
        if (providers.isEmpty()) {
            logger.warning("[ResourceProvider] No provider setted. Using mojang download provider.");
            providers.add(mojang);
        }
        {
            StringBuilder lcb = new StringBuilder();
            Locale l = Locale.getDefault();
            lcb.append(l.getLanguage());
            final String country = l.getCountry();
            if (country != null && country.length() > 0) {
                lcb.append('_').append(country);
            }
            language = config.getNode("language").getString(systemLanguage = lcb.toString().toLowerCase());
        }
    }

    private static boolean isValidJson(File file) {
        try {
            parseFile(file);
            return true;
        } catch (Throwable ignored) {
        }
        return false;
    }

    static JsonElement parseFile(File file, String alias) throws IOException {
        try (Reader reader = new BufferedReader(new InputStreamReader(
                new BufferedInputStream(
                        new FileInputStream(file)
                ),
                StandardCharsets.UTF_8
        ))) {
            return JSON_PARSER.parse(reader);
        } catch (Throwable any) {
            if (alias != null) {
                throw new IOException("Exception in parsing " + alias + " - " + file, any);
            }
            throw new IOException("Exception in parsing " + file, any);
        }
    }

    static JsonElement parseFile(File file) throws IOException {
        return parseFile(file, null);
    }

    @SuppressWarnings("UnstableApiUsage")
    public /* internal */ static void init() {
        String verx = MinecraftFramework.minecraftFramework.getMinecraftVersion();

        IPluginProvider pprovider = Internal.PLUGIN_PROVIDER.get();
        final ScheduledExecutorService executor = Executors.newScheduledThreadPool(3, new ThreadFactory() {
            private final AtomicInteger counter = new AtomicInteger();
            private final ThreadGroup group = new ThreadGroup("KarFramework - Resource Download Group");

            @Override
            public Thread newThread(@NotNull Runnable r) {
                return new Thread(group, r, "KarFramework Resource Download Thread #" + counter.getAndIncrement());
            }
        });
        Logger logger = pprovider.getLogger();
        class Downloader {
            private String vw(String tx) {
                while (!tx.isEmpty()) {
                    if (tx.charAt(0) != '0') {
                        return tx;
                    }
                    tx = tx.substring(1);
                }
                return tx;
            }

            final char[] cr = "0123456789abcdef".toCharArray();

            String toHex(byte[] ax) {
                StringBuilder sb = new StringBuilder(ax.length * 2);
                for (byte b : ax) {
                    sb.append(cr[(b >> 4) & 0xF]);
                    sb.append(cr[b & 0xF]);
                }
                return sb.toString();
            }

            boolean checkSHA1(DownloadingTask downloadingTask) {
                try {
                    final MessageDigest instance = MessageDigest.getInstance("SHA-1");
                    byte[] buffer = new byte[1024];
                    try (RandomAccessFileInputStream raf = new RandomAccessFileInputStream(downloadingTask.file)) {
                        while (true) {
                            int len = raf.read(buffer);
                            if (len == -1) break;
                            instance.update(buffer, 0, len);
                        }
                    }
                    String s1 = vw(toHex(instance.digest()));
                    String s2 = vw(downloadingTask.sha1);
                    boolean result = s1.equalsIgnoreCase(s2);
                    if (!result) {
                        logger.warning(" ERROR: SHA-1 Not Match. require " + s2 + ", but got " + s1 + ", " + downloadingTask.resName);
                    }
                    return result;
                } catch (Throwable e) {
                    e.printStackTrace();
                    return false;
                }
            }

            void register(DownloadingTask downloadingTask) {
                if (downloadingTask.file.isFile()) {
                    if (downloadingTask.file.length() == downloadingTask.size) {
                        if (checkSHA1(downloadingTask)) {
                            downloadingTask.future.complete(null);
                            return;
                        }
                    }
                }
                executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (downloadingTask.future.isDone())
                                return;
                            logger.info("OPEN CONNECT " + downloadingTask.url + " - " + downloadingTask.resName);
                            for (DownloadProvider provider : providers) {
                                final String url = provider.injectURL(downloadingTask.url);
                                if (!url.equals(downloadingTask.url)) {
                                    logger.info("        ---- " + url);
                                }
                                OpenByteArrayOutputStream data;
                                try {
                                    data = NetworkUtils.doGetB(NetworkUtils.toURL(url));
                                } catch (IOException ioException) {
                                    logger.log(Level.WARNING, "Exception in downloading version load with " + provider + ": " + ioException);
                                    continue;
                                }
                                Files.createParentDirs(downloadingTask.file);
                                try (RandomAccessFileOutputStream bos = new RandomAccessFileOutputStream(downloadingTask.file)) {
                                    data.writeTo(bos);
                                }
                                if (!checkSHA1(downloadingTask)) {
                                    executor.submit(this);
                                    return;
                                }
                                logger.info("  DOWNLOADED " + downloadingTask.url + " - " + downloadingTask.resName);
                                downloadingTask.future.complete(null);
                                return;
                            }
                            executor.submit(this);
                        } catch (Throwable any) {
                            any.printStackTrace();
                            downloadingTask.future.completeExceptionally(any);
                        }
                    }
                });
            }
        }
        Downloader downloader = new Downloader();
        try {

            // region downloading version

            // Found version file
            File version = new File(pprovider.getPluginDataFolder(), "version-" + verx + ".json");
            if (!version.isFile() || !isValidJson(version)) {
                String versions;
                top:
                while (true) {
                    for (DownloadProvider provider : providers) {
                        final String url = provider.injectURL(provider.getVersionListURL());
                        String data;
                        try {
                            data = NetworkUtils.doGet(NetworkUtils.toURL(url));
                        } catch (IOException ioException) {
                            logger.warning("Exception in downloading version load with " + pprovider);
                            continue;
                        }
                        versions = data;
                        break top;
                    }
                    try {
                        //noinspection BusyWait
                        Thread.sleep(1000L);
                    } catch (InterruptedException ignore) {
                    }
                }
                final JsonObject object = JSON_PARSER.parse(versions).getAsJsonObject();
                String url = null;
                for (JsonElement elm : object.getAsJsonArray("versions")) {
                    JsonObject ver = elm.getAsJsonObject();
                    if (ver.getAsJsonPrimitive("id").getAsString().equals(verx)) {
                        url = ver.getAsJsonPrimitive("url").getAsString();
                        break;
                    }
                }
                if (url == null)
                    throw new AssertionError("No url found with version " + verx);
                top:
                while (true) {
                    for (DownloadProvider provider : providers) {
                        OpenByteArrayOutputStream data;
                        try {
                            data = NetworkUtils.doGetB(NetworkUtils.toURL(provider.injectURL(url)));
                        } catch (IOException ioException) {
                            logger.warning("Exception in downloading version load with " + pprovider);
                            continue;
                        }
                        try {
                            Files.write(data.toByteArray(), version);
                        } catch (IOException ioException) {
                            throw new RuntimeException("Exception in saving versions.");
                        }
                        break top;
                    }
                    try {
                        //noinspection BusyWait
                        Thread.sleep(1000L);
                    } catch (InterruptedException ignore) {
                    }
                }
            }

            JsonObject assetsJsonObject = parseFile(version).getAsJsonObject();
            JsonObject assetIndex = assetsJsonObject.getAsJsonObject("assetIndex");
            File assets = new File(pprovider.getPluginDataFolder(), "index-" + assetIndex.getAsJsonPrimitive("id").getAsString() + ".json");
            // endregion

            // region downloading assets
            DownloadingTask assertDownloadTask = new DownloadingTask(
                    assetIndex.getAsJsonPrimitive("sha1").getAsString(),
                    assetIndex.getAsJsonPrimitive("url").getAsString(),
                    assets,
                    assetIndex.getAsJsonPrimitive("size").getAsLong(),
                    "minecraft indexes."
            );
            downloader.register(assertDownloadTask);
            assertDownloadTask.future.get();
            // endregion

            // region download language files
            JsonObject jo = parseFile(assets).getAsJsonObject().getAsJsonObject("objects");
            LinkedList<DownloadingTask> tasks = new LinkedList<>();
            objects = new File(pprovider.getPluginDataFolder(), "objects");
            for (Map.Entry<String, JsonElement> entry : jo.entrySet()) {
                if (entry.getKey().startsWith("minecraft/lang/")) {
                    final JsonObject assetObj = entry.getValue().getAsJsonObject();
                    String hash = assetObj.getAsJsonPrimitive("hash").getAsString();

                    String hashX = hash.substring(0, 2) + '/' + hash;
                    hashMapping.put(entry.getKey(), hashX);

                    DownloadingTask task = new DownloadingTask(
                            hash,
                            "https://resources.download.minecraft.net/" + hashX,
                            new File(objects, hashX),
                            assetObj.getAsJsonPrimitive("size").getAsLong(),
                            entry.getKey()
                    );
                    downloader.register(task);
                    tasks.add(task);
                }
            }
            for (DownloadingTask t : tasks) t.future.get();
            // endregion

            for (Map.Entry<String, String> entry : hashMapping.entrySet()) {
                File file = new File(objects, entry.getValue());
                HashMap<String, String> hashMap = new HashMap<>();
                if (entry.getKey().endsWith(".lang")) {
                    Properties prop = new Properties();
                    try (Reader fr = Files.newReader(file, StandardCharsets.UTF_8)) {
                        prop.load(fr);
                    }
                    for (Map.Entry<Object, Object> entryZ : prop.entrySet()) {
                        hashMap.put(String.valueOf(entryZ.getKey()), String.valueOf(entryZ.getValue()));
                    }
                } else {
                    for (Map.Entry<String, JsonElement> entryZ : parseFile(file, entry.getKey()).getAsJsonObject().entrySet()) {
                        hashMap.put(entryZ.getKey(), entryZ.getValue().getAsString());
                    }
                }
                languages.put(entry.getKey(), hashMap);
            }
            String l = "minecraft/lang/" + language + ".json";
            String l2 = "minecraft/lang/" + language + ".lang";
            if ((selectLanguage = languages.entrySet()
                    .stream()
                    .filter(it -> it.getKey().equalsIgnoreCase(l) || it.getKey().equalsIgnoreCase(l2))
                    .findFirst()
                    .map(Map.Entry::getValue)
                    .orElse(null)) == null) {
                logger.warning("[Languages] Sorry, The translate resource of your selected language `" + l + "` not found. Please select the right language.");
                logger.warning("[Languages] Your Selected Language: " + language);
                logger.warning("[Languages] Your System   Language: " + systemLanguage);
            }
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }
    }
}