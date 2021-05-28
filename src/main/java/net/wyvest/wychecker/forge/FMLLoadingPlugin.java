package net.wyvest.wychecker.forge;

import club.sk1er.modcore.ModCoreInstaller;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion(ForgeVersion.mcVersion)
public class FMLLoadingPlugin implements IFMLLoadingPlugin {
    @Override
    public String[] getASMTransformerClass() {
        int initialize = ModCoreInstaller.initialize(Launch.minecraftHome, "1.8.9");

        // Technically wouldn't happen in simulated install but is important for actual implementation.
        if (ModCoreInstaller.isErrored() || initialize != 0 && initialize != -1)
            System.out.println("Failed to load Sk1er Modcore - " + initialize + " - " + ModCoreInstaller.getError());

        // If true the classes have been loaded.
        if (ModCoreInstaller.isIsRunningModCore())
            return new String[]{"club.sk1er.mods.core.forge.ClassTransformer"};

        return new String[]{};
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> map) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}