package net.poisonvenom.poisontools.modules;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.block.entity.SignText;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;
import net.poisonvenom.poisontools.PoisonTools;

import java.util.*;

public class OnlinePearls extends Module {
    private final List<PlayerEntity> playerList = new ArrayList<>();

    private final List<Text> potentialNames = new ArrayList<>();

    private static final Set<String> COMMON_WORDS = new HashSet<>(Arrays.asList(
            "the", "is", "in", "a", "and", "on", "of", "to", "for", "by", "with", "an", "that", "it", "at", "as", "this", "my", "me", "you", "your", "are", "was", "pearl", "pearls"
    ));

    public OnlinePearls() {
        super(PoisonTools.Exclusives, "OnlinePearls", "Determines if an enderpearl's thrower is online by using nearby sign text.");
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
                    if (blockEntity instanceof SignBlockEntity) {
                        SignText frontTxt = ((SignBlockEntity) blockEntity).getText(true);
                        SignText backTxt = ((SignBlockEntity) blockEntity).getText(true);
                        processSignText(frontTxt);
                        processSignText(backTxt);
                    }
                }
            }
        }

        for (PlayerEntity player : mc.world.getPlayers()) {
            for (Text name : potentialNames) {
                if (player.getName().toString().equalsIgnoreCase(name.toString())) {
                    playerList.add(player);
                }
            }
        }
        for (PlayerEntity player : playerList) {
            String name = player.getName().getLiteralString();
            ChatUtils.sendMsg(Text.of("This player is online: " + name));
        }
    }

    private void processSignText(SignText txt) {
        for (int i = 0; i < 4; i++) {
            List<Text> words = processSignLine(txt.getMessages(false)[i]);
            potentialNames.addAll(words);
            for (Text word : words) {
                //ChatUtils.sendMsg(word);
            }
        }
    }

    private List<Text> processSignLine(Text txt) {
        List<Text> uniqueWords = new ArrayList<>();

        String[] words = txt.getString().split("\\s+");
        for (String word : words) {
            //check for 's
            if (word.contains("'s")) {
                word = word.replace("'s", "");
            }
            String cleaned = word.replaceAll("[^a-zA-Z0-9]", "");
            if (cleaned.length() > 3 && !COMMON_WORDS.contains(cleaned)) {
                uniqueWords.add(Text.literal(cleaned));
            }
        }
        return uniqueWords;
    }
}
