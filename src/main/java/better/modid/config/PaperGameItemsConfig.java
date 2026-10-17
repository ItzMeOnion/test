package better.modid.config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple JSON-backed list of item IDs for the Paper game.
 * Saves to <minecraft>/config/bettertotem-items.json
 */
public class PaperGameItemsConfig {
    private static final Gson GSON = new Gson();
    private static final Type LIST_TYPE = new TypeToken<List<String>>() {}.getType();
    private static final String FILENAME = "bettertotem-items.json";
    private static File configFile;
    private static List<String> items = new ArrayList<>();

    public static void init() {
        if (configFile != null) return;
        File gameDir = MinecraftClient.getInstance().runDirectory;
        File configDir = new File(gameDir, "config");
        if (!configDir.exists()) configDir.mkdirs();
        configFile = new File(configDir, FILENAME);
        load();
    }

    public static void load() {
        try {
            if (!configFile.exists()) {
                items = new ArrayList<>();
                items.add("minecraft:paper");
                items.add("minecraft:map");
                save();
                return;
            }
            try (FileReader fr = new FileReader(configFile, StandardCharsets.UTF_8)) {
                List<String> loaded = GSON.fromJson(fr, LIST_TYPE);
                items = (loaded != null) ? loaded : new ArrayList<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
            items = new ArrayList<>();
        }
    }

    public static void save() {
        try (FileWriter fw = new FileWriter(configFile, StandardCharsets.UTF_8)) {
            GSON.toJson(items, fw);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String> getItems() {
        init();
        return items;
    }

    public static boolean addItem(String id) {
        init();
        String normalized = id.trim().toLowerCase();
        if (normalized.isEmpty()) return false;
        if (!isValidItemId(normalized)) return false;
        if (!items.contains(normalized)) {
            items.add(normalized);
            save();
            return true;
        }
        return false;
    }

    public static boolean removeItem(String id) {
        init();
        boolean removed = items.remove(id.trim().toLowerCase());
        if (removed) save();
        return removed;
    }

    private static boolean isValidItemId(String id) {
        try {
            Identifier identifier = new Identifier(id);
            return Registry.ITEM.containsId(identifier);
        } catch (Exception e) {
            return false;
        }
    }
}
