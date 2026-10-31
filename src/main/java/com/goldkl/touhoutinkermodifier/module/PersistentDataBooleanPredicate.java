package com.goldkl.touhoutinkermodifier.module;

import net.minecraft.resources.ResourceLocation;
import slimeknights.mantle.data.loadable.Loadables;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.tconstruct.library.json.predicate.tool.ToolContextPredicate;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;

public record PersistentDataBooleanPredicate(ResourceLocation key) implements ToolContextPredicate {
  public static final RecordLoadable<PersistentDataBooleanPredicate> LOADER = RecordLoadable.create(Loadables.RESOURCE_LOCATION.requiredField("key", PersistentDataBooleanPredicate::key), PersistentDataBooleanPredicate::new);

  @Override
  public boolean matches(IToolContext tool) {
    return tool.getPersistentData().getBoolean(key);
  }

  @Override
  public RecordLoadable<PersistentDataBooleanPredicate> getLoader() {
    return LOADER;
  }
}
