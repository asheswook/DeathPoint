package ee.jwl.deathpoint.config;

import ee.jwl.deathpoint.DeathPoint;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

public class ConfigManager {
    private final DeathPoint plugin = DeathPoint.getPlugin(DeathPoint.class);

    private HashMap<String, ConfigGenerator> configs = new HashMap<>();

    public ConfigManager() {
        final String pluginPath = plugin.getDataFolder().getAbsolutePath();
        this.configs.put("deaths", new ConfigGenerator(pluginPath, "deaths.yml"));
        this.configs.put("config", new ConfigGenerator(pluginPath, "config.yml"));
    }

    public void reloadConfigs() {
        for (String key : configs.keySet()) {
            plugin.getLogger().info("Reloading config: " + key);
            this.configs.get(key).reloadConfig();
        }
    }

    public void saveConfigs() {
        for (String key : configs.keySet()) {
            plugin.getLogger().info("Saving config: " + key);
            this.configs.get(key).saveConfig();
        }
    }

    public FileConfiguration getConfig(String filename) {
        return this.configs.get(filename).getConfig();
    }

    public void loadConfigs() {
        FileConfiguration deathsConfig = getConfig("deaths");
        FileConfiguration config = getConfig("config");
        deathsConfig.options().copyDefaults(true);
        config.options().copyDefaults(true);

        String[] targetWorlds = {"world", "world_nether", "world_the_end"};
        config.addDefault("settings.targetWorlds", targetWorlds);
        config.addDefault("settings.maxSaves", 5);
    }
}
