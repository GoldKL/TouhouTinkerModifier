package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.ModifierIds;
import com.goldkl.touhoutinkermodifier.registries.TagsRegistry;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierTraitHook;
import slimeknights.tconstruct.library.modifiers.modules.behavior.AttributeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;

public class BibliophiliaModifier extends Modifier {
    //识文解字：本居小铃 每层增加7.5%的法术强度
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        //hookBuilder.addHook(this);
        super.registerHooks(hookBuilder);
        hookBuilder.addModule(AttributeModule.builder(AttributeRegistry.SPELL_POWER.get(), AttributeModifier.Operation.MULTIPLY_BASE).uniqueFrom(ModifierIds.bibliophilia).eachLevel(0.2f));
    }
}
