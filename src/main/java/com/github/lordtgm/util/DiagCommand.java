package com.github.lordtgm.util;

import com.github.lordtgm.redstone.ComponentRedstoneHandler;
import com.github.lordtgm.redstone.RedstoneHandler;
import com.github.lordtgm.redstone.RedstoneTags;
import com.github.lordtgm.redstone.SolidRedstoneHandler;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;

import java.util.Map;

public class DiagCommand extends Command {
    public DiagCommand() {
        super("diag");
        setDefaultExecutor((sender, context) -> {
            if (sender instanceof Player player) {
                Location location = new Location(player.getInstance(),player.getTargetBlockPosition(4));
                for (Map.Entry<String, String> entry : (location).getBlock().properties().entrySet()) {
                    player.sendMessage(entry.getKey() + " : " + entry.getValue());
                }

                if (RedstoneHandler.getHandler(location) instanceof ComponentRedstoneHandler) {
                    player.sendMessage("Component power level:" + String.valueOf(location.getBlock().getTag(RedstoneTags.ComponentPowerLevel)));
                }

                if (RedstoneHandler.getHandler(location) instanceof SolidRedstoneHandler) {
                    player.sendMessage("Strong power level:" + String.valueOf(location.getBlock().getTag(RedstoneTags.StrongPowerLevel)));
                    player.sendMessage("Weak power level:" + String.valueOf(location.getBlock().getTag(RedstoneTags.WeakPowerLevel)));
                }


            }
        });


    }
}
