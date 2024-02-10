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
                return new SixSidedBlockModel(TextureLoadUtil.load(texturesObj.get("all").getAsString(), textures.textureDir));
            } else if (parent.equals("minecraft:block/cube_column")) {
                return new TopSideBlockModel(
                        TextureLoadUtil.load(texturesObj.get("end").getAsString(), textures.textureDir),
                        TextureLoadUtil.load(texturesObj.get("side").getAsString(), textures.textureDir));
            }

        }

        return Textures.FAILED_TO_LOAD;
    }
}
