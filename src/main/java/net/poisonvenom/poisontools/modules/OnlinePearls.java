package net.poisonvenom.poisontools.modules;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.block.entity.SignText;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;
import net.poisonvenom.poisontools.PoisonTools;

import java.util.ArrayList;
import java.util.List;

public class OnlinePearls extends Module {
    private final List<ClientPlayerEntity> playerList = new ArrayList<>();
    public OnlinePearls() {
        super(PoisonTools.Exclusives, "OnlinePearls", "Determines if an enderpearl's thrower is online by using the sign text.");
    }

    @Override
    public void onActivate() {
        playerList.clear();
    }
    @Override
    public void onDeactivate() {
        playerList.clear();
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
                    if(blockEntity instanceof SignBlockEntity) {
                        SignText frontTxt = ((SignBlockEntity) blockEntity).getText(true);
                        SignText backTxt = ((SignBlockEntity) blockEntity).getText(true);
                        processSignText(frontTxt);
                        processSignText(backTxt);
                    }
                }
            }
        }
        mc.world.getPlayers().getFirst();
    }

    private void processSignText(SignText txt) {
        ChatUtils.sendMsg(txt.getMessages(false)[0]);
        ChatUtils.sendMsg(txt.getMessages(false)[1]);
        ChatUtils.sendMsg(txt.getMessages(false)[2]);
        ChatUtils.sendMsg(txt.getMessages(false)[3]);
    }
}
