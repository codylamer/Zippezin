package com.codylamer.zippezin;

import com.denizenscript.denizen.Denizen;
import com.denizenscript.denizencore.DenizenCore;

import com.codylamer.zippezin.commands.*;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Zippezin extends JavaPlugin {

    public static Zippezin instance;


    @Override
    public void onEnable() {
        instance = this;

        getLogger().info("║ ■=■=■=■=■=■=■=■=■=■=■=■=■=■=■=■=■=■=■=■");
        getLogger().info("║ Zippezin ");

        if (isPluginAvailable("Denizen")) {
            getLogger().severe("║ Denizen is not installed! ║");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        registerDenizen();

        getLogger().info("╔══════════════════════════════════════════════════════╗");
        getLogger().info("║             ✨ Created by CodYLameR ✨              ║");
        getLogger().info("║       🌐 https://github.com/codylamer/Zippezin       ║");
        getLogger().info("║           Thank you for using this plugin!           ║");
        getLogger().info("╚══════════════════════════════════════════════════════╝");
        getLogger().info("║ ■=■=■=■=■=■=■=■=■=■=■=■=■=■=■=■=■=■=■=■");
    }

    @Override
    public void onDisable() {
        Denizen.getInstance().onDisable();
    }

    private void registerDenizen() {
        getLogger().info("║ Successfully started Denizen hook ║");

        getLogger().info("║ Initializing Denizen Commands ║");
        DenizenCore.commandRegistry.registerCommand(ExtractCommand.class);
        DenizenCore.commandRegistry.registerCommand(CompressCommand.class);
    }

    private boolean isPluginAvailable(String pluginName) {
        return !Bukkit.getPluginManager().isPluginEnabled(pluginName);
    }
}
