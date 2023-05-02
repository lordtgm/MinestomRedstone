package com.github.lordtgm.minestom;

import com.github.lordtgm.redstone.RedstoneBlockHandler;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.event.trait.BlockEvent;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class RegistryManager {
    public final HashMap<Material, RedstoneBlockHandler> handlers;

    public RegistryManager(HashMap<Material, RedstoneBlockHandler> handlers) {
        this.handlers = handlers;
    }
    public void register(@NotNull EventNode<BlockEvent> eventNode) {

        eventNode.addListener(PlayerBlockPlaceEvent.class, event -> {
            if (handlers.containsKey(event.getBlock().registry().material())) {
                event.setBlock(event.getBlock().withHandler(handlers.get(event.getBlock().registry().material())));
            }
        });
    }
}
