package me.fly.newmod.flyfun.camera.model;

import com.google.gson.*;
import me.fly.newmod.flyfun.camera.Textures;
import me.fly.newmod.flyfun.camera.texture.TextureLoadUtil;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Orientable;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.function.Predicate;

public class BlockStatesDeserializer implements JsonDeserializer<BlockStates> {
    private final Textures textures;

    public BlockStatesDeserializer(Textures textures) {
        this.textures = textures;
    }

    @Override
    public BlockStates deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject object = jsonElement.getAsJsonObject();

        if (object.has("variants")) {
            JsonObject variants = object.get("variants").getAsJsonObject();

            BlockStates states = new BlockStates();

            for(Map.Entry<String, JsonElement> element : variants.asMap().entrySet()) {
                String variantKey = element.getKey();

                JsonElement variantRaw = element.getValue();

                JsonObject variantInfo = variantRaw.isJsonArray() ? variantRaw.getAsJsonArray().get(0).getAsJsonObject() : variantRaw.getAsJsonObject();

                int x = getOrDefaultInt(variantInfo, "x", 0);
                int y = getOrDefaultInt(variantInfo, "y", 0);

                if(variantKey.isEmpty()) {
                    states.addState((j) -> true, new BlockStates.BlockState(textures.getModel(variantInfo.get("model").getAsString()), x, y));

                    return states;
                }

                System.out.println("Loading block states for " + variantInfo.get("model").getAsString());

                String[] variantKeyAndValue = variantKey.split("=");

                states.addState(createPredicate(variantKeyAndValue[0], variantKeyAndValue[1]), new BlockStates.BlockState(textures.getModel(variantInfo.get("model").getAsString()), x, y));

                states.print();

                return states;
            }
        }

        return Textures.FAILED_TO_LOAD_STATES;
    }

    @SuppressWarnings("SameParameterValue")
    private static int getOrDefaultInt(JsonObject object, String key, int def) {
        return object.has(key) ? object.get(key).getAsInt() : def;
    }

    private static Predicate<BlockData> createPredicate(String key, String value) {
        System.out.println(key + "," + value);
        if(key.equalsIgnoreCase("axis")) {
            return (j) -> {
                if(j instanceof Orientable orientable) {
                    return orientable.getAxis().name().equalsIgnoreCase(value);
                }

                return false;
            };
        }

        return (j) -> true;
    }
}
