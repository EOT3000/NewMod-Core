package me.bergenfly.newmod.flyfun.technology;

import org.bukkit.block.Block;

public interface EnergyComponent {
    EnergyComponentType getType();

    int getCapacity();

    Block getDestination(Block from, Block to, int stack);

    int push(Block from, Block to, int energy);

    enum EnergyComponentType {
        SENDER,
        RECEIVER,
        CONSUMER,
        PRODUCER,
        STORAGE,
        MANAGER,
        SENDER_RECEIVER,
        PULLER
    }
}
