package org.jurassicraft.client.render.entity;

import org.jurassicraft.JurassiCraft;
import org.jurassicraft.client.model.ResetControlTabulaModel;
import org.jurassicraft.client.model.animation.entity.vehicle.CarAnimator;
import org.jurassicraft.client.model.animation.entity.vehicle.CarAnimator.Door;
import org.jurassicraft.client.model.animation.entity.vehicle.HelicopterAnimator;
import org.jurassicraft.server.entity.vehicle.CarEntity;
import org.jurassicraft.server.entity.vehicle.FordExplorerEntity;
import org.jurassicraft.server.entity.vehicle.HelicopterEntity;
import org.jurassicraft.server.tabula.TabulaModelHelper;

import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.ilexiconn.llibrary.client.model.tabula.container.TabulaModelContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class HeliRenderer extends HelicopterRenderer {

    public HeliRenderer(RenderManager manager) {
        super(manager, "helicopter");
    }

    @Override
    protected HelicopterAnimator createCarAnimator() {
        return new HelicopterAnimator();
    }
}