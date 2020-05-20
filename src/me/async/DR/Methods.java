package me.async.DR;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Methods {
	Main plugin;
	public Methods (Main instance){
		plugin = instance;
	}
	ItemStack chestfinder(int radius){
		ItemStack is = new ItemStack(Material.NETHER_STAR);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(Variables.itemName);
		List<String> ls = new ArrayList<String>();
		ls.add("§aRadius§7: "+radius+"x"+radius);
		ls.add("§aUsesLeft§7: 250");
		im.setLore(ls);
		is.setItemMeta(im);
		
		return is;
	}
	int getRadi(ItemStack is){
		try{
			ItemMeta im = is.getItemMeta();
			List<String> data = im.getLore();
			String[] lore = data.toArray(new String[data.size()]);
			String[] split = lore[0].split("x");
			return Integer.parseInt(split[1]);
		}catch(Exception ex){
			return 1;
		}
	}
	int getUses(ItemStack is){
		try{
			ItemMeta im = is.getItemMeta();
			List<String> data = im.getLore();
			String[] lore = data.toArray(new String[data.size()]);
			String[] split = lore[1].split(": ");
			return Integer.parseInt(split[1]);
		}catch(Exception ex){
			return 0;
		}
	}
	ItemMeta setUsed(ItemStack is){
		try{
			ItemMeta im = is.getItemMeta();
			List<String> data = im.getLore();
			String[] lore = data.toArray(new String[data.size()]);
			String[] split = lore[1].split(": ");
			lore[1] = "§aUsesLeft§7: " + (Integer.parseInt(split[1])-1);
			List<String> fin = Arrays.asList(lore);
			im.setLore(fin);
			return im;
		}catch(Exception ex){
			return null;
		}
	}
	void sendHelp(CommandSender sender){
		sender.sendMessage(ChatColor.GOLD+"--------"+ChatColor.RED+"ChestFinder"+ChatColor.GOLD+"--------");
		sender.sendMessage(ChatColor.AQUA+"/chestfinder give <Player> <Radius> [Amount]"+ChatColor.WHITE+" -Gives <Player> a chestfinder with radius <Radius>, with optional [Amount].");
		sender.sendMessage(ChatColor.AQUA+"/chestfinder has <Player>"+ChatColor.WHITE+" -Checks if <Player> has a chest finder in their inventory.");
		sender.sendMessage(ChatColor.AQUA+"/chestfinder find <Radius>"+ChatColor.WHITE+" -Drowses a radius of <Radius>");
	}
	boolean hasCF(Player p){
		Inventory inv = p.getInventory();
		for(ItemStack is : inv){
			if(is!=null){
				if(is.getType() == Material.NETHER_STAR){
					if(is.hasItemMeta()){
						if(is.getItemMeta().getDisplayName().contains(Variables.itemName)){
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	void readHashMap(HashMap<String, Integer> hash, Player p){
		int chests = hash.get("chests");
		int spawners = hash.get("spawners");
		if(chests + spawners == 0){
			p.sendMessage(ChatColor.AQUA+"Nothing found in this area ;(");
		}else{
			String message = "";
			if(chests>0){
				message +=ChatColor.YELLOW+""+chests+" "+ChatColor.AQUA+"Chests where found.";
			}
			if(spawners>0){
				message+=ChatColor.YELLOW+" "+spawners+" "+ChatColor.AQUA+"Spawners where found.";
			}
			p.sendMessage(message);
			p.playSound(p.getLocation(), Sound.GHAST_FIREBALL, 2, 0);
		}
	}
	HashMap<String, Integer> find(Location loc, Integer radius, Player p){
		HashMap<String, Integer> hashmap = new HashMap<String, Integer>();
		hashmap.put("chests", 0);
		hashmap.put("spawners", 0);
		List<Entity> ents = p.getNearbyEntities(radius, 255, radius);
		for(Entity ent : ents){
			if(ent instanceof StorageMinecart){
				hashmap.put("chests", hashmap.get("chests")+1);
			}
		}
		int x = loc.getBlockX();
		loc.setX(x);
		int z = loc.getBlockZ();
		loc.setZ(z);
		if(radius ==1){
			for(int y =0; y<256; y++){
				Location cur = loc;
				cur.setY(y);
				if(cur.getBlock().getType() == Material.CHEST){
					hashmap.put("chests", hashmap.get("chests")+1);
				}else if (cur.getBlock().getType() == Material.MOB_SPAWNER){
					hashmap.put("spawners", hashmap.get("spawners")+1);
				}else if(cur.getBlock().getType() == Material.TRAPPED_CHEST){
					hashmap.put("chests", hashmap.get("chests")+1);
				}else if(cur.getBlock().getType() == Material.DISPENSER){
					hashmap.put("chests", hashmap.get("chests")+1);
				}else if(cur.getBlock().getType() == Material.DROPPER){
					hashmap.put("chests", hashmap.get("chests")+1);
				}else if(cur.getBlock().getType() == Material.ENDER_CHEST){
					hashmap.put("chests", hashmap.get("chests")+1);
				}
			}
		}else{
			int rad;
			if(radius % 2 == 0){
				rad = radius/2;
			}else{
				rad = (int) Math.ceil((radius-1)/2);
			}
			
			    int maxX = x + rad;
				int maxZ = z + rad;
				int minX = x - rad;
				int minZ = z - rad;
				Location cur = loc;
				for (int curX = minX; curX<=maxX; curX++){
					cur.setX(curX);
					
					for(int curZ = minZ; curZ<= maxZ; curZ++){
						cur.setZ(curZ);
						for(int y = 0; y < 256; y++){
							cur.setY(y);
							if(cur.getBlock().getType() == Material.CHEST){
								hashmap.put("chests", hashmap.get("chests")+1);
							}else if (cur.getBlock().getType() == Material.MOB_SPAWNER){
								hashmap.put("spawners", hashmap.get("spawners")+1);
							}else if(cur.getBlock().getType() == Material.TRAPPED_CHEST){
								hashmap.put("chests", hashmap.get("chests")+1);
							}else if(cur.getBlock().getType() == Material.DISPENSER){
								hashmap.put("chests", hashmap.get("chests")+1);
							}else if(cur.getBlock().getType() == Material.DROPPER){
								hashmap.put("chests", hashmap.get("chests")+1);
							}else if(cur.getBlock().getType() == Material.ENDER_CHEST){
								hashmap.put("chests", hashmap.get("chests")+1);
							}
						}
					}
				}
			
		}
		return hashmap;
	}
	

}
