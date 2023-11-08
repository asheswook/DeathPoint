package ee.jwl.deathpoint;

import ee.jwl.deathpoint.event.OnDeath;
import org.bukkit.plugin.java.JavaPlugin;
import ee.jwl.deathpoint.config.ConfigManager;

public class DeathPoint extends JavaPlugin {
    private final ConfigManager configManager = new ConfigManager();

    @Override
    public void onEnable() {
        getLogger().info("DeathPoint plugin enabled!");

        ConfigManager configManager = getConfigManager();
        configManager.loadConfigs();
    }

    @Override
    public void onDisable() {
        getLogger().info("DeathPoint plugin disabled!");
    }

    public ConfigManager getConfigManager() {
        return this.configManager;
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new OnDeath(this.getConfigManager()), this);
    }
}
