package me.bergenfly.villagers.entity;

import com.mojang.serialization.Dynamic;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public abstract class AbstractNPC extends Villager {
    public AbstractNPC(EntityType<? extends Villager> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public Brain<Villager> getBrain() {
        return super.getBrain();
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        return super.makeBrain(dynamic);
    }

    @Override
    protected void customServerAiStep(ServerLevel level) {
        super.customServerAiStep(level);
    }

    @Override
    protected void customServerAiStep(ServerLevel level, boolean inactive) {
        super.customServerAiStep(level, inactive);
    }

    @Override
    public void inactiveTick() {
        super.inactiveTick();
    }

    @Override
    public void refreshBrain(ServerLevel serverLevel) {
        super.refreshBrain(serverLevel);
    }

    @Override
    protected Brain.Provider<Villager> brainProvider() {
        return super.brainProvider();
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        return super.mobInteract(player, hand);
    }
}
