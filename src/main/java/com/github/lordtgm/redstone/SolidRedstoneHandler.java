package com.github.lordtgm.redstone;

import com.github.lordtgm.util.Location;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface SolidRedstoneHandler extends RedstoneHandler {

    class SolidRedstoneHandlerImpl implements SolidRedstoneHandler {

        @Override
        public boolean updatePower(Location location) {
            return updateSolidPowerLevels(location);
        }
    }

    default boolean updateSolidPowerLevels(@NotNull Location location) {
        byte maxStrongLevel = 0;
        byte maxWeakLevel = 0;
        byte powerDelivery;
        boolean changed = false;
        for (Location nearbyLocation :
                this.getNeighbors(location)) {
            if (RedstoneHandler.getHandler(nearbyLocation) instanceof ComponentRedstoneHandler componentRedstoneHandler) {
                powerDelivery = componentRedstoneHandler.getPowerDelivery(nearbyLocation, RedstoneHandler.getDirectionOf(nearbyLocation, location));
                if (componentRedstoneHandler.doesStronglyPower()) {
                    if (maxStrongLevel < powerDelivery) maxStrongLevel = powerDelivery;
                } else {
                    if (maxWeakLevel < powerDelivery) maxWeakLevel = powerDelivery;
                }
            }
        }

        if (maxStrongLevel != getStrongPowerLevel(location)) {
            setStrongPowerLevel(location, maxStrongLevel);
            changed = true;
        }

        if (maxWeakLevel != getWeakPowerLevel(location)) {
            setWeakPowerLevel(location, maxWeakLevel);
            changed = true;
        }

        return changed;
    }

    default byte getWeakPowerLevel(@NotNull Location location) {
        return Optional.ofNullable(location.getBlock().getTag(RedstoneTags.WeakPowerLevel)).orElse((byte) 0);
    }

    default byte getStrongPowerLevel(@NotNull Location location) {
        return Optional.ofNullable(location.getBlock().getTag(RedstoneTags.StrongPowerLevel)).orElse((byte) 0);
    }

    default void setWeakPowerLevel(@NotNull Location location, byte value) {
        location.modifyBlock(block -> block.withTag(RedstoneTags.WeakPowerLevel, value));
    }

    private void setStrongPowerLevel(@NotNull Location location, byte value) {
        location.modifyBlock(block -> block.withTag(RedstoneTags.StrongPowerLevel, value));
    }

}
