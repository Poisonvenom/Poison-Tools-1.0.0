package net.poisonvenom.poisontools;

import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.commands.Commands;
import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.poisonvenom.poisontools.commands.*;
import net.poisonvenom.poisontools.hud.ElytraCount;
import net.poisonvenom.poisontools.modules.*;

public class PoisonTools extends MeteorAddon {
        public static final Category Main = new Category("Poison Tools");
        @Override
        public void onInitialize() {
                Modules.get().add(new BaseFinder());
                Modules.get().add(new ActivatedSpawnerDetector());
                Modules.get().add(new PortalPatternFinder());
                Modules.get().add(new CaveDisturbanceDetector());
                Modules.get().add(new AdvancedItemESP());
                Modules.get().add(new MobGearESP());
                Modules.get().add(new PotESP());
                Modules.get().add(new HoleAndTunnelAndStairsESP());
                Modules.get().add(new AttributeSwap());
                Modules.get().add(new BetterAutoSign());
                Modules.get().add(new MultiUse());
                Modules.get().add(new AutoDrop());
                Commands.add(new WorldInfoCommand());
                Commands.add(new ViewNbtCommand());
                Commands.add(new GarbageCleanerCommand());
                Hud.get().register(ElytraCount.INFO);
        }

        @Override
        public void onRegisterCategories() {
                Modules.registerCategory(Main);
        }

        public String getPackage() {
                return "net.poisonvenom.poisontools";
        }

}