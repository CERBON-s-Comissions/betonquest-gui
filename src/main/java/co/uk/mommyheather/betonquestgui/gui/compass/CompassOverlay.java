package co.uk.mommyheather.betonquestgui.gui.compass;

import co.uk.mommyheather.betonquestgui.BetonQuestGui;
import co.uk.mommyheather.betonquestgui.config.BQGConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

@OnlyIn(Dist.CLIENT)
public class CompassOverlay
{
    protected static final ResourceLocation COMPASS_DECORATION = new ResourceLocation(BetonQuestGui.MOD_ID, "textures/gui/compass/wooddecoration.png");
    protected static final ResourceLocation COMPASS_BACKGROUND = new ResourceLocation(BetonQuestGui.MOD_ID, "textures/gui/compass/background.png");
    protected static final ResourceLocation QUEST_MARKER_SMALL = new ResourceLocation(BetonQuestGui.MOD_ID, "textures/gui/compass/marker911.png");
    protected static final ResourceLocation ARROWS = new ResourceLocation(BetonQuestGui.MOD_ID, "textures/gui/compass/arrows.png");

    protected static final int COMPASS_FRAME_X = 344;
    protected static final int COMPASS_FRAME_Y = 21;
    protected static final int COMPASS_MARKER_X = 9;
    protected static final int COMPASS_MARKER_Y = 11;

    public static final int POSY = 5;

    protected static final int VIEWING_ANGLE = 90;

    public static Vec3 marker_location;

    public static final IGuiOverlay COMPASS_OVERLAY = ((gui, guiGraphics, partialTicks, screenWidth, screenHeight) ->
    {
        if(BQGConfig.CONFIG.showCompass.get()){
            int t = 0;
            if (screenHeight >= 300) t=18;
            gui.setupOverlayRenderState(true, false);
            guiGraphics.pose().translate(0, t, 0);
            renderCompass(guiGraphics, screenWidth);
            guiGraphics.pose().translate(0, -t, 0);
        }
    });

    @SuppressWarnings("resource")
    public static void renderCompass(GuiGraphics guiGraphics, int screenWidth)
    {
        int halfWidth = screenWidth / 2;

        float cameraAngle = -Mth.wrapDegrees(Minecraft.getInstance().player.getYRot() + 90);

        guiGraphics.blit(COMPASS_BACKGROUND, halfWidth - (COMPASS_FRAME_X / 2), POSY, 0, 0, COMPASS_FRAME_X, COMPASS_FRAME_Y, COMPASS_FRAME_X, COMPASS_FRAME_Y);

        double guiScale = Minecraft.getInstance().getWindow().getGuiScale();

        RenderSystem.enableScissor((int) ((halfWidth - (COMPASS_FRAME_X / 2) + 18) * guiScale), 0, (int) ((COMPASS_FRAME_X - 36) * guiScale), (int) (480 * guiScale));

        drawString(guiGraphics, "N", (int) wrapDegrees(cameraAngle + 90), halfWidth);
        drawString(guiGraphics, "W", (int) wrapDegrees(cameraAngle + 0), halfWidth);
        drawString(guiGraphics, "S", (int) wrapDegrees(cameraAngle + 270), halfWidth);
        drawString(guiGraphics, "E", (int) wrapDegrees(cameraAngle + 180), halfWidth);

        drawStringScaled(guiGraphics, "NW", (int) wrapDegrees(cameraAngle + 45), halfWidth, 1F);
        drawStringScaled(guiGraphics, "NE", (int) wrapDegrees(cameraAngle + 135), halfWidth, 1F);
        drawStringScaled(guiGraphics, "SE", (int) wrapDegrees(cameraAngle + 225), halfWidth, 1F);
        drawStringScaled(guiGraphics, "SW", (int) wrapDegrees(cameraAngle + 315), halfWidth, 1F);

        if (marker_location != null)
        {
            double playerToTargetAngleD = Math.toDegrees(anglePosPlayer(new Vec3(marker_location.x, marker_location.y, marker_location.z)));
            double cameraToTargetAngle = cameraAngle + playerToTargetAngleD;

            if (cameraToTargetAngle < -180) {
                cameraToTargetAngle += 360;
            } else if (cameraToTargetAngle > 180) {
                cameraToTargetAngle -= 360;
            }

            drawLocation(guiGraphics, new Vec3(marker_location.x, marker_location.y, marker_location.z), (float) cameraToTargetAngle, halfWidth);
        }

        RenderSystem.disableScissor();

        guiGraphics.blit(COMPASS_DECORATION, halfWidth - (COMPASS_FRAME_X / 2), POSY, 0, 0, COMPASS_FRAME_X, COMPASS_FRAME_Y, COMPASS_FRAME_X, COMPASS_FRAME_Y);
    }

