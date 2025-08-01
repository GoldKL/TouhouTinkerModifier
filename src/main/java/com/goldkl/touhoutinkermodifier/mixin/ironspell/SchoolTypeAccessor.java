package com.goldkl.touhoutinkermodifier.mixin.ironspell;

import io.redspace.ironsspellbooks.api.spells.SchoolType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SchoolType.class)
public interface SchoolTypeAccessor {
    @Accessor(value = "powerAttribute",remap = false)
    LazyOptional<Attribute> getpowerAttribute();
    @Accessor(value ="resistanceAttribute",remap = false)
    LazyOptional<Attribute> getresistanceAttribute();
}
