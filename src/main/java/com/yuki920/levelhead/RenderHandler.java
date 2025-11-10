package com.yuki920.levelhead;

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

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class RenderHandler {

    private final Minecraft mc = Minecraft.getMinecraft();
    private final RenderManager renderManager = mc.getRenderManager();
    private final FontRenderer fontRenderer = mc.fontRendererObj;
    private final Map<UUID, String> levelCache = new ConcurrentHashMap<>();
    private final Map<UUID, Long> lastRequestTimes = new ConcurrentHashMap<>();


    public RenderHandler() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRenderPlayer(RenderPlayerEvent.Specials.Post event) {
        if (!LevelHeadConfig.apiKey.isEmpty()) {
            EntityPlayer player = event.entityPlayer;
            String level = getBedwarsLevel(player);
            if (level != null) {
                renderLevel(player, level, event.x, event.y, event.z);
            }
        } else {
            LevelHeadMod.logger.warn("API key is not set!");
        }
    }

    private void renderLevel(EntityPlayer player, String level, double x, double y, double z) {
        String displayText = EnumChatFormatting.AQUA + "Bedwars Level: " + level;
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

        int stringWidth = fontRenderer.getStringWidth(displayText) / 2;

        GlStateManager.disableTexture2D();
        worldrenderer.begin(7, worldrenderer.getVertexFormat());
        worldrenderer.pos(-stringWidth - 1, -1, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos(-stringWidth - 1, 8, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos(stringWidth + 1, 8, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        worldrenderer.pos(stringWidth + 1, -1, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();

        fontRenderer.drawString(displayText, -fontRenderer.getStringWidth(displayText) / 2, 0, 0xFFFFFFFF);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    private String getBedwarsLevel(EntityPlayer player) {
        UUID playerUUID = player.getUniqueID();
        if (levelCache.containsKey(playerUUID)) {
            return levelCache.get(playerUUID);
        }

        long currentTime = System.currentTimeMillis();
        if (lastRequestTimes.containsKey(playerUUID) && (currentTime - lastRequestTimes.get(playerUUID) < 600000)) { // 10 minutes
            return null; // Don't request again for a while
        }

        levelCache.put(player.getUniqueID(), "..."); // Placeholder
        lastRequestTimes.put(playerUUID, currentTime);
        LevelHeadMod.logger.info("Requesting Bedwars level for " + player.getName());

        HypixelAPI.getPlayerData(player.getUniqueID()).thenAccept(data -> {
            if (data != null) {
                int level = data.getBedwarsLevel();
                if (level > 0) {
                    String formattedLevel;
                    if (LevelHeadConfig.prestigeFormat) {
                        formattedLevel = PrestigeFormatter.formatPrestige(level);
                    } else {
                        formattedLevel = String.valueOf(level);
                    }
                    levelCache.put(player.getUniqueID(), formattedLevel);
                } else {
                    levelCache.put(player.getUniqueID(), EnumChatFormatting.RED + "N/A");
                }
            } else {
                levelCache.put(player.getUniqueID(), EnumChatFormatting.RED + "Error");
            }
        });

        return levelCache.get(player.getUniqueID());
    }
}
