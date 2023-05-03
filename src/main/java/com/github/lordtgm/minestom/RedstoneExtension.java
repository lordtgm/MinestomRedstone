package com.github.lordtgm.minestom;

import com.github.lordtgm.handlers.LeverHandler;
import com.github.lordtgm.handlers.RedstoneDustHandler;
import com.github.lordtgm.redstone.RedstoneHandler;
import net.minestom.server.extensions.Extension;
import net.minestom.server.item.Material;

public class RedstoneExtension extends Extension {

    public RedstoneExtension() {
        RedstoneHandler.updateHandlers.put(Material.REDSTONE, new RedstoneDustHandler());
        RedstoneHandler.updateHandlers.put(Material.LEVER, new LeverHandler());
    }

    public void initialize() {
    }

    public void terminate() {

    }
}