package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ProcessLootModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;
import java.util.Map;

public class RedblacktreeModifier extends NoLevelsModifier implements ProcessLootModifierHook {
    //红黑之影：小恶魔
    private static final ResourceLocation TREASURE = ResourceLocation.fromNamespaceAndPath(TouhouTinkerModifier.MODID, "modifiers_loot/treasure_enchantment");
    private static final ResourceLocation COMMON = ResourceLocation.fromNamespaceAndPath(TouhouTinkerModifier.MODID, "modifiers_loot/common_enchantment");
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.PROCESS_LOOT);
    }
    @Override
    public void processLoot(IToolStackView tool, ModifierEntry modifier, List<ItemStack> generatedLoot, LootContext context) {
        if (!context.hasParam(LootContextParams.DAMAGE_SOURCE)) {
            return;
        }
        Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        if (entity != null && entity.getType().is(Tags.EntityTypes.BOSSES)) {
            Entity killer = context.getParamOrNull(LootContextParams.KILLER_ENTITY);
            if (killer instanceof ServerPlayer player) {
                ItemStack ECbook = findEnchantedBook(player);
                if (!ECbook.isEmpty()) {
                    ListTag ECMap = EnchantedBookItem.getEnchantments(ECbook);
                    while(!ECMap.isEmpty()) {
                        CompoundTag compoundtag = ECMap.getCompound(0);
                        Enchantment EC = ForgeRegistries.ENCHANTMENTS.getValue(EnchantmentHelper.getEnchantmentId(compoundtag));
                        if(EC == null)
                        {
                            ECMap.remove(0);
                            continue;
                        }
                        int level = EnchantmentHelper.getEnchantmentLevel(compoundtag);
                        ResourceLocation lootTableId = EC.isTreasureOnly()? TREASURE : COMMON;
                        ECMap.remove(0);
                        LootTable lootTable = player.level().getServer().getLootData().getLootTable(lootTableId);
                        LootParams lootParams = new LootParams.Builder((ServerLevel) player.level()).withLuck(level).create(LootContextParamSets.EMPTY);
                        LootContext lootContext = new LootContext.Builder(lootParams).withQueriedLootTableId(lootTableId).create(null);
                        lootTable.getRandomItems(lootContext,(generatedLoot::add));
                    }
                    ECbook.setCount(0);
                    /*CompoundTag compoundtag = ECMap.getCompound(0);
                    Enchantment EC = ForgeRegistries.ENCHANTMENTS.getValue(EnchantmentHelper.getEnchantmentId(compoundtag));
                    if(EC != null)
                    {
                        ResourceLocation lootTableId = null;
                        if(EC.isTreasureOnly()) {
                            lootTableId = TREASURE;
                        }
                        else {
                            lootTableId = COMMON;
                        }
                        ECMap.remove(0);
                        if(ECMap.isEmpty()) {
                            ECbook.setCount(0);
                        }
                        else {
                            ECbook.getOrCreateTag().put("StoredEnchantments", ECMap);
                        }
                        LootTable lootTable = player.level().getServer().getLootData().getLootTable(lootTableId);
                        LootContext lootContext = new LootContext.Builder(context).withQueriedLootTableId(lootTableId).create(null);
                        lootTable.getRandomItems(lootContext,(generatedLoot::add));
                    }*/
                }
            }
        }
    }
    @NotNull
    private ItemStack findEnchantedBook(Player player)
    {
        Inventory inventory = player.getInventory();
        ItemStack mainHanditem = player.getMainHandItem();
        if (!mainHanditem.isEmpty() && mainHanditem.is(Items.ENCHANTED_BOOK) && !EnchantedBookItem.getEnchantments(mainHanditem).isEmpty()) return mainHanditem;

        ItemStack offHanditem = player.getOffhandItem();
        if (!offHanditem.isEmpty() && offHanditem.is(Items.ENCHANTED_BOOK) && !EnchantedBookItem.getEnchantments(offHanditem).isEmpty()) return offHanditem;

        for (int i = 0; i < inventory.getContainerSize(); ++i) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty() && stack.is(Items.ENCHANTED_BOOK) && !EnchantedBookItem.getEnchantments(stack).isEmpty()) return stack;
        }
        return ItemStack.EMPTY;
    }
}
