package better.modid.client;

import better.modid.gui.PaperGameItemsUiBridge;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.LiteralText;
import org.lwjgl.glfw.GLFW;

public class ClientEntrypoint implements ClientModInitializer {
    private static KeyBinding openItemsKey;

    @Override
    public void onInitializeClient() {
        openItemsKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.better.open_items",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_I,
                "category.better"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openItemsKey.wasPressed()) {
                // open the items screen, pass current screen as parent
                try {
                    PaperGameItemsUiBridge.open(MinecraftClient.getInstance().currentScreen);
                } catch (Exception e) {
                    // fallback: notify the player in chat
                    if (MinecraftClient.getInstance().player != null) {
                        MinecraftClient.getInstance().player.sendMessage(new LiteralText("Failed to open Paper Items UI: " + e.getMessage()), false);
                    }
                    e.printStackTrace();
                }
            }
        });
    }
}
