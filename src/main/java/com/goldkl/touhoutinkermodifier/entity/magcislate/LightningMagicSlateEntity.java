package com.goldkl.touhoutinkermodifier.entity.magcislate;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;

public class LightningMagicSlateEntity extends MagicSlateEntity{
    public LightningMagicSlateEntity(EntityType<? extends AbstractSpellCastingMob> p_21368_, Level p_21369_) {
        super(p_21368_, p_21369_);
    }

    @Override
    protected AbstractSpell getUseSpell() {
        return SpellRegistry.LIGHTNING_LANCE_SPELL.get();
    }

    @Override
    protected float getMinSpellQuality() {
        return 0.5f;
    }

    @Override
    protected float getMaxSpellQuality() {
        return 0.7f;
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_KNOCKBACK, 0.0)
                .add(Attributes.ATTACK_DAMAGE, 0.0)
                .add(Attributes.MAX_HEALTH, 50)
                .add(Attributes.ARMOR, 20)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1)
                .add(Attributes.FOLLOW_RANGE, 32)
                .add(ForgeMod.ENTITY_REACH.get(), 4)
                .add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 2)
                .add(Attributes.MOVEMENT_SPEED, .35)
                .add(Attributes.FLYING_SPEED,0.6F)
                .add(AttributeRegistry.SPELL_RESIST.get(), 1.5)
                .add(AttributeRegistry.LIGHTNING_SPELL_POWER.get(), 2);
    }
}
