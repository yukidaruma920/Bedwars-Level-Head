package com.yuki920.levelhead;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.Arrays;
import java.util.List;

public class LevelHeadCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "levelhead";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/levelhead [config|refresh]";
    }
    
    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("lh");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            // 引数なしの場合は使用方法を表示
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "LevelHead Commands:"));
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.YELLOW + "/levelhead config" + EnumChatFormatting.GRAY + " - Open config GUI"));
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.YELLOW + "/levelhead refresh" + EnumChatFormatting.GRAY + " - Refresh level cache"));
            return;
        }
        
        String subCommand = args[0].toLowerCase();
        
        switch (subCommand) {
            case "config":
            case "cfg":
                LevelHeadMod.logger.info("Opening LevelHead config GUI");
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Opening LevelHead config..."));
                Minecraft.getMinecraft().addScheduledTask(() -> {
                    Minecraft.getMinecraft().displayGuiScreen(new ConfigGui(null));
                });
                break;
                
            case "refresh":
            case "reload":
            case "reflesh": // よくあるタイポに対応
                LevelHeadMod.logger.info("Refreshing LevelHead cache");
                RenderHandler renderHandler = LevelHeadMod.getRenderHandler();
                if (renderHandler != null) {
                    renderHandler.clearCache();
                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "LevelHead cache cleared! Levels will be refreshed."));
                } else {
                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Error: RenderHandler not initialized."));
                }
                break;
                
            default:
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Unknown subcommand. Use /levelhead for help."));
                break;
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
    
    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}