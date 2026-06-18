package com.goldkl.touhoutinkermodifier.module;

import com.goldkl.touhoutinkermodifier.hook.MagicAffinityHook;
import com.goldkl.touhoutinkermodifier.predicate.AbstractSpellPredicate;
import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.data.loadable.primitive.BooleanLoadable;
import slimeknights.mantle.data.loadable.primitive.IntLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.predicate.IJsonPredicate;
import slimeknights.mantle.data.predicate.entity.LivingEntityPredicate;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.library.modifiers.modules.util.ModifierCondition;
import slimeknights.tconstruct.library.modifiers.modules.util.ModuleBuilder;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

//passconfig为真时，即使法术被ban依然获得亲和
public record SpellAffinityModule(IJsonPredicate<AbstractSpell> spell, boolean passConfig, int level, boolean fixedLevel, IJsonPredicate<LivingEntity> entity, ModifierCondition<IToolStackView> condition)
        implements ModifierModule, MagicAffinityHook, ModifierCondition.ConditionalModule<IToolStackView> {
    private static final List<ModuleHook<?>> DEFAULT_HOOKS = HookProvider.<SpellAffinityModule>defaultHooks(ModifierHooksRegistry.MAGIC_AFFINITY);

    public static final RecordLoadable<SpellAffinityModule> LOADER = RecordLoadable.create(
            AbstractSpellPredicate.LOADER.requiredField("spell", SpellAffinityModule::spell),
            BooleanLoadable.INSTANCE.requiredField("pass_config", SpellAffinityModule::passConfig),
            IntLoadable.FROM_ONE.requiredField("level", SpellAffinityModule::level),
            BooleanLoadable.INSTANCE.requiredField("fixed_level", SpellAffinityModule::fixedLevel),
            LivingEntityPredicate.LOADER.defaultField("wearing_entity", SpellAffinityModule::entity),
            ModifierCondition.TOOL_FIELD,
            SpellAffinityModule::new);

    @Override
    public RecordLoadable<? extends ModifierModule> getLoader() {
        return LOADER;
    }

    @Override
    public @NotNull List<ModuleHook<?>> getDefaultHooks() {
        return DEFAULT_HOOKS;
    }

    @Override
    public int getMagicAffinity(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, AbstractSpell spell, int baseLevel, int totalLevel){
        if((passConfig || spell.isEnabled()) && this.spell.matches(spell) && condition.matches(tool,modifier) && entity.matches(context.getEntity())){
            int truelevel = fixedLevel?level:modifier.getLevel() * level;
            if(truelevel > 0) {
                return totalLevel + truelevel;
            }
        }
        return totalLevel;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends ModuleBuilder.Stack<Builder> {
        private IJsonPredicate<LivingEntity> entity = LivingEntityPredicate.ANY;
        private int level = 1;
        private boolean fixedLevel = false;
        private boolean passConfig = false;
        public Builder entity(IJsonPredicate<LivingEntity> source) {
            this.entity = source;
            return this;
        }
        @SafeVarargs
        public final Builder entitys(IJsonPredicate<LivingEntity>... sources) {
            this.entity = LivingEntityPredicate.and(sources);
            return this;
        }
        public Builder level(int level){
            this.level = level;
            return this;
        }
        public Builder passConfig(boolean passConfig){
            this.passConfig = passConfig;
            return this;
        }
        public Builder fixedLevel(boolean fixedLevel){
            this.fixedLevel = fixedLevel;
            return this;
        }
        public SpellAffinityModule spell(IJsonPredicate<AbstractSpell> spell) {
            return new SpellAffinityModule(spell, passConfig, level, fixedLevel, entity, condition);
        }
    }
}
