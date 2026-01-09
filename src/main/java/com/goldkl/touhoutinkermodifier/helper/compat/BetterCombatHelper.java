package com.goldkl.touhoutinkermodifier.helper.compat;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.hook.BetterCombatAtackRangeHook;
import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import com.goldkl.touhoutinkermodifier.utils.TTMModListUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;

import java.lang.Class;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

public class BetterCombatHelper {
    private static Class<?> Context;
    private static Class<?> attackhand;
    private static Class<?> entityplayer_bettercombat;
    private static Object ADD;
    private static Object MUL;
    private static Method getplayer;
    private static Method getattackRange;
    private static Method getCurrentAttack;
    private static Method attackhand_isoffhand;
    private static Constructor<?> Modifier_constructor;
    public static void load(IEventBus modEventBus){};
    static{
        if (TTMModListUtil.BetterComBatLoaded) {
            // Mod 已加载
            try {
                Class<?> attackrangeextensions = Class.forName("net.bettercombat.api.client.AttackRangeExtensions");
                attackhand = Class.forName("net.bettercombat.api.AttackHand");
                entityplayer_bettercombat = Class.forName("net.bettercombat.api.EntityPlayer_BetterCombat");
                getCurrentAttack = entityplayer_bettercombat.getMethod("getCurrentAttack");
                attackhand_isoffhand = attackhand.getMethod("isOffHand");
                Method registermethod = attackrangeextensions.getMethod("register", Function.class);
                Context = Class.forName("net.bettercombat.api.client.AttackRangeExtensions$Context");
                Class<?> modifier = Class.forName("net.bettercombat.api.client.AttackRangeExtensions$Modifier");
                Class<?> Operation = Class.forName("net.bettercombat.api.client.AttackRangeExtensions$Operation");
                getplayer = Context.getMethod("player");
                getattackRange = Context.getMethod("attackRange");
                Modifier_constructor = modifier.getConstructor(double.class, Operation);
                if(Operation.isEnum())
                {
                    Method valueOfMethod = Operation.getMethod("valueOf", String.class);
                    ADD = valueOfMethod.invoke(null, "ADD");
                    MUL = valueOfMethod.invoke(null, "MULTIPLY");
                    Function<Object,Object> add = input ->{
                        try {
                            Player player = (Player)getplayer.invoke(Context.cast(input));
                            double base = (Double)getattackRange.invoke(Context.cast(input));
                            EquipmentContext equipmentContext = new EquipmentContext(player);
                            Object obj = getCurrentAttack.invoke(entityplayer_bettercombat.cast(player));
                            InteractionHand hand = InteractionHand.MAIN_HAND;
                            if(obj!=null)
                            {
                                hand = ((Boolean)attackhand_isoffhand.invoke(attackhand.cast(obj)))?InteractionHand.OFF_HAND:InteractionHand.MAIN_HAND;
                            }
                            double fixrange = BetterCombatAtackRangeHook.attackermodifyBetterCombatAtackRange(ModifierHooksRegistry.ATTACK_RANGE_ADD,equipmentContext,true,0,base,hand);
                            return Modifier_constructor.newInstance(fixrange , ADD);
                        } catch (InstantiationException|IllegalAccessException|InvocationTargetException e) {
                            TouhouTinkerModifier.LOGGER.error("error to register better combat add",e);
                        }
                        return null;
                    };
                    Function<Object,Object> mul = input ->{
                        try {
                            Player player = (Player)getplayer.invoke(Context.cast(input));
                            double base = (Double)getattackRange.invoke(Context.cast(input));
                            EquipmentContext equipmentContext = new EquipmentContext(player);
                            Object obj = getCurrentAttack.invoke(entityplayer_bettercombat.cast(player));
                            InteractionHand hand = InteractionHand.MAIN_HAND;
                            if(obj!=null)
                            {
                                hand = ((Boolean)attackhand_isoffhand.invoke(attackhand.cast(obj)))?InteractionHand.OFF_HAND:InteractionHand.MAIN_HAND;
                            }
                            double fixrange = BetterCombatAtackRangeHook.attackermodifyBetterCombatAtackRange(ModifierHooksRegistry.ATTACK_RANGE_MUL,equipmentContext,false,1.0,base,hand);
                            return Modifier_constructor.newInstance(fixrange , MUL);
                        } catch (InstantiationException|IllegalAccessException|InvocationTargetException e) {
                            TouhouTinkerModifier.LOGGER.error("error to register better combat mul",e);
                        }
                        return null;
                    };
                    registermethod.invoke(null,add);
                    registermethod.invoke(null,mul);
                }
            } catch (ClassNotFoundException|NoSuchMethodException|InvocationTargetException|IllegalAccessException e) {
                Context = null;
                attackhand = null;
                entityplayer_bettercombat = null;
                ADD = null;
                MUL = null;
                getplayer = null;
                getattackRange = null;
                getCurrentAttack = null;
                attackhand_isoffhand = null;
                Modifier_constructor = null;
                TouhouTinkerModifier.LOGGER.error("error to get better combat",e);
            }
        }
    }
}
