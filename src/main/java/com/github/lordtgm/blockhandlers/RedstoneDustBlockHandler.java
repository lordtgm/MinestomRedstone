package com.github.lordtgm.blockhandlers;

import com.github.lordtgm.util.Location;
import it.unimi.dsi.fastutil.Pair;
import net.minestom.server.item.Material;
import net.minestom.server.utils.Direction;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class RedstoneDustBlockHandler extends RedstoneConnectableBlockHandler {

    public void updateShape(Location location) {
        List<Pair<String,String>> state = new ArrayList<>();
        for (Pair<Location,String> nearbyLocation : Arrays.stream(Direction.HORIZONTAL)
                .map(direction -> Pair.of(
                        location.withPoint(
                                point -> point.add(direction.normalX(), direction.normalY(), direction.normalZ())),
                        direction.name().toLowerCase(Locale.ROOT))).toList()) {
            if (nearbyLocation.left().getBlock().registry().material() == Material.REDSTONE) {
                state.add(Pair.of(nearbyLocation.right(), "side"));
            } else if (nearbyLocation.left().withPoint(point -> point.add(0,1,0)).getBlock().registry().material() == Material.REDSTONE) {
                state.add(Pair.of(nearbyLocation.right(), "up"));
            } else if (nearbyLocation.left().withPoint(point -> point.add(0,-1,0)).getBlock().registry().material() == Material.REDSTONE) {
                state.add(Pair.of(nearbyLocation.right(), "side"));
            } else {
                state.add(Pair.of(nearbyLocation.right(), "none"));
            }
        }


        location.modifyBlock(block -> {
            for (Pair<String,String> direction :state) {
                block = block.withProperty(direction.left(), direction.right());
            }
            return block;
        });

    }

    @Override
    public void onPlace(@NotNull Placement placement) {
        super.onPlace(placement);
        Location location = new Location(placement.getInstance(), placement.getBlockPosition());
        for (Location nearbyLocation : Arrays.stream(Direction.HORIZONTAL).map(direction -> (
                location.withPoint(point -> point.add(direction.normalX(), direction.normalY(), direction.normalZ()))
        )).toList()) {
            if (nearbyLocation.getBlock().handler() instanceof RedstoneDustBlockHandler redstoneDustBlockHandler) {
                redstoneDustBlockHandler.updateShape(nearbyLocation);
            }

            if (nearbyLocation.withPoint(point -> point.add(0, -1, 0)).getBlock().handler() instanceof RedstoneDustBlockHandler redstoneDustBlockHandler) {
                redstoneDustBlockHandler.updateShape(nearbyLocation.withPoint(point -> point.add(0, -1, 0)));
            }

            if (nearbyLocation.withPoint(point -> point.add(0, 1, 0)).getBlock().handler() instanceof RedstoneDustBlockHandler redstoneDustBlockHandler) {
                redstoneDustBlockHandler.updateShape(nearbyLocation.withPoint(point -> point.add(0, 1, 0)));
            }
        }
    }

    @Override
    public void onDestroy(@NotNull Destroy destroy) {
        super.onDestroy(destroy);
        Location location = new Location(destroy.getInstance(), destroy.getBlockPosition());
        for (Location nearbyLocation : Arrays.stream(Direction.HORIZONTAL).map(direction -> (
                location.withPoint(point -> point.add(direction.normalX(), direction.normalY(), direction.normalZ()))
        )).toList()) {
            if (nearbyLocation.getBlock().handler() instanceof RedstoneDustBlockHandler redstoneDustBlockHandler) {
                redstoneDustBlockHandler.updateShape(nearbyLocation);
            }

            if (nearbyLocation.withPoint(point -> point.add(0, -1, 0)).getBlock().handler() instanceof RedstoneDustBlockHandler redstoneDustBlockHandler) {
                redstoneDustBlockHandler.updateShape(nearbyLocation.withPoint(point -> point.add(0, -1, 0)));
            }

            if (nearbyLocation.withPoint(point -> point.add(0, 1, 0)).getBlock().handler() instanceof RedstoneDustBlockHandler redstoneDustBlockHandler) {
                redstoneDustBlockHandler.updateShape(nearbyLocation.withPoint(point -> point.add(0, 1, 0)));
            }
        }
    }

    @Override
    public @NotNull NamespaceID getNamespaceId() {
        return NamespaceID.from("redstone:redstone-dust");
    }

}
