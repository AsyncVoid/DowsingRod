package me.async.DR;

import java.util.HashMap;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

public class InteractListener implements Listener{
	Main plugin;
	public InteractListener(Main instance){
		plugin = instance;
	}
	@EventHandler
	void interactEvent(PlayerInteractEvent e){
		final Player p = e.getPlayer();
		ItemStack is = p.getItemInHand();
		if(is.getType() == Material.NETHER_STAR){
			if(e.getAction() == Action.RIGHT_CLICK_AIR ||e.getAction() == Action.RIGHT_CLICK_BLOCK){
				try{
					ItemMeta im = is.getItemMeta();
					final LivingEntity le = p;
					if(!le.hasMetadata("cantfindchest")){
						if(im.getDisplayName().contains(Variables.itemName)){
							int radius = plugin.m.getRadi(is);
							if(plugin.m.getUses(is) == 1){
								p.setItemInHand(new ItemStack(Material.AIR));
								p.playSound(p.getLocation(), Sound.ANVIL_BREAK, 5, 0);
							}else{
								p.getItemInHand().setItemMeta(plugin.m.setUsed(is));
								p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 5, 0);
							}
							HashMap<String, Integer> hash = plugin.m.find(p.getLocation(), radius, p);
							plugin.m.readHashMap(hash, p);
							le.setMetadata("cantfindchest", new FixedMetadataValue(plugin, false));
							plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
								 
				                @Override
				                public void run() {
				                	le.removeMetadata("cantfindchest", plugin);
				                }
							}, 20*2);
						}
					}else{
					}
					
				}catch(Exception ex){
					
				}
			}
			
			
		}
	}

}
