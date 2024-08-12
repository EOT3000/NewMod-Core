package me.bergenfly.nations.api.command;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class CommandStem {
    protected int CURRENT_LOCATION = CommandFlower.CURRENT_LOCATION;
    protected int MEMBERSHIP = CommandFlower.MEMBERSHIP;
    protected int SELF = CommandFlower.SELF;

    final Map<String, CommandStem> branches = new HashMap<>();
    public final CommandFlower flower;

    final String key;
    final CommandStem parent;

    protected CommandStem(@Nullable CommandFlower flower, CommandStem parent, String key) {
        this.flower = flower == null ? new HelpCommandFlower(this) : flower;
        this.key = key;
        this.parent = parent;
    }

    public CommandStem addBranch(String s) {
        return branches.put(s, new CommandStem(null, this, s));
    }

    public CommandStem addBranch(String s, CommandFlower flower) {
        return branches.put(s, new CommandStem(flower, this, s));
    }

    public CommandStem next(String strings) {
        return branches.get(strings);
    }
}
