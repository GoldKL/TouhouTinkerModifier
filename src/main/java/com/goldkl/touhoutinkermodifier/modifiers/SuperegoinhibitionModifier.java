package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.hook.AttackerWithEquipmentModifyDamageModifierHook;
import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import dev.shadowsoffire.attributeslib.api.ALObjects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class SuperegoinhibitionModifier extends Modifier implements AttackerWithEquipmentModifyDamageModifierHook {
    //超我抑制：古明地恋
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooksRegistry.ATTACKER_MODIFY_HURT);
    }

    @Override
    public void attackermodifyDamageTaken(IToolStackView tool, ModifierEntry modifier, LivingEntity target, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float baseamount, DamageModifier damageModifier, boolean isDirectDamage) {
        if(slotType == EquipmentSlot.OFFHAND && tool.hasTag(TinkerTags.Items.BOWS) && isDirectDamage
        && (source.is(DamageTypes.PLAYER_ATTACK)||source.is(DamageTypes.MOB_ATTACK))) {
            int level = modifier.getLevel();
            double fixdamage = tool.getStats().get(ToolStats.PROJECTILE_DAMAGE) * context.getEntity().getAttributeValue(ALObjects.Attributes.ARROW_DAMAGE.get());
            fixdamage *= 0.9 + level * 0.1;
            damageModifier.addFixed((float) fixdamage);
        }
    }
}
