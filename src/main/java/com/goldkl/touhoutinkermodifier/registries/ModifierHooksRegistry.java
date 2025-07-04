package com.goldkl.touhoutinkermodifier.registries;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;

import com.goldkl.touhoutinkermodifier.hook.AttackerWithEquipmentModifyDamageModifierHook;
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
        AttackerWithEquipmentModifyDamageModifierHook fallback = (tool, modifier, context, slotType, source, amount, isDirectDamage) -> amount;
        ATTACKER_MODIFY_HURT = register("attacker_modify_hurt", AttackerWithEquipmentModifyDamageModifierHook.class, merger, fallback);
        ATTACKER_MODIFY_DAMAGE = register("attacker_modify_damage", AttackerWithEquipmentModifyDamageModifierHook.class, merger, fallback);
    }
    private static <T> ModuleHook<T> register(String name, Class<T> filter, @Nullable Function<Collection<T>,T> merger, T defaultInstance) {
        return ModifierHooks.register(TouhouTinkerModifier.getResource(name), filter, merger, defaultInstance);
    }
    private static <T> ModuleHook<T> register(String name, Class<T> filter, T defaultInstance) {
        return register(name, filter, null, defaultInstance);
    }
}
