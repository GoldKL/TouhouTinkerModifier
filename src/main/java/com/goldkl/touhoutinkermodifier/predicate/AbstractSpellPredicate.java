package com.goldkl.touhoutinkermodifier.predicate;

import com.goldkl.touhoutinkermodifier.registries.TTMTinkerLoadables;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraft.tags.TagKey;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.loadable.record.SingletonLoader;
import slimeknights.mantle.data.predicate.IJsonPredicate;
import slimeknights.mantle.data.predicate.RegistryPredicateRegistry;
import slimeknights.mantle.util.RegistryHelper;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public interface AbstractSpellPredicate extends IJsonPredicate<AbstractSpell> {
    AbstractSpellPredicate ANY = simple((spell) -> true);
    AbstractSpellPredicate NONE = simple((spell) -> false);
    RegistryPredicateRegistry<AbstractSpell, AbstractSpell> LOADER = new RegistryPredicateRegistry<>("AbstractSpell Predicate", ANY, NONE, TTMTinkerLoadables.SPELL, Function.identity(), "abstractspells", TTMTinkerLoadables.SPELL_TAG, RegistryHelper::contains);

    @Override
    default IJsonPredicate<AbstractSpell> inverted() {
        return LOADER.invert(this);
    }

    static AbstractSpellPredicate simple(Predicate<AbstractSpell> predicate) {
        return SingletonLoader.singleton(loader -> new AbstractSpellPredicate() {
            @Override
            public boolean matches(AbstractSpell spell) {
                return predicate.test(spell);
            }

            @Override
            public RecordLoadable<? extends AbstractSpellPredicate> getLoader() {
                return loader;
            }
        });
    }

    /** Creates am spell set predicate */
    static IJsonPredicate<AbstractSpell> set(AbstractSpell... spells) {
        return LOADER.setOf(spells);
    }

    /** Creates a tag predicate */
    static IJsonPredicate<AbstractSpell> tag(TagKey<AbstractSpell> tag) {
        return LOADER.tag(tag);
    }

    /** Creates an and predicate */
    @SafeVarargs
    static IJsonPredicate<AbstractSpell> and(IJsonPredicate<AbstractSpell>... predicates) {
        return LOADER.and(List.of(predicates));
    }

    /** Creates an or predicate */
    @SafeVarargs
    static IJsonPredicate<AbstractSpell> or(IJsonPredicate<AbstractSpell>... predicates) {
        return LOADER.or(List.of(predicates));
    }
}
