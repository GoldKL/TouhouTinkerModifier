package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.ModifierIds;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.modules.behavior.AttributeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;

public class BibliophiliaModifier extends Modifier{
    //识文解字：每层增加7.5%的法术强度
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        //hookBuilder.addHook(this);
        super.registerHooks(hookBuilder);
        hookBuilder.addModule(AttributeModule.builder(AttributeRegistry.SPELL_POWER.get(), AttributeModifier.Operation.MULTIPLY_BASE).uniqueFrom(ModifierIds.bibliophilia).eachLevel(0.075f));
    }
}
