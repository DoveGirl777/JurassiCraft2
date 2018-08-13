package org.jurassicraft.server.entity.vehicle;


import net.minecraft.block.BlockAir;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.BlockStateLoader;
import net.minecraftforge.client.model.b3d.B3DModel;
import net.minecraftforge.server.permission.context.ContextKeys;
import org.jurassicraft.server.event.KeyBindingHandler;
import org.lwjgl.input.Keyboard;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jurassicraft.server.entity.ai.util.InterpValue;
import org.jurassicraft.server.item.ItemHandler;
import org.jurassicraft.server.util.MutableVec3;

import javax.annotation.Nonnull;

public class HelicopterEntity extends CarEntity {

    private static final BlockPos INACTIVE = new BlockPos(-1, -1, -1);

    private BlockPos prevPos = INACTIVE;

    private boolean lastDirBackwards;
    private static final float MAX_POWER = 80.0F;
    private static final float REQUIRED_POWER = MAX_POWER / 2.0F;
    private float enginePower;
    public float gearLift;
    public boolean shouldGearLift = true;
    private final InterpValue rotationYawInterp = new InterpValue(this, 4f);
    private static final float SPEEDMODIFIER = 2.5f;
    public boolean isFlying;
    public float rotorRotationAmount;
    public final InterpValue interpRotationPitch = new InterpValue(this, 0.25D);
    public final InterpValue interpRotationRoll = new InterpValue(this, 0.25D);
    private MutableVec3 direction;
    public float rotationAmount;
    public float sideRotationAmount;
    private static final float MAXMOVEMENTROTATION = 15f;
    private boolean shouldFallDamage;
    /* =================================== CAR START ===========================================*/

    public HelicopterEntity(World worldIn) {
        super(worldIn);
        double w = 5f; // width in blocks
        double h = 3.5f; // height in blocks
        double d = 8f; // depth in blocks
        this.setEntityBoundingBox(new AxisAlignedBB( 0, 0, 0, w, h, d));
        this.setSize(5f, 3.5f);
        this.speedModifier = 1.5f;
        this.isFlying = false;
        this.direction = new MutableVec3(0,1,0);
    }

    @Override
    public void dropItems() {
        this.dropItem(ItemHandler.HELICOPTER, 1);
    }

    @Override
    protected Seat[] createSeats() {
        Seat middle = new Seat(0F, -0.23F, 1.2F, 0.5F, 0.25F);
        Seat frontLeft = new Seat(-0.55F, -0.34F, 0.1F, 0.5F, 0.25F);
        Seat frontRight = new Seat(0.55F, -0.34F, 0.1F, 0.5F, 0.25F);
        Seat backLeft = new Seat( 0.4F, 0.25F, -1F, 0.5F, 0.25F);
        return new Seat[] { middle, frontLeft, frontRight, backLeft};
    }

    @Override
    protected boolean shouldStopUpdates() {
        return false;
    }

    @Override
    public void onUpdate() {
        BlockPos startPos = this.getPosition();
        this.setPosition(this.posX, this.posY, this.posZ); //Make sure that the car is in the right position. Can cause issues when changing size of car
        super.onUpdate();
        if(!startPos.equals(this.getPosition())) {
            prevPos = this.getPosition();
        }
    }

    @Override
    protected void doBlockCollisions() {
        super.doBlockCollisions();

    }

    @Override
    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
        for (Seat seat : this.seats) {
            if (passenger.equals(seat.getOccupant())) {
                passenger.noClip = false;
                break;
            }
        }
    }

    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();
        //this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX-0.65f, this.posY+2f, this.posZ+ -2.9, 0.0f, 0.0f, 0.0f, new int[0]);
        //this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX+0.65f, this.posY+2f, this.posZ+ -2.9, 0.0f, 0.0f, 0.0f, new int[0]);
        if (this.forward() && this.isFlying) {
            this.rotationAmount += 1f;
        } else if (this.backward() && this.isFlying) {
            this.rotationAmount -= 1f;
        }else{
            if(this.rotationAmount < 0f){
                this.rotationAmount += 1f;
            }else if(this.rotationAmount >0f){
                this.rotationAmount -= 1f;
            }
        }
        if (this.left() && this.isFlying) {
            this.sideRotationAmount += 1f;
        } else if (this.right() && this.isFlying) {
            this.sideRotationAmount -= 1f;
        }else{
            if(this.sideRotationAmount < 0f){
                this.sideRotationAmount += 1f;
            }else if(this.sideRotationAmount > 0f){
                this.sideRotationAmount -= 1f;
            }
        }
