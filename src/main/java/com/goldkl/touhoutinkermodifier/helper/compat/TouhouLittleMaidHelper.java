package com.goldkl.touhoutinkermodifier.helper.compat;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class TouhouLittleMaidHelper {
    public static void TraverseMaidBackPack(LivingEntity entity, Predicate<ItemStack> condition, Consumer<ItemStack> consumer, boolean stopinfirst)
    {
        if(entity instanceof EntityMaid maid)
        {
            CombinedInvWrapper handler = maid.getAvailableInv(true);
            for(int i = 0; i < handler.getSlots(); ++i) {
                ItemStack stack = handler.getStackInSlot(i);
                if (condition.test(stack)) {
                    consumer.accept(stack);
                    if(stopinfirst)
                    {
                        break;
                    }
                }
            }
        }
    }
}
