package ee.jwl.deathpoint.util;

import org.bukkit.block.Container;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Barrel;
import org.bukkit.inventory.Inventory;

import javax.annotation.Nullable;

public final class blockUtil {

    @Nullable
    public Inventory createBarrelAtLocation(Location loc) {
        try {
            Block block = loc.getBlock();
            block.setType(Material.BARREL);
            BlockState blockState = block.getState();
            if (blockState instanceof Barrel) {
                return ((Barrel) blockState).getInventory();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
