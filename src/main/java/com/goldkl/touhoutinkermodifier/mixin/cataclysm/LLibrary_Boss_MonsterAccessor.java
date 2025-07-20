package com.goldkl.touhoutinkermodifier.mixin.cataclysm;

import com.github.L_Ender.cataclysm.entity.AnimationMonster.BossMonsters.LLibrary_Boss_Monster;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LLibrary_Boss_Monster.class)
public interface LLibrary_Boss_MonsterAccessor {
    @Accessor(value = "reducedDamageTicks",remap = false)
    int getreducedDamageTicks();
    @Accessor(value = "reducedDamageTicks",remap = false)
    void setreducedDamageTicks(int reducedDamageTicks);
}
