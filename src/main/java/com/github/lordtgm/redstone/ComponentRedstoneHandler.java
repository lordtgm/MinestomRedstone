package com.github.lordtgm.redstone;

import com.github.lordtgm.util.Location;
import net.minestom.server.utils.Direction;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface ComponentRedstoneHandler extends RedstoneHandler {
    default byte getComponentPowerLevel(@NotNull Location location) {
        return Optional.ofNullable(location.getBlock().getTag(RedstoneTags.ComponentPowerLevel)).orElse((byte) 0);
    }

    default void setComponentPowerLevel(@NotNull Location location, byte value) {
        location.modifyBlock(block -> block.withTag(RedstoneTags.ComponentPowerLevel, value));
    }

    default byte getPowerDelivery(@NotNull Location location, @NotNull Direction direction) {
        return (doesDeliverPower(location, direction) ? getComponentPowerLevel(location) : 0);
    }

    default boolean doesStronglyPower() {
        return true;
    }


    @Override
    default boolean updatePower(Location location) {
        return updateComponentPowerLevel(location);
    }

    default boolean updateComponentPowerLevel(Location location) {
        byte powerLevel = 0;
        for (Direction direction : powerInputs(location)) {
            Location relativeLocation = RedstoneHandler.getRelativeLocation(location, direction);
            RedstoneHandler handler = RedstoneHandler.getHandler(relativeLocation);

            if (handler instanceof SolidRedstoneHandler solidRedstoneHandler) {

                if (solidRedstoneHandler.getWeakPowerLevel(relativeLocation) > powerLevel) {
                    powerLevel = solidRedstoneHandler.getWeakPowerLevel(relativeLocation);
                }

                if (doesStronglyPower()) {

                    if (solidRedstoneHandler.getStrongPowerLevel(relativeLocation) > powerLevel) {
                        powerLevel = solidRedstoneHandler.getStrongPowerLevel(relativeLocation);
                    }

                }

            }

            if (handler instanceof ComponentRedstoneHandler componentRedstoneHandler) {

                if (componentRedstoneHandler.getPowerDelivery(relativeLocation,direction) > powerLevel) {
                    powerLevel = componentRedstoneHandler.getPowerDelivery(relativeLocation,direction.opposite());
                }

            }
        }

        if (powerLevel != getComponentPowerLevel(location)) {
            setComponentPowerLevel(location, powerLevel);
            return true;
        }
        return false;
    }

    Direction[] powerInputs(@NotNull Location location);

    boolean doesDeliverPower(@NotNull Location location, @NotNull Direction direction);
}
