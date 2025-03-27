package net.poisonvenom.poisontools.modules;

import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.meteorclient.utils.render.color.Color;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.chunk.WorldChunk;
import net.poisonvenom.poisontools.PoisonTools;

import java.util.ArrayList;
import java.util.List;

public class BetterBeeHives extends Module {
    private final List<BeehiveBlockEntity> hives = new ArrayList<>();
    public BetterBeeHives() {
        super(PoisonTools.Exclusives, "BetterBeehives", "Highlights bee hives and nests with 3 bees in them.");
    }

    @Override
    public void onActivate() {
        hives.clear();
    }
    @Override
    public void onDeactivate() {
        hives.clear();
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
                    if (blockEntity instanceof BeehiveBlockEntity hive) {
                        if (!hives.contains(hive) && beeHiveCount.get().equals(hive.getBeeCount())) {
                            hives.add(hive);
                            ChatUtils.sendMsg(Text.literal("Bees: " + hives.size()));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    private void onRender(Render3DEvent event) {
        for (BeehiveBlockEntity hive : hives) {
            render(new Box(new Vec3d(hive.getPos().getX() + 1, hive.getPos().getY() + 1, hive.getPos().getZ() + 1),
                    new Vec3d(hive.getPos().getX(), hive.getPos().getY(), hive.getPos().getZ())), sideColor.get(), lineColor.get(), shapeMode.get(), event);
        }
    }

    private void render(Box box, Color sides, Color lines, ShapeMode shapemode, Render3DEvent event) {
        event.renderer.box(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, sides, lines, shapemode, 0);
    }

    private void updateBeeHives(Integer count) {
        hives.removeIf(hive -> !count.equals(hive.getBeeCount()));
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgRender = settings.createGroup("Render Settings");

    private final Setting<ShapeMode> shapeMode = sgRender.add(new EnumSetting.Builder<ShapeMode>()
            .name("shape-mode")
            .description("How the shapes are rendered.")
            .defaultValue(ShapeMode.Both)
            .build()
    );
    private final Setting<SettingColor> sideColor = sgRender.add(new ColorSetting.Builder()
            .name("Side Color")
            .description("Color of the block side.")
            .defaultValue(new SettingColor(251, 5, 5, 55))
            .visible(() -> (shapeMode.get() == ShapeMode.Sides || shapeMode.get() == ShapeMode.Both))
            .build()
    );
    private final Setting<SettingColor> lineColor = sgRender.add(new ColorSetting.Builder()
            .name("Line Color")
            .description("Color of the block lines.")
            .defaultValue(new SettingColor(251, 5, 5, 200))
            .visible(() -> (shapeMode.get() == ShapeMode.Lines || shapeMode.get() == ShapeMode.Both))
            .build()
    );
    public final Setting<Integer> beeHiveCount = sgGeneral.add(new IntSetting.Builder()
            .name("Bee Count")
            .description("The number of bees you are looking for in a hive.")
            .defaultValue(3)
            .min(0)
            .sliderRange(0, 3)
            .onChanged(this::updateBeeHives)
            .build()
    );
}
