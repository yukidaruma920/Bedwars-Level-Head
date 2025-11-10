package com.yuki920.levelhead;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class Keybindings {

    public static KeyBinding openConfigGui;

    public static void init() {
        openConfigGui = new KeyBinding("Open LevelHead Config", Keyboard.KEY_L, "LevelHead");
        ClientRegistry.registerKeyBinding(openConfigGui);
    }
}
