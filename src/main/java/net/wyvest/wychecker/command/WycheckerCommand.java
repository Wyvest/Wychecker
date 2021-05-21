package net.wyvest.wychecker.command;

import club.sk1er.mods.core.ModCore;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.wyvest.wychecker.checker.ModChecker;
import net.wyvest.wychecker.gui.GuiMain;

public class WycheckerCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "wychecker";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/wychecker";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length <= 0) {
            ModChecker.instance.init();
            ModCore.getInstance().getGuiHandler().open(new GuiMain());
            return;
        } else {
            switch (args[0]) {
                case "reload":
                    ModChecker.instance.init();
            }
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return -1;
    }
}
