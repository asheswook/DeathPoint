package ee.jwl.deathpoint.event;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import ee.jwl.deathpoint.config.ConfigManager;
import ee.jwl.deathpoint.DeathPoint;

public class OnDeath implements Listener {

    private final FileConfiguration deathsConfig;

    public OnDeath(ConfigManager configManager) {
        this.deathsConfig = configManager.getConfig("deaths");
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        byte maxSaves = 5;
        Player player = event.getEntity();
        String playerUuid = player.getUniqueId().toString();
        Location playerPos = player.getLocation();

        String formattedDeathPoint = "%s에서, %d, %d, %d".formatted(
                playerPos.getWorld().getName(), playerPos.getBlockX(), playerPos.getBlockY(), playerPos.getBlockZ()
        );

        boolean isDeathPointSaved = false;
        // 순회하며 null인 곳에 저장
        for (int i = 0; i < maxSaves; i++) {
            String key = "deaths.%s.%d".formatted(playerUuid, i);
            if (deathsConfig.get(key) == null) {
                deathsConfig.set(key, formattedDeathPoint);
                isDeathPointSaved = true;
            }
        }

        // 저장할 곳이 없으면
        if (!isDeathPointSaved) {
            // 최신 순으로 당기기
            for (int i = maxSaves-1; 0 <= i; i--) {
                String key = "deaths.%s.%d".formatted(playerUuid, i);
                String prevKey = "deaths.%s.%d".formatted(playerUuid, i - 1);
                deathsConfig.set(key, deathsConfig.get(prevKey));
            }

            // 빈 0번에 저장
            String key = "deaths.%s.%d".formatted(playerUuid, 0);
            deathsConfig.set(key, formattedDeathPoint);
        }
    }
}
