package com.goldkl.touhoutinkermodifier.predicate;

import com.goldkl.touhoutinkermodifier.registries.TTMTinkerLoadables;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.predicate.IJsonPredicate;

public record IsSchoolTypeSpellPredicate(SchoolType schoolType) implements AbstractSpellPredicate {
    public static final RecordLoadable<IsSchoolTypeSpellPredicate> LOADER = RecordLoadable.create(
            TTMTinkerLoadables.SPELL_SCHOOLTYPE.requiredField("school_type", IsSchoolTypeSpellPredicate::schoolType),
            IsSchoolTypeSpellPredicate::new);
    @Override
    public boolean matches(AbstractSpell input) {
        return input.getSchoolType() == schoolType;
    }

    @Override
    public RecordLoadable<? extends IJsonPredicate<AbstractSpell>> getLoader() {
        return LOADER;
    }
}
