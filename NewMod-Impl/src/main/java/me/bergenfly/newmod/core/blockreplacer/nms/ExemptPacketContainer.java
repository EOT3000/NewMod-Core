package me.bergenfly.newmod.core.blockreplacer.nms;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;

public class ExemptPacketContainer extends PacketContainer {
    public ExemptPacketContainer(PacketType type) {
        super(type);
    }

    public ExemptPacketContainer(PacketType type, Object handle) {
        super(type, handle);
    }

    public ExemptPacketContainer(PacketType type, Object handle, StructureModifier<Object> structure) {
        super(type, handle, structure);
    }
}
