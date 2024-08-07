package me.bergenfly.newmod.villagers.pathfinding;

import me.bergenfly.newmod.villagers.pathfinding.place.Place;

import java.util.LinkedList;

public class NodeLink {
    public PathNode end;

    public LinkedList<Place> leftSide = new LinkedList<>();
    public LinkedList<Place> rightSide = new LinkedList<>();
}
