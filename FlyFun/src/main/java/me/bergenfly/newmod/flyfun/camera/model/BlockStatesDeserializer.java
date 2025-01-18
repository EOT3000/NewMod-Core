package me.bergenfly.newmod.flyfun.camera.model;

import com.google.gson.*;
import me.bergenfly.newmod.flyfun.camera.Textures;
import me.bergenfly.newmod.flyfun.camera.texture.TextureLoadUtil;
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


                //Came back some time later, I think what this means is
                // If empty variants, then just 1 state, so can return with 1
                // If stairs, or slabs, then all states would be equivalent (the other half of a slab would never get hit, so just 1 state is enough)
                if(variantKey.isEmpty()) {
                    states.addState((j) -> true, new BlockStates.BlockState(textures.getModel(variantInfo.get("model").getAsString()), x, y));

                    return states;
                } else if((variantKey.contains("facing") && variantKey.contains("half") && variantKey.contains("shape")) || variantKey.contains("type")) {
                    states.addState((j) -> true, new BlockStates.BlockState(textures.getModel(variantInfo.get("model").getAsString()), 0, 0));

                    return states;
                }

                //System.out.println("Loading block states for " + variantInfo.get("model").getAsString());

                String[] variantKeyAndValue = variantKey.split("=");

                states.addState(createPredicate(variantKeyAndValue[0], variantKeyAndValue[1]), new BlockStates.BlockState(textures.getModel(variantInfo.get("model").getAsString()), x, y));

                //states.print();
            }

            return states;
        }

        return Textures.FAILED_TO_LOAD_STATES;
    }

    @SuppressWarnings("SameParameterValue")
    private static int getOrDefaultInt(JsonObject object, String key, int def) {
        return object.has(key) ? object.get(key).getAsInt() : def;
    }

    private static Predicate<BlockData> createPredicate(String key, String value) {
        //System.out.println(key + "," + value);
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
