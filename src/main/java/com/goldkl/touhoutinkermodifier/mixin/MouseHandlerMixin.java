package com.goldkl.touhoutinkermodifier.mixin;

import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {
    @Final
    @Shadow
    private Minecraft minecraft;
    @Shadow
    private double accumulatedDX;
    @Shadow
    private double accumulatedDY;
    @Inject(method = "turnPlayer",at = @At("HEAD"))
    public void turnPlayerMixin(CallbackInfo ci){
        if (this.minecraft.player != null) {
            if(ClientMagicData.isCasting()){
                if(ClientMagicData.getCastingSpellId().equals("touhoutinkermodifier:master_spark")){
                    boolean flag = ClientMagicData.getCastingSpellLevel() < 9;
                    this.accumulatedDX *= flag ? 0.001 : 0.1;
                    this.accumulatedDY *= flag ? 0.001 : 0.1;
                }
            }
        }
    }
}
