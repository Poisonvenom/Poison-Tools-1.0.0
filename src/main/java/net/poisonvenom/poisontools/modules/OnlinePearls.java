package net.poisonvenom.poisontools.modules;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.poisonvenom.poisontools.PoisonTools;

public class OnlinePearls extends Module {
    public OnlinePearls() {
        super(PoisonTools.Exclusives, "OnlinePearls", "Determines if an enderpearl's thrower is online by using the sign text.");
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
