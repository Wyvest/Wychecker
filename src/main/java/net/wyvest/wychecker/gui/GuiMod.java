package net.wyvest.wychecker.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import net.wyvest.wychecker.ModChecker;

import java.io.IOException;

public class GuiMod extends GuiScreen {
    ModChecker.BadModMetadata badModMetadata = null;
    ModChecker.NeededModMetadata neededModMetadata = null;
    GuiScreen parent;

    public GuiMod(ModChecker.BadModMetadata badModMetadata, GuiScreen parent) {
        this.badModMetadata = badModMetadata;
        this.parent = parent;
    }

    public GuiMod(ModChecker.NeededModMetadata neededModMetadata, GuiScreen parent) {
        this.neededModMetadata = neededModMetadata;
        this.parent = parent;
    }

    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(0, this.width / 2 - 50, this.height - 20, 100, 20, this.parent == null ? "Save and close" : "Save and go back"));
    }

    public GuiScreen getParent() {
        return parent;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) Minecraft.getMinecraft().displayGuiScreen(getParent());
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawDefaultBackground();
        int scale = 2;
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 0);
        drawCenteredString(this.fontRendererObj, EnumChatFormatting.RED + (badModMetadata == null ? "Needed mod: " + neededModMetadata.name : "Bad mod: " + badModMetadata.name), width / 20 / scale, 5 / scale + 10, -1);
        GlStateManager.popMatrix();
        scale = 1;
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 0);
        drawCenteredString(this.fontRendererObj, EnumChatFormatting.WHITE + "(made by Wyvest!)", width / 2 / scale, 5 / scale + 55, -1);
        GlStateManager.popMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
