package com.goldkl.touhoutinkermodifier.mixin.youkaishomecoming;

import dev.xkmc.youkaishomecoming.content.entity.boss.BossYoukaiEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BossYoukaiEntity.class)
public interface BossYoukaiEntityAccessor {
    @Accessor(value = "hurtCD",remap = false)
    int gethurtCD();
    @Accessor(value = "hurtCD",remap = false)
    void sethurtCD(int hurtCD);
}
