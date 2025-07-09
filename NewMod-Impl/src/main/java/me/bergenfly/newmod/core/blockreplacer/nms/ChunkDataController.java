package me.bergenfly.newmod.core.blockreplacer.nms;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import it.unimi.dsi.fastutil.bytes.ByteList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import me.bergenfly.newmod.core.NewModPlugin;
import net.minecraft.core.IdMapper;
import net.minecraft.network.VarInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChunkDataController {
    ProtocolManager protocolManager;
    IdMapper<BlockState> registry;

    int[] cactusStateIds = new int[16];

    public void onEnable() {
        registry = Block.BLOCK_STATE_REGISTRY;

        for(int i = 0; i < 16; i++) {
            cactusStateIds[i] = registry.getId(((CraftBlockData) Bukkit.createBlockData(Material.CACTUS, "[age=" + i + "]")).getState());
        }

        protocolManager = ProtocolLibrary.getProtocolManager();

        protocolManager.addPacketListener(new PacketAdapter(
                NewModPlugin.get(),
                ListenerPriority.HIGH,
                PacketType.Play.Server.MAP_CHUNK
        ) {
            @Override
            public void onPacketSending(PacketEvent event) {
                byte[] data = event.getPacket().getLevelChunkData().read(0).getBuffer();

                try {
                    ChunkSection[] processed = process(data);

                    for (ChunkSection section : processed) {
                        if (section.bitsPerEntry == 0) {
                            continue;
                        }

                        if (section.bitsPerEntry == 15) {
                            for(int i = 0; i < section.blockData.length; i++) {
                                long l = section.blockData[i];

                                int id0 = (int) (l & 0b0000_000000000000000_000000000000000_000000000000000_111111111111111);
                                int id1 = (int) ((l & 0b0000_000000000000000_000000000000000_111111111111111_000000000000000) >>> 15);
                                int id2 = (int) ((l & 0b0000_000000000000000_111111111111111_000000000000000_000000000000000L) >>> 30);
                                int id3 = (int) ((l & 0b0000_111111111111111_000000000000000_000000000000000_000000000000000L) >>> 45);

                                boolean dirty = false;

                                if(id0 > cactusStateIds[0] && id0 <= cactusStateIds[15]) {
                                    id0 = cactusStateIds[0];

                                    dirty = true;
                                }

                                if(id1 > cactusStateIds[0] && id1 <= cactusStateIds[15]) {
                                    id1 = cactusStateIds[0];

                                    dirty = true;
                                }

                                if(id2 > cactusStateIds[0] && id2 <= cactusStateIds[15]) {
                                    id2 = cactusStateIds[0];

                                    dirty = true;
                                }

                                if(id3 > cactusStateIds[0] && id3 <= cactusStateIds[15]) {
                                    id3 = cactusStateIds[0];

                                    dirty = true;
                                }

                                if(dirty) {
                                    section.dirty = true;
                                    section.blockData[i] = id0 | (long) id1 << 15 | (long) id2 << 30 | (long) id3 << 45;
                                }
                            }

                            continue;
                        }

                        for(int i = 0; i < section.palette.length; i++) {
                            int id = section.palette[i];

                            if(id > cactusStateIds[0] && id <= cactusStateIds[15]) {
                                section.palette[i] = cactusStateIds[0];
                            }
                        }

                        /*for(int i = 0; i < section.blockData.length; i++) {
                            int index = 0;
                            int bpe = section.bitsPerEntry;

                            long l = section.blockData[i];

                            while(true) {
                                long cur = ((0x1L << (index+bpe))-1) & -(0x1L << (index));

                                if (((int) cur) )
                            }
                        }*/
                    }
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private ChunkSection[] process(byte[] data) {
        List<ChunkSection> list = new ArrayList<>();

        ByteBuffer byteBuffer = ByteBuffer.wrap(data);

        while (true) {
            byte[] nonAir = {byteBuffer.get(), byteBuffer.get()};
            byte bitsPerEntryBlock = byteBuffer.get();

            ChunkSection section = new ChunkSection();

            section.numNotAir = nonAir;
            section.bitsPerEntry = bitsPerEntryBlock;

            if(bitsPerEntryBlock == 0) {
                // SINGLE VALUE BLOCK PALETTE
                int type = readVarInt(byteBuffer);

                section.palette = new int[]{type};

                loadBiomes(byteBuffer, section);
            } else if (bitsPerEntryBlock >= 4 && bitsPerEntryBlock <= 8) {
                // INDIRECT BLOCK PALETTE
                int lengthInt = readVarInt(byteBuffer);

                byte[] paletteLength = intToVarIntBytes(lengthInt);

                int[] palette = new int[lengthInt];

                for (int i = 0; i < lengthInt; i++) {
                    int blockId = readVarInt(byteBuffer);

                    palette[i] = blockId;
                }

                section.paletteLength = paletteLength;
                section.palette = palette;

                @SuppressWarnings("IntegerDivisionInFloatingPointContext")
                int numLongs = (int) Math.ceil(4096.0/(64/bitsPerEntryBlock));

                long[] blockData = new long[numLongs];

                for(int x = 0; x < numLongs; x++) {
                    //byteBuffer.end
                    blockData[x] = byteBuffer.getLong();
                }

                section.blockData = blockData;

                loadBiomes(byteBuffer, section);
            } else if(bitsPerEntryBlock == 15) {
                // DIRECT BLOCK PALETTE
                @SuppressWarnings("IntegerDivisionInFloatingPointContext")
                int numLongs = (int) Math.ceil(4096.0/(64/bitsPerEntryBlock));

                long[] blockData = new long[numLongs];

                for(int x = 0; x < numLongs; x++) {
                    //byteBuffer.end
                    blockData[x] = byteBuffer.getLong();
                }

                section.blockData = blockData;

                loadBiomes(byteBuffer, section);
            } else {
                throw new RuntimeException();
            }
        }
    }

    private void loadBiomes(ByteBuffer byteBuffer, ChunkSection section) {
        byte bitsPerEntryBiome = byteBuffer.get();


        ByteList biomeData = new ByteArrayList();

        biomeData.add(bitsPerEntryBiome);

        if(bitsPerEntryBiome == 0) {
            for(byte b : intToVarIntBytes(readVarInt(byteBuffer))) {
                biomeData.add(b);
            }
        } else if (bitsPerEntryBiome >= 1 && bitsPerEntryBiome <= 3) {
            byte paletteLength = byteBuffer.get(); // Supposedly this is a varint, but the value is always at or below 2^6 for biomes, so byte is fine

            for(int i = 0; i < paletteLength; i++) {
                for(byte b : intToVarIntBytes(readVarInt(byteBuffer))) {
                    biomeData.add(b);
                }
            }

            @SuppressWarnings("IntegerDivisionInFloatingPointContext")
            int numLongs = (int) Math.ceil(64.0/(64/bitsPerEntryBiome));

            for(int x = 0; x < numLongs*8; x++) {
                biomeData.add(byteBuffer.get());
            }
        } else if (bitsPerEntryBiome == 6) { //TODO custom biomes
            @SuppressWarnings("IntegerDivisionInFloatingPointContext")
            int numLongs = (int) Math.ceil(64.0/(64/bitsPerEntryBiome));

            for(int x = 0; x < numLongs*8; x++) {
                biomeData.add(byteBuffer.get());
            }
        } else {
            throw new RuntimeException();
        }

        section.biomeData = biomeData.toArray((byte[]) null);
    }

    private int toInt(byte[] varInt) {
        int ret = 0;

        int shift = 0;

        for(byte b : varInt) {

            ret = ret | ((b & 0b01111111) << shift);

            shift+=7;
        }

        return ret;
    }

    private int readVarInt(ByteBuffer byteBuffer) {
        /*byte[] blockId = new byte[5];

        int count = 0;

        while (true) {
            byte currentByte = byteBuffer.get();

            blockId[count] = currentByte;

            //TODO: check for all final 0s, if it becomes a problem. Shouldn't be, assuming the server uses a sane int to varint function.

            if (count > 3) {
                blockId[4] = (byte) (blockId[4] & 0b00001111);

                if (blockId[4] == 0) {
                    blockId[3] = (byte) (blockId[3] & 0b01111111);

                    break;
                }

                break;
            }

            if (currentByte > 0) {
                break;
            }

            if (currentByte == 0) {
                blockId[count - 1] = (byte) (blockId[count - 1] & 0b01111111);

                break;
            }

            count++;
        }

        byte[] ret = blockId[count-1] == 0 ? new byte[count-1] : new byte[count];

        //Supposedly with small arrays this is more performant
        //noinspection ManualArrayCopy
        for(int x = 0; x < ret.length; x++) {
            ret[x] = blockId[x];
        }

        return ret;*/

        int i = 0;
        int j = 0;

        byte b;
        do {
            b = byteBuffer.get();
            i |= (b & 127) << j++ * 7;
            if (j > 5) {
                throw new RuntimeException("VarInt too big");
            }
        } while(hasContinuationBit(b));

        return i;
    }

    public static byte[] intToVarIntBytes(int i) {
        ByteList list = new ByteArrayList();

        while((i & -128) != 0) {
            list.add((byte) (i & 127 | 128));
            i >>>= 7;
        }

        list.add((byte) i);
        return list.toArray((byte[]) null);
    }

    public static boolean hasContinuationBit(byte b) {
        return (b & 128) == 128;
    }
}
