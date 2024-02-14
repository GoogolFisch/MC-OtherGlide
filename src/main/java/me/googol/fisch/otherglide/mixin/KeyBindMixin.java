package me.googol.fisch.otherglide.mixin;

import me.googol.fisch.otherglide.util.KeyBindUtil;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(KeyBinding.class)
public class KeyBindMixin implements KeyBindUtil {
    @Shadow private InputUtil.Key boundKey;

    @Override
    public InputUtil.Key getKey() {return boundKey;}
}
