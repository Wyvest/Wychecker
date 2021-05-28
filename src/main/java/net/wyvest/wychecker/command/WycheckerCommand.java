package net.wyvest.wychecker.command;

import club.sk1er.mods.core.ModCore;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.wyvest.wychecker.Wychecker;
import net.wyvest.wychecker.gui.GuiMain;

import java.util.Collections;
import java.util.List;

public class WycheckerCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "wychecker";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length <= 0) {
            Wychecker.getChecker().init();
            ModCore.getInstance().getGuiHandler().open(new GuiMain());
        } else if ("reload".equals(args[0])) Wychecker.getChecker().init();
    }

    @Override
    public int getRequiredPermissionLevel() {
        return -1;
    }

    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        return Collections.singletonList("reload");
    }

}
