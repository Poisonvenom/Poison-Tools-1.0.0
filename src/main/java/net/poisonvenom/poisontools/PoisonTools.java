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
        public static final Category Other = new Category("PT - Borrowed Mods");
        public static final Category Exclusives = new Category("PT - Exclusives");
        @Override
        public void onInitialize() {
                //taken from other mods
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

                //Poison Tools Exclusives
                Modules.get().add(new B52());
                Modules.get().add(new BetterBeeHives());
                Modules.get().add(new OnlinePearls());
                Modules.get().add(new MaskSpawner());
        }

        @Override
        public void onRegisterCategories() {
                Modules.registerCategory(Other);
                Modules.registerCategory(Exclusives);
        }

        public String getPackage() {
                return "net.poisonvenom.poisontools";
        }

}