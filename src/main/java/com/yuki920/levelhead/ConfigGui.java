package com.yuki920.levelhead;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.EnumChatFormatting;

import java.io.IOException;

public class ConfigGui extends GuiScreen {

    private GuiTextField apiKeyField;
    private GuiScreen parentScreen;

    public ConfigGui(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
    }

    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 2 - 48, "Show Own Level: " + (LevelHeadConfig.showOwnLevel ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF")));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 2 - 24, "Adjust Own Level: " + (LevelHeadConfig.adjustOwnLevel ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF")));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 2, "Prestige Format: " + (LevelHeadConfig.prestigeFormat ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF")));
        this.apiKeyField = new GuiTextField(3, this.fontRendererObj, this.width / 2 - 100, this.height / 2 + 24, 200, 20);
        this.apiKeyField.setMaxStringLength(36);
        this.apiKeyField.setText(LevelHeadConfig.apiKey);
        this.buttonList.add(new GuiButton(4, this.width / 2 - 100, this.height / 2 + 48, "Done"));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                LevelHeadConfig.showOwnLevel = !LevelHeadConfig.showOwnLevel;
                button.displayString = "Show Own Level: " + (LevelHeadConfig.showOwnLevel ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF");
                break;
            case 1:
                LevelHeadConfig.adjustOwnLevel = !LevelHeadConfig.adjustOwnLevel;
                button.displayString = "Adjust Own Level: " + (LevelHeadConfig.adjustOwnLevel ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF");
                break;
            case 2:
                LevelHeadConfig.prestigeFormat = !LevelHeadConfig.prestigeFormat;
                button.displayString = "Prestige Format: " + (LevelHeadConfig.prestigeFormat ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF");
                break;
            case 4:
                this.mc.displayGuiScreen(this.parentScreen);
                break;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, "LevelHead Config", this.width / 2, 20, 16777215);
        this.drawString(this.fontRendererObj, "API Key:", this.width / 2 - 100, this.height / 2 + 14, 10526880);
        this.apiKeyField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        this.apiKeyField.textboxKeyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.apiKeyField.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void onGuiClosed() {
        LevelHeadConfig.apiKey = this.apiKeyField.getText();
        LevelHeadConfig.saveConfig();
    }
}
