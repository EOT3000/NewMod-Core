package me.bergenfly.nations.serializer.type;

import me.bergenfly.nations.model.plot.Lot;

import java.util.List;

//TODO try with UUIDs
public record TownDeserialized(String name, String leader, String[] residents, String[] outlaws, String initialName, long creationTime, Lot[] lots) {
}
