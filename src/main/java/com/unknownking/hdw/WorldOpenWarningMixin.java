package com.unknownking.hdw;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unchecked")
@Mixin(targets = {
    "net.minecraft.class_34", 
    "net.minecraft.class_528$class_4272"
})
public class WorldOpenWarningMixin {

    // 1.17+ World Downgrade / Conversion Warning (clears warning overlay in modern versions)
    @Inject(method = "method_33405", at = @At("HEAD"), cancellable = true, require = 0)
    private void disableConversionWarning(CallbackInfoReturnable<Object> cir) {
        try {
            Class<?> warningClass = Class.forName("net.minecraft.class_34$class_5781");
            Object noneValue = Enum.valueOf((Class<Enum>) warningClass, "NONE");
            cir.setReturnValue(noneValue);
        } catch (Exception e) {
            try {
                Class<?> warningClass = Class.forName("net.minecraft.class_34$class_5781");
                Object[] constants = warningClass.getEnumConstants();
                if (constants != null && constants.length > 0) {
                    cir.setReturnValue(constants[0]);
                }
            } catch (Exception ignored) {}
        }
    }

    // 1.20.3+ Allow world to be playable
    @Inject(method = "method_54550", at = @At("HEAD"), cancellable = true, require = 0)
    private void allowDowngradedWorldLoad(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }

    // 1.14 - 1.16 Outdated Check
    @Inject(method = "method_256", at = @At("HEAD"), cancellable = true, require = 0)
    private void disableOutdatedCheck(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }

    // 1.14 World Load Confirmation Screen Bypass
    @Inject(method = "method_20164", at = @At("HEAD"), cancellable = true, require = 0)
    private void directLoadWorld(CallbackInfo ci) {
        try {
            java.lang.reflect.Method startMethod = this.getClass().getSuperclass().getDeclaredMethod("method_20174");
            startMethod.setAccessible(true);
            startMethod.invoke(this);
            ci.cancel();
        } catch (Exception e) {
            try {
                java.lang.reflect.Method[] methods = this.getClass().getDeclaredMethods();
                for (java.lang.reflect.Method m : methods) {
                    if (m.getName().equals("method_20174") || m.getName().equals("start")) {
                        m.setAccessible(true);
                        m.invoke(this);
                        ci.cancel();
                        break;
                    }
                }
            } catch (Exception ignored) {}
        }
    }
}