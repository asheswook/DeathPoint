package ee.jwl.deathpoint.util;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Optional;

public final class deathUtil {

    private final FileConfiguration deathConfig;
    private final FileConfiguration config;

    public deathUtil() {
        
    }

    private String getFormattedKey(String playerUuid, int i) {
        return "deaths.%s.%d".formatted(playerUuid, i);
    }

    private String getFormattedValue(Location loc) {
        return "%s에서, %d, %d, %d".formatted(
                loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()
        );
    }

    public boolean saveDeathPoint(Player player) {
        String playerUuid = player.getUniqueId().toString();
        Location playerPos = player.getLocation();
        String formattedValue = getFormattedValue(playerPos);

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
