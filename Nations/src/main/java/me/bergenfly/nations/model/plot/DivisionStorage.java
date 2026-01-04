package me.bergenfly.nations.model.plot;

import me.bergenfly.nations.NationsPlugin;
import me.bergenfly.nations.model.LandAdministrator;
import me.bergenfly.nations.registry.Registry;
import me.bergenfly.nations.serializer.Serializable;
import me.bergenfly.nations.serializer.type.ChunkChunkDeserialized;
import me.bergenfly.nations.serializer.type.DivisionStorageDeserialized;

import java.util.*;

public class DivisionStorage implements Serializable {
    private ChunkChunk[][] divisionsStorage;

    private int numberDivisions;



    public DivisionStorage(int divisions, ClaimedChunk claimedChunk) {
        if(divisions < 0 || divisions > 4) {
            throw new IllegalArgumentException("Number of DivisionStorage divisions cannot be below 0, or above 4");
        }

        numberDivisions = divisions;

        divisionsStorage = new ChunkChunk[(int) Math.pow(2,divisions)][(int) Math.pow(2,divisions)];
    }

    public DivisionStorage(DivisionStorageDeserialized data, ClaimedChunk claimedChunk) {
        this(data.numDivisions(), claimedChunk);

        Registry<Serializable, String> ID_HAVERS = NationsPlugin.getInstance().idHaverRegistry();

        for(ChunkChunkDeserialized chunkChunk : data.divisions()) {
            ChunkChunk pieceOfChunk = new ChunkChunk(
                    (LandAdministrator) ID_HAVERS.get(chunkChunk.administrator()),
                    (LandAdministrator) ID_HAVERS.get(chunkChunk.claimer()));

            divisionsStorage[chunkChunk.xColumn()][chunkChunk.zRow()] = pieceOfChunk;

            pieceOfChunk.holder.addLand();
        }
    }

    public DivisionStorage(LandAdministrator holder, LandAdministrator administrator) {
        numberDivisions = 1;

        divisionsStorage = new ChunkChunk[1][1];

        divisionsStorage[0][0] = new ChunkChunk(holder, administrator);
    }


    public void divide() {
        if(numberDivisions >= 4) {
            throw new IllegalStateException("Cannot split a chunk further than 4 times");
        }

        numberDivisions++;

        ChunkChunk[][] newStorage = new ChunkChunk[(int) Math.pow(2,numberDivisions)][(int) Math.pow(2,numberDivisions)];

        for(int xColumn = 0; xColumn < divisionsStorage.length; xColumn++) {
            for(int zRow = 0; zRow < divisionsStorage.length; zRow++) {
                ChunkChunk here = divisionsStorage[xColumn][zRow];

                newStorage[xColumn*2][zRow*2] = here;
                newStorage[xColumn*2+1][zRow*2] = here;
                newStorage[xColumn*2][zRow*2+1] = here;
                newStorage[xColumn*2+1][zRow*2+1] = here;
            }
        }

        divisionsStorage = newStorage;
    }

    public boolean simplify() {
        for(int xColumn = 0; xColumn < divisionsStorage.length/2; xColumn++) {
            for(int zRow = 0; zRow < divisionsStorage.length/2; zRow++) {
                if(!Objects.equals(divisionsStorage[xColumn*2][zRow*2], divisionsStorage[xColumn*2][zRow*2+1]) ||
                        !Objects.equals(divisionsStorage[xColumn*2][zRow*2+1], divisionsStorage[xColumn*2+1][zRow*2]) ||
                        !Objects.equals(divisionsStorage[xColumn*2+1][zRow*2], divisionsStorage[xColumn*2+1][zRow*2+1])) {
                    return false;
                }
            }
        }

        numberDivisions--;

        ChunkChunk[][] newStorage = new ChunkChunk[(int) Math.pow(2,numberDivisions)][(int) Math.pow(2,numberDivisions)];

        for(int xColumn = 0; xColumn < divisionsStorage.length; xColumn+=2) {
            for(int zRow = 0; zRow < divisionsStorage.length; zRow+=2) {
                ChunkChunk here = divisionsStorage[xColumn][zRow];

                newStorage[xColumn/2][zRow/2] = here;
            }
        }

        divisionsStorage = newStorage;

        simplify();

        return true;
    }

    public void setAt(int x0_15, int z0_15, LandAdministrator holder, LandAdministrator administrator) {
        int xIndex = x0_15/((int) Math.pow(2, 4-numberDivisions));
        int zIndex = z0_15/((int) Math.pow(2, 4-numberDivisions));

        divisionsStorage[xIndex][zIndex] = new ChunkChunk(holder, administrator);
    }

    @Override
    public Object serialize() {
        Map<String, Object> ret = new HashMap<>();

        ret.put("numDivisions", numberDivisions);

        List<Map<String, Object>> divisions = new ArrayList<>();

        for(int xColumn = 0; xColumn < divisionsStorage.length; xColumn++) {
            for (int zRow = 0; zRow < divisionsStorage.length; zRow++) {
                if(divisionsStorage[xColumn][zRow] != null) {
                    ChunkChunk bit = divisionsStorage[xColumn][zRow];

                    Map<String, Object> bitStorage = new HashMap<>();

                    bitStorage.put("claimer", bit.holder.getId());
                    bitStorage.put("administrator", bit.administrator.getId());
                    bitStorage.put("xColumn", xColumn);
                    bitStorage.put("zRow", zRow);

                    divisions.add(bitStorage);
                }
            }
        }

        ret.put("divisions", divisions);

        return ret;
    }

    @Override
    public String getId() {
        throw new UnsupportedOperationException("DivisionStorage does not implement getId");
    }

    private static record ChunkChunk(LandAdministrator holder, LandAdministrator administrator) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ChunkChunk that = (ChunkChunk) o;
            return Objects.equals(holder, that.holder) && Objects.equals(administrator, that.administrator);
        }

        @Override
        public int hashCode() {
            return Objects.hash(holder, administrator);
        }
    }
}
