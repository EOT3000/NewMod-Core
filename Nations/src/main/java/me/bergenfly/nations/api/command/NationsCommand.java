package me.bergenfly.nations.api.command;

import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.Nation;
import me.bergenfly.nations.api.model.organization.Settlement;

import java.util.Set;

public class NationsCommand {


    public static final class NationsCommandInvocation {
        public final Nation[] nations;
        public final Settlement[] settlements;
        public final User[] users;

        public final int[] ints;
        public final float[] floats;

        public final boolean[] booleans;

        public final String[] strings;
    }
}
