package com.github.lordtgm.redstone;

import com.github.lordtgm.util.Location;
import net.minestom.server.coordinate.Point;
import net.minestom.server.item.Material;
import net.minestom.server.utils.Direction;

import java.util.HashMap;

public interface RedstoneHandler {

    HashMap<Material, RedstoneHandler> updateHandlers = new HashMap<>();

    RedstoneHandler SolidRedstoneHandler = new SolidRedstoneHandler.SolidRedstoneHandlerImpl();

    RedstoneHandler NonSolidRedstoneHandler = new RedstoneHandlerImpl();

    class RedstoneHandlerImpl implements RedstoneHandler {

        @Override
        public boolean updatePower(Location location) {
            return false;
        }
    }

    static RedstoneHandler getHandler(Location location) {
        return (updateHandlers.containsKey(location.getBlock().registry().material())
                ? updateHandlers.get(location.getBlock().registry().material())
                : (location.getBlock().isSolid() ? SolidRedstoneHandler : NonSolidRedstoneHandler));
    }

    static void updateAt(Location location) {
        getHandler(location).update(location);
    }

    /**
     * updates the power level of block in this location.
     * if the block is changed, nearby blocks will get updated as well.
     * @param location the location to update.
     * @return if the block was changed.
     */
    boolean updatePower(Location location);

    /**
     * updates all neighbors of this location.
     * @param location the location to update its neighbors.
     */
    default void updateNearby(Location location) {
        for (Location nearbyLocation : this.getNeighbors(location)) {
            if (!nearbyLocation.equals(location)) {
                RedstoneHandler.getHandler(nearbyLocation).update(nearbyLocation);
            }
        }
    }

    /**
     * updates the block at this location. if changed, this will also update nearby locations.
     * @param location the location to update.
     */
    default void update(Location location) {
        if (this.updatePower(location)) {
            this.updateNearby(location);
        }
    }

    default Location[] getNeighbors(Location location) {
        return new Location[] {
                location.withPoint(location.point().add(-1, 0, 0)),
                location.withPoint(location.point().add(1, 0, 0)),
                location.withPoint(location.point().add(0, -1, 0)),
                location.withPoint(location.point().add(0, 1, 0)),
                location.withPoint(location.point().add(0, 0, -1)),
                location.withPoint(location.point().add(0, 0, 1))
        };
    }

    static Direction getDirectionOf(Location location, Location target) {
        assert (location.instance() == target.instance());
        Point relative = location.point().sub(target.point());
        for (Direction direction : Direction.values()) {
            if (direction.normalX() == relative.blockX() && direction.normalY() == relative.blockY() && direction.normalZ() == relative.blockZ()) {
                return direction;
            }
        }
        throw new RuntimeException("Cannot find Direction for relative point " + relative);
    }

    static Location getRelativeLocation(Location location, Direction direction) {
        return location.withPoint(point -> point.add(direction.normalX(), direction.normalY(), direction.normalZ()));
    }
}
