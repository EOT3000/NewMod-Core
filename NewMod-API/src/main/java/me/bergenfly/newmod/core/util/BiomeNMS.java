package me.bergenfly.newmod.core.util;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.biome.Biome;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftServer;

//TODO: move all NMS into separate module
public class BiomeNMS {
    public static DedicatedServer server = ((CraftServer) Bukkit.getServer()).getServer();
    public static Registry<Biome> biomes = server.registryAccess().lookup(Registries.BIOME).orElseThrow();

    private static Holder.Reference<Biome> reference(String string) {
        return biomes.get(ResourceKey.create(
                Registries.BIOME,
                ResourceLocation.bySeparator(string, ':')
        )).orElse(null);
    }

    public static int getFoliageColor(String string) {
        Holder.Reference<Biome> reference = reference(string);

        if(reference == null) return -1;

        return reference.value().getFoliageColor();
    }

    public static int getGrassColorGeneral(String string) {
        Holder.Reference<Biome> reference = reference(string);

        if(reference == null) return -1;

        float temperature = reference.value().climateSettings.temperature();
        float downfall = reference.value().climateSettings.downfall();

        double d = Mth.clamp(temperature, 0.0F, 1.0F);
        double d1 =Mth.clamp(downfall, 0.0F, 1.0F);
        return GrassColor.get(d, d1);
    }

    public static int getGrassColorAtBlock(String string, int x, int z) {
        Holder.Reference<Biome> reference = reference(string);

        if(reference == null) return -1;

        return reference.value().getGrassColor(x, z);
    }
}
