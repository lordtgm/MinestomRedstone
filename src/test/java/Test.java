import com.github.lordtgm.minestom.RedstoneExtension;
import com.github.lordtgm.util.DiagCommand;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.block.Block;
import net.minestom.server.utils.NamespaceID;
import net.minestom.server.world.DimensionType;

import java.util.Objects;

public class Test {
    public static void main(String[] args) {
        // Initialization
        MinecraftServer minecraftServer = MinecraftServer.init();
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        // Create the instance
        MinecraftServer.getDimensionTypeManager().addDimension(DimensionType.builder(NamespaceID.from("correct-overworld")).ambientLight(2.0F).build());

        InstanceContainer instanceContainer = instanceManager
                .createInstanceContainer(Objects.requireNonNull(MinecraftServer.getDimensionTypeManager().getDimension(NamespaceID.from("correct-overworld"))));

        // Set the ChunkGenerator
        instanceContainer.setGenerator(unit ->
                unit.modifier().fillHeight(0, 40, Block.GRASS_BLOCK));
        // Add an event callback to specify the spawning instance (and the spawn position)
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        RedstoneExtension redstoneExtension = new RedstoneExtension();
        redstoneExtension.initialize();

        globalEventHandler.addListener(PlayerLoginEvent.class, event -> {
            Player player = event.getPlayer();
            player.setGameMode(GameMode.CREATIVE);
            event.setSpawningInstance(instanceContainer);
            player.setRespawnPoint(new Pos(0, 42, 0));

        });

        MinecraftServer.getCommandManager().register(new DiagCommand());
        // Start the server on port 25565
        minecraftServer.start("0.0.0.0", 25565);
    }
}
