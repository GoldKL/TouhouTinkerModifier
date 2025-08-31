package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.ModifierIds;
import com.goldkl.touhoutinkermodifier.hook.NightVisionHook;
import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import com.hollingsworth.arsnouveau.api.perk.PerkAttributes;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.ForgeMod;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.modules.behavior.AttributeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class ScarletdevilModifier extends Modifier implements NightVisionHook {
    //鲜红恶魔：蕾米莉亚&芙兰朵露
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        //hookBuilder.addHook(this);
        super.registerHooks(hookBuilder);
        hookBuilder.addModule(AttributeModule.builder(AttributeRegistry.BLOOD_SPELL_POWER.get(), AttributeModifier.Operation.MULTIPLY_BASE).uniqueFrom(ModifierIds.scarletdevil).eachLevel(0.3f));
        hookBuilder.addModule(AttributeModule.builder(PerkAttributes.MANA_REGEN_BONUS.get(), AttributeModifier.Operation.ADDITION).uniqueFrom(ModifierIds.scarletdevil).eachLevel(40f));
        hookBuilder.addHook(this,ModifierHooksRegistry.NIGHT_VISION_HOOK);
    }
    @Override
    public boolean cannightvision(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, boolean isnightVision)
    {
        return true;
    }
    @Override
    public float getnightvisionscale(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType,float basescale,float scale)
    {
        return 1.0f;
    }
}
