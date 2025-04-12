package net.poisonvenom.poisontools.modules;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Items;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.poisonvenom.poisontools.PoisonTools;

public class AutoIgnite extends Module {
    private int currentSlot;
    private int cdCounter;
    private boolean cdHelper;
    private boolean switched;

    public AutoIgnite() {
        super(PoisonTools.Exclusives, "Auto Ignite", "Automatically ignites flammable blocks when moused over.");
    }

    @Override
    public void onActivate() {
        cdHelper = true;
        switched = false;
        cdCounter = 0;
    }

    @Override
    public void onDeactivate() {
        cdHelper = true;
        switched = false;
        cdCounter = 0;
    }

    @EventHandler
    private void onPreTick(TickEvent.Pre event) {
        mainAlgorithm();
        cooldownHelper();
    }

    private void mainAlgorithm() {
        if (mc.world == null || mc.player == null || mc.crosshairTarget == null) return;
        Inventory inventory = mc.player.getInventory();
        int flintSlot = -1;
        for (int i = 0; i < 9; i++) {
            if (inventory.getStack(i).getItem().equals(Items.FLINT_AND_STEEL)) {
                flintSlot = i;
            }
        }

        if (flintSlot == -1) {
            return;
        }
        if (switched) {
            mc.player.getInventory().setSelectedSlot(currentSlot);
            switched = false;
        }

        if (mc.crosshairTarget.getType() == HitResult.Type.BLOCK) {
            BlockHitResult bhr = (BlockHitResult) mc.crosshairTarget;
            BlockPos pos = bhr.getBlockPos();
            if (mc.world.getBlockState(pos).isBurnable() && cdHelper) {
                if (tntOnly.get() && mc.world.getBlockState(pos).getBlock().equals(Blocks.TNT)) {
                    currentSlot = mc.player.getInventory().selectedSlot;
                    mc.player.getInventory().setSelectedSlot(flintSlot);
                    mc.options.useKey.setPressed(true);
                    cdHelper = false;
                    switched = true;
                } else if (!tntOnly.get()) {
                    currentSlot = mc.player.getInventory().selectedSlot;
                    mc.player.getInventory().setSelectedSlot(flintSlot);
                    mc.options.useKey.setPressed(true);
                    cdHelper = false;
                    switched = true;
                }
            } else {
                mc.options.useKey.setPressed(false);
            }
        } else {
            mc.options.useKey.setPressed(false);
        }
    }

    private void cooldownHelper() {
        if (!cdHelper) {
            cdCounter++;
        }
        if (cdCounter == cooldown.get()) {
            cdHelper = true;
            cdCounter = 0;
        }
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    public final Setting<Integer> cooldown = sgGeneral.add(new IntSetting.Builder()
            .name("Cooldown")
            .description("Set the number of ticks in between clicks. Helpful for saving durability or avoiding server rate limits.")
            .defaultValue(1)
            .min(1)
            .sliderRange(1, 40)
            .build()
    );
    public final Setting<Boolean> tntOnly = sgGeneral.add(new BoolSetting.Builder()
            .name("TNT Only")
            .description("Only light TNT blocks.")
            .defaultValue(false)
            .build()
    );
}