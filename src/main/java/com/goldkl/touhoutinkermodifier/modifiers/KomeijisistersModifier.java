package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.TTMModifierIds;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
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
        builder.add(TTMModifierIds.terriblesouvenir,1);
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
    public static boolean KomeijisisterscanUse(IToolStackView tool)
    {
        return tool.getModifier(TTMModifierIds.komeijisisters) != ModifierEntry.EMPTY
                && tool.getModifier(TTMModifierIds.terriblesouvenir) != ModifierEntry.EMPTY
                && tool.getModifier(TTMModifierIds.koishiseye) != ModifierEntry.EMPTY;
    }
}
