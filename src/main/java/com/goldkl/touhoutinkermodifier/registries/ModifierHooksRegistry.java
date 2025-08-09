package com.goldkl.touhoutinkermodifier.registries;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;

import com.goldkl.touhoutinkermodifier.hook.*;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.hook.armor.EquipmentChangeModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.armor.OnAttackedModifierHook;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.function.Function;

public class ModifierHooksRegistry {
    public static final ModuleHook<AttackerWithEquipmentModifyDamageModifierHook> ATTACKER_MODIFY_HURT;
    public static final ModuleHook<AttackerWithEquipmentModifyDamageModifierHook> ATTACKER_MODIFY_DAMAGE;
    static {
        Function<Collection<AttackerWithEquipmentModifyDamageModifierHook>,AttackerWithEquipmentModifyDamageModifierHook> merger = AttackerWithEquipmentModifyDamageModifierHook.AllMerger::new;
        AttackerWithEquipmentModifyDamageModifierHook fallback = (tool, modifier, context, slotType, source,baseamount , amount, isDirectDamage) -> amount;
        ATTACKER_MODIFY_HURT = register("attacker_modify_hurt", AttackerWithEquipmentModifyDamageModifierHook.class, merger, fallback);
        ATTACKER_MODIFY_DAMAGE = register("attacker_modify_damage", AttackerWithEquipmentModifyDamageModifierHook.class, merger, fallback);
    }
    public static final ModuleHook<NightVisionHook> NIGHT_VISION_HOOK = register("night_vision_hook", NightVisionHook.class, NightVisionHook.AllMerger::new,new NightVisionHook() {});
    public static final ModuleHook<BetterCombatAtackRangeHook> ATTACK_RANGE_ADD;
    public static final ModuleHook<BetterCombatAtackRangeHook> ATTACK_RANGE_MUL;
    static {
        Function<Collection<BetterCombatAtackRangeHook>,BetterCombatAtackRangeHook> merger = BetterCombatAtackRangeHook.AllMerger::new;
        BetterCombatAtackRangeHook fallback = (tool, modifier, context, slotType,IsAdd , amount,tool_attack_range,hand) -> amount;
        ATTACK_RANGE_ADD = register("attack_range_add", BetterCombatAtackRangeHook.class, merger, fallback);
        ATTACK_RANGE_MUL = register("attack_range_mul", BetterCombatAtackRangeHook.class, merger, fallback);
    }
    public static final ModuleHook<EntityDodgeHook> ENTITY_DODGE_HOOK = register("entity_dodge_hook", EntityDodgeHook.class, EntityDodgeHook.AllMerger::new,new EntityDodgeHook() {});
    public static final ModuleHook<EntityHealHook> ENTITY_HEAL_HOOK = register("entity_heal_hook", EntityHealHook.class, EntityHealHook.AllMerger::new,new EntityHealHook() {});
    public static final ModuleHook<EntityEffectApplicableModifierHook>ENTITY_EFFECT_APPLICABLE_HURT = register("entity_effect_applicable_modifier_hook",EntityEffectApplicableModifierHook.class,EntityEffectApplicableModifierHook.AllMerger::new, (tool, entry, slot, instance, entity, notApplicable) -> notApplicable);
    public static final ModuleHook<MeleeDamagePercentModifierHook>MELEE_DAMAGE_PERCENT = register("melee_damage_percent_hook", MeleeDamagePercentModifierHook.class,MeleeDamagePercentModifierHook.AllMerger::new,new MeleeDamagePercentModifierHook(){});
    private static <T> ModuleHook<T> register(String name, Class<T> filter, @Nullable Function<Collection<T>,T> merger, T defaultInstance) {
        return ModifierHooks.register(TouhouTinkerModifier.getResource(name), filter, merger, defaultInstance);
    }
    private static <T> ModuleHook<T> register(String name, Class<T> filter, T defaultInstance) {
        return register(name, filter, null, defaultInstance);
    }
}
