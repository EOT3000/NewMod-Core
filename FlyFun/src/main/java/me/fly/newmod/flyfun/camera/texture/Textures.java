package me.fly.newmod.flyfun.camera.texture;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import me.fly.newmod.flyfun.camera.texture.TextureDeserializer;
import me.fly.newmod.flyfun.camera.texture.TexturedBlock;
import org.bukkit.block.data.BlockData;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class Textures {
    public static final TexturedBlock FAILED_TO_LOAD = (x, y, face, data, brightness) -> (x/8+y/8)%2==0
            ? (short) (186)
            : (short) (119);

    final Map<String, String> textureDir = new HashMap<>();

    public static final double[][] DARKNESS_MODIFIERS = {
            {0.139, 0.139, 0.139},
            {0.183, 0.167, 0.155},
            {0.271, 0.227, 0.195},
            {0.359, 0.291, 0.235},
            {0.450, 0.363, 0.279},
            {0.538, 0.438, 0.327},
            {0.625, 0.518, 0.382},
            {0.713, 0.602, 0.450},
            {0.793, 0.689, 0.522},
            {0.861, 0.777, 0.610},
            {0.920, 0.857, 0.709},
            {0.964, 0.928, 0.821},
            {0.988, 0.976, 0.924},
            {1.000, 0.996, 0.984},
            {1.000, 1.000, 1.000},
            {1.000, 1.000, 1.000},
    };


    public void loadTextures(File dir) {
        Gson gson = new GsonBuilder().registerTypeAdapter(TexturedBlock.class, new TextureDeserializer(this)).create();

        File models = new File("/plugins/FlyFun/texture/models");

        for(File model : models.listFiles()) {
            try {
                JsonReader reader = new JsonReader(new FileReader(model));

                TexturedBlock block = gson.fromJson(reader, TexturedBlock.class);
            } catch (Exception e) {

            }
        }
    }
}
