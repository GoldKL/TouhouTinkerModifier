package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.ModifierIds;
import dev.xkmc.youkaishomecoming.init.food.YHFood;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ProcessLootModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.List;

public class ApparitionsstalkthenightModifier extends Modifier implements ProcessLootModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.PROCESS_LOOT);
    }
    @Override
    public void processLoot(IToolStackView tool, ModifierEntry modifier, List<ItemStack> generatedLoot, LootContext context) {
        if (!context.hasParam(LootContextParams.DAMAGE_SOURCE)) {
            return;
        }
        Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        Entity attacker = context.getParamOrNull(LootContextParams.KILLER_ENTITY);
        if (entity instanceof Mob) {
            int level = 0;
            if (attacker instanceof LivingEntity lvingentitiy) {
                for(EquipmentSlot equipmentSlot : EquipmentSlot.values())
                {
                    if(!(lvingentitiy.getItemBySlot(equipmentSlot).getItem() instanceof IModifiable))continue;
                    ToolStack nwtool = ToolStack.from(lvingentitiy.getItemBySlot(equipmentSlot));
                    level += nwtool.getModifier(ModifierIds.apparitionsstalkthenight).intEffectiveLevel();
                }
            }
            else
            {
                level = modifier.intEffectiveLevel();
            }
            if(level == 0) level = 1;
            int looting = context.getLootingModifier();
            int num = 12 / level;
            if(num < 1) num = 1;
            if (RANDOM.nextInt(num) <= looting) {
                generatedLoot.add(new ItemStack(YHFood.FLESH.item.get()));
            }
        }
    }
}
