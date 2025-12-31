package me.bergenfly.nations.model.plot;

import me.bergenfly.nations.model.LandAdministrator;

import java.util.Objects;

public class DivisionStorage {
    private ChunkChunk[][] divisionsStorage;

    private int numberDivisions;

    public DivisionStorage(int divisions) {
        if(divisions < 0 || divisions > 4) {
            throw new IllegalArgumentException();
        }

        numberDivisions = divisions;

        divisionsStorage = new ChunkChunk[(int) Math.pow(2,divisions)][(int) Math.pow(2,divisions)];
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
