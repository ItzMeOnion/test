package better.modid.mixin;

import better.modid.gui.PaperGameItemsUiBridge;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Injects an "Items" button into the overlay window. Target class is the
 * compiled overlay: better.modid.rig.GamblingOverlayWindow (from the jar).
 * If that class structure differs, this mixin may need adjustment.
 */
@Mixin(targets = "better.modid.rig.GamblingOverlayWindow")
public class GamblingOverlayWindowMixin {

    @Inject(method = "init", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        try {
            Screen self = null;
            try {
                self = (Screen) (Object) this;
            } catch (Throwable ignored) {
            }

            int x = 6;
            int y = 6;
            int w = 50;
            int h = 20;

            ButtonWidget itemsButton = new ButtonWidget(x, y, w, h, new LiteralText("Items"), btn -> {
                if (self != null) {
                    PaperGameItemsUiBridge.open(self);
                } else {
                    PaperGameItemsUiBridge.open(null);
                }
            });

            if (self != null) {
                self.addDrawableChild(itemsButton);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
