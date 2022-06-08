package lectern_system.plugin.main;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Barrel;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class Main {
	
	public static void getinventory(Location loc, Player player) {
		int conut = -1, invsize = 27;

		Material btype = loc.getBlock().getType();
		Inventory cash = null, inv = null;
		Barrel barrel = null;
		Chest chest = null;
		ItemStack item = null;

		inv = Bukkit.createInventory(null, invsize, "Lectern");

		if (btype == Material.CHEST || btype == Material.TRAPPED_CHEST || btype == Material.BARREL) {
			if (!(btype == Material.BARREL)) {

				chest = ((Chest) loc.getBlock().getState());
				if (chest.getInventory().getHolder() instanceof DoubleChest) {
					chest = (Chest) loc.getBlock().getState();
					InventoryHolder holder = chest.getInventory().getHolder();
					DoubleChest doubleChest = ((DoubleChest) holder);

					cash = doubleChest.getInventory();
					invsize = 54;

				} else {
					chest = (Chest) loc.getBlock().getState();
					cash = chest.getBlockInventory();
				}
			} else {
				barrel = (Barrel) loc.getBlock().getState();
				cash = barrel.getInventory();
			}
			HashMap<Integer, ItemStack> items = new HashMap<>();
			for (int i = 0, size = invsize; i < size; ++i) {

				item = cash.getItem(i);
				if (item == null) continue;
				if (!(item.getType() == Material.WRITTEN_BOOK)) continue;
				conut++;
				items.put(conut, item);

			}
			if (conut >= 27) {
				inv = Bukkit.createInventory(null, 54, "Lectern 54");
			}
			for (java.util.Map.Entry<Integer, ItemStack> item1 : items.entrySet()) {
				inv.setItem(item1.getKey(), item1.getValue());
			}

			if (conut == 0) {
				player.openBook(inv.getItem(0));
			} else if (conut == -1) {
				return;
			} else {

				player.openInventory(inv);
			}
		}
	}

}
