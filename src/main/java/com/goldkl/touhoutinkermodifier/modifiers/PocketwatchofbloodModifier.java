package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.data.TTMModifierIds;
import com.goldkl.touhoutinkermodifier.hook.EntityDodgeHook;
import com.goldkl.touhoutinkermodifier.registries.MobeffectRegistry;
import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import com.goldkl.touhoutinkermodifier.registries.TagsRegistry;
import com.goldkl.touhoutinkermodifier.utils.TTMEntityUtils;
import com.hollingsworth.arsnouveau.api.perk.PerkAttributes;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.network.SyncManaPacket;
import io.redspace.ironsspellbooks.setup.PacketDistributor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.behavior.AttributeModule;
import slimeknights.tconstruct.library.modifiers.modules.util.ModifierCondition;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

public class PocketwatchofbloodModifier extends Modifier implements AttributesModifierHook, EntityDodgeHook {
    final String unique = TTMModifierIds.pocketwatchofblood.getNamespace()+  ".modifier."+ TTMModifierIds.pocketwatchofblood.getPath();
    final UUID[] slotUUIDs = AttributeModule.slotsToUUIDs(unique, List.of(EquipmentSlot.values()));
    private static final List<Attribute> attributes = List.of(PerkAttributes.MAX_MANA.get());
    private static final List<Float> attributes_amount = List.of(100f);
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.ATTRIBUTES, ModifierHooksRegistry.ENTITY_DODGE_HOOK);
    }
    @Override
    public boolean CanDodge(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, @Nullable Entity attacker, @Nullable Entity directattacker){
        LivingEntity entity = context.getEntity();
        if(entity instanceof ServerPlayer player)
        {
            MagicData entitymagicdata = MagicData.getPlayerMagicData(player);
            float mana = entitymagicdata.getMana();
            float costmana = 200;
            int costlevel = -1;
            MobEffectInstance instance = player.getEffect(MobeffectRegistry.BROKENPOCKETWATCH.get());
            if(instance != null)
            {
                costlevel = instance.getAmplifier();
            }
            costlevel += 1;
            costmana *= (float) Math.pow(2,costlevel);
            MobEffectInstance new_instance = new MobEffectInstance(MobeffectRegistry.BROKENPOCKETWATCH.get(),3000 , costlevel);
            if(mana >= 1600 && mana >= costmana && player.canBeAffected(new_instance))
            {
                entitymagicdata.setMana(mana - costmana);
                player.removeEffect(MobeffectRegistry.BROKENPOCKETWATCH.get());
                player.addEffect(new_instance);
                PacketDistributor.sendToPlayer(player, new SyncManaPacket(entitymagicdata));
                return true;
            }
            return false;
        }
        else
        {
            double dodge_chane = 0.1 * Math.min(TTMEntityUtils.gettotallevelwithtag(tool,TagsRegistry.ModifiersTag.ScarletDevilMansion),5);
            return RANDOM.nextDouble() < dodge_chane;
        }
    }
    private @Nullable UUID getUUID(EquipmentSlot slot) {
        return this.slotUUIDs[slot.getFilterFlag()];
    }
    @Override
    public void addAttributes(IToolStackView tool, ModifierEntry modifier, EquipmentSlot slot, BiConsumer<Attribute, AttributeModifier> consumer) {
        if (ModifierCondition.ANY_TOOL.matches(tool, modifier)) {
            int level = TTMEntityUtils.gettotallevelwithtag(tool,TagsRegistry.ModifiersTag.ScarletDevilMansion);
            for(int i = 0; i < 1; i++) {
                AttributeModifier attributeModifier = this.createModifier(tool, modifier, slot, level,i);
                if (attributeModifier != null) {
                    consumer.accept(attributes.get(i), attributeModifier);
                }
            }
        }
    }
    private @Nullable AttributeModifier createModifier(IToolStackView tool, ModifierEntry modifier, EquipmentSlot slot,int level,int index) {
        UUID uuid = this.getUUID(slot);
        return uuid != null ? new AttributeModifier(uuid, this.unique + "." + slot.getName(), level * attributes_amount.get(index), AttributeModifier.Operation.ADDITION) : null;
    }
}
