package com.github.lordtgm.minestom;

import com.github.lordtgm.handlers.RedstoneDustHandler;
import com.github.lordtgm.redstone.RedstoneBlockHandler;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.extensions.Extension;
import net.minestom.server.item.Material;

import java.util.HashMap;
import java.util.Map;

public class RedstoneExtension extends Extension {

    private final HashMap<Material, RedstoneBlockHandler> handlers;

    public RedstoneExtension() {
        handlers = new HashMap<>();
        handlers.putAll(Map.ofEntries(
                Map.entry(Material.REDSTONE, new RedstoneDustHandler())
        ));
    }

    public void initialize() {
        EventNode eventNode = EventNode.type("redstone-block-listener", EventFilter.BLOCK);
        MinecraftServer.getGlobalEventHandler().addChild(eventNode);
        new RegistryManager(handlers).register(eventNode);
    }

    public void terminate() {

    }
}