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
    //WIP
    public B52() {
        super(PoisonTools.Exclusives, "B-52", "Requires air place. Start bombing while flying.");
    }
    private int tick;
    @Override
    public void onActivate() {
        tick = 0;
        mc.options.useKey.setPressed(true);
        info("Bombs away!");
    }

    @Override
    public void onDeactivate() {
        tick = 0;
        mc.options.useKey.setPressed(false);
    }

    @EventHandler
    private void onPreTick(TickEvent.Pre event) {
        mainAlgorithm();
    }

    private void mainAlgorithm() {
        if (mc.world == null || mc.player == null || mc.crosshairTarget == null) return;
        //ChatUtils.sendMsg(Text.of("" + tick));
        Inventory inventory = mc.player.getInventory();
        int tntSlot = -1, flintSlot = -1;
        for (int i = 0; i < 9; i++) {
            if (inventory.getStack(i).getItem().equals(Items.FLINT_AND_STEEL)) {
                flintSlot = i;
            } else if (inventory.getStack(i).getItem().equals(Items.TNT)) {
                tntSlot = i;
            }
        }

        if (mc.crosshairTarget.getType() == HitResult.Type.BLOCK) {
            BlockHitResult bhr = (BlockHitResult) mc.crosshairTarget;
            BlockPos pos = bhr.getBlockPos();

            // Get the block at that position
            if (mc.world.getBlockState(pos).getBlock() == Blocks.TNT) {
                mc.player.getInventory().setSelectedSlot(flintSlot);
            } else {
                mc.player.getInventory().setSelectedSlot(tntSlot);
            }
        } else {
            mc.player.getInventory().setSelectedSlot(tntSlot);
        }
        //ChatUtils.sendMsg(Text.of(mc.crosshairTarget.getPos().toString()));
//        if (mc.world.getBlockState(BlockPos.ofFloored(mc.crosshairTarget.getPos())).getBlock() == Blocks.TNT) {
//            mc.player.getInventory().setSelectedSlot(flintSlot);
//        } else {
//            mc.player.getInventory().setSelectedSlot(tntSlot);
//        }
//        if (tick == 0) {
//            mc.player.getInventory().setSelectedSlot(tntSlot);
//            mc.options.useKey.setPressed(true);
//            ChatUtils.sendMsg(Text.of("tnt click"));
//            tick++;
//        } else if (tick == 2) {
//            if () {
//                mc.options.useKey.setPressed(false);
//            } else {
//                mc.player.getInventory().setSelectedSlot(flintSlot);
//            }
//            tick++;
//        } else if (tick == 4) {
//
//            tick++;
//        } else if (tick == 6) {
//            mc.options.useKey.setPressed(false);
//            tick = -1;
//        }
//        tick++;
    }
}
