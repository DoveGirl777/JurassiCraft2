package org.jurassicraft.client.render.entity;

import com.google.common.collect.Maps;
import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.client.model.animation.entity.vehicle.HelicopterAnimator;
import org.jurassicraft.server.entity.vehicle.HelicopterEntity;
import org.jurassicraft.server.tabula.TabulaModelHelper;
import org.lwjgl.opengl.GL11;

import java.util.Map;

@SideOnly(Side.CLIENT)
public class HelicopterRenderer implements IRenderFactory<HelicopterEntity> {
    @Override
    public Render<? super HelicopterEntity> createRenderFor(RenderManager manager) {
        return new Renderer(manager);
    }

    public static class Renderer extends Render<HelicopterEntity> {
        private static final ResourceLocation TEXTURE = new ResourceLocation(JurassiCraft.MODID, "textures/entities/helicopter/helicopter.png");
        private final Map<String, TabulaModel> moduleMap;
        private final Map<String, ResourceLocation> moduleTextures;
        private TabulaModel baseModel;

        public Renderer(RenderManager manager) {
            super(manager);
            this.moduleMap = Maps.newHashMap();
            this.moduleTextures = Maps.newHashMap();
            try {
                this.baseModel = new TabulaModel(TabulaModelHelper.loadTabulaModel("/assets/jurassicraft/models/entities/helicopter/helicopter"), new HelicopterAnimator());

                // Modules init.
            } catch (Exception e) {
                JurassiCraft.getLogger().fatal("Failed to load the models for the Helicopter", e);
            }
        }

        @Override
        public void doRender(HelicopterEntity helicopter, double x, double y, double z, float yaw, float partialTicks) {
            GlStateManager.pushMatrix();
            GlStateManager.translate((float) x, (float) y + 1.5F, (float) z);
            GlStateManager.rotate(180.0F - yaw, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate((float) helicopter.interpRotationPitch.getValueForRendering(partialTicks), 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate((float) helicopter.interpRotationRoll.getValueForRendering(partialTicks), 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(helicopter.rotationAmount, -1f, 0f, 0f);
            float f4 = 1f;
            GlStateManager.scale(f4, f4, f4);
            GlStateManager.scale(1.0F / f4, 1.0F / f4, 1.0F / f4);
            this.bindEntityTexture(helicopter);
            GlStateManager.scale(-1.0F, -1.0F, 1.0F);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            this.baseModel.render(helicopter, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);

            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GlStateManager.popMatrix();
            super.doRender(helicopter, x, y, z, yaw, partialTicks);
        }


        @Override
        protected ResourceLocation getEntityTexture(HelicopterEntity entity) {
            return TEXTURE;
        }
    }
}