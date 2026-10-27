package com.partatoes.littleguys.entity.goal;

import com.partatoes.littleguys.entity.custom.LittleGuyEntity;
import com.partatoes.littleguys.entity.custom.LittleHorseEntity;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;

public class BoardLittleHorseGoal extends Goal {

    private LittleGuyEntity mob;
    private LittleHorseEntity target;
    public BoardLittleHorseGoal(LittleGuyEntity entity) {
        this.mob = entity;
    }

    @Override
    public boolean canUse() {
        List<LittleHorseEntity> littleHorsesList = this.mob.level().getEntitiesOfClass(LittleHorseEntity.class, this.mob.getBoundingBox().inflate(5.0));
        boolean ret = false;
        for (LittleHorseEntity horse : littleHorsesList) {
            Entity entity = horse.getControllingPassenger();
            if (entity == null) {
                ret = true;
                this.target = horse;
            }
        }
        return ret && !this.mob.isPassenger();
    }

    @Override
    public void stop() {
        this.target = null;
        this.mob.getNavigation().stop();
    }

    @Override
    public boolean canContinueToUse() {
        // In the future, we can have an additional attribute on the LittleGuyEntity, `ticksSinceAttacked` or something
        //   that shows the last time it was attacked.  If it was recently attacked, it should likely fight back instead
        //   of getting on the horse.
        if (!this.target.isAlive()) {
            return false;
        }
        if (this.mob.distanceToSqr(this.target) > 225.0) {
            return false;
        }
        return !this.mob.getNavigation().isDone() || this.canUse();
    }

    @Override
    public void tick() {
        this.mob.getLookControl().setLookAt(this.target, 30.0f, 30.0f);
//        double d = this.mob.getWidth() * 2.0f * (this.mob.getWidth() * 2.0f);
        double d = 1;
        double e = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
        double f = 0.8;
        if (e > d && e < 16.0) {
            f = 1.33;
        } else if (e < 225.0) {
            f = 0.6;
        }
        this.mob.getNavigation().moveTo(this.target, f);
        if (e > d) {
            return;
        }
//        this.mob.tryAttack(this.target);
        this.mob.startRiding(this.target);
    }
}
