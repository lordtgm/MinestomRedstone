package com.github.lordtgm.redstone;

import net.minestom.server.instance.block.Block;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class RedstoneTags {
    public static final Tag<Byte> StrongPowerLevel = Tag.Byte("redstoneStrongPowerLevel");

    public static final Tag<Byte> WeakPowerLevel = Tag.Byte("redstoneWeakPowerLevel");

    public static byte getStrongPowerLevel(@NotNull Block block) {
        return Optional.ofNullable(block.getTag(StrongPowerLevel)).orElse((byte) 0);

    }
    public static byte getWeakPowerLevel(@NotNull Block block) {
        return Optional.ofNullable(block.getTag(WeakPowerLevel)).orElse((byte) 0);
    }

    public static Block withStrongPowerLevel(@NotNull Block block, byte powerLevel) {
        return block.withTag(StrongPowerLevel, powerLevel);
    }

    public static Block withWeakPowerLevel(@NotNull Block block, byte powerLevel) {
        return block.withTag(WeakPowerLevel, powerLevel);
    }

}
