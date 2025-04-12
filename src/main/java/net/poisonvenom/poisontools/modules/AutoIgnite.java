package net.poisonvenom.poisontools.modules;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Items;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.poisonvenom.poisontools.PoisonTools;

public class AutoIgnite extends Module {
    public AutoIgnite() {
        super(PoisonTools.Exclusives, "AutoIgnite", "Automatically ignites flammable blocks when moused over.");
    }

    @Override
    public void onActivate() {

    }

    @Override
    public void onDeactivate() {

    }

    @EventHandler
    private void onPreTick(TickEvent.Pre event) {
        mainAlgorithm();
    }

    private void mainAlgorithm() {
        if (mc.world == null || mc.player == null || mc.crosshairTarget == null) return;
        Inventory inventory = mc.player.getInventory();
        int currentSlot = mc.player.getInventory().selectedSlot;
        int flintSlot = -1;
        for (int i = 0; i < 9; i++) {
            if (inventory.getStack(i).getItem().equals(Items.FLINT_AND_STEEL)) {
                flintSlot = i;
            }
        }

        if (flintSlot == -1) {
            return;
        }

        if (mc.crosshairTarget.getType() == HitResult.Type.BLOCK) {
            BlockHitResult bhr = (BlockHitResult) mc.crosshairTarget;
            BlockPos pos = bhr.getBlockPos();

            if (mc.world.getBlockState(pos).isBurnable()) {
                mc.player.getInventory().setSelectedSlot(flintSlot);
                mc.options.useKey.setPressed(true);
            } else {
                mc.player.getInventory().setSelectedSlot(currentSlot);
                mc.options.useKey.setPressed(false);
            }
        } else {
            mc.player.getInventory().setSelectedSlot(currentSlot);
            mc.options.useKey.setPressed(false);
        }
    }
}
