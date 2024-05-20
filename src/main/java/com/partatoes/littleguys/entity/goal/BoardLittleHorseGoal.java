package com.partatoes.littleguys.entity.goal;

import com.partatoes.littleguys.entity.custom.LittleGuyEntity;
import com.partatoes.littleguys.entity.custom.LittleHorseEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.List;

public class BoardLittleHorseGoal extends Goal {

    private LittleGuyEntity mob;
    private LittleHorseEntity target;
    public BoardLittleHorseGoal(LittleGuyEntity entity) {
        this.mob = entity;
    }

    @Override
    public boolean canStart() {
        List<LittleHorseEntity> littleHorsesList = this.mob.getWorld().getNonSpectatingEntities(LittleHorseEntity.class, this.mob.getBoundingBox().expand(5.0));
        boolean ret = false;
        for (LittleHorseEntity horse : littleHorsesList) {
            Entity entity = horse.getControllingPassenger();
            if (entity == null) {
                ret = true;
                this.target = horse;
            }
        }
        return ret && !this.mob.hasVehicle();
    }

    @Override
    public boolean canStop() {
        return true;
    }

    @Override
    public void stop() {
        this.target = null;
        this.mob.getNavigation().stop();
    }

    @Override
    public boolean shouldContinue() {
        // In the future, we can have an additional attribute on the LittleGuyEntity, `ticksSinceAttacked` or something
        //   that shows the last time it was attacked.  If it was recently attacked, it should likely fight back instead
        //   of getting on the horse.
        if (!this.target.isAlive()) {
            return false;
        }
        if (this.mob.squaredDistanceTo(this.target) > 225.0) {
            return false;
        }
        return !this.mob.getNavigation().isIdle() || this.canStart();
    }

    @Override
    public void tick() {
        this.mob.getLookControl().lookAt(this.target, 30.0f, 30.0f);
//        double d = this.mob.getWidth() * 2.0f * (this.mob.getWidth() * 2.0f);
        double d = 1;
        double e = this.mob.squaredDistanceTo(this.target.getX(), this.target.getY(), this.target.getZ());
        double f = 0.8;
        if (e > d && e < 16.0) {
            f = 1.33;
        } else if (e < 225.0) {
            f = 0.6;
        }
        this.mob.getNavigation().startMovingTo(this.target, f);
        if (e > d) {
            return;
        }
//        this.mob.tryAttack(this.target);
        this.mob.startRiding(this.target);
    }
}
