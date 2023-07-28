package com.github.lordtgm.handlers;

import com.github.lordtgm.redstone.ComponentRedstoneHandler;
import com.github.lordtgm.util.Location;
import net.minestom.server.item.Material;
import net.minestom.server.utils.Direction;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class RedstoneDustHandler implements ComponentRedstoneHandler {

    @Override
    public byte getPowerDelivery(@NotNull Location location, @NotNull Direction direction) {
        return (byte) ((ComponentRedstoneHandler.super.getPowerDelivery(location, direction))
                        + ((location.withPoint(point -> point.add(direction.normalX(), direction.normalY(), direction.normalZ()))
                                .getBlock().registry().material() == Material.REDSTONE)
                                ? (byte) -1 : (byte) 0));
    }

    @Override
    public boolean doesDeliverPower(@NotNull Location location, @NotNull Direction direction) {
        return Arrays.asList(Direction.HORIZONTAL).contains(direction);
    }

    @Override
    public boolean doesStronglyPower() {
        return false;
    }

    @Override
    public Direction[] powerInputs(@NotNull Location location) {
        return Direction.values();
    }

    @Override
    public void setComponentPowerLevel(@NotNull Location location, byte value) {
        ComponentRedstoneHandler.super.setComponentPowerLevel(location, value);
        location.modifyBlock(block -> block.withProperty("power", String.valueOf(value)));
    }
}
