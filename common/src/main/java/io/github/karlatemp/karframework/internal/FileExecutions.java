package io.github.karlatemp.karframework.internal;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import io.github.karlatemp.karframework.IPluginProvider;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.logging.Level;

@SuppressWarnings("UnusedReturnValue")
public class FileExecutions {
    @SuppressWarnings("UnstableApiUsage")
    public static boolean store(
            @NotNull IPluginProvider provider,
            @NotNull File file,
            @NotNull String path,
            boolean force
    ) {
        if (!force && file.exists()) return true;
        final InputStream resource = provider.getResource(path);
        if (resource == null) {
            provider.getLogger().warning("Failed save `" + path + "`: Resource missing.");
            return false;
        }
        try {
            Files.createParentDirs(file);
            try (InputStream from = resource;
                 OutputStream target = new BufferedOutputStream(new FileOutputStream(file))) {
                ByteStreams.copy(from, target);
                return true;
            }
        } catch (IOException exception) {
            provider.getLogger().log(Level.SEVERE, "Failed to save resource `" + path + "`", exception);
            return false;
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    public static void backup(File file) throws IOException {
        String name = file.getName();
        File dir = file.getParentFile();
        if (dir == null) {
            dir = new File(".");
        }
        String ne = Files.getNameWithoutExtension(name);
        String suffix = Files.getFileExtension(name);

        File target = new File(dir, ne + ".bak." + suffix);

        int counter = 1;
        while (target.exists()) {
            target = new File(dir, ne + ".bak" + (counter++) + '.' + suffix);
        }
        Files.move(file, target);
    }
}
