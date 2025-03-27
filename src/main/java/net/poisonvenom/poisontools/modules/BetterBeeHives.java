package net.poisonvenom.poisontools.modules;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;
import net.poisonvenom.poisontools.PoisonTools;

import java.util.ArrayList;
import java.util.List;

public class BetterBeeHives extends Module {
    private List<BeehiveBlockEntity> hives;
    public BetterBeeHives() {
        super(PoisonTools.Exclusives, "BetterBeehives", "Highlights bee hives and nests with 3 bees in them.");
    }

    @Override
    public void onActivate() {

    }
    @Override
    public void onDeactivate() {

    }

    @EventHandler
    private void onPreTick(TickEvent.Pre event) {
        if (mc.world == null || mc.player == null) return;

        int renderDistance = mc.options.getViewDistance().getValue();
        ChunkPos playerChunkPos = new ChunkPos(mc.player.getBlockPos());
        for (int chunkX = playerChunkPos.x - renderDistance; chunkX <= playerChunkPos.x + renderDistance; chunkX++) {
            for (int chunkZ = playerChunkPos.z - renderDistance; chunkZ <= playerChunkPos.z + renderDistance; chunkZ++) {
                WorldChunk chunk = mc.world.getChunk(chunkX, chunkZ);
                List<BlockEntity> blockEntities = new ArrayList<>(chunk.getBlockEntities().values());
                for (BlockEntity blockEntity : blockEntities) {
                    if (blockEntity instanceof BeehiveBlockEntity) {
                        int count = ((BeehiveBlockEntity) blockEntity).getBeeCount();

                    }
                }
            }
        }
    }
}
