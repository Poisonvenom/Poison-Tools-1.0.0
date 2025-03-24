package net.poisonvenom.poisontools.modules;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.poisonvenom.poisontools.PoisonTools;

public class B52  extends Module {
    public B52() {
        super(PoisonTools.Exclusives, "B-52", "Requires air place. Start bombing while flying.");
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
        mc.crosshairTarget.getPos();
    }
}
