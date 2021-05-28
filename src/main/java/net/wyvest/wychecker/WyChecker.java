package net.wyvest.wychecker;

import club.sk1er.modcore.ModCoreInstaller;
import kotlin.Unit;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.wyvest.wychecker.checker.ModChecker;
import net.wyvest.wychecker.command.WyCheckerCommand;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

@Mod(name = WyChecker.MOD_NAME, version = WyChecker.VERSION, modid = WyChecker.MOD_ID)
public class WyChecker {

    public static final String MOD_ID = "wychecker";
    public static final String MOD_NAME = "WyChecker";
    public static final String VERSION = "0.3";

    @Mod.Instance(MOD_ID)
    private static WyChecker INSTANCE;
    private static final ModChecker checker = new ModChecker();

    @Mod.EventHandler
    protected void onInit(FMLInitializationEvent event) {
        ModCoreInstaller.initializeModCore(Minecraft.getMinecraft().mcDataDir);
        ClientCommandHandler.instance.registerCommand(new WyCheckerCommand());
    }

    @Mod.EventHandler
    protected void onPostInit(FMLPostInitializationEvent event) {
        /*/
        try {
            if (!APICaller.version.matches(VERSION)) Notifications.INSTANCE.pushNotification("WyChecker", "Your version of WyChecker is outdated. Please update to the latest version by clicking here.", this::browseDownloadPage);
        } catch (Exception e) {e.printStackTrace();}

         */
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

    public static WyChecker getInstance() {
        return INSTANCE;
    }

    public static ModChecker getChecker() {
        return checker;
    }

}