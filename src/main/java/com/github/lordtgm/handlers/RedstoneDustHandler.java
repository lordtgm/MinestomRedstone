package com.github.lordtgm.handlers;

import com.github.lordtgm.redstone.RedstoneBlockHandler;
import com.github.lordtgm.util.Location;
import net.minestom.server.item.Material;
import net.minestom.server.utils.Direction;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class RedstoneDustHandler extends RedstoneBlockHandler {

    public static final Object updateLock = new Object();
    public void updateNearby(@NotNull Location location, Location initiator) {
        for (Location nearbyLocation : new Location[] {
                location.withPoint(location.point().add(-1, -1, 0)),
                location.withPoint(location.point().add(-1, 0, 0)),
                location.withPoint(location.point().add(-1, 1, 0)),

                location.withPoint(location.point().add(1, -1, 0)),
                location.withPoint(location.point().add(1, 0, 0)),
                location.withPoint(location.point().add(1, 1, 0)),

                location.withPoint(location.point().add(0, -1, 0)),
                location.withPoint(location.point().add(0, 1, 0)),

                location.withPoint(location.point().add(0, -1, -1)),
                location.withPoint(location.point().add(0, 0, -1)),
                location.withPoint(location.point().add(0, 1, -1)),

                location.withPoint(location.point().add(0, -1, 1)),
                location.withPoint(location.point().add(0, 0, 1)),
                location.withPoint(location.point().add(0, 1, 1)),
        }) {
            if (!nearbyLocation.point().samePoint(initiator.point())) {
                if (getHandlerOrDummy(nearbyLocation.getBlock()).update(nearbyLocation, initiator)) {
                    updateNearby(nearbyLocation, location);
                }
            }
        }
    }

    @Override
    public boolean updatePowerLevel(@NotNull Location location) {
        byte strongPowerLevel = 0;
        boolean changed = false;

        for (Location nearbyLocation : new Location[] {
                location.withPoint(location.point().add(-1, 0, 0)),
                location.withPoint(location.point().add(1, 0, 0)),
                location.withPoint(location.point().add(0, -1, 0)),
                location.withPoint(location.point().add(0, 1, 0)),
                location.withPoint(location.point().add(0, 0, -1)),
                location.withPoint(location.point().add(0, 0, 1))
        }) {
            switch (getHandlerOrDummy(nearbyLocation.getBlock()).getPowerType()) {

                case POWER_STRONG -> {
                    if (getStrongPowerLevel(location) > strongPowerLevel) {
                        strongPowerLevel = getStrongPowerLevel(location);
                    }
                }

                default -> {
                }
            }
        }

        for (Location nearbyLocation : new Location[] {
                location.withPoint(location.point().add(-1, -1, 0)),
                location.withPoint(location.point().add(1, -1, 0)),
                location.withPoint(location.point().add(0, -1, -1)),
                location.withPoint(location.point().add(0, -1, 1))
        }) {
            switch (getHandlerOrDummy(nearbyLocation.getBlock()).getPowerType()) {

                case POWER_STRONG -> {
                    if (getStrongPowerLevel(location) > strongPowerLevel &&
                            !nearbyLocation.withPoint(nearbyLocation.point().add(0,1,0)).getBlock().registry().isSolid()) {
                        strongPowerLevel = getStrongPowerLevel(location);
                    }
                }

                default -> {
                }
            }
        }

        for (Location nearbyLocation : new Location[] {
                location.withPoint(location.point().add(-1, 1, 0)),
                location.withPoint(location.point().add(1, 1, 0)),
                location.withPoint(location.point().add(0, 1, -1)),
                location.withPoint(location.point().add(0, 1, 1))
        }) {
            switch (getHandlerOrDummy(nearbyLocation.getBlock()).getPowerType()) {

                case POWER_STRONG -> {
                    if (getStrongPowerLevel(location) > strongPowerLevel &&
                            !location.withPoint(location.point().add(0,1,0)).getBlock().registry().isSolid()) {
                        strongPowerLevel = getStrongPowerLevel(location);
                    }
                }

                default -> {
                }
            }
        }

        if (strongPowerLevel != getStrongPowerLevel(location)) {
            setStrongPowerLevel(location, strongPowerLevel);
            changed = true;
        }
        return changed;
    }

    private boolean isRedstoneDust(@NotNull Location location) {
        return location.getBlock().registry().material() == Material.REDSTONE;
    }

    @Override
    public boolean update(Location location, Location initiator) {
        // return super.update(location) || updateShape(location);
        return updateShape(location, initiator);
    }

    public boolean updateShape(@NotNull Location location, Location initiator) {
        synchronized (updateLock) {
            HashMap<Direction, String> shapeMap = new java.util.HashMap<>(Map.ofEntries(
                    Map.entry(Direction.WEST, location.getBlock().getProperty("west")),
                    Map.entry(Direction.NORTH, location.getBlock().getProperty("north")),
                    Map.entry(Direction.SOUTH, location.getBlock().getProperty("south")),
                    Map.entry(Direction.EAST, location.getBlock().getProperty("east"))));

            HashMap<Direction, String> originalShapeMap = new HashMap<>(shapeMap);

            ArrayList<String> strings = new ArrayList<>();
            strings.add("update started: " + location.point().toString() + "\n");
            strings.add("Update issued on " + location.point() + " from " + initiator.point().toString() + "\n");

            for (Direction direction :
                    shapeMap.keySet()) {
                if (
                        isRedstoneDust(location.withPoint(point -> point.add(direction.normalX(), direction.normalY(), direction.normalZ()))) ^
                                (shapeMap.get(direction).equals("side"))) {
                    shapeMap.put(direction,
                            (isRedstoneDust(location.withPoint(point -> point.add(direction.normalX(), direction.normalY(), direction.normalZ()))) ?
                                    "side" : "none"));
                    strings.add("changed " + direction.name() + " to " + (isRedstoneDust(location.withPoint(point -> point.add(direction.normalX(), direction.normalY(), direction.normalZ()))) ?
                            "side\n" : "none\n"));
                }
            }

            for (Direction direction :
                    shapeMap.keySet()) {
                if (
                        isRedstoneDust(location.withPoint(point -> point.add(direction.normalX(), direction.normalY() + 1, direction.normalZ()))) ^
                                (shapeMap.get(direction).equals("up"))) {
                    shapeMap.put(direction,
                            (isRedstoneDust(location.withPoint(point -> point.add(direction.normalX(), direction.normalY() + 1, direction.normalZ()))) ?
                                    "up" : "none"));
                    strings.add("(up) changed " + direction.name() + " to " + (isRedstoneDust(location.withPoint(point -> point.add(direction.normalX(), direction.normalY() + 1, direction.normalZ()))) ?
                            "up\n" : "none\n"));
                }
            }
            if (shapeMap.keySet().stream().filter(direction -> (!shapeMap.get(direction).equals("none"))).count() == 1) {
                /*
                for (Direction direction :
                        shapeMap.keySet()) {
                    System.out.println(direction);
                    System.out.println(shapeMap.get(direction));
                }
                */

                // shapeMap.put(shapeMap.keySet().stream().filter(direction -> (!shapeMap.get(direction).equals("none"))).findFirst().get().opposite(), "side");
                // shapeMap.get(shapeMap.keySet().stream().filter(direction  -> (shapeMap.get(direction) != "none")).findFirst().get()));
            }

            location.modifyBlock(block -> block
                    .withProperty("west", shapeMap.get(Direction.WEST))
                    .withProperty("north", shapeMap.get(Direction.SOUTH))
                    .withProperty("south", shapeMap.get(Direction.SOUTH))
                    .withProperty("east", shapeMap.get(Direction.EAST))
            );
            // System.out.println(location.point().x() + " " + location.point().y() + " " + location.point().z());
            for (Direction direction :
                    shapeMap.keySet()) {
                if (shapeMap.get(direction) != originalShapeMap.get(direction)) {
                    System.out.println(String.join(" ", strings) + " update ended changed true: " + location.point().toString());
                    return true;
                }
            }
            System.out.println(String.join(" ", strings) + " update ended changed false: " + location.point().toString());
            return false;
        }

    }

    @Override
    public boolean hasWeakPower(Location location) {
        return false;
    }

    @Override
    protected void setStrongPowerLevel(@NotNull Location location, byte powerLevel) {
        super.setStrongPowerLevel(location, powerLevel);
        location.modifyBlock(block -> block.withProperty("power", String.valueOf(powerLevel)));
    }

    @Override
    public @NotNull NamespaceID getNamespaceId() {
        return NamespaceID.from("redstone:redstone_dust");
    }
}
