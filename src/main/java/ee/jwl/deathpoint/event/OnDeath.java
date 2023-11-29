package ee.jwl.deathpoint.event;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import ee.jwl.deathpoint.config.ConfigManager;
import ee.jwl.deathpoint.DeathPoint;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.Optional;
import java.util.OptionalInt;

public class OnDeath implements Listener {

    private final FileConfiguration deathsConfig;
    private final FileConfiguration config;

    public OnDeath(ConfigManager configManager) {
        this.deathsConfig = configManager.getConfig("deaths");
        this.config = configManager.getConfig("config");
    }

    private String getFormattedKey(String playerUuid, int i){
        return "deaths.%s.%d".formatted(playerUuid, i);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Optional<String[]> targetWorlds = Optional.ofNullable((String[]) this.config.get("settings.targetWorlds"));
        OptionalInt configMaxSaves = OptionalInt.of((int) this.config.get("settings.maxSaves"));
        int maxSaves = configMaxSaves.orElse(5);

        Player player = event.getEntity();
        String playerUuid = player.getUniqueId().toString();
        Location playerPos = player.getLocation();

        String formattedDeathPoint = "%s에서, %d, %d, %d".formatted(
                playerPos.getWorld().getName(), playerPos.getBlockX(), playerPos.getBlockY(), playerPos.getBlockZ()
        );

        boolean isDeathPointSaved = false;
        // 순회하며 null인 곳에 저장
        for (int i = 0; i < maxSaves; i++) {
            String key = getFormattedKey(playerUuid, i);
            if (this.deathsConfig.get(key) == null) {
                this.deathsConfig.set(key, formattedDeathPoint);
                isDeathPointSaved = true;
                break;
            }
        }

        // 저장할 곳이 없으면
        if (!isDeathPointSaved) {
            // 최신 순으로 당기기, i == 0일때는 안함
            for (int i = maxSaves - 1; 1 <= i; i--) {
                String key = getFormattedKey(playerUuid, i);
                String prevKey = getFormattedKey(playerUuid, i - 1);
                Optional<String> prevDeathPoint = Optional.ofNullable(deathsConfig.get(prevKey).toString());
                this.deathsConfig.set(key, prevDeathPoint.orElseThrow());
            }

            // 빈 0번에 저장
            String key = getFormattedKey(playerUuid, 0);
            this.deathsConfig.set(key, formattedDeathPoint);
        }
    }
}
