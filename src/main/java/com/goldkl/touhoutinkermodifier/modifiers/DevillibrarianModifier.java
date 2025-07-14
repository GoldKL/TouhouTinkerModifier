package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.ModifierIds;
import com.goldkl.touhoutinkermodifier.hook.AttackerWithEquipmentModifyDamageModifierHook;
import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.modules.technical.SlotInChargeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class DevillibrarianModifier extends Modifier implements AttackerWithEquipmentModifyDamageModifierHook {
    //恶魔使魔：小恶魔&&姆Q
    public static final TinkerDataCapability.TinkerDataKey<SlotInChargeModule.SlotInCharge> SLOT_IN_CHARGE = TinkerDataCapability.TinkerDataKey.of(ModifierIds.devillibrarian);
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooksRegistry.ATTACKER_MODIFY_DAMAGE);
        hookBuilder.addModule(new SlotInChargeModule(SLOT_IN_CHARGE));
    }

    @Override
    public float attackermodifyDamageTaken(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source,float baseamount , float amount, boolean isDirectDamage) {
        if (source instanceof SpellDamageSource spellDamageSource && spellDamageSource.hasPostHitEffects() && SlotInChargeModule.isInCharge(context.getTinkerData(), SLOT_IN_CHARGE, slotType)) {
            Entity attacker = source.getEntity();
            float level = (float)SlotInChargeModule.getLevel(context.getTinkerData(), SLOT_IN_CHARGE, slotType);
            float scale = level / (level + 2);
            if (attacker instanceof LivingEntity livingAttacker) {
                if (spellDamageSource.getLifestealPercent() > 0) {
                    float maxabsorb = livingAttacker.getMaxHealth() * 0.5f;
                    livingAttacker.setAbsorptionAmount(Math.min(maxabsorb,livingAttacker.getAbsorptionAmount() + spellDamageSource.getLifestealPercent() * amount * scale));
                }
            }
        }
        return amount;
    }
}
