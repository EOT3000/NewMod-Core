package me.fly.newmod.villagers.sddds;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.npc.EntityVillager;
import net.minecraft.world.entity.schedule.Activity;
import org.apache.logging.log4j.util.TriConsumer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_20_R3.CraftServer;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftVillager;
import org.bukkit.entity.Villager;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;

public class VillagerBehavior extends Behavior<EntityVillager> {
    protected static final Map<WorldServer, World> converted = new HashMap<>();

    private WorldVillagerTimeConsumer start = null;
    private WorldVillagerTimeConsumer tick = null;
    private WorldVillagerTimeConsumer stop = null;

    private WorldVillagerTimePredicate canStillUse = null;
    private LongPredicate isTimedOut = null;

    private BiPredicate<World, Villager> hasExtraStartConditions = null;
    private Predicate<Villager> hasRequiredMemories = null;

    private static Map<MemoryModuleType<?>, MemoryStatus> x(Map<String, VillagerMemoryStatus> requiredMemoryState) {
        ImmutableMap.Builder<MemoryModuleType<?>, MemoryStatus> map = ImmutableMap.builder();

        for(Map.Entry<String, VillagerMemoryStatus> entry : requiredMemoryState.entrySet()) {
            map.put(BuiltInRegistries.B.a(new MinecraftKey(entry.getKey())), entry.getValue().toNMS());
        }

        return map.build();
    }

    public VillagerBehavior(Map<String, VillagerMemoryStatus> requiredMemoryState) {
        super(x(requiredMemoryState), 60, 60);
    }

    public VillagerBehavior(Map<String, VillagerMemoryStatus> requiredMemoryState, int runTime) {
        super(x(requiredMemoryState), runTime, runTime);
    }

    public VillagerBehavior(Map<String, VillagerMemoryStatus> requiredMemoryState, int minRunTime, int maxRunTime) {
        super(x(requiredMemoryState), minRunTime, maxRunTime);
    }

    public VillagerBehavior start(WorldVillagerTimeConsumer start) {
        if (this.start != null) {
            throw new RuntimeException();
        }

        this.start = start;

        return this;
    }


    public VillagerBehavior tick(WorldVillagerTimeConsumer tick) {
        if (this.tick != null) {
            throw new RuntimeException();
        }

        this.tick = tick;

        return this;
    }

    public VillagerBehavior stop(WorldVillagerTimeConsumer stop) {
        if (this.stop != null) {
            throw new RuntimeException();
        }

        this.stop = stop;

        return this;
    }

    public VillagerBehavior canStillUse(WorldVillagerTimePredicate canStillUse) {
        if (this.canStillUse != null) {
            throw new RuntimeException();
        }

        this.canStillUse = canStillUse;

        return this;
    }

    public VillagerBehavior isTimedOut(LongPredicate isTimedOut) {
        if (this.isTimedOut != null) {
            throw new RuntimeException();
        }

        this.isTimedOut = isTimedOut;

        return this;
    }

    public VillagerBehavior hasExtraStartConditions(BiPredicate<World, Villager> hasExtraStartConditions) {
        if (this.hasExtraStartConditions != null) {
            throw new RuntimeException();
        }

        this.hasExtraStartConditions = hasExtraStartConditions;

        return this;
    }

    public VillagerBehavior hasRequiredMemories(Predicate<Villager> hasRequiredMemories) {
        if (this.hasRequiredMemories != null) {
            throw new RuntimeException();
        }

        this.hasRequiredMemories = hasRequiredMemories;

        return this;
    }

    //Start
    @Override
    protected void d(WorldServer world, EntityVillager entity, long time) {
        if (start != null) {
            start.accept(converted.get(world), new CraftVillager((CraftServer) Bukkit.getServer(), entity), time);
        } else {
            super.d(world, entity, time);
        }
    }

    //Tick
    @Override
    protected void c(WorldServer world, EntityVillager entity, long time) {
        if (tick != null) {
            tick.accept(converted.get(world), new CraftVillager((CraftServer) Bukkit.getServer(), entity), time);
        } else {
            super.c(world, entity, time);
        }
    }

    //Stop
    @Override
    protected void b(WorldServer world, EntityVillager entity, long time) {
        if (stop != null) {
            stop.accept(converted.get(world), new CraftVillager((CraftServer) Bukkit.getServer(), entity), time);
        } else {
            super.b(world, entity, time);
        }
    }

    //Can Still Use?
    @Override
    protected boolean a(WorldServer world, EntityVillager entity, long time) {
        if (canStillUse != null) {
            return canStillUse.test(converted.get(world), new CraftVillager((CraftServer) Bukkit.getServer(), entity), time);
        } else {
            return super.a(world, entity, time);
        }
    }

    //Is Timed Out?
    @Override
    protected boolean a(long time) {
        if (isTimedOut != null) {
            return isTimedOut.test(time);
        } else {
            return super.a(time);
        }
    }

    //Has Extra Start Conditions?
    @Override
    protected boolean a(WorldServer world, EntityVillager entity) {
        if (hasExtraStartConditions != null) {
            return hasExtraStartConditions.test(converted.get(world), new CraftVillager((CraftServer) Bukkit.getServer(), entity));
        } else {
            return super.a(world, entity);
        }
    }

    //Has Required Memories?
    @Override
    protected boolean a(EntityVillager entity) {
        if (hasRequiredMemories != null) {
            return hasRequiredMemories.test(new CraftVillager((CraftServer) Bukkit.getServer(), entity));
        } else {
            return super.a(entity);
        }
    }

    public interface WorldVillagerTimeConsumer {
        void accept(World world, Villager villager, long time);
    }

    public interface WorldVillagerTimePredicate {
        boolean test(World world, Villager villager, long time);
    }

    public enum VillagerMemoryStatus {
        VALUE_PRESENT,
        VALUE_ABSENT,
        REGISTERED;

        private MemoryStatus toNMS() {
            return switch (this) {
                case VALUE_PRESENT -> MemoryStatus.a;
                case VALUE_ABSENT -> MemoryStatus.b;
                case REGISTERED -> MemoryStatus.c;
            };
        }
    }
}
