package me.bergenfly.nations.api.command;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface ArgumentTabCompleter {
    List<String> complete(CommandSender sender);
}
