package com.yuki920.levelhead;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import java.io.IOException;

public class SimpleTestGui extends GuiScreen {

    private GuiScreen parentScreen;

    public SimpleTestGui(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
        LevelHeadMod.logger.info("SimpleTestGui created!");
    }

    @Override
    public void initGui() {
        LevelHeadMod.logger.info("SimpleTestGui initGui!");
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 2 - 10, "Close"));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            this.mc.displayGuiScreen(this.parentScreen);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, "Test GUI - It Works!", this.width / 2, 20, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
