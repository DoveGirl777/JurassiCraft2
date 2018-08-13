package org.jurassicraft.server.item.vehicles;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jurassicraft.server.entity.vehicle.HelicopterEntity;
import org.jurassicraft.server.tab.TabHandler;

import java.util.List;

public class HelicopterItem extends Item {
    public HelicopterItem() {
        this.setCreativeTab(TabHandler.ITEMS);
        this.setMaxStackSize(1);
    }

    @Override
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag tooltipFlag) {
        super.addInformation(stack, world, tooltip, tooltipFlag);
        tooltip.add("Right click on a block to spawn the helicopter");
        this.setUnlocalizedName("helicopter");
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            HelicopterEntity helicopter = new HelicopterEntity(world);
            helicopter.setPosition(pos.getX() + hitX, pos.getY() + hitY, pos.getZ() + hitZ);
            world.spawnEntity(helicopter); //Uncomment for testing
        }

        return EnumActionResult.SUCCESS;
    }
}
