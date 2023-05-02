package com.github.lordtgm.redstone;

import com.github.lordtgm.util.Location;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockHandler;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;

public abstract class RedstoneBlockHandler implements BlockHandler {

    public static final RedstoneBlockHandler opaqueBlockHandler = new RedstoneBlockHandler() {

        @Override
        public @NotNull NamespaceID getNamespaceId() {
            return NamespaceID.from("redstone:opaque");
        }

        @SuppressWarnings("SameReturnValue")
        @Override
        public boolean hasWeakPower(Location location) {
            return true;
        }

        @Override
        public PowerType getPowerType() {
            return PowerType.POWER_WEAK;
        }
    };

    public static final RedstoneBlockHandler transparentBlockHandler = new RedstoneBlockHandler() {

        @Override
        public boolean update(Location location, Location initiator) {
            return false;
        }

        @Override
        public @NotNull NamespaceID getNamespaceId() {
            return NamespaceID.from("redstone:transparent");
        }

        @SuppressWarnings("SameReturnValue")
        @Override
        public boolean hasWeakPower(Location location) {
            return false;
        }

        @Override
        public PowerType getPowerType() {
            return PowerType.POWER_NULL;
        }
    };
    public static @NotNull Block getWithHandler(@NotNull Class<RedstoneBlockHandler> handlerClass, @NotNull Block block) {
        try {
            return block.withHandler(handlerClass.getDeclaredConstructor().newInstance());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean update(Location location, Location initiator) {
        return updatePowerLevel(location);
    }
    public void onNextTick(Runnable runnable) {
        MinecraftServer.getSchedulerManager().scheduleNextTick(runnable);
    }
    public void updateNearby(@NotNull Location location, Location initiator) {
        for (Location nearbyLocation : new Location[] {
                location.withPoint(location.point().add(-1, 0, 0)),
                location.withPoint(location.point().add(1, 0, 0)),
                location.withPoint(location.point().add(0, -1, 0)),
                location.withPoint(location.point().add(0, 1, 0)),
                location.withPoint(location.point().add(0, 0, -1)),
                location.withPoint(location.point().add(0, 0, 1))
                }) {
            if (!nearbyLocation.point().samePoint(initiator.point())) {
                if (getHandlerOrDummy(nearbyLocation.getBlock()).update(nearbyLocation, initiator)) {
                    updateNearby(nearbyLocation, location);
                }
            }
        }
    }

    public byte getStrongPowerLevel(@NotNull Location location) {
        return RedstoneTags.getStrongPowerLevel(location.getBlock());
    }

    public byte getWeakPowerLevel(@NotNull Location location) {
        return RedstoneTags.getWeakPowerLevel(location.getBlock());
    }

    protected void setStrongPowerLevel(@NotNull Location location, byte powerLevel) {
        location.modifyBlock((Block block) -> (RedstoneTags.withStrongPowerLevel(block, powerLevel)));
    }

    protected void setWeakPowerLevel(@NotNull Location location, byte powerLevel) {
        location.modifyBlock((Block block) -> (RedstoneTags.withWeakPowerLevel(block, powerLevel)));
    }

    public boolean updatePowerLevel(@NotNull Location location) {
        byte strongPowerLevel = 0;
        byte weakPowerLevel = 0;
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

                case POWER_NULL -> {
                    // do nothing
                }
                case POWER_WEAK -> {
                    if (getWeakPowerLevel(location) > weakPowerLevel) {
                        weakPowerLevel = getWeakPowerLevel(location);
                    }
                }
                case POWER_STRONG -> {
                    if (getStrongPowerLevel(location) > strongPowerLevel) {
                        strongPowerLevel = getStrongPowerLevel(location);
                    }
                }
            }
        }
        if (weakPowerLevel != getWeakPowerLevel(location)) {
                setWeakPowerLevel(location, weakPowerLevel);
                changed = true;
            }

        if (strongPowerLevel != getStrongPowerLevel(location)) {
                setStrongPowerLevel(location, strongPowerLevel);
                changed = true;
            }

        return changed;
    }

    @SuppressWarnings("SameReturnValue")
    public boolean hasWeakPower() {
        return false;
    }

    public PowerType getPowerType() {
        return PowerType.POWER_STRONG;
    }

    public static RedstoneBlockHandler getHandlerOrDummy(@NotNull Block block) {
        return (block.handler() instanceof RedstoneBlockHandler redstoneBlockHandler ? redstoneBlockHandler :
                (block.registry().isSolid() ? opaqueBlockHandler : transparentBlockHandler));
    }

    /*
    public void updateNearby(Location location) {
        updateNearby(location, location);
    }
    */

    @Override
    public void onPlace(@NotNull Placement placement) {
        System.out.println("Block placed");
        Location location = new Location(placement.getInstance(), placement.getBlockPosition());
        update(location, location);
        updateNearby(location, location);
    }

    @Override
    public void onDestroy(@NotNull Destroy destroy) {
        System.out.println("Block destroyed");
        Location location = new Location(destroy.getInstance(), destroy.getBlockPosition());
        updateNearby(location, location);
    }

    @Override
    public boolean onInteract(@NotNull Interaction interaction) {
        /*
        Location location = new Location(interaction.getInstance(), interaction.getBlockPosition());
        update(location);
        updateNearby(location, location);
         */
        return false;
    }

    @Override
    public void onTouch(@NotNull Touch touch) {
        /*
        Location location = new Location(touch.getInstance(), touch.getBlockPosition());
        update(location);
        updateNearby(location, location);
         */
    }

    @SuppressWarnings("SameReturnValue")
    public abstract boolean hasWeakPower(Location location);
}
