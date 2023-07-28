package com.github.lordtgm.minestom;

import com.github.lordtgm.blockhandlers.LeverBlockHandler;
import com.github.lordtgm.blockhandlers.RedstoneDustBlockHandler;
import com.github.lordtgm.handlers.LeverHandler;
import com.github.lordtgm.handlers.RedstoneDustHandler;
import com.github.lordtgm.redstone.RedstoneHandler;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.extensions.Extension;
import net.minestom.server.instance.block.BlockHandler;
import net.minestom.server.item.Material;

import java.util.HashMap;

public class RedstoneExtension extends Extension {

    public final HashMap<Material, BlockHandler> blockHandlers;

    public RedstoneExtension() {
        RedstoneHandler.updateHandlers.put(Material.REDSTONE, new RedstoneDustHandler());
        RedstoneHandler.updateHandlers.put(Material.LEVER, new LeverHandler());

        blockHandlers = new HashMap<>();
        blockHandlers.put(Material.LEVER, new LeverBlockHandler());
        blockHandlers.put(Material.REDSTONE, new RedstoneDustBlockHandler());

        MinecraftServer.getGlobalEventHandler().addChild(EventNode.type("redstone-extension", EventFilter.BLOCK))
                .addListener(PlayerBlockPlaceEvent.class, playerBlockPlaceEvent -> {
                    if (blockHandlers.containsKey(playerBlockPlaceEvent.getBlock().registry().material())) {
                        playerBlockPlaceEvent.setBlock(playerBlockPlaceEvent.getBlock().withHandler(
                                blockHandlers.get(playerBlockPlaceEvent.getBlock().registry().material())));
                    }
                });

    }

    public void initialize() {
    }

    public void terminate() {

    }
}