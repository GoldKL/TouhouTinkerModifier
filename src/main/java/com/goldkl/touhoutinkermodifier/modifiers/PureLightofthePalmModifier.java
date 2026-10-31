package com.goldkl.touhoutinkermodifier.modifiers;

import com.c2h6s.etstlib.tool.hooks.modifierModules.AddDamageTypeTagMeleeModule;
import com.hollingsworth.arsnouveau.api.perk.PerkAttributes;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.network.SyncManaPacket;
import io.redspace.ironsspellbooks.setup.PacketDistributor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.mantle.data.predicate.entity.LivingEntityPredicate;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MonsterMeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.util.ModifierCondition;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class PureLightofthePalmModifier extends Modifier implements MeleeHitModifierHook, MonsterMeleeHitModifierHook {
    //掌上的纯光：纯狐
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addModule(new AddDamageTypeTagMeleeModule(LivingEntityPredicate.ANY, ModifierCondition.ANY_TOOL, DamageTypeTags.BYPASSES_ARMOR,true));
        hookBuilder.addModule(new AddDamageTypeTagMeleeModule(LivingEntityPredicate.ANY, ModifierCondition.ANY_TOOL, DamageTypeTags.BYPASSES_EFFECTS,true));
        hookBuilder.addHook(this, ModifierHooks.MELEE_HIT, ModifierHooks.MONSTER_MELEE_HIT);
    }
    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt){
        LivingEntity attacker = context.getAttacker();
        LivingEntity target = context.getLivingTarget();
        if(target != null && target.isDeadOrDying())
        {
            int level = modifier.getLevel();
            attacker.heal(attacker.getMaxHealth() * 0.05f * level);
            if(attacker instanceof ServerPlayer player){
                MagicData entitymagicdata = MagicData.getPlayerMagicData(player);
                float max_mana = (float) player.getAttributeValue(PerkAttributes.MAX_MANA.get());
                entitymagicdata.addMana(max_mana * 0.05f * level);
                PacketDistributor.sendToPlayer(player, new SyncManaPacket(entitymagicdata));
            }
        }
    }
    @Override
    public void onMonsterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage){
        LivingEntity attacker = context.getAttacker();
        LivingEntity target = context.getLivingTarget();
        if(target != null && target.isDeadOrDying())
        {
            int level = modifier.getLevel();
            attacker.heal(attacker.getMaxHealth() * 0.05f * level);
        }
    }
}
