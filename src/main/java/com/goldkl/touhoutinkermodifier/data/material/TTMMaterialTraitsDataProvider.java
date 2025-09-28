package com.goldkl.touhoutinkermodifier.data.material;

import com.goldkl.touhoutinkermodifier.data.TTMModifierIds;
import net.minecraft.data.PackOutput;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;
import slimeknights.tconstruct.library.data.material.AbstractMaterialTraitDataProvider;
import slimeknights.tconstruct.library.modifiers.ModifierId;

import static slimeknights.tconstruct.library.materials.MaterialRegistry.MELEE_HARVEST;

public class TTMMaterialTraitsDataProvider extends AbstractMaterialTraitDataProvider {
    public TTMMaterialTraitsDataProvider(PackOutput packOutput, AbstractMaterialDataProvider materials) {
        super(packOutput, materials);
    }

    @Override
    public String getName() {
        return "Touhou Tinker Modifier's Material Traits";
    }

    @Override
    protected void addMaterialTraits() {
        //tier 1
        addDefaultTraits(TTMMaterialIds.yinangorb, new ModifierId[]{});
        //tier 2
        addTraits(TTMMaterialIds.spellpaper, MELEE_HARVEST, TTMModifierIds.jikicoercion);
        addTraits(TTMMaterialIds.persuasionneedle, MELEE_HARVEST, TTMModifierIds.jikicoercion);
        addTraits(TTMMaterialIds.konpeito, MELEE_HARVEST, TTMModifierIds.jikicoercion);
        addTraits(TTMMaterialIds.eighttrigramsfurnace, MELEE_HARVEST, TTMModifierIds.jikicoercion);

    }
}