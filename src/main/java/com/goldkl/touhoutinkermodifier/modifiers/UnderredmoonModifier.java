package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.ModifierIds;
import dev.shadowsoffire.attributeslib.api.ALObjects;
import dev.xkmc.l2damagetracker.init.L2DamageTracker;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.modules.behavior.AttributeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;


public class UnderredmoonModifier extends Modifier {
    //绯色月下：芙兰朵路·斯卡雷特
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addModule(AttributeModule.builder(L2DamageTracker.CRIT_DMG.get(), AttributeModifier.Operation.ADDITION).uniqueFrom(ModifierIds.underredmoon).eachLevel(0.5f));
        hookBuilder.addModule(AttributeModule.builder(L2DamageTracker.CRIT_RATE.get(), AttributeModifier.Operation.ADDITION).uniqueFrom(ModifierIds.underredmoon).eachLevel(0.1f));
        hookBuilder.addModule(AttributeModule.builder(ALObjects.Attributes.ARMOR_SHRED.get(), AttributeModifier.Operation.ADDITION).uniqueFrom(ModifierIds.underredmoon).eachLevel(0.1f));

    }
}
