package xyz.wisecraft.plotclaimer;

import com.plotsquared.bukkit.BukkitPlatform;
import com.plotsquared.core.PlotSquared;
import com.plotsquared.core.events.PlayerClaimPlotEvent;
import com.plotsquared.core.player.PlotPlayer;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.plot.PlotArea;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class Plotclaimer extends JavaPlugin {

    Plotclaimer plugin = this;
    BukkitPlatform plotSquared;

    @Override
    public void onEnable() {
        // Plugin startup logic

        plotSquared = setupDependency("PlotSquared", BukkitPlatform.class);

        Bukkit.getPluginManager().registerEvents(new FListener(plotSquared), plugin);
    }
    public <T> T setupDependency(String pluginName, Class<T> clazz) {
        Plugin setupPlugin = plugin.getServer().getPluginManager().getPlugin(pluginName);
        if (setupPlugin == null) {
            plugin.getLogger().warning("Could not find plugin " + pluginName);
            return null;}
        if(!setupPlugin.isEnabled()) {
            plugin.getLogger().warning("Plugin " + pluginName + " is not enabled");
            return null;}

        return clazz.cast(setupPlugin);
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}

class FListener implements Listener {

    BukkitPlatform plotSquared1;

    public FListener(BukkitPlatform plotSquared1) {
        this.plotSquared1 = plotSquared1;
    }

    @EventHandler
    public void onF(PlayerSwapHandItemsEvent e) {
        PlotPlayer<Player> pPlayer = PlotPlayer.from(e.getPlayer());



        PlotArea plotArea = plotSquared1.plotAreaManager().getPlotArea(pPlayer.getLocation());
        if (plotArea == null) return;

        Plot plot = plotArea.getPlot(pPlayer.getLocation());

        if (!pPlayer.getPlots().isEmpty()) {
            if (plot == null || plot.getOwner() == null)
                e.getPlayer().sendMessage("You already have a plot");
            else if (!plot.getOwner().equals(pPlayer.getUUID())) {
                e.getPlayer().sendMessage("You can't claim someone else's plot");
            }

            return;
        }

        if (plot != null && plot.canClaim(pPlayer)) {
            plot.claim(pPlayer, true, null, true, false);

        }


    }

}