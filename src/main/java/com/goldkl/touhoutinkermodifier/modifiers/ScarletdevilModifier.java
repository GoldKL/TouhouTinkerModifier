package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.ModifierIds;
import com.hollingsworth.arsnouveau.api.perk.PerkAttributes;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.modules.behavior.AttributeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;

public class ScarletdevilModifier extends Modifier{
    //鲜红恶魔：蕾米莉亚&芙兰朵露
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        //hookBuilder.addHook(this);
        super.registerHooks(hookBuilder);
        hookBuilder.addModule(AttributeModule.builder(AttributeRegistry.BLOOD_SPELL_POWER.get(), AttributeModifier.Operation.MULTIPLY_BASE).uniqueFrom(ModifierIds.scarletdevil).eachLevel(0.2f));
        hookBuilder.addModule(AttributeModule.builder(PerkAttributes.MANA_REGEN_BONUS.get(), AttributeModifier.Operation.ADDITION).uniqueFrom(ModifierIds.scarletdevil).eachLevel(5f));
    }
}
