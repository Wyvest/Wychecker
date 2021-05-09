package net.wyvest.wychecker;

import club.sk1er.modcore.ModCoreInstaller;
import club.sk1er.mods.core.gui.notification.Notifications;
import kotlin.Unit;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.wyvest.wychecker.checker.ModChecker;
import net.wyvest.wychecker.command.WycheckerCommand;
import net.wyvest.wychecker.utils.APICaller;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

@Mod(name = Wychecker.MOD_NAME, version = Wychecker.VERSION, modid = Wychecker.MOD_ID)
public class Wychecker {

    public static final String MOD_ID = "wychecker";
    public static final String MOD_NAME = "Wychecker";
    public static final String VERSION = "0.3.0";

    @Mod.Instance(MOD_ID)
    public static Wychecker INSTANCE;

    @Mod.EventHandler
    protected void onInit(FMLInitializationEvent event) {
        ModCoreInstaller.initializeModCore(Minecraft.getMinecraft().mcDataDir);
        ClientCommandHandler.instance.registerCommand(new WycheckerCommand());
        ModChecker.instance.init();
        APICaller.getVersion();
    }


    @Mod.EventHandler
    protected void onPostInit(FMLPostInitializationEvent event) {
        try {
            if (!APICaller.version.matches(VERSION)) Notifications.INSTANCE.pushNotification("Wychecker", "Your version of Wychecker is outdated. Please update to the latest version by clicking here.", this::browseDownloadPage);
        } catch (Exception e) {e.printStackTrace();}
    }

    private Unit browseDownloadPage() {
        try {
            Desktop.getDesktop().browse(URI.create("https://wyvest.net/checker"));
            return Unit.INSTANCE;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

}