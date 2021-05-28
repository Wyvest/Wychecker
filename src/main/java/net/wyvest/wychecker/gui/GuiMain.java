package net.wyvest.wychecker.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class GuiMain extends GuiScreen {

    @Override
    public void initGui() {
        this.buttonList.addAll(Arrays.asList(
                new GuiButton(0, this.width / 2 - 50, this.height - 20, 100, 20, "Close"),
                new GuiButton(1, this.width / 2 - 50, this.height / 2 - 30, 100, 20, "Bad Mods"),
                new GuiButton(2, this.width / 2 - 50, this.height / 2, 100, 20, "Needed Mods")
        ));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) Minecraft.getMinecraft().displayGuiScreen(null);
        if (button.id == 1) Minecraft.getMinecraft().displayGuiScreen(new GuiMods.GuiBadMods(this));
        if (button.id == 2) Minecraft.getMinecraft().displayGuiScreen(new GuiMods.GuiNeededMods(this));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawDefaultBackground();
        int scale = 3;
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 0);
        drawCenteredString(this.fontRendererObj, EnumChatFormatting.LIGHT_PURPLE + "Wychecker", width / 2 / scale, 5 / scale + 10, -1);
        GlStateManager.popMatrix();
        scale = 1;
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 0);
        drawCenteredString(this.fontRendererObj, EnumChatFormatting.WHITE + "(made by Wyvest!)", width / 2 / scale, 5 / scale + 55, -1);
        GlStateManager.popMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

}
