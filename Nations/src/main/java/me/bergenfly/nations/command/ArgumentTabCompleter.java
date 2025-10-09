package me.bergenfly.nations.command;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface ArgumentTabCompleter {
    List<String> complete(CommandSender sender);
}
