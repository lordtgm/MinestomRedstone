package com.github.lordtgm.blockhandlers;

import com.github.lordtgm.redstone.RedstoneHandler;
import com.github.lordtgm.redstone.RedstoneTags;
import com.github.lordtgm.util.Location;
import net.minestom.server.tag.Tag;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class LeverBlockHandler extends RedstoneConnectableBlockHandler {

    public static final Tag<Boolean> leverOn = Tag.Boolean("redstone-lever-on");

    @Override
    public boolean onInteract(@NotNull Interaction interaction) {
        Location location = new Location(interaction.getInstance(), interaction.getBlockPosition());
        location.modifyBlock(block -> (block
                .withTag(leverOn,(!Objects.equals(block.getProperty("powered"), "true")))
                .withTag(RedstoneTags.ComponentPowerLevel,(Objects.equals(block.getProperty("powered"), "true")
                        ? (byte) 0 : (byte) 15)))
                .withProperty("powered",(block.getProperty("powered").equals("true")
                        ? "false" : "true"))
        );
        RedstoneHandler.updateAt(location);
        return false;
    }



    @Override
    public @NotNull NamespaceID getNamespaceId() {
        return NamespaceID.from("redstone:lever");
    }
}
