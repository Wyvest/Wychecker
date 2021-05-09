package net.wyvest.wychecker.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import net.wyvest.wychecker.checker.BadMod;
import net.wyvest.wychecker.checker.ModChecker;
import net.wyvest.wychecker.checker.NeededMod;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class GuiMods {
    public static class GuiBadMods extends GuiScreen{
        @Override
        public void initGui() {
            try {
                this.setupBadModButtons("init", null);
            } catch (IOException ignored) { }
            this.buttonList.add(new GuiButton(0, this.width / 2 - 50, this.height - 20, 100, 20, this.parent == null ? "Save and close" : "Save and go back"));
        }

        private final GuiScreen parent;

        public GuiBadMods(GuiScreen parent) {
            this.parent = parent;
        }

        public GuiScreen getParent() {
            return parent;
        }

        @Override
        protected void actionPerformed(GuiButton button) throws IOException {
            if (button.id == 0) Minecraft.getMinecraft().displayGuiScreen(getParent());
            this.setupBadModButtons("action", button);
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

        private void setupBadModButtons(String type, GuiButton button) throws IOException {
            int offset = this.height / 2 - 50;
            int offsetX = this.width / 2 - 50;
            for (BadMod b : ModChecker.instance.actualBadMods) {
                if (type.equalsIgnoreCase("init")) {
                    this.buttonList.add(new GuiButton(ModChecker.instance.actualBadMods.indexOf(b) + 1, offsetX, this.height - offset, 100, 20, b.name));
                    offset += 25;
                    if (offset > ((this.height / 2) / ModChecker.instance.actualBadMods.size() * 20)) {
                        offsetX = this.width / 2 + 5;
                        offset = this.height / 2 - 50;
                    }
                } else if (type.equalsIgnoreCase("action")) {
                    if (button.id == ModChecker.instance.actualBadMods.indexOf(b) + 1 && b.url != null) {
                        Desktop.getDesktop().browse(URI.create(b.url));
                    }
                }
            }
        }
    }
    public static class GuiNeededMods extends GuiScreen{

        @Override
        public void initGui() {
            try {
                this.setupNeededModButtons("init", null);
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.buttonList.add(new GuiButton(0, this.width / 2 - 50, this.height - 20, 100, 20, this.parent == null ? "Save and close" : "Save and go back"));
        }

        private final GuiScreen parent;

        public GuiNeededMods(GuiScreen parent) {
            this.parent = parent;
        }

        public GuiScreen getParent() {
            return parent;
        }

        @Override
        protected void actionPerformed(GuiButton button) throws IOException {
            if (button.id == 0) Minecraft.getMinecraft().displayGuiScreen(this.parent);
            this.setupNeededModButtons("action", button);
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

        private void setupNeededModButtons(String type, GuiButton button) throws IOException {
            int offset = this.height / 2 - 50;
            int offsetX = this.width / 2 - 50;
            for (NeededMod n : ModChecker.instance.actualNeededMods) {
                if (type.equalsIgnoreCase("init")) {
                    this.buttonList.add(new GuiButton(ModChecker.instance.actualNeededMods.indexOf(n) + 1, offsetX, this.height - offset, 100, 20, n.name));
                    offset += 25;
                    if (offset > ((this.height / 2) / ModChecker.instance.actualNeededMods.size() * 20)) {
                        offsetX = this.width / 2 + 5;
                        offset = this.height / 2 - 50;
                    }
                } else if (type.equalsIgnoreCase("action")) {
                    if (button.id == ModChecker.instance.actualNeededMods.indexOf(n) + 1) {
                        Desktop.getDesktop().browse(URI.create(n.url));
                    }
                }
            }
        }
    }
}
