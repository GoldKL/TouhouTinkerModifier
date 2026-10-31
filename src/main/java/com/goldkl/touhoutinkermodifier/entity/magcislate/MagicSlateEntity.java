package com.goldkl.touhoutinkermodifier.entity.magcislate;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.entity.mobs.IAnimatedAttacker;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.goals.MomentHurtByTargetGoal;
import io.redspace.ironsspellbooks.entity.mobs.goals.WizardAttackGoal;
import io.redspace.ironsspellbooks.entity.mobs.ice_spider.IceSpiderEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public abstract class MagicSlateEntity extends AbstractSpellCastingMob implements GeoEntity, IAnimatedAttacker, FlyingAnimal {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final AnimationController<MagicSlateEntity> meleeController = new AnimationController<>(this, "melee_animations", 0, this::predicate);
    RawAnimation animationToPlay = null;
    protected MagicSlateEntity(EntityType<? extends AbstractSpellCastingMob> p_21368_, Level p_21369_) {
        super(p_21368_, p_21369_);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.xpReward = 25;
    }
    protected abstract AbstractSpell getUseSpell();
    protected abstract float getMinSpellQuality();
    protected abstract float getMaxSpellQuality();
    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(2, new WizardAttackGoal(this, 1f, 25, 50)
                .setSpells(
                        List.of(this.getUseSpell()),
                        List.of(),
                        List.of(),
                        List.of()
                )
                .setSpellQuality(this.getMinSpellQuality(),this.getMaxSpellQuality())
        );
        this.goalSelector.addGoal(3, new WaterAvoidingRandomFlyingGoal(this, 1.0D));
        this.targetSelector.addGoal(1, new MomentHurtByTargetGoal(this, MagicSlateEntity.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, true, livingEntity ->
                livingEntity instanceof Player
                        || livingEntity instanceof IronGolem));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class, true, livingEntity ->
                livingEntity instanceof Animal
                        || livingEntity instanceof AbstractVillager
                        || livingEntity instanceof Raider));
    }
    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level p_186262_) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, p_186262_);
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(meleeController);
    }
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void playAnimation(String animationId) {
        animationToPlay = RawAnimation.begin().thenPlay(animationId);
    }

    private PlayState predicate(AnimationState<MagicSlateEntity> animationEvent) {
        var controller = animationEvent.getController();

        if (this.animationToPlay != null) {
            controller.forceAnimationReset();
            controller.setAnimation(animationToPlay);
            animationToPlay = null;
        }
        return PlayState.CONTINUE;
    }
    public boolean isFlying() {
        return !this.onGround();
    }
    @Override
    public boolean isAnimating() {
        return meleeController.getAnimationState() == AnimationController.State.RUNNING;
    }
    /*@Override
    public void initiateCastSpell(AbstractSpell spell, int spellLevel) {
        serverTriggerAnimation("attack");
        super.initiateCastSpell(spell, spellLevel);
    }*/
    @Override
    public void castComplete() {
        if (!level().isClientSide) {
            serverTriggerAnimation("attack");
        }
        super.castComplete();
    }
}
