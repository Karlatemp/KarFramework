/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/06 22:34:57
 *
 * kar-framework/kar-framework.common.main/GroovyScriptManager.java
 */

package io.github.karlatemp.karframework.groovy;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import groovy.lang.GroovyClassLoader;
import io.github.karlatemp.karframework.IKarFramework;
import io.github.karlatemp.karframework.IPluginProvider;
import io.github.karlatemp.karframework.io.RandomAccessFileOutputStream;
import org.codehaus.groovy.jsr223.GroovyScriptEngineImpl;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@SuppressWarnings("UnstableApiUsage")
public class GroovyScriptManager {
    private final IPluginProvider provider;
    private final GroovyScriptEngineImpl engine;
    private final Map<String, GroovyScript> scripts = new HashMap<>();
    private final File dir;
    private final IKarFramework framework;
    private final Logger logger;

    public GroovyScriptManager(
            @NotNull IPluginProvider provider,
            @NotNull IKarFramework framework,
            @NotNull Logger logger
    ) {
        this.provider = provider;
        this.framework = framework;
        this.logger = logger;
        engine = new GroovyScriptEngineImpl(
                new GroovyClassLoader(
                        GroovyScriptManager.class.getClassLoader()
                )
        );
        this.dir = new File(provider.getPluginDataFolder(), "scripts");
    }

    public File getDir() {
        return dir;
    }

    public Logger getLogger() {
        return logger;
    }

    public void invalidate() {
        scripts.clear();
    }

    public GroovyScript loadScript(String scriptPath) {
        File file = new File(dir, scriptPath);
        scriptPath = scriptPath.replace('\\', '/');
        if (scriptPath.startsWith("/") || scriptPath.startsWith("../") || scriptPath.contains("/../")) {
            return null; // Illegal access
        }
        if (file.isFile()) {
            Map<String, GroovyScript> scripts;
            synchronized (scripts = this.scripts) {
                GroovyScript script = scripts.get(scriptPath);
                if (script != null) {
                    return script;
                }
                try (Reader reader = Files.newReader(file, StandardCharsets.UTF_8)) {
                    GroovyScript gs = new GroovyScript(reader, provider, framework, this, scriptPath);
                    scripts.put(scriptPath, gs);
                    return gs;
                } catch (Throwable any) {
                    provider.getLogger().log(Level.SEVERE, "Exception in loading script", any);
                }
            }
        }
        return null;
    }

    public GroovyScriptEngineImpl getEngine() {
        return engine;
    }

    public Map<String, GroovyScript> getScripts() {
        return scripts;
    }

    public void release() {
        File update = new File(provider.getPluginDataFolder(), "groovy-update");
        boolean needUpdate;
        if (!update.isFile() || !dir.isDirectory()) {
            needUpdate = true;
        } else {
            try {
                long current = Long.parseLong(Files.readFirstLine(update, StandardCharsets.UTF_8));
                long packing;
                try (BufferedReader latest = new BufferedReader(
                        new InputStreamReader(
                                GroovyScriptManager.class.getResourceAsStream(
                                        "/karframework/groovy.txt"
                                )
                        ))
                ) {
                    packing = Long.parseLong(latest.readLine());
                }
                needUpdate = current < packing;
            } catch (Throwable ignored) {
                needUpdate = true;
            }
        }
        if (needUpdate) {
            try (ZipInputStream zip = new ZipInputStream(GroovyScriptManager.class.getResourceAsStream(
                    "/karframework/groovy.zip"
            ))) {
                Files.write(String.valueOf(System.currentTimeMillis()).getBytes(), update);
                while (true) {
                    ZipEntry entry = zip.getNextEntry();
                    if (entry == null) break;
                    File f = new File(dir, entry.getName());
                    if (entry.isDirectory()) {
                        f.mkdirs();
                    } else {
                        boolean override = !f.isFile() ||
                                !(entry.getName().equals("startup.groovy") || entry.getName().startsWith("example"));
                        if (override) {
                            Files.createParentDirs(f);
                            try (RandomAccessFileOutputStream raf = new RandomAccessFileOutputStream(
                                    f
                            )) {
                                ByteStreams.copy(zip, raf);
                            }
                        }
                    }
                }
            } catch (IOException ioException) {
                provider.getLogger().log(Level.SEVERE, "Exception in releasing groovy sdk", ioException);
            }
        }
    }
}
