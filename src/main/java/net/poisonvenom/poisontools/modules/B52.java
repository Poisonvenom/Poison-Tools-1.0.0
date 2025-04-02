package net.poisonvenom.poisontools.modules;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
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
    }

    @Override
    public void onDeactivate() {
        tick = 0;
    }

    @EventHandler
    private void onPreTick(TickEvent.Pre event) {
        mainAlgorithm();
    }

    private void mainAlgorithm() {
        if (mc.world == null || mc.player == null || mc.crosshairTarget == null) return;
        ChatUtils.sendMsg(Text.of("" + tick));
        Inventory inventory = mc.player.getInventory();
        int tntSlot = -1, flintSlot = -1;
        for (int i = 0; i < 9; i++) {
            if (inventory.getStack(i).getItem().equals(Items.FLINT_AND_STEEL)) {
                flintSlot = i;
            } else if (inventory.getStack(i).getItem().equals(Items.TNT)) {
                tntSlot = i;
            }
        }

        if (tick == 0) {
            mc.player.getInventory().setSelectedSlot(tntSlot);
            mc.options.useKey.setPressed(true);
            ChatUtils.sendMsg(Text.of("tnt click"));
            tick++;
        } else if (tick == 2) {
            mc.options.useKey.setPressed(false);
            ChatUtils.sendMsg(Text.of("tnt unclick"));
            tick++;
        } else if (tick == 4) {
            if (mc.world.getBlockState(BlockPos.ofFloored(mc.crosshairTarget.getPos())).getBlock() == Blocks.TNT) {
                mc.player.getInventory().setSelectedSlot(flintSlot);
                mc.options.useKey.setPressed(true);
            }
            tick++;
        } else if (tick == 6) {
            mc.options.useKey.setPressed(false);
            tick = -1;
        }
        tick++;
    }
}
