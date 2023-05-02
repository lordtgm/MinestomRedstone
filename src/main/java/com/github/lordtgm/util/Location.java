package com.github.lordtgm.util;

import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

import java.util.function.Function;

public record Location(Instance instance, Point point) {

    public Block getBlock() {
        return instance.getBlock(point);
    }

    public void setBlock(Block block) {
        instance.setBlock(point, block);
    }

    public void modifyBlock(Function<Block, Block> function) {
        setBlock(function.apply(getBlock()));
    }

    public Location withPoint(Point point) {
        return new Location(this.instance, point);
    }

// --Commented out by Inspection START (5/2/2023 11:22 AM):
//    public Location withInstance(Instance instance) {
//        return new Location(instance, this.point);
//    }
// --Commented out by Inspection STOP (5/2/2023 11:22 AM)

}
