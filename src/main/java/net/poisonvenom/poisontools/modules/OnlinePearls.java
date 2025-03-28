package net.poisonvenom.poisontools.modules;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.block.entity.SignText;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;
import net.poisonvenom.poisontools.PoisonTools;

import java.util.*;
import java.util.stream.Collectors;

public class OnlinePearls extends Module {
    private final List<Text> playerList = new ArrayList<>();
    private final List<Text> potentialNames = new ArrayList<>();
    private static final Set<String> COMMON_WORDS = new HashSet<>(Arrays.asList(
            "the", "is", "in", "a", "and", "on", "of", "to", "for", "by", "with", "an", "that", "it", "at", "as", "this", "my", "me", "you", "your", "are", "was", "pearl", "pearls", "back", "up", "backup", "poisonvenom", ":)", "main"
    ));

    public OnlinePearls() {
        super(PoisonTools.Exclusives, "OnlinePearls", "Determines if an enderpearl's thrower is online by using nearby sign text.");
    }

    @Override
    public void onActivate() {
        playerList.clear();
        potentialNames.clear();
        if (!continous.get()) {
            mainAlgorithm();
            this.toggle();
        }
    }

    @Override
    public void onDeactivate() {
        playerList.clear();
        potentialNames.clear();
    }

    @EventHandler
    private void onPreTick(TickEvent.Pre event) {
        if (continous.get()) mainAlgorithm();
    }

    private void mainAlgorithm() {
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
        try {
            List<String> playerNames = mc.getNetworkHandler().getPlayerList().stream()
                    .map(player -> player.getProfile().getName())
                    .collect(Collectors.toList());
            for (Text name : potentialNames) {
                if (playerNames.contains(name.getLiteralString()) && !playerList.contains(name)) {
                    playerList.add(name);
                    ChatUtils.sendMsg(Text.of(name.getLiteralString() + " is online."));
                }
            }

        } catch (NullPointerException e) {
            ChatUtils.sendMsg(Text.of("null"));
        }
    }

    private void processSignText(SignText txt) {
        for (int i = 0; i < 4; i++) {
            List<Text> words = processSignTextHelper(txt.getMessages(false)[i]);
            potentialNames.addAll(words);
            for (Text word : words) {
                //ChatUtils.sendMsg(word);
            }
        }
    }

    private List<Text> processSignTextHelper(Text txt) {
        List<Text> uniqueWords = new ArrayList<>();

        String[] words = txt.getString().split("\\s+");
        for (String word : words) {
            //check for 's
            if (word.contains("'s")) {
                word = word.replace("'s", "");
            }
            String cleaned = word.replaceAll("[^a-zA-Z0-9_]", "");
            if (cleaned.length() > 2 && !COMMON_WORDS.contains(cleaned) && !potentialNames.contains(Text.of(cleaned))) {
                uniqueWords.add(Text.literal(cleaned));
            }
        }
        return uniqueWords;
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> continous = sgGeneral.add(new BoolSetting.Builder()
            .name("Run Constantly")
            .description("Continuously runs the module while toggled on (not recommended for performance reasons).")
            .defaultValue(false)
            .build());
    private final Setting<Boolean> individule = sgGeneral.add(new BoolSetting.Builder()
            .name("Individule Signs")
            .description("Check a single sign.")
            .defaultValue(false)
            .build());
}
