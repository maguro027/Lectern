package lectern_system.plugin.event;

import java.util.HashSet;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.block.data.Directional;

import com.google.common.collect.Sets;

import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Event implements Listener {

	private static final HashSet<Material> BOOK_STORAGE_TYPES = Sets.newHashSet(Material.CHEST, Material.TRAPPED_CHEST,
			Material.BARREL);

	public Event(Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void SignChangeEvent(SignChangeEvent e) {
		if (e.getLine(0).equals("[Lectern]") || e.getLine(0).equals("[lectern]") || e.getLine(0).equals("[lec]")
				|| e.getLine(0).equals("[Lec]")) {
			e.setLine(0, "[Lectern]");
		}
	}

	private static boolean isNotBookStorageType(Material type) {
		return !BOOK_STORAGE_TYPES.contains(type);
	}

	@EventHandler(ignoreCancelled = true)
	public void onEnSignClick(PlayerInteractEvent e) {

		if (e.getPlayer().isSneaking()) return;
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;


		Block block = e.getClickedBlock();
		Material material = block.getType();

		if (material == Material.ENCHANTING_TABLE) {

			for (BlockFace face : BlockFace.values()) {
				Block relative = block.getRelative(face);
				if (!relative.getType().toString().endsWith("_WALL_SIGN")) continue;

				if (face != ((Directional) relative.getBlockData()).getFacing()) continue;

				Sign signboard = (Sign) relative.getState();
				if (signboard.getLine(0).equals("[Lectern]") || signboard.getLine(0).equals("��1��l[Lectern]")) {
					e.setCancelled(true);
					e.getPlayer().closeInventory();

					List<Entity> nearbyEntites = (List<Entity>) block.getLocation().getWorld()
							.getNearbyEntities(block.getLocation(), 2, 2, 2);

					for (Entity value : nearbyEntites) {

						if (value.getType().toString().endsWith("ITEM_FRAME")) {

							final ItemFrame frame = (ItemFrame) value;
							final ItemStack item = frame.getItem();

							if (item.getType() == Material.WRITTEN_BOOK) {
								e.getPlayer().openBook(item);
								return;
							}
						}
					}
				}
			}

			Block down = block.getRelative(BlockFace.DOWN);
			if (isNotBookStorageType(down.getType())) return;

			for (BlockFace face : BlockFace.values()) {
				Block relative = block.getRelative(face);
				if (!relative.getType().toString().endsWith("_WALL_SIGN")) continue;

				if (face != ((Directional) relative.getBlockData()).getFacing()) continue;

				Sign signboard = (Sign) relative.getState();
				if (signboard.getLine(0).equals("[Lectern]") || signboard.getLine(0).equals("��1��l[Lectern]")) {
					e.setCancelled(true);

					e.getPlayer().closeInventory();
					Location loc = e.getClickedBlock().getLocation();
					loc.setY(loc.getY() - 1);
					lectern_system.plugin.main.Main.getinventory(loc, e.getPlayer());
					break;
				}
			}
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onSignClick(PlayerInteractEvent e) {
		if (!(e.getClickedBlock().getState() instanceof Sign)) return;
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		if (!e.hasBlock()) return;

		Sign signboard = (Sign) e.getClickedBlock().getState();

		if (signboard.getLine(0).equals("[Lectern]") || signboard.getLine(0).equals("��1��l[Lectern]")) {

			Location loc = e.getClickedBlock().getLocation();

			switch (e.getBlockFace().toString()) {
			case "NORTH":
				loc.setZ(loc.getZ() + 1);
				break;
			case "SOUTH":
				loc.setZ(loc.getZ() - 1);
				break;
			case "EAST":
				loc.setX(loc.getX() - 1);
				break;
			case "WEST":
				loc.setX(loc.getX() + 1);
				break;
			}

			if (loc.getBlock().getType() == Material.AIR) return;

			e.getPlayer().closeInventory();

			List<Entity> nearbyEntites = (List<Entity>) loc.getWorld().getNearbyEntities(loc, 2, 2, 2);
			for (Entity value : nearbyEntites) {
				if (value.getType().toString().endsWith("ITEM_FRAME")) {
					final ItemFrame frame = (ItemFrame) value;
					final ItemStack item = frame.getItem();
					if (item.getType() == Material.WRITTEN_BOOK) {
						e.getPlayer().openBook(item);
						return;
					}
				}
			}

			loc.setY(loc.getY() - 1);
			lectern_system.plugin.main.Main.getinventory(loc, e.getPlayer());
		}
	}

	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent e) {
		int size = 26;
		Player player = (Player) e.getWhoClicked();
		if (player.getOpenInventory().getTitle().equals("Lectern")
				|| player.getOpenInventory().getTitle().equals("Lectern 54")) {
			e.setCancelled(true);
			if (player.getOpenInventory().getTitle().equals("Lectern 54")) size = 54;
			if (e.getRawSlot() > size || e.getRawSlot() == -999 || e.getInventory().getItem(e.getRawSlot()) == null)
				return;
			player.openBook(e.getInventory().getItem(e.getRawSlot()));

		}
	}
}
