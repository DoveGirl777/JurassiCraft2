package org.jurassicraft.client.render.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderPainting;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.client.render.WorldRendererUtils;
import org.jurassicraft.common.entity.item.EntityPaddockSign;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderPaddockSign extends Render
{
    private static final ResourceLocation texture = new ResourceLocation(JurassiCraft.MODID, "textures/paddock/paddock_signs.png");

    public RenderPaddockSign()
    {
        super(Minecraft.getMinecraft().getRenderManager());
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic (Render<T extends Entity>) and this method has signature public void func_76986_a(T entity, double d, double d1, double d2, float f, float f1). But JAD is pre 1.5 so doe
     */
    public void doRender(EntityPaddockSign entity, double x, double y, double z, float yaw, float partialTicks)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(180.0F - yaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.enableRescaleNormal();
        this.bindEntityTexture(entity);

        float scale = 0.0625F;
        GlStateManager.scale(scale, scale, scale);

        int id = entity.getDinosaur();

        this.drawPaddockSign(entity, 16, 16, ((id) % 16) * 16, (int) Math.floor((id) / 16) * 16); // 8x16 art
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, yaw, partialTicks);
    }

    protected ResourceLocation getPaddockTexture(EntityPaddockSign sign)
    {
        return texture;
    }

    private void drawPaddockSign(EntityPaddockSign sign, int width, int height, int textureU, int textureV)
    {
        float f = (float)(-width) / 2.0F;
        float f1 = (float)(-height) / 2.0F;
        float f2 = 0.001F;
        float f3 = 0.75F;
        float f4 = 0.8125F;
        float f5 = 0.0F;
        float f6 = 0.0625F;
        float f7 = 0.75F;
        float f8 = 0.8125F;
        float f9 = 0.001953125F;
        float f10 = 0.001953125F;
        float f11 = 0.7519531F;
        float f12 = 0.7519531F;
        float f13 = 0.0F;
        float f14 = 0.0625F;

        for (int x = 0; x < width / 16; ++x)
        {
            for (int y = 0; y < height / 16; ++y)
            {
                float bottomX = f + (float)((x + 1) * 16);
                float f16 = f + (float)(x * 16);
                float f17 = f1 + (float)((y + 1) * 16);
                float f18 = f1 + (float)(y * 16);
                this.setLightmap(sign, (bottomX + f16) / 2.0F, (f17 + f18) / 2.0F);
                float f19 = (float)(textureU + width - x * 16) / 256.0F;
                float f20 = (float)(textureU + width - (x + 1) * 16) / 256.0F;
                float f21 = (float)(textureV + height - y * 16) / 256.0F;
                float f22 = (float)(textureV + height - (y + 1) * 16) / 256.0F;
                Tessellator tessellator = Tessellator.getInstance();
                WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181710_j);
                worldrenderer.func_181662_b((double)bottomX, (double)f18, (double)(-f2)).func_181673_a((double)f20, (double)f21).func_181663_c(0.0F, 0.0F, -1.0F).func_181675_d();
                worldrenderer.func_181662_b((double)f16, (double)f18, (double)(-f2)).func_181673_a((double)f19, (double)f21).func_181663_c(0.0F, 0.0F, -1.0F).func_181675_d();
                worldrenderer.func_181662_b((double)f16, (double)f17, (double)(-f2)).func_181673_a((double)f19, (double)f22).func_181663_c(0.0F, 0.0F, -1.0F).func_181675_d();
                worldrenderer.func_181662_b((double)bottomX, (double)f17, (double)(-f2)).func_181673_a((double)f20, (double)f22).func_181663_c(0.0F, 0.0F, -1.0F).func_181675_d();
                worldrenderer.func_181662_b((double)bottomX, (double)f17, (double)f2).func_181673_a((double)f3, (double)f5).func_181663_c(0.0F, 0.0F, 1.0F).func_181675_d();
                worldrenderer.func_181662_b((double)f16, (double)f17, (double)f2).func_181673_a((double)f4, (double)f5).func_181663_c(0.0F, 0.0F, 1.0F).func_181675_d();
                worldrenderer.func_181662_b((double)f16, (double)f18, (double)f2).func_181673_a((double)f4, (double)f6).func_181663_c(0.0F, 0.0F, 1.0F).func_181675_d();
                worldrenderer.func_181662_b((double)bottomX, (double)f18, (double)f2).func_181673_a((double)f3, (double)f6).func_181663_c(0.0F, 0.0F, 1.0F).func_181675_d();
                worldrenderer.func_181662_b((double)bottomX, (double)f17, (double)(-f2)).func_181673_a((double)f7, (double)f9).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
                worldrenderer.func_181662_b((double)f16, (double)f17, (double)(-f2)).func_181673_a((double)f8, (double)f9).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
                worldrenderer.func_181662_b((double)f16, (double)f17, (double)f2).func_181673_a((double)f8, (double)f10).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
                worldrenderer.func_181662_b((double)bottomX, (double)f17, (double)f2).func_181673_a((double)f7, (double)f10).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
                worldrenderer.func_181662_b((double)bottomX, (double)f18, (double)f2).func_181673_a((double)f7, (double)f9).func_181663_c(0.0F, -1.0F, 0.0F).func_181675_d();
                worldrenderer.func_181662_b((double)f16, (double)f18, (double)f2).func_181673_a((double)f8, (double)f9).func_181663_c(0.0F, -1.0F, 0.0F).func_181675_d();
                worldrenderer.func_181662_b((double)f16, (double)f18, (double)(-f2)).func_181673_a((double)f8, (double)f10).func_181663_c(0.0F, -1.0F, 0.0F).func_181675_d();
                worldrenderer.func_181662_b((double)bottomX, (double)f18, (double)(-f2)).func_181673_a((double)f7, (double)f10).func_181663_c(0.0F, -1.0F, 0.0F).func_181675_d();
                worldrenderer.func_181662_b((double)bottomX, (double)f17, (double)f2).func_181673_a((double)f12, (double)f13).func_181663_c(-1.0F, 0.0F, 0.0F).func_181675_d();
                worldrenderer.func_181662_b((double)bottomX, (double)f18, (double)f2).func_181673_a((double)f12, (double)f14).func_181663_c(-1.0F, 0.0F, 0.0F).func_181675_d();
                worldrenderer.func_181662_b((double)bottomX, (double)f18, (double)(-f2)).func_181673_a((double)f11, (double)f14).func_181663_c(-1.0F, 0.0F, 0.0F).func_181675_d();
                worldrenderer.func_181662_b((double)bottomX, (double)f17, (double)(-f2)).func_181673_a((double)f11, (double)f13).func_181663_c(-1.0F, 0.0F, 0.0F).func_181675_d();
                worldrenderer.func_181662_b((double)f16, (double)f17, (double)(-f2)).func_181673_a((double)f12, (double)f13).func_181663_c(1.0F, 0.0F, 0.0F).func_181675_d();
                worldrenderer.func_181662_b((double)f16, (double)f18, (double)(-f2)).func_181673_a((double)f12, (double)f14).func_181663_c(1.0F, 0.0F, 0.0F).func_181675_d();
                worldrenderer.func_181662_b((double)f16, (double)f18, (double)f2).func_181673_a((double)f11, (double)f14).func_181663_c(1.0F, 0.0F, 0.0F).func_181675_d();
                worldrenderer.func_181662_b((double)f16, (double)f17, (double)f2).func_181673_a((double)f11, (double)f13).func_181663_c(1.0F, 0.0F, 0.0F).func_181675_d();
                tessellator.draw();
            }
        }
    }

    private void setLightmap(EntityPaddockSign sign, float p_77008_2_, float p_77008_3_)
    {
        int i = MathHelper.floor_double(sign.posX);
        int j = MathHelper.floor_double(sign.posY + (double) (p_77008_3_ / 16.0F));
        int k = MathHelper.floor_double(sign.posZ);
        EnumFacing enumfacing = sign.facingDirection;

        if (enumfacing == EnumFacing.NORTH)
        {
            i = MathHelper.floor_double(sign.posX + (double) (p_77008_2_ / 16.0F));
        }

        if (enumfacing == EnumFacing.WEST)
        {
            k = MathHelper.floor_double(sign.posZ - (double) (p_77008_2_ / 16.0F));
        }

        if (enumfacing == EnumFacing.SOUTH)
        {
            i = MathHelper.floor_double(sign.posX - (double) (p_77008_2_ / 16.0F));
        }

        if (enumfacing == EnumFacing.EAST)
        {
            k = MathHelper.floor_double(sign.posZ + (double) (p_77008_2_ / 16.0F));
        }

        int l = this.renderManager.worldObj.getCombinedLight(new BlockPos(i, j, k), 0);
        int i1 = l % 65536;
        int j1 = l / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) i1, (float) j1);
        GlStateManager.color(1.0F, 1.0F, 1.0F);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return this.getPaddockTexture((EntityPaddockSign) entity);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic (Render<T extends Entity>) and this method has signature public void func_76986_a(T entity, double d, double d1, double d2, float f, float f1). But JAD is pre 1.5 so doe
     */
    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks)
    {
        this.doRender((EntityPaddockSign) entity, x, y, z, yaw, partialTicks);
    }
}
