package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.TTMModifierIds;
import com.goldkl.touhoutinkermodifier.registries.TagsRegistry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.materials.definition.MaterialVariant;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierTraitHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import javax.annotation.Nullable;

public class KomeijisistersModifier extends NoLevelsModifier implements ModifierTraitHook{
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.MODIFIER_TRAITS);
    }

    @Override
    public void addTraits(IToolContext context, ModifierEntry modifier, ModifierTraitHook.TraitBuilder builder, boolean firstEncounter) {
        if(KomeijisisterscanUse(context)){
            if(context.getModifier(TTMModifierIds.terriblesouvenir) != ModifierEntry.EMPTY){
                builder.add(TTMModifierIds.terriblesouvenir,1);
            }
            if(context.getModifier(TTMModifierIds.overthinking) != ModifierEntry.EMPTY){
                builder.add(TTMModifierIds.overthinking,1);
            }
        }
    }
    @Override
    public Component getDisplayName(IToolStackView tool, ModifierEntry entry, @Nullable RegistryAccess access) {
        if(KomeijisisterscanUse(tool)) {
            return entry.getDisplayName();
        }
        return entry.getDisplayName().copy().withStyle((style) -> style.withStrikethrough(true)
                .withItalic(true)
                .withColor(TextColor.fromRgb(0x808080)));
    }
    public static boolean KomeijisisterscanUse(IToolContext tool)
    {
        if(tool.getModifier(TTMModifierIds.komeijisisters) == ModifierEntry.EMPTY)return false;
        boolean satori = false,koishi = false;
        for (MaterialVariant material : tool.getMaterials()) {
            satori = satori | MaterialRegistry.getInstance().isInTag(material.getId(),TagsRegistry.MaterialsTag.Satori);
            koishi = koishi | MaterialRegistry.getInstance().isInTag(material.getId(),TagsRegistry.MaterialsTag.Koishi);
        }
        return satori && koishi;
    }
}
