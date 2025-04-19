package net.poisonvenom.poisontools.modules;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Items;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.poisonvenom.poisontools.PoisonTools;

public class B52 extends Module {
    private boolean switched;
    private int tntSlot;
    private int flintSlot;
    public B52() {
        super(PoisonTools.Exclusives, "B-52", "Requires air place. Start bombing while flying.");
    }

    @Override
    public void onActivate() {
        mc.options.useKey.setPressed(true);
        switched = false;
        tntSlot = -1;
        flintSlot = -1;
        info("Bombs away!");
    }

    @Override
    public void onDeactivate() {
        mc.options.useKey.setPressed(false);
        tntSlot = -1;
        flintSlot = -1;
        switched = false;
    }

    @EventHandler
    private void onPreTick(TickEvent.Pre event) {
        mainAlgorithm();
    }

    @EventHandler
    private void onPostTick(TickEvent.Post event) {
        if (switched && mc.player != null) {
            mc.player.getInventory().setSelectedSlot(tntSlot);
        }
    }
    private void mainAlgorithm() {
        if (mc.world == null || mc.player == null || mc.crosshairTarget == null) return;
        Inventory inventory = mc.player.getInventory();
        for (int i = 0; i < 9; i++) {
            if (inventory.getStack(i).getItem().equals(Items.FLINT_AND_STEEL)) {
                flintSlot = i;
            } else if (inventory.getStack(i).getItem().equals(Items.TNT)) {
                tntSlot = i;
            }
        }

        if (tntSlot == -1 || flintSlot == -1) {
            return;
        }

        if (mc.crosshairTarget.getType() == HitResult.Type.BLOCK) {
            BlockHitResult bhr = (BlockHitResult) mc.crosshairTarget;
            BlockPos pos = bhr.getBlockPos();

            if (mc.world.getBlockState(pos).getBlock() == Blocks.TNT) {
                mc.player.getInventory().setSelectedSlot(flintSlot);
                switched = true;
            } else {
                mc.player.getInventory().setSelectedSlot(tntSlot);
            }
        } else {
            mc.player.getInventory().setSelectedSlot(tntSlot);
        }
    }
}
