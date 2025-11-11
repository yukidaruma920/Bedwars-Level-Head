package com.yuki920.levelhead;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.client.ClientCommandHandler;
import org.apache.logging.log4j.Logger;

@Mod(modid = LevelHeadMod.MODID, version = LevelHeadMod.VERSION, guiFactory = "com.yuki920.levelhead.LevelHeadGuiFactory")
public class LevelHeadMod
{
    public static final String MODID = "levelhead";
    public static final String VERSION = "1.7.0";
    public static Logger logger;
    
    // RenderHandlerインスタンスを保持
    private static RenderHandler renderHandler;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        LevelHeadConfig.init(event.getSuggestedConfigurationFile());
        logger.info("LevelHead Mod PreInit完了");
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // RenderHandlerインスタンスを作成して保持
        renderHandler = new RenderHandler();
        MinecraftForge.EVENT_BUS.register(this);
        if(event.getSide().isClient()){
            ClientCommandHandler.instance.registerCommand(new LevelHeadCommand());
            logger.info("LevelHead Mod Init完了 - RenderHandler登録済み");
        }
    }
    
    // RenderHandlerのインスタンスを取得するメソッド
    public static RenderHandler getRenderHandler() {
        return renderHandler;
    }
}
