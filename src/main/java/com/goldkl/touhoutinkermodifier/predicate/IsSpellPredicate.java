package com.goldkl.touhoutinkermodifier.predicate;

import com.goldkl.touhoutinkermodifier.registries.TTMTinkerLoadables;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.predicate.IJsonPredicate;

public record IsSpellPredicate(AbstractSpell spell) implements AbstractSpellPredicate {
    public static final RecordLoadable<IsSpellPredicate> LOADER = RecordLoadable.create(
            TTMTinkerLoadables.SPELL.requiredField("spell", IsSpellPredicate::spell),
            IsSpellPredicate::new);
    @Override
    public boolean matches(AbstractSpell input) {
        return input.equals(spell);
    }

    @Override
    public RecordLoadable<? extends IJsonPredicate<AbstractSpell>> getLoader() {
        return LOADER;
    }
}
