package net.wyvest.wychecker.checker;

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

    public ModHandler handler = new ModHandler();
    public JsonArray badModsFetched = new JsonArray();
    public JsonArray neededModsFetched = new JsonArray();

    public ArrayList<String> badMods = new ArrayList<>();
    public ArrayList<String> neededMods = new ArrayList<>();

    File modsFolder = new File(Launch.minecraftHome, "mods");
    File[] coreModList = modsFolder.listFiles((dir, name) -> name.endsWith(".jar"));

    public void init() {
        handler.handleBadMods(badModsFetched);
        handler.handleNeededMods(neededModsFetched);

        getBadMods();
        getNeededMods();
    }

    private void getBadMods() {
        try {
            if (!modsFolder.exists())
                modsFolder.mkdirs(); // This should never happen, but it's still a needed precaution.
            File[] coreModList = modsFolder.listFiles((dir, name) -> name.endsWith(".jar")); // Fetch every potential mod inside the mods folder.
            for (JsonElement b : badModsFetched) {
                String url = "https://wyvest.net/checker/" + b.getAsString() + ".json";
                JsonArray mainArr = WebUtil.fetchJSON(url).optJSONArray("main");
                if (mainArr == null) continue; // Precaution against NPEs.
                if (mainArr.get(3) == null) {
                    if (Loader.isModLoaded(mainArr.get(2).getAsString())) // Check if the mod is loaded.
                        badMods.add(b.getAsString()); // Add to the bad mods list if the mod is loaded.
                } else {
                    for (File file : coreModList) {
                        try {
                            try (ZipFile zipFile = new ZipFile(file)) {
                                if (zipFile.getEntry(mainArr.get(3).getAsString()) != null)
                                    badMods.add(b.getAsString());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
            if (is5zig()) badMods.add("fivezigmod");
        } catch (Exception e) {
            handler.handleBadMods(badModsFetched); // Recursively fetch mods until it works.
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
                JsonArray mainArr = WebUtil.fetchJSON(url).optJSONArray("main");
                if (mainArr == null) continue;
                if (mainArr.get(3) == null) {
                    if (!Loader.isModLoaded(mainArr.get(2).getAsString())) {
                        neededMods.add(n.getAsString());
                    }
                } else {
                    for (File file : coreModList) {
                        try {
                            try (ZipFile zipFile = new ZipFile(file)) {
                                if (zipFile.getEntry(mainArr.get(3).getAsString()) == null)
                                    neededMods.add(n.getAsString());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        } catch (Exception e) {
            handler.handleNeededMods(neededModsFetched); // Recursively fetch until it works.
        }
    }

    public static class ModHandler {

        private final JsonParser parser = new JsonParser();

        private JsonArray getBadMods() {
            return ((JsonObject) parser.parse(WebUtil.fetchString("https://wyvest.net/checker/checkermain.json"))).getAsJsonArray("badmods");
        }

        private JsonArray getNeededMods() {
            return ((JsonObject) parser.parse(WebUtil.fetchString("https://wyvest.net/checker/checkermain.json"))).getAsJsonArray("neededmods");
        }

        public void handleBadMods(JsonArray badModsList) {
            JsonArray badMods = getBadMods();
            for (JsonElement element : badMods) {
                String url = "https://wyvest.net/checker/" + element.getAsString() + ".json";
                JsonObject urlRet = (JsonObject) parser.parse(WebUtil.fetchString(url));
                if (urlRet != null && urlRet.has("name"))
                    badModsList.add(urlRet.get("name").getAsString());
            }
        }

        public void handleNeededMods(JsonArray neededModsList) {
            JsonArray neededMods = getNeededMods();
            for (JsonElement element : neededMods) {
                String url = "https://wyvest.net/checker/" + element.getAsString() + ".json";
                JsonObject urlRet = (JsonObject) parser.parse(WebUtil.fetchString(url));
                if (urlRet != null && urlRet.has("name"))
                    neededModsList.add(urlRet.get("name").getAsString());
            }
        }

    }

}
