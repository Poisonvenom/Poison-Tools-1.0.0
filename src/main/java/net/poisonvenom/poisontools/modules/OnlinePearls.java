package net.poisonvenom.poisontools.modules;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.block.entity.SignText;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.chunk.WorldChunk;
import net.poisonvenom.poisontools.PoisonTools;

import java.util.*;

public class OnlinePearls extends Module {
    private final List<String> playerList = new ArrayList<>();
    private final List<String> potentialNames = new ArrayList<>();
    private static final Set<String> COMMON_WORDS = new HashSet<>(Arrays.asList(
            "the", "is", "one", "two", "three", "in", "a", "and", "on", "of", "to", "for", "by", "with", "an",
            "that", "it", "at", "as", "this", "my", "me", "you", "your", "are", "was", "pearl", "pearls", "back", "up", "backup", "poisonvenom", ":)", "main"
    ));

    public OnlinePearls() {
        super(PoisonTools.Exclusives, "OnlinePearls", "Determines if an enderpearl's thrower is online by using nearby sign text.");
    }

    @Override
    public void onActivate() {
        playerList.clear();
        potentialNames.clear();
        if (!continuous.get()) {
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
        if (continuous.get()) mainAlgorithm();
    }

    private void singleSign() {
        if (mc.world == null || mc.player == null) return;


    }

    private void mainAlgorithm() {
        if (mc.world == null || mc.player == null) return;

        if (individual.get()) { // single sign scan
            Vec3d pos = null;
            try {
                pos = mc.crosshairTarget.getPos();
            } catch (NullPointerException e) {
                ChatUtils.sendMsg(Text.of("Cursor not pointing at anything."));
                return;
            }
            BlockEntity sign = mc.world.getBlockEntity(new BlockPos((int) pos.getX(), (int) pos.getY(), (int) pos.getZ()));
            if (sign instanceof SignBlockEntity) {
                SignText frontTxt = ((SignBlockEntity) sign).getText(true);
                SignText backTxt = ((SignBlockEntity) sign).getText(true);
                processSignText(frontTxt);
                processSignText(backTxt);
            }
        } else { // scan for all signs in loaded chunks
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
        }
        // logic for finding and displaying online players
        try {
            List<String> playerNames = mc.getNetworkHandler().getPlayerList().stream().map(player -> player.getProfile().getName()).toList();
            for (String name : potentialNames) {
                if (containsIgnoreCase(playerNames, name) && !containsIgnoreCase(playerList, name)) {
                    playerList.add(name);
                    ChatUtils.sendMsg(Text.of(name + " is online."));
                }
            }
        } catch (NullPointerException e) {
            ChatUtils.sendMsg(Text.of("Not connected to a server!"));
        }
    }

    private void processSignText(SignText txt) {
        for (int i = 0; i < 4; i++) {
            List<String> words = processSignTextHelper(txt.getMessages(false)[i]);
            potentialNames.addAll(words);
        }
    }

    private List<String> processSignTextHelper(Text txt) {
        List<String> uniqueWords = new ArrayList<>();

        String[] words = txt.getString().split("\\s+");
        for (String word : words) {
            //check for 's
            if (word.contains("'s")) {
                word = word.replace("'s", "");
            }
            String cleaned = word.replaceAll("[^a-zA-Z0-9_]", "");
            if (cleaned.length() > 2 && !containsIgnoreCase(COMMON_WORDS.stream().toList(), cleaned) && !potentialNames.contains(cleaned)) {
                uniqueWords.add(cleaned);
            }
        }
        return uniqueWords;
    }

    private boolean containsIgnoreCase(List<String> list, String str) {
        for (String str2 : list) {
            if (str2.equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> continuous = sgGeneral.add(new BoolSetting.Builder()
            .name("Run Constantly")
            .description("Continuously runs the module while toggled on (not recommended for performance reasons).")
            .defaultValue(false)
            .build());
    private final Setting<Boolean> individual = sgGeneral.add(new BoolSetting.Builder()
            .name("Individual Signs")
            .description("Check a single sign.")
            .defaultValue(false)
            .build());
}
