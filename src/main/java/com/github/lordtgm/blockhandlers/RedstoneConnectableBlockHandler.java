package com.github.lordtgm.blockhandlers;

import com.github.lordtgm.redstone.RedstoneHandler;
import com.github.lordtgm.util.Location;
import net.minestom.server.instance.block.BlockHandler;
import net.minestom.server.item.Material;
import net.minestom.server.utils.Direction;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public abstract class RedstoneConnectableBlockHandler implements BlockHandler {
    public static final Material[] redstoneConnectableMaterials = new Material[] {
            Material.REDSTONE,
            Material.LEVER
    };


    @Override
    public void onPlace(@NotNull Placement placement) {
        Location location = new Location(placement.getInstance(), placement.getBlockPosition());
        for (Location nearbyLocation : Arrays.stream(Direction.HORIZONTAL).map(direction -> (
                location.withPoint(point -> point.add(direction.normalX(), direction.normalY(), direction.normalZ()))
                )).toList()) {
            if (nearbyLocation.getBlock().handler() instanceof RedstoneDustBlockHandler redstoneDustBlockHandler) {
                redstoneDustBlockHandler.updateShape(nearbyLocation);
                RedstoneHandler.updateAt(nearbyLocation);
            }
        }
        RedstoneHandler.updateAt(location);
    }

    @Override
    public void onDestroy(@NotNull Destroy destroy) {
        Location location = new Location(destroy.getInstance(), destroy.getBlockPosition());
        for (Location nearbyLocation : Arrays.stream(Direction.HORIZONTAL).map(direction -> (
                location.withPoint(point -> point.add(direction.normalX(), direction.normalY(), direction.normalZ()))
        )).toList()) {
            if (nearbyLocation.getBlock().handler() instanceof RedstoneDustBlockHandler redstoneDustBlockHandler) {
                redstoneDustBlockHandler.updateShape(nearbyLocation);
            }
            RedstoneHandler.updateAt(nearbyLocation);

        }
    }

}
