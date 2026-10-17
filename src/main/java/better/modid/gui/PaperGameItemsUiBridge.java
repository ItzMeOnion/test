package better.modid.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

public final class PaperGameItemsUiBridge {
    private PaperGameItemsUiBridge() {
    }

    public static void open(Screen parent) {
        MinecraftClient.getInstance().setScreen(new PaperGameItemsScreen(parent));
    }
}
