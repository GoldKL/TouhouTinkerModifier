package com.goldkl.touhoutinkermodifier.api;

import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

public interface IAbstractSpell {
    int touhouTinkerModifier$getEntityManaCost(int level, @Nullable Entity sourceEntity);
}
