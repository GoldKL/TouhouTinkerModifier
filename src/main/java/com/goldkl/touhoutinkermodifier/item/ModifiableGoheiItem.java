package com.goldkl.touhoutinkermodifier.item;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.bullettype.AbstractBulletType;
import com.goldkl.touhoutinkermodifier.registries.TTMToolStats;
import com.goldkl.touhoutinkermodifier.tracking.ClientSetup;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.interaction.UsingToolModifierHook;
import slimeknights.tconstruct.library.tools.capability.PersistentDataCapability;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.tools.item.ModifiableSwordItem;


public class ModifiableGoheiItem extends ModifiableSwordItem {
    public ModifiableGoheiItem(Properties properties, ToolDefinition toolDefinition) {
        super(properties, toolDefinition);
    }

    public ModifiableGoheiItem(Properties properties, ToolDefinition toolDefinition, int maxStackSize) {
        super(properties, toolDefinition, maxStackSize);
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 12000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        ToolStack tool = ToolStack.from(itemstack);
        if (tool.isBroken()) {
            return InteractionResultHolder.fail(itemstack);
        }
        AbstractBulletType bulletType = tool.getStats().get(TTMToolStats.BULLET_TYPE);
        bulletType.startShootBullet(world, player, itemstack);
        player.startUsingItem(hand);
        if(world.isClientSide && player instanceof AbstractClientPlayer localplayer)
        {
            this.startAnimation(localplayer);
        }
        return InteractionResultHolder.consume(itemstack);
    }
    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity living, int timeLeft) {
        ToolStack tool = ToolStack.from(itemStack);
        int duration = getUseDuration(itemStack);
        for (ModifierEntry entry : tool.getModifiers()) {
            entry.getHook(ModifierHooks.TOOL_USING).beforeReleaseUsing(tool, entry, living, duration, timeLeft, ModifierEntry.EMPTY);
        }
    }
    private static final ResourceLocation KEY_GOHEI_TOOL_DAMAGE = TouhouTinkerModifier.getResource("key_gohei_tool_damage");
    @Override
    public void onUseTick(Level level, LivingEntity living, ItemStack stack, int chargeRemaining) {
        ToolStack tool = ToolStack.from(stack);
        int duration = getUseDuration(stack);
        for (ModifierEntry entry : tool.getModifiers()) {
            entry.getHook(ModifierHooks.TOOL_USING).onUsingTick(tool, entry, living, duration, chargeRemaining, ModifierEntry.EMPTY);
        }
        AbstractBulletType bulletType = tool.getStats().get(TTMToolStats.BULLET_TYPE);
        if(bulletType.isShootBulletTick(level,living,stack,chargeRemaining) && duration - chargeRemaining > 0){
            bulletType.ShootBulletTick(level,living,stack,chargeRemaining);
            if(!living.level().isClientSide)
            {
                living.getCapability(PersistentDataCapability.CAPABILITY).ifPresent(data -> {
                    int time = data.getInt(KEY_GOHEI_TOOL_DAMAGE);
                    data.putInt(KEY_GOHEI_TOOL_DAMAGE, Math.max(time,0) + 1);
                });
            }
        }
    }
    @Override
    public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack)
    {
        ToolStack tool = ToolStack.from(newStack);
        return super.canContinueUsing(oldStack, newStack) && !tool.isBroken();
    }
    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level pLevel, LivingEntity living) {
        ToolStack tool = ToolStack.from(stack);
        int duration = getUseDuration(stack);
        for (ModifierEntry entry : tool.getModifiers()) {
            entry.getHook(ModifierHooks.TOOL_USING).beforeReleaseUsing(tool, entry, living, duration, 0, ModifierEntry.EMPTY);
        }
        return stack;
    }
    @Override
    public void onStopUsing(ItemStack stack, LivingEntity entity, int timeLeft) {
        ToolStack tool = ToolStack.from(stack);
        UsingToolModifierHook.afterStopUsing(tool, entity, timeLeft);
        AbstractBulletType bulletType = tool.getStats().get(TTMToolStats.BULLET_TYPE);
        if(!entity.level().isClientSide)
        {
            entity.getCapability(PersistentDataCapability.CAPABILITY).ifPresent(data -> {
                int time = data.getInt(KEY_GOHEI_TOOL_DAMAGE);
                ToolDamageUtil.damageAnimated(tool, time, entity, entity.getUsedItemHand());
                data.remove(KEY_GOHEI_TOOL_DAMAGE);
            });
        }
        bulletType.stopShootBullet(entity.level(), entity, stack,timeLeft);
        if(entity.level().isClientSide && entity instanceof AbstractClientPlayer localplayer)
        {
            this.stopAnimation(localplayer);
        }
    }
    private static final ResourceLocation using_gohei_anim = TouhouTinkerModifier.getResource("using_gohei_anim");
    private void startAnimation(AbstractClientPlayer player) {
        var animation = (ModifierLayer<IAnimation>) PlayerAnimationAccess
                .getPlayerAssociatedData(player)
                .get(ClientSetup.ANIMA);
        if(animation != null){
            var keyframeAnimation = PlayerAnimationRegistry.getAnimation(using_gohei_anim);
            if(keyframeAnimation != null)
            {
                var castingAnimationPlayer = new KeyframeAnimationPlayer(keyframeAnimation);
                castingAnimationPlayer.setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL);
                castingAnimationPlayer.setFirstPersonConfiguration(new FirstPersonConfiguration(false, false, true, true));
                animation.setAnimation(castingAnimationPlayer);
            }
        }
    }
    private void stopAnimation(AbstractClientPlayer player) {
        var animation = (ModifierLayer<IAnimation>) PlayerAnimationAccess
                .getPlayerAssociatedData(player)
                .get(ClientSetup.ANIMA);
        if(animation != null){
            animation.setAnimation(null);
        }
    }
}
