package me.bergenfly.nations.serializer.type;

import me.bergenfly.nations.model.plot.Lot;

public record NationDeserialized(String name, String leader, String[] towns, String[] outlaws, String initialName, long creationTime, String founder) {
}
