package me.bergenfly.nations.command;

import it.unimi.dsi.fastutil.objects.Object2IntFunction;

public interface CommandCompleterWithCodeFunctional {
    int complete(CommandFlower.NationsCommandInvocation o);
}