    public static float wrapDegrees(float angle)
    {
        float f = (angle + 180F) % 360F;
        if (f < -180.0F) {
            f = (float) (f + 360.0D);
        } else if (f >= 180.0F) {
            f = (float) (f - 360.0D);
        }

        return f;
    }

    public static void drawString(GuiGraphics guiGraphics, String compassCardinal, int viewDegree, int halfWidth)
    {
        if (Math.abs(viewDegree) > VIEWING_ANGLE / 2) {
            return;
        }

        guiGraphics.drawString(
                Minecraft.getInstance().font,
                compassCardinal,
                halfWidth + getBarPosition(viewDegree, (COMPASS_FRAME_X / 2)) - (Minecraft.getInstance().font.width(compassCardinal) /2),
                POSY + 7,
                16777215,
                false
        );
    }

    
    public static void drawStringScaled(GuiGraphics guiGraphics, String compassCardinal, int viewDegree, int halfWidth, float scale)
    {
        if (Math.abs(viewDegree) > VIEWING_ANGLE / 2) {
            return;
        }

        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(scale, scale, scale);

        guiGraphics.drawString(
                Minecraft.getInstance().font,
                compassCardinal,
                (int) (((halfWidth + getBarPosition(viewDegree, (COMPASS_FRAME_X / 2))) * (1 / scale)) - (Minecraft.getInstance().font.width(compassCardinal) / 2)),
                (int) ((POSY + 8) * (1 / scale)),
                16777215,
                false
        );

        guiGraphics.pose().popPose();
    }

    public static void drawLocation(GuiGraphics guiGraphics, Vec3 pos, float viewDegree, int halfWidth)
    {
        if (Math.abs(viewDegree) > VIEWING_ANGLE / 2) {
            return;
        }

        int distanceFromObjective = (int) Math.sqrt(Minecraft.getInstance().player.distanceToSqr(pos));
        int xBarPosition = halfWidth + getBarPosition(viewDegree, COMPASS_FRAME_X / 2);

        if(distanceFromObjective < 5) {
            return;
        }

        guiGraphics.blit(QUEST_MARKER_SMALL, xBarPosition - (COMPASS_MARKER_X / 2), POSY + (COMPASS_MARKER_Y / 2), 200, 0, 0, COMPASS_MARKER_X, COMPASS_MARKER_Y, COMPASS_MARKER_X, COMPASS_MARKER_Y);

        /* Distance */
        if(BQGConfig.CONFIG.showDistance.get())
        {
            String distance = distanceFromObjective + "m";
            MutableComponent formattedDistance = Component.literal(distance).withStyle(ChatFormatting.WHITE);//.withStyle(ChatFormatting.BOLD);
            int stringWidth = Minecraft.getInstance().font.width(formattedDistance);

            if(marker_location.y - Minecraft.getInstance().player.getY() > 4.0)
            {
                guiGraphics.blit(ARROWS, xBarPosition + (stringWidth / 2), (POSY + 22), 200, 7, 0, 7, 5, 14, 5);

            }
            else if(marker_location.y - Minecraft.getInstance().player.getY() < -4.0) {
                guiGraphics.blit(ARROWS, xBarPosition + (stringWidth / 2), (POSY + 22), 200, 0, 0, 7, 5, 14, 5);
            }

            float scale = 1F;
            guiGraphics.pose().pushPose();
            guiGraphics.pose().scale(scale, scale, scale);

            guiGraphics.drawString(
                    Minecraft.getInstance().font,
                    formattedDistance,
                    (int) ((xBarPosition * (1 / scale) - (stringWidth / 2 ))),
                    (int) ((POSY + 22) * (1 / scale)),
                    16777215,
                    false
            );

            guiGraphics.pose().popPose();
        }
    }

    public static double anglePosPlayer(Vec3 pos)
    {
        final double deltaZ = (pos.z() - Minecraft.getInstance().player.getZ());
        final double deltaX = (pos.x() - Minecraft.getInstance().player.getX());
        final double result = Math.atan2(deltaZ, deltaX);

        return result;
    }

    public static float getScale(int width, int w)
    {
        if (width < w) {
            return width / w;
        }
        return 1.0F;
    }

    public static int getBarPosition(float angle, int barHalfWidth)
    {
        return (int) Mth.clamp(angle / 45 * barHalfWidth, -barHalfWidth, barHalfWidth);
    }
}