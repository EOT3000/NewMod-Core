package me.bergenfly.nations.api.command;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class CommandStem {
    protected int CURRENT_LOCATION = CommandFlower.CURRENT_LOCATION;
    protected int MEMBERSHIP = CommandFlower.MEMBERSHIP;
    protected int SELF = CommandFlower.SELF;

    private final Map<String, CommandStem> branches = new HashMap<>();
    public final CommandFlower flower;

    public CommandStem(@Nullable CommandFlower flower) {
        this.flower = flower;
    }

    public void addBranch(String s, CommandStem c) {
        branches.put(s, c);
    }

    public CommandStem next(String strings) {
        return branches.get(strings);
    }
}
