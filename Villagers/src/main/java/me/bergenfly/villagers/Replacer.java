package me.bergenfly.villagers;

import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.EntityType;

import java.lang.reflect.Field;

public class Replacer {
    public static void replace() {
        WritableRegistry<EntityType<?>> registry = (WritableRegistry<EntityType<?>>) MinecraftServer.getServer().registryAccess().lookup(Registries.ENTITY_TYPE).orElseThrow();

        try {
            Field frozen = MappedRegistry.class.getDeclaredField("frozen");
            frozen.setAccessible(true);
            frozen.set(registry, false);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
