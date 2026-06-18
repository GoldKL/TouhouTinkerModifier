package com.goldkl.touhoutinkermodifier.module;

import com.goldkl.touhoutinkermodifier.hook.AddPlayerMagicHook;
import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import com.goldkl.touhoutinkermodifier.registries.TTMTinkerLoadables;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.util.TooltipsUtils;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.mantle.data.loadable.primitive.BooleanLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.predicate.IJsonPredicate;
import slimeknights.mantle.data.predicate.entity.LivingEntityPredicate;
import slimeknights.tconstruct.library.json.math.FormulaLoadable;
import slimeknights.tconstruct.library.json.math.ModifierFormula;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.library.modifiers.modules.util.ModifierCondition;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.ArrayList;
import java.util.List;
//由于本module并未直接向物品添加法术nbt，而是在事件上执行，所以女仆等无法获得法术
//实际触发时机依赖铁魔法触发时机，也就是装备装卸，所以entity判断请写一些套装判断
public record SpellModule(AbstractSpell spell, boolean passConfig, boolean passMaxlevel, ModifierFormula formula, IJsonPredicate<LivingEntity> entity, ModifierCondition<IToolStackView> condition)
        implements ModifierModule, TooltipModifierHook, AddPlayerMagicHook, ModifierCondition.ConditionalModule<IToolStackView> {
    private static final List<ModuleHook<?>> DEFAULT_HOOKS = HookProvider.<SpellModule>defaultHooks(ModifierHooksRegistry.ADD_PLAYER_MAGIC, ModifierHooks.TOOLTIP);
    private static final FormulaLoadable FORMULA = new FormulaLoadable(ModifierFormula.FallbackFormula.IDENTITY, "level");

    public static final RecordLoadable<SpellModule> LOADER = RecordLoadable.create(
            TTMTinkerLoadables.SPELL.requiredField("spell", SpellModule::spell),
            BooleanLoadable.INSTANCE.requiredField("pass_config", SpellModule::passConfig),
            BooleanLoadable.INSTANCE.requiredField("pass_maxlevel", SpellModule::passMaxlevel),
            FORMULA.directField(SpellModule::formula),
            LivingEntityPredicate.LOADER.defaultField("wearing_entity", SpellModule::entity),
            ModifierCondition.TOOL_FIELD,
            SpellModule::new);

    @Override
    public RecordLoadable<? extends ModifierModule> getLoader() {
        return LOADER;
    }

    @Override
    public @NotNull List<ModuleHook<?>> getDefaultHooks() {
        return DEFAULT_HOOKS;
    }

    @Override
    public ArrayList<SpellData> getmagiclist(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, SpellSelectionManager manager) {
        if((passConfig || spell.isEnabled()) && condition.matches(tool,modifier) && entity.matches(context.getEntity())){
            int truelevel = (int) formula.apply(formula.processLevel(modifier));
            truelevel = passMaxlevel?truelevel:Math.min(truelevel,spell.getMaxLevel());
            if(truelevel > 0) {
                return new ArrayList<>(List.of(new SpellData(spell, truelevel,true)));
            }
        }
        return null;
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if(player != null && (passConfig || spell.isEnabled()) && condition.matches(tool,modifier) && entity.matches(player)){
            int truelevel = (int) formula.apply(formula.processLevel(modifier));
            truelevel = passMaxlevel?truelevel:Math.min(truelevel,spell.getMaxLevel());
            if(truelevel > 0)
            {
                SpellData data = new SpellData(spell, truelevel,true);
                tooltip.add(modifier.getModifier().applyStyle(Component.translatable("tooltip.irons_spellbooks.spellbook_tooltip")
                        .append(TooltipsUtils.getTitleComponent(data, (LocalPlayer) player))));
            }

        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends ModifierFormula.Builder<Builder,SpellModule> {
        private IJsonPredicate<LivingEntity> entity = LivingEntityPredicate.ANY;
        private boolean passConfig = false;
        private boolean passMaxlevel = false;
        private AbstractSpell spell = SpellRegistry.none();
        private Builder() {
            super(FORMULA.variables());
        }
        public Builder entity(IJsonPredicate<LivingEntity> source) {
            this.entity = source;
            return this;
        }
        @SafeVarargs
        public final Builder entitys(IJsonPredicate<LivingEntity>... sources) {
            this.entity = LivingEntityPredicate.and(sources);
            return this;
        }
        public Builder passConfig(boolean passConfig){
            this.passConfig = passConfig;
            return this;
        }
        public Builder passMaxlevel(boolean passMaxlevel){
            this.passMaxlevel = passMaxlevel;
            return this;
        }
        public Builder spell(AbstractSpell spell) {
            this.spell = spell;
            return this;
        }
        @Override
        @NotNull
        protected SpellModule build(@NotNull ModifierFormula formula) {
            return new SpellModule(spell,passConfig,passMaxlevel,formula,entity, condition);
        }
    }
}
