package com.goldkl.touhoutinkermodifier.mixin.cataclysm;

import com.github.L_Ender.cataclysm.entity.AnimationMonster.BossMonsters.LLibrary_Boss_Monster;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LLibrary_Boss_Monster.class)
public interface LLibrary_Boss_MonsterAccessor {
    @Accessor(value = "damageBucket",remap = false)
    float getdamageBucket();
    @Accessor(value = "damageBucket",remap = false)
    void setdamageBucket(float damageBucket);
}
