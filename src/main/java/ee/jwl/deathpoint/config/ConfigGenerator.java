package ee.jwl.deathpoint.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigGenerator {
    private FileConfiguration config;
    private final File file;

    public ConfigGenerator(String path, String filename) {
        this.file = new File(path, filename);
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    public FileConfiguration getConfig() {
        return this.config;
    }

    public void saveConfig() {
        if (this.config == null) return;
        try {
            this.config.save(this.file);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    public boolean exists() {
        return this.file.exists();
    }

    public void reloadConfig() {
        if (!exists()) return;
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }
}