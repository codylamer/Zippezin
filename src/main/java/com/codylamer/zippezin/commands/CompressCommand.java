package com.codylamer.zippezin.commands;

import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.scripts.commands.Holdable;
import com.denizenscript.denizencore.scripts.commands.generator.ArgName;
import com.denizenscript.denizencore.scripts.commands.generator.ArgPrefixed;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.DenizenCore;

import java.io.*;
import java.nio.file.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CompressCommand extends AbstractCommand implements Holdable {

    public CompressCommand() {
        setName("compress");
        setSyntax("compress [origin:<origin>] [destination:<destination>] (overwrite)");
        setRequiredArguments(2, 3);
        addRemappedPrefixes("origin", "o");
        addRemappedPrefixes("destination", "d");
        autoCompile();
    }

    // <--[command]
    // @Name Compress
    // @Syntax compress [origin:<origin>] [destination:<destination>] (overwrite)
    // @Required 2
    // @Maximum 3
    // @Short Compresses a folder or its contents into a zip archive.
    // @Group file
    //
    // @Description
    // Compresses the specified folder or its contents (if origin ends with a slash) into a zip file.
    //
    // Destination path should end with ".zip". If a file already exists, it must be overwritten or the command fails.
    //
    // @Tags
    // <entry[saveName].success> returns whether the compression succeeded.
    //
    // @Usage
    // - ~compress o:plugins/MyPlugin/ d:data/myplugin.zip overwrite save:zip
    // - narrate "Zip success<&co> <entry[zip].success>"
    // -->

    public static void autoExecute(final ScriptEntry scriptEntry,
                                   @ArgPrefixed @ArgName("origin") final String origin,
                                   @ArgPrefixed @ArgName("destination") final String destination,
                                   @ArgName("overwrite") final boolean overwrite) {

        File source = new File(origin);
        File zipFile = new File(destination);

        if (!source.exists()) {
            Debug.echoError(scriptEntry, "Compression failed: origin does not exist!");
            scriptEntry.saveObject("success", new ElementTag("false"));
            scriptEntry.setFinished(true);
            return;
        }

        if (zipFile.exists() && !overwrite) {
            Debug.echoError(scriptEntry, "Compression failed: destination zip already exists and overwrite is false.");
            scriptEntry.saveObject("success", new ElementTag("false"));
            scriptEntry.setFinished(true);
            return;
        }

        Runnable runme = () -> {
            try {
                if (zipFile.exists() && !zipFile.delete()) {
                    Debug.echoError(scriptEntry, "Failed to delete existing destination file.");
                    scriptEntry.saveObject("success", new ElementTag("false"));
                    scriptEntry.setFinished(true);
                    return;
                }
                if (!zipFile.getParentFile().exists() && !zipFile.getParentFile().mkdirs()) {
                    Debug.echoError(scriptEntry, "Failed to create parent directories for destination.");
                    scriptEntry.saveObject("success", new ElementTag("false"));
                    scriptEntry.setFinished(true);
                    return;
                }

                try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
                    Path basePath = source.toPath();
                    boolean includeRoot = !origin.endsWith("/") && source.isDirectory();

                    Files.walk(source.toPath())
                            .filter(path -> !Files.isDirectory(path))
                            .forEach(path -> {
                                try {
                                    Path relative = includeRoot ? basePath.getParent().relativize(path) : basePath.relativize(path);
                                    ZipEntry zipEntry = new ZipEntry(relative.toString().replace("\\", "/"));
                                    zos.putNextEntry(zipEntry);
                                    Files.copy(path, zos);
                                    zos.closeEntry();
                                } catch (IOException e) {
                                    Debug.echoError(scriptEntry, "Failed to zip entry: " + path + " - " + e.getMessage());
                                }
                            });
                }

                scriptEntry.saveObject("success", new ElementTag("true"));
                scriptEntry.setFinished(true);

            } catch (Exception e) {
                Debug.echoError(scriptEntry, e);
                scriptEntry.saveObject("success", new ElementTag("false"));
                scriptEntry.setFinished(true);
            }
        };

        if (scriptEntry.shouldWaitFor()) {
            DenizenCore.runAsync(runme);
        } else {
            runme.run();
        }
    }
}
