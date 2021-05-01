package net.wyvest.mod;

import club.sk1er.modcore.ModCoreInstaller;
import club.sk1er.mods.core.gui.notification.Notifications;
import kotlin.Unit;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.wyvest.mod.utils.VersionChecker;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

@Mod(name = ExampleMod.MOD_NAME, version = ExampleMod.VERSION, modid = ExampleMod.MOD_ID)
public class ExampleMod {

    public static final String MOD_ID = "examplemod";
    public static final String MOD_NAME = "Example Mod";
    public static final String VERSION = "1.0";

    @Mod.Instance(MOD_ID)
    public static ExampleMod INSTANCE;

    @Mod.EventHandler
    protected void onPreInit(FMLPreInitializationEvent event) {
        VersionChecker.getVersion();
    }

    @Mod.EventHandler
    protected void onInit(FMLInitializationEvent event) {
        ModCoreInstaller.initializeModCore(Minecraft.getMinecraft().mcDataDir);
    }


    @Mod.EventHandler
    protected void onPostInit(FMLPostInitializationEvent event) {
        if (Double.parseDouble(VersionChecker.version) > Double.parseDouble(VERSION)) {
            Notifications.INSTANCE.pushNotification(MOD_NAME, MOD_NAME + " is out of date. Please update to the latest version.", this::browseDownloadPage);
        }
    }

    private Unit browseDownloadPage() {
        try {
            Desktop.getDesktop().browse(this.URLtoURI(new URL("https://wyvest.net/example")));
            return Unit.INSTANCE;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    private URI URLtoURI(URL url) {
        try {
            return url.toURI();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}