package me.bergenfly.newmod.flyfun.camera.model;

import me.bergenfly.newmod.flyfun.camera.Textures;
import org.bukkit.block.data.BlockData;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class BlockStates {
    private final List<Function<BlockData, BlockState>> states = new ArrayList<>();
    private final List<BlockState> s = new ArrayList<>();

    public void addState(Predicate<BlockData> predicate, BlockState state) {
        states.add((x) -> predicate.test(x) ? state : null);

        s.add(state);
    }

    public BlockState getState(BlockData data) {
        for(Function<BlockData, BlockState> state : states) {
            BlockState check = state.apply(data);

            if(check != null) {
                return check;
            }
        }

        return Textures.FAILED_TO_LOAD_STATES.getState(null);
    }

    public void print() {
        for(BlockState st : s) {
            System.out.println(st.x);
            System.out.println(st.y);
            System.out.println(st.model);
        }
    }

    public List<BlockState> getStates() {
        return new ArrayList<>(s);
    }

    public static record BlockState(BlockModel model, int x, int y) {}
}
