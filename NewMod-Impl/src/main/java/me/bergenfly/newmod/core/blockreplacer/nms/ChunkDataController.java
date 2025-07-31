package me.bergenfly.newmod.core.blockreplacer.nms;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import it.unimi.dsi.fastutil.bytes.ByteList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import me.bergenfly.newmod.core.NewModPlugin;
import me.bergenfly.newmod.core.blockreplacer.nms.wrapper.FixedWrappedBlockData;
import net.minecraft.core.IdMapper;
import net.minecraft.network.VarInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChunkDataController {
    ProtocolManager protocolManager;
    IdMapper<BlockState> registry;

    int[] cactusStateIds = new int[16];

    public void sendCactus(Location location, Player player) {
        PacketContainer blockPacket = new ExemptPacketContainer(PacketType.Play.Server.BLOCK_CHANGE);

        System.out.println("Sent dead cactus: " + location.toString());

        blockPacket.getBlockPositionModifier().write(0, new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
        blockPacket.getBlockData().write(0, new FixedWrappedBlockData(Material.CACTUS, 5));

        protocolManager.sendServerPacket(player, blockPacket, false);
    }

    public void onEnable() {
        registry = Block.BLOCK_STATE_REGISTRY;

        for(int i = 0; i < 16; i++) {
            cactusStateIds[i] = registry.getId(((CraftBlockData) Bukkit.createBlockData(Material.CACTUS, "[age=" + i + "]")).getState());
        }

        protocolManager = ProtocolLibrary.getProtocolManager();

        protocolManager.addPacketListener(new PacketAdapter(
                NewModPlugin.get(),
                ListenerPriority.HIGH,
                ObjectLists.singleton(PacketType.Play.Server.BLOCK_CHANGE),
                ListenerOptions.SYNC
        ) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if(event.getPacket() instanceof ExemptPacketContainer) {
                    return;
                }

                new Exception().printStackTrace();

                WrappedBlockData w = event.getPacket().getBlockData().read(0);

                if (w.getType().equals(Material.CACTUS)) {
                    System.out.println("Receieved cactus packet: " + event.getPacket());

                    FixedWrappedBlockData data = new FixedWrappedBlockData(w.getHandle());

                    System.out.println("Receieved cactus packet: " + data.getData());

                    data.setTypeAndData(Material.CACTUS, 1);

                    event.getPacket().getBlockData().write(0, data);
                }
            }
        });

        protocolManager.addPacketListener(new PacketAdapter(
                NewModPlugin.get(),
                ListenerPriority.HIGH,
                PacketType.Play.Server.MAP_CHUNK
        ) {
            @Override
            public void onPacketSending(PacketEvent event) {
                byte[] data = event.getPacket().getLevelChunkData().read(0).getBuffer();

                boolean dirtyChunk = false;

                long timeStart = System.nanoTime();

                try(ByteArrayOutputStream newChunk = new ByteArrayOutputStream()) {

                    ChunkSection[] processed = process(data);

                    //System.out.println("processed length: " + processed.length);

                    for (ChunkSection section : processed) {
                        if (section.bitsPerEntry == 0) {
                            //do nothing
                        } else if (section.bitsPerEntry == 15) {
                            for(int i = 0; i < section.blockData.length; i++) {
                                long l = section.blockData[i];

                                int id0 = (int) (l & 0b0000_000000000000000_000000000000000_000000000000000_111111111111111);
                                int id1 = (int) ((l & 0b0000_000000000000000_000000000000000_111111111111111_000000000000000) >>> 15);
                                int id2 = (int) ((l & 0b0000_000000000000000_111111111111111_000000000000000_000000000000000L) >>> 30);
                                int id3 = (int) ((l & 0b0000_111111111111111_000000000000000_000000000000000_000000000000000L) >>> 45);

                                boolean dirty = false;

                                if(needsFlag(id0)) {
                                    id0 = cactusStateIds[0];

                                    dirty = true;
                                }

                                if(needsFlag(id1)) {
                                    id1 = cactusStateIds[0];

                                    dirty = true;
                                }

                                if(needsFlag(id2)) {
                                    id2 = cactusStateIds[0];

                                    dirty = true;
                                }

                                if(needsFlag(id3)) {
                                    id3 = cactusStateIds[0];

                                    dirty = true;
                                }

                                if(dirty) {
                                    section.dirty = true;
                                    section.blockData[i] = id0 | (long) id1 << 15 | (long) id2 << 30 | (long) id3 << 45;
                                }
                            }
                        } else {
                            if(section.flagged) {
                                for (int i = 0; i < section.palette.length; i++) {
                                    int id = section.palette[i];

                                    if (id > cactusStateIds[0] && id <= cactusStateIds[15]) {
                                        section.palette[i] = cactusStateIds[0];
                                        section.dirty = true;
                                    }
                                }
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

                        if(section.dirty) {
                            dirtyChunk = true;

                            newChunk.write(section.numNotAir);
                            newChunk.write(section.bitsPerEntry);

                            if(section.bitsPerEntry == 0) {
                                newChunk.write(intToVarIntBytes(section.palette[0]));
                                newChunk.write(section.biomeData);
                            } else if(section.bitsPerEntry == 15) {
                                for(long l : section.blockData) {
                                    newChunk.write((byte) ((l&0xff_00_00_00_00_00_00_00L) >>> 56));
                                    newChunk.write((byte) ((l&0x00_ff_00_00_00_00_00_00L) >>> 48));
                                    newChunk.write((byte) ((l&0x00_00_ff_00_00_00_00_00L) >>> 40));
                                    newChunk.write((byte) ((l&0x00_00_00_ff_00_00_00_00L) >>> 32));
                                    newChunk.write((byte) ((l&0x00_00_ff_00_ff_00_00_00L) >>> 24));
                                    newChunk.write((byte) ((l&0x00_00_ff_00_00_ff_00_00L) >>> 16));
                                    newChunk.write((byte) ((l&0x00_00_ff_00_00_00_ff_00L) >>> 8));
                                    newChunk.write((byte) ((l&0x00_00_ff_00_00_00_00_ffL)));
                                }

                                newChunk.write(section.biomeData);
                            } else {
                                newChunk.write(section.paletteLength);

                                for(int paletteEntry : section.palette) {
                                    newChunk.write(intToVarIntBytes(paletteEntry));
                                }

                                for(long l : section.blockData) {
                                    newChunk.write((byte) ((l&0xff_00_00_00_00_00_00_00L) >>> 56));
                                    newChunk.write((byte) ((l&0x00_ff_00_00_00_00_00_00L) >>> 48));
                                    newChunk.write((byte) ((l&0x00_00_ff_00_00_00_00_00L) >>> 40));
                                    newChunk.write((byte) ((l&0x00_00_00_ff_00_00_00_00L) >>> 32));
                                    newChunk.write((byte) ((l&0x00_00_00_00_ff_00_00_00L) >>> 24));
                                    newChunk.write((byte) ((l&0x00_00_00_00_00_ff_00_00L) >>> 16));
                                    newChunk.write((byte) ((l&0x00_00_00_00_00_00_ff_00L) >>> 8));
                                    newChunk.write((byte) ((l&0x00_00_00_00_00_00_00_ffL)));
                                }

                                newChunk.write(section.biomeData);
                            }

                        } else {
                            newChunk.write(section.fullData);

                            //if(dirtyChunk) {
                                //System.out.println("clean chunk section: " + Arrays.toString(section.fullData));
                            //}
                        }
                    }

                    if(dirtyChunk) {

                        //System.out.println(Arrays.toString(newChunk.toByteArray()));
                        //System.out.println(Arrays.toString(event.getPacket().getLevelChunkData().read(0).getBuffer()));

                        event.getPacket().getLevelChunkData().read(0).setBuffer(newChunk.toByteArray());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //if(dirtyChunk) {
                //    System.out.println("Processed dirty chunk in " + (System.nanoTime()-timeStart));
                //}
            }
        });
    }

    private ChunkSection[] process(byte[] data) {
        List<ChunkSection> list = new ArrayList<>();

        ByteBuffer byteBuffer = ByteBuffer.wrap(data);

        while (byteBuffer.hasRemaining()) {
            int firstByte = byteBuffer.position();

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

                    if(needsFlag(blockId)) {
                        section.flagged = true;
                    }

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

                section.flagged = true;

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

            int lastPosition = byteBuffer.position();

            byte[] sectionData = new byte[lastPosition-firstByte];

            System.arraycopy(data, firstByte, sectionData, 0, sectionData.length);

            section.fullData = sectionData;

            list.add(section);
        }

        return list.toArray(new ChunkSection[0]);
    }

    private boolean needsFlag(int id) {
        return id > cactusStateIds[0] && id <= cactusStateIds[15];
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

        section.biomeData = biomeData.toArray(new byte[0]);
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
        return list.toArray(new byte[0]);
    }

    public static boolean hasContinuationBit(byte b) {
        return (b & 128) == 128;
    }

    public BlockData get(int id) {
        return CraftBlockData.createData(registry.byId(id));
    }
}
