package net.timeless.jurassicraft.common.entity;

import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.timeless.jurassicraft.common.entity.base.EntityDinosaur;

public class EntityDodo extends EntityDinosaur
{
    private static final String[] hurtSounds = new String[] { "dodo_hurt_1", "dodo_hurt_2" };
    private static final String[] livingSounds = new String[] { "dodo_living_1", "dodo_living_2", "dodo_living_3" };
    private static final String[] deathSounds = new String[] { "dodo_death_1" };
    
    public EntityDodo(World world)
    {
        super(world);
        this.defendFromAttacker(EntityPlayer.class, 3);

        this.tasks.addTask(1, new EntityAIPanic(this, 2.0D));

        this.tasks.addTask(6, new EntityAIWander(this, dinosaur.getAdultSpeed()));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
    }
    
    public String getLivingSound()
    {
        return randomSound(livingSounds);
    }

    public String getHurtSound()
    {
        return randomSound(hurtSounds);
    }

    public String getDeathSound()
    {
        return randomSound(deathSounds);
    }
}