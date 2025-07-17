package com.goldkl.touhoutinkermodifier.registries;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;

import com.goldkl.touhoutinkermodifier.hook.AttackerWithEquipmentModifyDamageModifierHook;
import com.goldkl.touhoutinkermodifier.hook.BetterCombatAtackRangeHook;
import com.goldkl.touhoutinkermodifier.hook.EntityDodgeHook;
import com.goldkl.touhoutinkermodifier.hook.NightVisionHook;
import slimeknights.tconstruct.library.modifiers.hook.armor.EquipmentChangeModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.armor.OnAttackedModifierHook;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
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

    private static <T> ModuleHook<T> register(String name, Class<T> filter, @Nullable Function<Collection<T>,T> merger, T defaultInstance) {
        return ModifierHooks.register(TouhouTinkerModifier.getResource(name), filter, merger, defaultInstance);
    }
    private static <T> ModuleHook<T> register(String name, Class<T> filter, T defaultInstance) {
        return register(name, filter, null, defaultInstance);
    }
}
