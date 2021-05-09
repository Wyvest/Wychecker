package net.wyvest.wychecker.checker;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.common.Loader;
import net.wyvest.wychecker.checker.impl.*;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ModChecker {
    public final static ModChecker instance = new ModChecker();
    public final List<BadMod> badMods = new ArrayList<>();
    public final List<NeededMod> neededMods = new ArrayList<>();
    public final List<BadMod> actualBadMods = new ArrayList<>();
    public final List<NeededMod> actualNeededMods = new ArrayList<>();
    File modsInModsFolder = new File(Launch.minecraftHome, "mods");
    File[] coreModList = modsInModsFolder.listFiles((dir, name) -> name.endsWith(".jar"));

    public void init() {
        this.badMods.add(new CanalexPerspectiveMod());
        this.badMods.add(new FramesPlus());
        this.badMods.add(new LabyMod());
        this.badMods.add(new ModCoreContainer());
        this.badMods.add(new OrangeOAM());
        this.badMods.add(new PlayerAPI());
        this.badMods.add(new ResourcePackOrganizer());
        this.badMods.add(new SkyblockExtras());
        this.badMods.add(new SpiderFrogOAM());
        this.badMods.add(new VanillaEnhancements());
        this.badMods.add(new Skypixel());
        this.neededMods.add(new Patcher());
        this.neededMods.add(new Optifine());
        getActualBadMods();
        getActualNeededMods();
    }

    private void getActualBadMods() {
        if (!modsInModsFolder.exists()) {
            modsInModsFolder.mkdirs();
        }

        File[] coreModList = modsInModsFolder.listFiles((dir, name) -> name.endsWith(".jar"));
        for (BadMod b : this.badMods) {
            if (b.fileName == null) {
                if (Loader.isModLoaded(b.modId)) {
                    actualBadMods.add(b);
                }
            } else {
                for (File file : coreModList) {
                    try {

                        try (ZipFile zipFile = new ZipFile(file)) {
                            if (zipFile.getEntry(b.fileName) != null) {
                                actualBadMods.add(b);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        if (is5zig()) actualBadMods.add(new FiveZigMod());
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
        if (!modsInModsFolder.exists()) {
            modsInModsFolder.mkdirs();
        }

        File[] coreModList = modsInModsFolder.listFiles((dir, name) -> name.endsWith(".jar"));
        for (NeededMod n : this.neededMods) {
            if (n.fileName == null) {
                if (!Loader.isModLoaded(n.name)) {
                    actualNeededMods.add(n);
                }
            } else {
                for (File file : coreModList) {
                    try {

                        try (ZipFile zipFile = new ZipFile(file)) {
                            ZipEntry entry = zipFile.getEntry("mcmod.info");
                            if (zipFile.getEntry(n.fileName) == null) {
                                actualNeededMods.add(n);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }
}
