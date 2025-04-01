package me.bergenfly.obfuscator.aroundtheworld;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class NMSGhost extends ServerPlayer {
    public NMSGhost(MinecraftServer server, ServerLevel level, com.mojang.authlib.GameProfile gameProfile, ClientInformation clientInformation) {
        super(server, level, gameProfile, clientInformation);
    }
}
