package com.goldkl.touhoutinkermodifier.mixin.improvemobs;
import io.github.flemmli97.improvedmobs.client.ClientEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientEvents.class)
public interface ClientEventsAccessor{
    @Accessor("clientDifficulty")
    public static float getClientDifficulty() {
        throw new AssertionError();
    }
}