/*
        if(!(this.forward())){
            this.rotationAmount -=1f;
            if(this.rotationAmount <=0f)
                this.rotationAmount = 0f;
        }else if(!this.backward()){
            this.rotationAmount += 1f;
            if(this.rotationAmount >= 0f)
                this.rotationAmount = 0f;
        }
        */
        if(this.rotationAmount >= MAXMOVEMENTROTATION){
            this.rotationAmount = MAXMOVEMENTROTATION;
        }
        if(this.rotationAmount <= -MAXMOVEMENTROTATION){
            this.rotationAmount = -MAXMOVEMENTROTATION;
        }
        if(this.sideRotationAmount >= MAXMOVEMENTROTATION){
            this.sideRotationAmount = MAXMOVEMENTROTATION;
        }
        if(this.sideRotationAmount <= -MAXMOVEMENTROTATION){
            this.sideRotationAmount = -MAXMOVEMENTROTATION;
        }

        rotationYawInterp.reset(this.rotationYaw - 180D);
        if (forward()) {
            lastDirBackwards = false;
        } else if (backward()) {
            lastDirBackwards = true;
        }
        this.interpRotationPitch.setTarget(this.direction.zCoord * -30D);
        this.interpRotationRoll.setTarget(this.direction.xCoord * 20D);
        if (this.seats[0].getOccupant() != null) {
            if (KeyBindingHandler.HELICOPTER_UP.isKeyDown()) {
                this.motionY += 0.2f;
                if (this.motionY >= 5f) {
                    this.motionY = 5f;
                }

                this.isFlying = true;
                this.shouldFallDamage = true;
                this.rotorRotationAmount += 0.1f;
                this.setNoGravity(true);
            } else if (KeyBindingHandler.HELICOPTER_DOWN.isKeyDown()) {
                this.motionY -= 0.3f;
                if (this.motionY <= -5f) {
                    this.motionY = -5f;
                }

                if(this.onGround == true) {
                    this.isFlying = false;
                }
            } else {
                if(!this.isFlying){
                    this.setNoGravity(false);

                }else{
                    this.rotorRotationAmount += 0.1f;
                }
            }
        }

        if(!this.isFlying){
            this.speedModifier = -0.75f;
            this.rotorRotationAmount -= 0.2f;
        }else{
            this.speedModifier = 1.5f;


        }
        if(!this.shouldGearLift) {
            this.gearLift += 0.02f;
        }else{
            this.gearLift -= 0.02f;
        }
        if(world.getBlockState(new BlockPos.MutableBlockPos((int)Math.floor( this.posX), (int)Math.floor( this.posY - 10f), (int)Math.floor( this.posZ))).getBlock() != Blocks.AIR){
            this.shouldGearLift = false;
        }else{
            this.shouldGearLift = true;
        }
        if(this.seats[0].getOccupant() == null){
            this.setNoGravity(false);
        }
        if(this.seats[0].getOccupant() == null && this.onGround == true && this.shouldFallDamage == true){
            this.setHealth(this.getHealth() - 20);
            this.shouldFallDamage = false;
        }
        if(this.rotorRotationAmount < 0f){
            this.rotorRotationAmount = 0f;
        }
        if(this.rotorRotationAmount > 4f){
            this.rotorRotationAmount = 5f;
        }
        if(this.gearLift < -0.5f){
            this.gearLift = -0.5f;
        }
        if(this.gearLift > 0){
            this.gearLift = 0f;
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setLong("PrevBlockPosition", this.prevPos.toLong());
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.prevPos = BlockPos.fromLong(compound.getLong("PrevBlockPosition"));
    }

    @Override
    public float getSoundVolume() {
        return this.getControllingPassenger() != null ? this.getSpeed().modifier / 2f : 0f;
    }



    @Nonnull
    @Override
    public EnumFacing getAdjustedHorizontalFacing() {
        return super.getAdjustedHorizontalFacing();
    }

    @Override
    protected WheelData createWheels() {
        return new WheelData(1, 2, -1, -2.2);
    }

    @Override
    protected boolean shouldTyresRender() {
        return false;
    }
    @Override
    public void updatePassenger(Entity passenger) {
        if (this.isPassenger(passenger)) {
            Seat seat = null;
            for (Seat s : this.seats) {
                if (passenger.equals(s.getOccupant())) {
                    seat = s;
                    break;
                }
            }
            Vec3d pos;
            if (seat == null) {
                pos = new Vec3d(this.posX, this.posY + this.height, this.posZ);
            } else {
                pos = seat.getPos();
            }
            passenger.setPosition(pos.x, pos.y + this.interpRotationPitch.getCurrent() / 75D, pos.z);
            passenger.rotationYaw += this.rotationDelta;
            passenger.setRotationYawHead(passenger.getRotationYawHead() + this.rotationDelta);
            if (passenger instanceof EntityLivingBase) {
                EntityLivingBase living = (EntityLivingBase) passenger;
                living.renderYawOffset += (living.rotationYaw - living.renderYawOffset) * 0.6F;
            }
        }
    }

