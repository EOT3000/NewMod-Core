package me.bergenfly.nations.serializer.type;

import me.bergenfly.nations.model.plot.Lot;

import java.util.List;

public record LotDeserialized(String[] rectangles, String owner, String administrator, String world, String[] trusted) {
}
