package com.yuki920.levelhead;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class LevelHeadCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "levelhead";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/levelhead";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        LevelHeadMod.logger.info("LevelHead command executed");
        Minecraft.getMinecraft().addScheduledTask(() -> {
            Minecraft.getMinecraft().displayGuiScreen(new ConfigGui(null));
        });
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
