package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.TTMModifierIds;
import com.goldkl.touhoutinkermodifier.registries.TagsRegistry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.materials.definition.MaterialVariant;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import javax.annotation.Nullable;

public class SecretsealingclubModifier extends NoLevelsModifier{
    @Override
    public Component getDisplayName(IToolStackView tool, ModifierEntry entry, @Nullable RegistryAccess access) {
        if(SecretSealingClubcanUse(tool)) {
            return entry.getDisplayName();
        }
        return entry.getDisplayName().copy().withStyle((style) -> style.withStrikethrough(true)
                .withItalic(true)
                .withColor(TextColor.fromRgb(0x808080)));
    }
    public static boolean SecretSealingClubcanUse(IToolContext tool)
    {
        if(tool.getModifier(TTMModifierIds.secretsealingclub) == ModifierEntry.EMPTY)return false;
        boolean renko = false,maribel = false;
        for (MaterialVariant material : tool.getMaterials()) {
            renko = renko | MaterialRegistry.getInstance().isInTag(material.getId(),TagsRegistry.MaterialsTag.Renko);
            maribel = maribel | MaterialRegistry.getInstance().isInTag(material.getId(),TagsRegistry.MaterialsTag.Maribel);
        }
        return renko && maribel;
    }
}
