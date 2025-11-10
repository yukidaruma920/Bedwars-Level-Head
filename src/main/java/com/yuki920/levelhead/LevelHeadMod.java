package com.yuki920.levelhead;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.client.ClientCommandHandler;
import org.apache.logging.log4j.Logger;

@Mod(modid = LevelHeadMod.MODID, version = LevelHeadMod.VERSION, guiFactory = "com.yuki920.levelhead.LevelHeadGuiFactory")
public class LevelHeadMod
{
    public static final String MODID = "levelhead";
    public static final String VERSION = "1.0";
    public static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        LevelHeadConfig.init(event.getSuggestedConfigurationFile());
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        new RenderHandler();
        MinecraftForge.EVENT_BUS.register(this);
        if(event.getSide().isClient()){
            ClientCommandHandler.instance.registerCommand(new LevelHeadCommand());
        }
    }
}
