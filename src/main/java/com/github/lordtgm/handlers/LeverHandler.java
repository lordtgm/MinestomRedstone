package com.github.lordtgm.handlers;

import com.github.lordtgm.redstone.ComponentRedstoneHandler;
import com.github.lordtgm.util.Location;
import net.minestom.server.utils.Direction;
import org.jetbrains.annotations.NotNull;

public class LeverHandler implements ComponentRedstoneHandler {
    @Override
    public Direction[] powerInputs(@NotNull Location location) {
        return new Direction[0];
    }

    @Override
    public boolean doesDeliverPower(@NotNull Location location, @NotNull Direction direction) {
        return true;
    }

    @Override
    public boolean updateComponentPowerLevel(Location location) {
        return false;
    }

    @Override
    public void update(Location location) {
        this.updateNearby(location);
    }
}
