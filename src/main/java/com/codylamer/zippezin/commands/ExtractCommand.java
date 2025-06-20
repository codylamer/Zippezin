package com.codylamer.zippezin.commands;

import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.scripts.commands.Holdable;
import com.denizenscript.denizencore.scripts.commands.generator.ArgName;
import com.denizenscript.denizencore.scripts.commands.generator.ArgPrefixed;
import com.denizenscript.denizencore.utilities.debugging.Debug;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.DenizenCore;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipFile;

public class ExtractCommand extends AbstractCommand implements Holdable {

    public ExtractCommand() {
        setName("extract");
        setSyntax("extract [origin:<origin>] [destination:<destination>] (overwrite)");
        setRequiredArguments(2, 3);
        addRemappedPrefixes("origin", "o");
        addRemappedPrefixes("destination", "d");
        autoCompile();
    }

    private static boolean isZipArchive(File file) {
        return file.getName().endsWith(".zip");
    }

    // <--[command]
    // @Name Extract
    // @Syntax extract [origin:<origin>] [destination:<destination>] (overwrite)
    // @Required 2
    // @Maximum 3
    // @Short Extracts the contents of a zip archive to the specified location.
    // @Group file
    //
    // @Description
    // Extracts the contents of a zip archive from one location to another.
    //
    // If a destination file already exists, it can be overwritten with the "overwrite" argument.
    //
    // @Tags
    // <entry[saveName].success> returns whether the extraction succeeded (if not, an error occurred).
    //
    // @Usage
    // Use to extract a zip archive to a destination folder, overwriting if necessary.
    // - ~extract o:data/archive.zip d:data/extracted/ overwrite save:extract
    // - narrate "Extraction success<&co> <entry[extract].success>"
    // -->

    public static void autoExecute(final ScriptEntry scriptEntry,
                                   @ArgPrefixed @ArgName("origin") final String origin,
                                   @ArgPrefixed @ArgName("destination") final String destination,
                                   @ArgName("overwrite") final boolean overwrite) {

        File o = new File(origin);
        File d = new File(destination);
        boolean dexists = d.exists();
        boolean disdir = d.isDirectory() || destination.endsWith("/");

        if (!o.exists()) {
            Debug.echoError(scriptEntry, "File extraction failed, origin does not exist!");
            scriptEntry.saveObject("success", new ElementTag("false"));
            scriptEntry.setFinished(true);
            return;
        }

        if (!isZipArchive(o)) {
            Debug.echoError(scriptEntry, "The origin file is not a valid zip archive!");
            scriptEntry.saveObject("success", new ElementTag("false"));
            scriptEntry.setFinished(true);
            return;
        }

        if (dexists && !disdir && !overwrite) {
            Debug.echoDebug(scriptEntry, "File extraction ignored, destination file already exists!");
            scriptEntry.saveObject("success", new ElementTag("false"));
            scriptEntry.setFinished(true);
            return;
        }

        Runnable runme = () -> {
            try {
                if (dexists && !disdir) {
                    if (!d.delete()) {
                        Debug.echoError(scriptEntry, "Failed to delete existing destination file: " + d.getAbsolutePath());
                        scriptEntry.saveObject("success", new ElementTag("false"));
                        scriptEntry.setFinished(true);
                        return;
                    }
                }

                if (disdir && !dexists) {
                    if (!d.mkdirs()) {
                        Debug.echoError(scriptEntry, "Failed to create destination directories: " + d.getAbsolutePath());
                        scriptEntry.saveObject("success", new ElementTag("false"));
                        scriptEntry.setFinished(true);
                        return;
                    }
                } else if (!dexists && !d.getParentFile().exists()) {
                    if (!d.getParentFile().mkdirs()) {
                        Debug.echoError(scriptEntry, "Failed to create parent directories for destination: " + d.getAbsolutePath());
                        scriptEntry.saveObject("success", new ElementTag("false"));
                        scriptEntry.setFinished(true);
                        return;
                    }
                }

                try (ZipFile zipFile = new ZipFile(o)) {
                    zipFile.stream().forEach(entry -> {
                        Path entryPath = Paths.get(d.getAbsolutePath(), entry.getName());
                        try {
                            if (entry.isDirectory()) {
                                Files.createDirectories(entryPath);
                            } else {
                                Files.createDirectories(entryPath.getParent());
                                Files.copy(zipFile.getInputStream(entry), entryPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                            }
                        } catch (Exception e) {
                            Debug.echoError(scriptEntry, "Failed to extract entry: " + entry.getName() + " - " + e.getMessage());
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
