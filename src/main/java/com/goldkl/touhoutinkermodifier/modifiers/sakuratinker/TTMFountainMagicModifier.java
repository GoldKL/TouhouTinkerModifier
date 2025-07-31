package com.goldkl.touhoutinkermodifier.modifiers.sakuratinker;


import com.hollingsworth.arsnouveau.api.perk.PerkAttributes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.modules.behavior.AttributeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;

public class TTMFountainMagicModifier extends Modifier {
    private final static ResourceLocation modifierId = ResourceLocation.fromNamespaceAndPath("sakuratinker","fountain_magic");
    @Override
    public void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addModule(AttributeModule.builder(PerkAttributes.MAX_MANA.get(), AttributeModifier.Operation.ADDITION).uniqueFrom(modifierId).eachLevel(800));
        hookBuilder.addModule(AttributeModule.builder(PerkAttributes.MANA_REGEN_BONUS.get(), AttributeModifier.Operation.ADDITION).uniqueFrom(modifierId).eachLevel(50));
    }
}
