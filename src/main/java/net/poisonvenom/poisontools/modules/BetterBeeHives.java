package net.poisonvenom.poisontools.modules;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.poisonvenom.poisontools.PoisonTools;

public class BetterBeeHives extends Module {
    public BetterBeeHives() {
        super(PoisonTools.Exclusives, "BetterBeehives", "Highlights bee hives and nests with 3 bees in them.");
    }

    @Override
    public void onActivate() {

    }
    @Override
    public void onDeactivate() {

    }

    @EventHandler
    private void onPreTick(TickEvent.Pre event) {
        //WIP
    }
}
