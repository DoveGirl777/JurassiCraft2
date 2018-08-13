package org.jurassicraft.client.model.animation.entity.vehicle;

import net.ilexiconn.llibrary.client.model.tabula.ITabulaModelAnimator;
import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.entity.vehicle.HelicopterEntity;

import java.util.Map;

@SideOnly(Side.CLIENT)
public class HelicopterAnimator implements ITabulaModelAnimator<HelicopterEntity> {

    @Override
    public void setRotationAngles(TabulaModel model, HelicopterEntity entity, float f, float f1, float rotation, float rotationYaw, float rotationPitch, float partialTicks) {
        AdvancedModelRenderer rotor = model.getCube("rotorbase_rotatehere");
        AdvancedModelRenderer tailrotor = model.getCube("tailrotor_rotatehere");

        rotor.rotateAngleY = (float)entity.rotAmount;
        tailrotor.rotateAngleX = (float)entity.rotAmount;
        AdvancedModelRenderer ctrl1 = model.getCube("controlstick1");
        AdvancedModelRenderer ctrl2 = model.getCube("controlstick2");
        AdvancedModelRenderer gearL1 = model.getCube("gearL1");
        AdvancedModelRenderer gearL2 = model.getCube("gearL2");
        AdvancedModelRenderer gearR1 = model.getCube("gearR1");
        AdvancedModelRenderer gearR2 = model.getCube("gearR2");
        AdvancedModelRenderer gearFront = model.getCube("gearFront1");
        AdvancedModelRenderer exhaustL = model.getCube("body79");
        AdvancedModelRenderer exhaustR = model.getCube("body80");

        //if (entity.isEngineRunning()) {
         //   ctrl1.offsetY = 0.01F;
         //   ctrl2.offsetY = -0.01F;
       //}
        if(entity.isFlying) {
            gearL1.offsetY = entity.gearLift;
            gearR1.offsetY = entity.gearLift;
            gearFront.offsetY = entity.gearLift;
        }
        ctrl1.rotateAngleX = (float) Math.toRadians(entity.interpRotationPitch.getValueForRendering(partialTicks) * -1F);
        ctrl1.rotateAngleZ = (float) Math.toRadians(entity.interpRotationRoll.getValueForRendering(partialTicks) * 1F);
    }
}