package me.bergenfly.newmod.flyfun.camera;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import me.fly.newmod.flyfun.camera.model.BlockModel;
import me.bergenfly.newmod.flyfun.camera.model.BlockModelDeserializer;
import me.bergenfly.newmod.flyfun.camera.model.BlockStates;
import me.bergenfly.newmod.flyfun.camera.model.BlockStatesDeserializer;
import me.bergenfly.newmod.flyfun.camera.texture.TextureData16x16;
import me.bergenfly.newmod.flyfun.camera.texture.TextureLoadUtil;
import org.bukkit.Color;
import org.bukkit.Material;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class Textures {
    public static final Textures me = new Textures();
    public final Gson gson;


    public static final TextureData16x16 FAILED_TO_LOAD_TEXTURE;
    public static final BlockStates FAILED_TO_LOAD_STATES;

    public static final BlockModel FAILED_TO_LOAD = (x, y, face, data, brightness) -> (x/8+y/8)%2==0
            ? (byte) (186)
            : (byte) (119);

    final Map<String, String> textureDir = new HashMap<>();

    private final Map<String, BlockModel> models = new HashMap<>();
    private final Map<Material, BlockStates> states = new HashMap<>();
    private final Map<String, TextureData16x16> textures = new HashMap<>();

    /*public static final double[][] DARKNESS_MODIFIERS = {
            { 0.139, 0.139, 0.139},
            { 0.183, 0.167, 0.155},
            { 0.271, 0.227, 0.195},
            { 0.359, 0.291, 0.235},
            { 0.450, 0.363, 0.279},
            { 0.538, 0.438, 0.327},
            { 0.625, 0.518, 0.382},
            { 0.713, 0.602, 0.450},
            { 0.793, 0.689, 0.522},
            { 0.861, 0.777, 0.610},
            { 0.920, 0.857, 0.709},
            { 0.964, 0.928, 0.821},
            { 0.988, 0.976, 0.924},
            {1.000, 0.996, 0.984},
            {1.000, 1.000, 1.000},
            {1.000, 1.000, 1.000},
    };*/

    public static final double[][] DARKNESS_MODIFIERS = {
            {0.139, 0.139, 0.139},
            {0.183, 0.183, 0.183},
            {0.271, 0.271, 0.271},
            {0.359, 0.359, 0.359},
            {0.450, 0.450, 0.450},
            {0.538, 0.538, 0.538},
            {0.625, 0.625, 0.625},
            {0.713, 0.713, 0.713},
            {0.793, 0.793, 0.793},
            {0.861, 0.861, 0.861},
            {0.920, 0.920, 0.920},
            {0.964, 0.964, 0.964},
            {0.988, 0.988, 0.988},
            {1.000, 1.000, 1.000},
            {1.000, 1.000, 1.000},
            {1.000, 1.000, 1.000},
    };

    static {
        int[] rawColor = new int[256];
        byte[] storedColor = new byte[4096];

        for(int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                if((x+y)%2 == 0) {
                    rawColor[x*16+y] = Color.FUCHSIA.asARGB();
                } else {
                    rawColor[x*16+y] = 0;
                }

                for(int i = 0; i < 16; i++) {
                    if((x+y)%2 == 0) {
                        storedColor[i*256+x*16+y] = (byte) (112);
                    } else {
                        storedColor[i*256+x*16+y] = (byte) (119);
                    }

                }
            }
        }

        FAILED_TO_LOAD_TEXTURE = new TextureData16x16(rawColor, storedColor);

        FAILED_TO_LOAD_STATES = new BlockStates();

        FAILED_TO_LOAD_STATES.addState((j) -> true, new BlockStates.BlockState(FAILED_TO_LOAD, 0,0));
    }

    public Textures() {
        gson = new GsonBuilder()
                .registerTypeAdapter(BlockModel.class, new BlockModelDeserializer(this))
                .registerTypeAdapter(BlockStates.class, new BlockStatesDeserializer(this)).create();
    }

    public void loadTextures(File dir) {
        for(File file : dir.listFiles()) {
            try {
                TextureData16x16 loaded = TextureLoadUtil.load(file);

                textures.put(file.getName().replace(".png", ""), loaded);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public TextureData16x16 getTexture(String name) {
        name = name.replace("minecraft:block/", "");
        name = name.replace("block/", "");

        return textures.getOrDefault(name, FAILED_TO_LOAD_TEXTURE);
    }

    public BlockModel getModel(String name) {
        name = name.replace("minecraft:block/", "");
        name = name.replace("block/", "");

        return models.getOrDefault(name, FAILED_TO_LOAD);
    }

    public BlockStates getStates(Material material) {
        return states.getOrDefault(material, FAILED_TO_LOAD_STATES);
    }

    public void loadModels(File dir) {
        for(File model : dir.listFiles()) {
            try {
                JsonReader reader = new JsonReader(new FileReader(model));

                BlockModel block = gson.fromJson(reader, BlockModel.class);

                this.models.put(model.getName().replace(".json", ""), block);

                //if(block instanceof TOP)
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void loadBlockStates(File dir) {
        for(File model : dir.listFiles()) {
            try {
                JsonReader reader = new JsonReader(new FileReader(model));

                BlockStates block = gson.fromJson(reader, BlockStates.class);

                this.states.put(Material.valueOf(model.getName().replace(".json", "").toUpperCase()), block);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
