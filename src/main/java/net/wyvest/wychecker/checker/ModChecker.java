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
    public final static ModChecker instance = new ModChecker();
    public JsonArray badModsOnline = new JsonArray();
    public JsonArray neededModsOnline = new JsonArray();
    public ArrayList<String> badMods = new ArrayList<>();
    public ArrayList<String> neededMods = new ArrayList<>();
    File modsInModsFolder = new File(Launch.minecraftHome, "mods");
    File[] coreModList = modsInModsFolder.listFiles((dir, name) -> name.endsWith(".jar"));

    public void init() {
        getOnlineMods();
        getActualBadMods();
        getActualNeededMods();
    }

    private void getOnlineMods() {
        try {
            badModsOnline = WebUtil.fetchJSON("https://wyvest.net/checker/checkermain.json").optJSONArray("badmods");
            neededModsOnline = WebUtil.fetchJSON("https://wyvest.net/checker/checkermain.json").optJSONArray("neededmods");
        } catch (Exception e) {
            this.getOnlineMods();
        }
    }

    private void getActualBadMods() {
        try {
            if (!modsInModsFolder.exists()) {
                modsInModsFolder.mkdirs();
            }
            File[] coreModList = modsInModsFolder.listFiles((dir, name) -> name.endsWith(".jar"));
            for (JsonElement b : badModsOnline) {
                String url = "https://wyvest.net/checker/" + b.getAsString() + ".json";
                if (WebUtil.fetchJSON(url).optJSONArray("main").get(3) == null) {
                    if (Loader.isModLoaded(WebUtil.fetchJSON(url).optJSONArray("main").get(2).getAsString())) {
                        badMods.add(b.getAsString());
                    }
                } else {
                    for (File file : coreModList) {
                        try {

                            try (ZipFile zipFile = new ZipFile(file)) {
                                if (zipFile.getEntry(WebUtil.fetchJSON(url).optJSONArray("main").get(3).getAsString()) != null) {
                                    badMods.add(b.getAsString());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
            if (is5zig()) badMods.add("fivezigmod");
        } catch (Exception e) {
            this.getActualBadMods(); //REALLY REALLY BAD but the mod can't function and crashes without these files so
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


    public void getActualNeededMods() {
        try {
            if (!modsInModsFolder.exists()) {
                modsInModsFolder.mkdirs();
            }

            File[] coreModList = modsInModsFolder.listFiles((dir, name) -> name.endsWith(".jar"));
            for (JsonElement n : this.neededModsOnline) {
                String url = "https://wyvest.net/checker/" + n.getAsString() + ".json";
                if (WebUtil.fetchJSON(url).optJSONArray("main").get(3) == null) {
                    if (!Loader.isModLoaded(WebUtil.fetchJSON(url).optJSONArray("main").get(2).getAsString())) {
                        neededMods.add(n.getAsString());
                    }
                } else {
                    for (File file : coreModList) {
                        try {

                            try (ZipFile zipFile = new ZipFile(file)) {
                                if (zipFile.getEntry(WebUtil.fetchJSON(url).optJSONArray("main").get(3).getAsString()) == null) {
                                    neededMods.add(n.getAsString());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        } catch (Exception e) {
            this.getActualNeededMods();
        }

    }
}
