package net.wyvest.wychecker;

import club.sk1er.mods.core.util.JsonHolder;
import club.sk1er.mods.core.util.WebUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.common.Loader;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ModChecker {

    public JsonArray badModsFetched = new JsonArray();
    public JsonArray neededModsFetched = new JsonArray();

    public ArrayList<BadModMetadata> badMods = new ArrayList<>();
    public ArrayList<NeededModMetadata> neededMods = new ArrayList<>();

    File modsFolder = new File(Launch.minecraftHome, "mods");
    File[] coreModList = modsFolder.listFiles((dir, name) -> name.endsWith(".jar"));

    public void init() {
        JsonHolder main = WebUtil.fetchJSON("https://wyvest.net/checker/checkermain.json");
        badModsFetched = main.optJSONArray("bad_mods");
        neededModsFetched = main.optJSONArray("needed_mods");
        System.out.println("bad fetch: " + badModsFetched);
        System.out.println("need fetch: " + neededModsFetched);
        getBadMods();
        getNeededMods();
        System.out.println("bad: " + badMods);
        System.out.println("need: " + neededMods);
    }

    private void getBadMods() {
        try {
            if (!modsFolder.exists())
                modsFolder.mkdirs(); // This should never happen, but it's still a needed precaution.
            File[] coreModList = modsFolder.listFiles((dir, name) -> name.endsWith(".jar")); // Fetch every potential mod inside the mods folder.
            for (JsonElement b : badModsFetched) {
                String url = "https://wyvest.net/checker/" + b.getAsString() + ".json";
                JsonHolder mainObject = WebUtil.fetchJSON(url);
                BadModMetadata metadata = new BadModMetadata(mainObject.optString("name"), mainObject.optString("replacement_name"), mainObject.optString("id"), mainObject.optString("file"), mainObject.optString("replacement_download"), mainObject.optString("explanation")); //add all the new values from the json
                if (metadata.file == null) {
                    if (Loader.isModLoaded(metadata.id)) // Check if the mod is loaded.
                        badMods.add(metadata); // Add to the bad mods list if the mod is loaded.
                } else {
                    for (File file : coreModList) {
                        try {
                            try (ZipFile zipFile = new ZipFile(file)) {
                                if (zipFile.getEntry(metadata.file) != null)
                                    badMods.add(metadata);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
            if (is5zig()) badMods.add(new BadModMetadata("5zig Mod", "5zig Reborn", null, null, "https://github.com/5zig-reborn/The-5zig-Mod/releases/download/3.14.0/5zig-Reborn-1.8.9-3.14.0.jar", "The original 5zig Mod caused crashes and was badly coded overall, and now is abandoned.")); //This is hard-coded in because checking 5zig uses a different system.
        } catch (Exception e) {
            getBadMods(); //Recursively run until it works
        }

    }

    public boolean is5zig() {
        for (File file : coreModList) {
            try {
                try (ZipFile zipFile = new ZipFile(file)) {
                    ZipEntry entry = zipFile.getEntry("mcmod.info");
                    if (entry != null) {
                        try (InputStream inputStream = zipFile.getInputStream(entry)) {
                            byte[] availableBytes = new byte[inputStream.available()];
                            inputStream.read(availableBytes, 0, inputStream.available());
                            JsonObject modInfo = (new JsonParser()).parse(new String(availableBytes)).getAsJsonArray().get(0).getAsJsonObject();
                            if (modInfo.has("modid")) {
                                String modId = modInfo.get("modid").getAsString();
                                if (modId.equals("the5zigMod") && modInfo.has("url") && !modInfo.get("url").getAsString().equalsIgnoreCase("https://5zigreborn.eu")) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    public void getNeededMods() {
        // Read previous documentation.
        try {
            if (!modsFolder.exists())
                modsFolder.mkdirs();
            File[] coreModList = modsFolder.listFiles((dir, name) -> name.endsWith(".jar"));
            for (JsonElement n : neededModsFetched) {
                String url = "https://wyvest.net/checker/" + n.getAsString() + ".json";
                JsonHolder mainObject = WebUtil.fetchJSON(url);
                NeededModMetadata metadata = new NeededModMetadata(mainObject.optString("name"), mainObject.optString("id"), mainObject.optString("file"), mainObject.optString("download"), mainObject.optString("explanation"));
                if (metadata.file == null) {
                    if (!Loader.isModLoaded(metadata.id)) {
                        neededMods.add(metadata);
                    }
                } else {
                    for (File file : coreModList) {
                        try {
                            try (ZipFile zipFile = new ZipFile(file)) {
                                if (zipFile.getEntry(metadata.file) == null)
                                    neededMods.add(metadata);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        } catch (Exception e) {
            getNeededMods();
        }
    }

    public static class BadModMetadata {
        public final String name;
        public final String replacementName;
        public final String id;
        public final String file;
        public final String replacementDownload;
        public final String explanation;
        BadModMetadata(String name, String replacementName, String id, String file, String replacementDownload, String explanation) {
            this.name = name;
            this.replacementName = replacementName;
            this.id = id;
            this.file = file;
            this.replacementDownload = replacementDownload;
            this.explanation = explanation;
        }
    }
    public static class NeededModMetadata {
        public final String name;
        public final String id;
        public final String file;
        public final String download;
        public final String explanation;
        NeededModMetadata(String name, String id, String file, String download, String explanation) {
            this.name = name;
            this.id = id;
            this.file = file;
            this.download = download;
            this.explanation = explanation;
        }
    }

}
