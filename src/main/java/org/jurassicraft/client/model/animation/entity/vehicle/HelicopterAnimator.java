package org.jurassicraft.client.model.animation.entity.vehicle;

import net.ilexiconn.llibrary.client.model.tabula.ITabulaModelAnimator;
import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.entity.vehicle.HelicopterEntity;

@SideOnly(Side.CLIENT)
public class HelicopterAnimator implements ITabulaModelAnimator<HelicopterEntity> {

    double rotAmount = 0D;

    @Override
    public void setRotationAngles(TabulaModel model, HelicopterEntity entity, float f, float f1, float rotation, float rotationYaw, float rotationPitch, float partialTicks) {
        AdvancedModelRenderer rotor = model.getCube("rotorbase_rotatehere");
        AdvancedModelRenderer tailrotor = model.getCube("tailrotor_rotatehere");
        rotAmount += (entity.rotorRotationAmount) / 2D;
        rotor.rotateAngleY = (float) rotAmount;
        tailrotor.rotateAngleX = (float) rotAmount;

        AdvancedModelRenderer ctrl1 = model.getCube("controlstick1");
        AdvancedModelRenderer ctrl2 = model.getCube("controlstick2");

        //if (entity.isEngineRunning()) {
         //   ctrl1.offsetY = 0.01F;
         //   ctrl2.offsetY = -0.01F;
       //}

        //ctrl1.rotateAngleX = (float) Math.toRadians(entity.interpRotationPitch.getValueForRendering(partialTicks) * -1F);
        //ctrl1.rotateAngleZ = (float) Math.toRadians(entity.interpRotationRoll.getValueForRendering(partialTicks) * 1F);
    }
}