/*
    @Override

    public Vector2d getBackWheelRotationPoint() {
        Vector2d point = super.getBackWheelRotationPoint();
        return new Vector2d(point.x, onRails ? 0 : point.y);
    }
    */

    @Override
    public float getCollisionBorderSize() {
        return 2.25f;
    }

    /* =================================== CAR END ===========================================*/
    /* ================================ MINECART START =======================================*/
/*

    public class MinecartLogic {
        private boolean isInReverse;
        private boolean prevKeyDown;
        private double adjustedRotationYaw;


        public EnumFacing getAdjustedHorizontalFacing() {
            return this.isInReverse ? getHorizontalFacing().getOpposite().rotateY() : getHorizontalFacing().rotateY();
        }

        public void onUpdate() {
            //CAR STUFF START
            rotationDelta *= 0.8f;
            allWheels.forEach(HelicopterEntity.this::processWheel);

            for(int i = 0; i < 4; i++) {
                List<WheelParticleData> markedRemoved = Lists.newArrayList();
                wheelDataList[i].forEach(wheel -> wheel.onUpdate(markedRemoved));
                markedRemoved.forEach(wheelDataList[i]::remove);
            }
            //CAR STUFF END

            if (posY < -64.0D) {
                outOfWorld();
            }
            MinecraftServer minecraftserver = world.getMinecraftServer();
            if (!world.isRemote && world instanceof WorldServer && minecraftserver != null) {
                world.profiler.startSection("portal");
                int i = getMaxInPortalTime();
                if (inPortal) {
                    if (minecraftserver.getAllowNether()) {
                        if (!isRiding() && portalCounter++ >= i) {
                            portalCounter = i;
                            timeUntilPortal = getPortalCooldown();
                            int j;
                            if (world.provider.getDimensionType().getId() == -1) {
                                j = 0;
                            } else {
                                j = -1;
                            }

                            changeDimension(j);
                        }

                        inPortal = false;
                    }
                } else {
                    if (portalCounter > 0) {
                        portalCounter -= 4;
                    }

                    if (portalCounter < 0) {
                        portalCounter = 0;
                    }
                }

                if (timeUntilPortal > 0) {
                    --timeUntilPortal;
                }

                world.profiler.endSection();
            }

            if (!hasNoGravity()) {
                motionY -= 0.03999999910593033D;
            }
            if(railTracks.equals(INACTIVE)) { //Shouldn't occur
                return;
            }
            if(getPassengers().isEmpty()) {
                return;
            }
            moveAlongTrack();

            if(!world.isRemote) {
                doBlockCollisions();
                rotationPitch = 0.0F;

                handleWaterMovement();
            }

        }

        protected void moveAlongTrack() {
            fallDistance = 0.0F;
            Vec3d vec3d = getPos();

            posY = (double)railTracks.getY();

            double slopeAdjustment = 0.0078125D;
            TourRailBlock.EnumRailDirection dir = TourRailBlock.getRailDirection(world, railTracks);

            EnumFacing facing = getFacingDir();

            switch (dir) {
                case ASCENDING_EAST:
                    motionX -= slopeAdjustment;
                    ++posY;
                    break;
                case ASCENDING_WEST:
                    motionX += slopeAdjustment;
                    ++posY;
                    break;
                case ASCENDING_NORTH:
                    motionZ += slopeAdjustment;
                    ++posY;
                    break;
                case ASCENDING_SOUTH:
                    motionZ -= slopeAdjustment;
                    ++posY;
            }
            double d1 = (double)(dir.getBackwardsX(facing) - dir.getForwardX(facing));
            double d2 = (double)(dir.getBackwardsZ(facing) - dir.getForwardZ(facing));
            double d3 = Math.sqrt(d1 * d1 + d2 * d2);
            double d4 = motionX * d1 + motionZ * d2;
            if (d4 < 0.0D) {
                d1 = -d1;
                d2 = -d2;
            }

            double d5 = Math.sqrt(motionX * motionX + motionZ * motionZ);

            if (d5 > 2.0D) {
                d5 = 2.0D;
            }
            double d = 1;
            if(forward()) {
                if(!prevKeyDown && isInReverse) {
                    d = -1;
                }
                isInReverse = false;
                prevKeyDown = true;
            } else if(backward()) {
                if(!prevKeyDown && !isInReverse) {
                    d = -1;
                }
                isInReverse = true;
                prevKeyDown = true;
            } else {
                prevKeyDown = false;
            }
            if(!world.isRemote) {
                d5 *= d;
            }


            motionX = d5 * d1 / d3;
            motionZ = d5 * d2 / d3;




            Vec3d vec = getPositionVector();
            Vec3d dirVec = new Vec3d(-d1, 0, d2).add(vec);
            double target = MathUtils.cosineFromPoints(vec.addVector(0, 0, 1), dirVec, vec);

            if(dirVec.x < vec.x) {
                target = -target;
            }

            this.adjustedRotationYaw = target;

            if(isInReverse) {
                target += 180F;
            }

            double d22;
            do {
                d22 = Math.abs(rotationYawInterp.getCurrent() - target);
                double d23 = Math.abs(rotationYawInterp.getCurrent() - (target + 360f));
                double d24 = Math.abs(rotationYawInterp.getCurrent() - (target - 360f));

                if(d23 < d22) {
                    target += 360f;
                } else if(d24 < d22) {
                    target -= 360f;
                }
            } while(d22 > 180);

            target = Math.round(target * 100D) / 100D;




            rotationYawInterp.setSpeed(this.getSpeedType().modifier * 4f);

            if(!prevOnRails) {
                rotationYawInterp.reset(target);
            } else if(d != -1) {
                rotationYawInterp.setTarget(target);
            }

            setRotation((float) rotationYawInterp.getCurrent(), rotationPitch);

            double d18 = (double)railTracks.getX() + 0.5D + (double)dir.getForwardX(facing) * 0.5D;
            double d19 = (double)railTracks.getZ() + 0.5D + (double)dir.getForwardZ(facing) * 0.5D;
            double d20 = (double)railTracks.getX() + 0.5D + (double)dir.getBackwardsX(facing) * 0.5D;
            double d21 = (double)railTracks.getZ() + 0.5D + (double)dir.getBackwardsZ(facing) * 0.5D;
            d1 = d20 - d18;
            d2 = d21 - d19;
            double d10;

            if (d1 == 0.0D) {
                posX = (double)railTracks.getX() + 0.5D;
                d10 = posZ - (double)railTracks.getZ();
            } else if (d2 == 0.0D) {
                posZ = (double)railTracks.getZ() + 0.5D;
                d10 = posX - (double)railTracks.getX();
            } else {
                double d11 = posX - d18;
                double d12 = posZ - d19;
                d10 = (d11 * d1 + d12 * d2) * 2.0D;
            }

            posX = d18 + d1 * d10;
            posZ = d19 + d2 * d10;
            setPosition(posX, posY, posZ);
            moveMinecartOnRail();

            double drag = isBeingRidden() ? 0.9D : 0.75D;

            motionX *= drag;
            motionZ *= drag;

            Vec3d vec3d1 = getPos();

            if (vec3d1 != null && vec3d != null) {
                double d14 = (vec3d.y - vec3d1.y) * 0.05D;
                d5 = Math.sqrt(motionX * motionX + motionZ * motionZ);

                if (d5 > 0.0D) {
                    motionX = motionX / d5 * (d5 + d14);
                    motionZ = motionZ / d5 * (d5 + d14);
                }
            }

            int j = MathHelper.floor(posX);
            int i = MathHelper.floor(posZ);

            if (j != railTracks.getX() || i != railTracks.getZ()) {
                d5 = Math.sqrt(motionX * motionX + motionZ * motionZ);
                motionX = d5 * (double)(j - railTracks.getX());
                motionZ = d5 * (double)(i - railTracks.getZ());
            }
            double d15 = Math.sqrt(motionX * motionX + motionZ * motionZ);
            if(d15 == 0) {
                d15 = 1;
            }
            double d16 = 0.06D;
            motionX += motionX / d15 * d16;
            motionZ += motionZ / d15 * d16;
        }

        private Vec3d getPos() {
            double x = posX;
            double y = posY;
            double z = posZ;

            IBlockState iblockstate = world.getBlockState(new BlockPos(railTracks));

            if (iblockstate.getBlock() instanceof TourRailBlock)
            {
                TourRailBlock.EnumRailDirection dir = TourRailBlock.getRailDirection(world, railTracks);

                EnumFacing facing = getFacingDir();

                double d0 = x + 0.5D + (double)dir.getForwardX(facing) * 0.5D;
                double d1 = y + 0.0625D + (double)dir.getForwardY(facing) * 0.5D;
                double d2 = z + 0.5D + (double)dir.getForwardZ(facing) * 0.5D;
                double d3 = x + 0.5D + (double)dir.getBackwardsX(facing) * 0.5D;
                double d4 = y + 0.0625D + (double)dir.getBackwardsY(facing) * 0.5D;
                double d5 = z + 0.5D + (double)dir.getBackwardsZ(facing) * 0.5D;
                double d6 = d3 - d0;
                double d7 = (d4 - d1) * 2.0D;
                double d8 = d5 - d2;
                double d9;

                if (d6 == 0.0D) {
                    d9 = z - z;
                } else if (d8 == 0.0D) {
                    d9 = x - x;
                } else {
                    double d10 = x - d0;
                    double d11 = z - d2;
                    d9 = (d10 * d6 + d11 * d8) * 2.0D;
                }

                x = d0 + d6 * d9;
                y = d1 + d7 * d9;
                z = d2 + d8 * d9;

                if (d7 < 0.0D) {
                    ++y;
                }

                if (d7 > 0.0D) {
                    y += 0.5D;
                }

                return new Vec3d(x, y, z);
            } else {
                return null;
            }
        }

        private void moveMinecartOnRail() {
            double mX = motionX;
            double mZ = motionZ;
            if(mX == 0 && mZ == 0 && !getPassengers().isEmpty()) { //Should only happen when re-logging. //TODO: make a more elegant solution
                mX = getLook(1f).x;
                mZ = getLook(1f).z;
            }

            double max = getSpeedType().modifier / 8f;
            mX = MathHelper.clamp(mX, -max, max);
            mZ = MathHelper.clamp(mZ, -max, max);
            HelicopterEntity.this.move(MoverType.SELF, mX, 0D, mZ);
        }

        private Speed getSpeedType() {
            return ((TourRailBlock)world.getBlockState(railTracks).getBlock()).getSpeedType().getSpeed(getSpeed());
        }

        private EnumFacing getFacingDir() {
            EnumFacing facing = EnumFacing.getHorizontal(MathHelper.floor(this.adjustedRotationYaw * 4.0D / 360.0D + 0.5D) & 3);
            if(this.isInReverse) {
                facing = facing.getOpposite();
            }
            return facing;
        }
    }
    /* ================================= MINECART END ========================================*/
}