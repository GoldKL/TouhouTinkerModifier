package com.goldkl.touhoutinkermodifier.api.event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Event;

import javax.annotation.Nullable;

@Event.HasResult
public class PredodgeEvent extends LivingEvent {
    @Nullable
    private final Entity attacker;
    @Nullable
    private final Entity directattacker;

    public PredodgeEvent(LivingEntity entity,@Nullable Entity attacker,@Nullable Entity directattacker) {
        super(entity);
        this.attacker = attacker;
        this.directattacker = directattacker;
    }

    @Nullable
    public Entity getAttacker() {
        return attacker;
    }

    @Nullable
    public Entity getDirectattacker() {
        return directattacker;
    }
}
