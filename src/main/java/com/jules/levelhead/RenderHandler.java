package com.jules.levelhead;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class RenderHandler {

    private final Minecraft mc = Minecraft.getMinecraft();
    private final RenderManager renderManager = mc.getRenderManager();
    private final FontRenderer fontRenderer = mc.fontRendererObj;

    public RenderHandler() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRenderPlayer(RenderPlayerEvent.Specials.Post event) {
        if (isHypixel() && !LevelHeadConfig.apiKey.isEmpty()) {
            EntityPlayer player = event.entityPlayer;
            String level = getBedwarsLevel(player);
            if (level != null) {
                renderLevel(player, level, event.x, event.y, event.z);
            }
        }
    }

    private void renderLevel(EntityPlayer player, String level, double x, double y, double z) {
        EntityPlayerSP localPlayer = mc.thePlayer;
        if (player.equals(localPlayer) && !LevelHeadConfig.showOwnLevel) {
            return;
        }

        float yOffset = 0.5F;
        if (player.equals(localPlayer) && LevelHeadConfig.adjustOwnLevel) {
            yOffset += 0.5F; // Adjust this value as needed
        }

        float distance = localPlayer.getDistanceToEntity(player);
        if (distance > 64) {
            return;
        }

        float scale = 0.02666667F;
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y + player.height + yOffset, (float) z);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(-scale, -scale, scale);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        int stringWidth = fontRenderer.getStringWidth(level) / 2;

        GlStateManager.disableTexture2D();
        worldrenderer.begin(7, worldrenderer.getVertexFormat());
        worldrenderer.pos(-stringWidth - 1, -1, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos(-stringWidth - 1, 8, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos(stringWidth + 1, 8, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos(stringWidth + 1, -1, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();

        fontRenderer.drawString(level, -fontRenderer.getStringWidth(level) / 2, 0, 0xFFFFFFFF);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    private boolean isHypixel() {
        return !mc.isSingleplayer() && mc.getCurrentServerData() != null && mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel.net");
    }

    private String getBedwarsLevel(EntityPlayer player) {
        HypixelAPI.PlayerData data = HypixelAPI.getPlayerData(player.getUniqueID());
        if (data != null) {
            int level = data.getBedwarsLevel();
            if (level > 0) {
                if (LevelHeadConfig.prestigeFormat) {
                    return getPrestigeFormat(level);
                } else {
                    return String.valueOf(level);
                }
            }
        }
        return null;
    }

    private String getPrestigeFormat(int level) {
        int prestige = level / 100;
        EnumChatFormatting color;
        switch (prestige) {
            case 0:
                color = EnumChatFormatting.GRAY;
                break;
            case 1:
                color = EnumChatFormatting.WHITE;
                break;
            case 2:
                color = EnumChatFormatting.GOLD;
                break;
            case 3:
                color = EnumChatFormatting.AQUA;
                break;
            case 4:
                color = EnumChatFormatting.DARK_GREEN;
                break;
            case 5:
                color = EnumChatFormatting.DARK_AQUA;
                break;
            case 6:
                color = EnumChatFormatting.DARK_RED;
                break;
            case 7:
                color = EnumChatFormatting.LIGHT_PURPLE;
                break;
            case 8:
                color = EnumChatFormatting.BLUE;
                break;
            case 9:
                color = EnumChatFormatting.DARK_PURPLE;
                break;
            default:
                color = EnumChatFormatting.RED;
                break;
        }
        return color + "[" + level + "✫]";
    }
}
