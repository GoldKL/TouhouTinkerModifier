package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.data.TTMModifierIds;
import com.goldkl.touhoutinkermodifier.hook.ProcessArmorLootModifierHook;
import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import com.hollingsworth.arsnouveau.api.perk.PerkAttributes;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.modules.technical.SlotInChargeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public class StardustfantasyModifier extends Modifier implements ProcessArmorLootModifierHook {
    //星尘幻想：雾雨魔理沙
    private static final ResourceLocation TREASURE = ResourceLocation.fromNamespaceAndPath(TouhouTinkerModifier.MODID, "modifiers_loot/stardustfantasy_treasure");
    private static final ResourceLocation COMMON = ResourceLocation.fromNamespaceAndPath(TouhouTinkerModifier.MODID, "modifiers_loot/stardustfantasy_mushroom");
    private static final TinkerDataCapability.TinkerDataKey<SlotInChargeModule.SlotInCharge> SLOT_IN_CHARGE = TinkerDataCapability.TinkerDataKey.of(TTMModifierIds.stardustfantasy);

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addModule(new SlotInChargeModule(SLOT_IN_CHARGE));
        hookBuilder.addHook(this, ModifierHooksRegistry.PROCESS_ARMOR_LOOT_MODIFIER);
    }
    @Override
    public void processArmorLoot(IToolStackView tool, ModifierEntry modifier, LivingEntity origin, List<ItemStack> generatedLoot, LootContext context, EquipmentSlot slot) {
        if (!context.hasParam(LootContextParams.DAMAGE_SOURCE)) {
            return;
        }
        Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        if (entity instanceof Enemy && SlotInChargeModule.isInCharge(origin.getCapability(TinkerDataCapability.CAPABILITY), SLOT_IN_CHARGE, slot)){
            int level = SlotInChargeModule.getLevel(origin.getCapability(TinkerDataCapability.CAPABILITY), SLOT_IN_CHARGE, slot);
            double fixnum = 5;
            AttributeInstance instance = origin.getAttribute(AttributeRegistry.SPELL_POWER.get());
            if(instance != null){
                fixnum = Math.max(fixnum, fixnum * instance.getValue());
            }
            if (RANDOM.nextFloat() * 100 <= 25 + fixnum * level) {
                double fixnum2 = 1;
                AttributeInstance instance2 = origin.getAttribute(PerkAttributes.MAX_MANA.get());
                if(instance2 != null){
                    fixnum2 = Math.max(fixnum2, fixnum2 * instance2.getValue() / 1200.0);
                }
                ResourceLocation lootTableId = RANDOM.nextFloat() * 100 <= 5 + fixnum2? TREASURE : COMMON;
                LootTable lootTable = origin.level().getServer().getLootData().getLootTable(lootTableId);
                LootParams lootParams = new LootParams.Builder((ServerLevel) origin.level()).withLuck(level).create(LootContextParamSets.EMPTY);
                LootContext lootContext = new LootContext.Builder(lootParams).withQueriedLootTableId(lootTableId).create(null);
                lootTable.getRandomItems(lootContext,(generatedLoot::add));
            }

        }
    }
}
