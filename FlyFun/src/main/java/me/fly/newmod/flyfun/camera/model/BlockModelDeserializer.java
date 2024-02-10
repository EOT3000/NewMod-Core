package me.fly.newmod.flyfun.camera.model;

import com.google.gson.*;
import me.fly.newmod.flyfun.camera.texture.TextureLoadUtil;
import me.fly.newmod.flyfun.camera.Textures;

import java.lang.reflect.Type;

public class BlockModelDeserializer implements JsonDeserializer<BlockModel> {
    private Textures textures;

    public BlockModelDeserializer(Textures textures) {
        this.textures = textures;
    }

    @Override
    public BlockModel deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject object = jsonElement.getAsJsonObject();

        if (object.has("parent")) {
            String parent = object.get("parent").getAsString();
            JsonObject texturesObj = object.getAsJsonObject("textures");

            if (parent.equals("minecraft:block/cube_all")) {
                return new SixSidedBlockModel(textures.getTexture(texturesObj.get("all").getAsString()));
            } else if (parent.equals("minecraft:block/cube_column") || parent.equals("minecraft:block/cube_column_horizontal")) {
                return new TopSideBlockModel(
                        textures.getTexture(texturesObj.get("end").getAsString()),
                        textures.getTexture(texturesObj.get("side").getAsString()));
            }

        }

        return Textures.FAILED_TO_LOAD;
    }
}
