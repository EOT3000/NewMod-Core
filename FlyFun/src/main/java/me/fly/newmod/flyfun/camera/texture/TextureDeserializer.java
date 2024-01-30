package me.fly.newmod.flyfun.camera.texture;

import com.google.gson.*;

import java.lang.reflect.Type;

public class TextureDeserializer implements JsonDeserializer<TexturedBlock> {
    private Textures textures;

    TextureDeserializer(Textures textures) {
        this.textures = textures;
    }

    @Override
    public TexturedBlock deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject object = jsonElement.getAsJsonObject();

        if (object.has("parent")) {
            String parent = object.get("parent").getAsString();
            JsonObject texturesObj = object.getAsJsonObject("textures");

            if (parent.equals("minecraft:block/cube_all")) {
                return new SixSidedTexturedBlock(TextureLoadUtil.load(texturesObj.get("all").getAsString(), textures.textureDir));
            } else if (parent.equals("minecraft:block/cube_column")) {
                return new OrientableTexturedBlock(
                        TextureLoadUtil.load(texturesObj.get("end").getAsString(), textures.textureDir),
                        TextureLoadUtil.load(texturesObj.get("side").getAsString(), textures.textureDir));
            }

        }

        return Textures.FAILED_TO_LOAD;
    }
}
