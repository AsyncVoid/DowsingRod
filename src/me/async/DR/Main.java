package me.async.DR;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{
	Methods m;
	public void onEnable(){
		getServer().getPluginManager().registerEvents(new InteractListener(this), this);
		m = new Methods(this);
	}
	public void onDisable(){
		
	}
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(commandLabel.equalsIgnoreCase("ChestFinder")){
			if(args.length == 0){
				m.sendHelp(sender);
			}else if(args.length == 1){
				sender.sendMessage(ChatColor.RED+"Strange Command. Do /chestfinder for help.");
			}else if(args.length == 2){
			    if(args[0].equalsIgnoreCase("has")){
			    	if(sender.hasPermission("chestfinder.has")||sender.hasPermission("chestfinder.admin")){
			    		try{
				    		Player target = getServer().getPlayer(args[1]);
				    		if(m.hasCF(target)){
				    			sender.sendMessage(ChatColor.GREEN+"Player "+target.getName()+" has a chest finder.");
				    		}
				    		else{
				    			sender.sendMessage(ChatColor.RED+"Player "+target.getName()+" has not got a chest finder.");
				    		}
				    		
				    	}catch(Exception ex){
				    		sender.sendMessage(ChatColor.RED+"Player "+args[1]+" not found.");
				    	}
				    }else{
				    	sender.sendMessage(ChatColor.RED+"You do not have permission to perform this command.");
				    }
			    	
			    }else if(args[0].equalsIgnoreCase("find")){
			    	if(sender.hasPermission("chestfinder.find")||sender.hasPermission("chestfinder.admin")){
			    		try{
			    			Player p = (Player) sender;
			    			int radius;
			    			try{
			    				radius = Integer.parseInt(args[1]);
			    			}catch(Exception ex){
			    				p.sendMessage(ChatColor.RED+args[1]+" is not a valid integer.");
			    				return false;
			    			}
			    			
			    			HashMap<String,Integer> hashmap = m.find(p.getLocation(), radius, p);
			    			m.readHashMap(hashmap, p);
			    			
			    		}catch(Exception ex){
			    			sender.sendMessage(ChatColor.RED+"You need to be a player to perform this command.");
			    			return false;
			    		}
			    	}else{
				    	sender.sendMessage(ChatColor.RED+"You do not have permission to perform this command.");
				    	return false;
				    }
			    }else{
			    	sender.sendMessage(ChatColor.RED+"Strange Command. Do /chestfinder for help.");
			    	return false;
			    }
			}else if (args.length == 3){
				if(args[0].equalsIgnoreCase("give")){
					if(sender.hasPermission("chestfinder.give")){
						try{
							Player target = Bukkit.getPlayer(args[1]);
							try{
								int radius = Integer.parseInt(args[2]);
									target.getInventory().addItem(m.chestfinder(radius));
									target.sendMessage(ChatColor.GREEN+"You have been given a chest finder");
									sender.sendMessage(ChatColor.GREEN+"Gave chest finder to "+target.getName());
							}catch(Exception ex){
								sender.sendMessage(ChatColor.RED+args[1]+" is not a valid integer.");
			    				return false;
							}
						}catch(Exception ex){
							sender.sendMessage(ChatColor.RED+"Player "+args[1]+" not found.");
							return false;
						}
					}else{
						sender.sendMessage(ChatColor.RED+"You do not have permission to perform this command.");
					}
				}else{
					sender.sendMessage(ChatColor.RED+"Strange Command. Do /chestfinder for help.");
				}
			}else if(args.length ==4){
				if(args[0].equalsIgnoreCase("give")){
					if(sender.hasPermission("chestfinder.give")){
						try{
							Player target = Bukkit.getPlayer(args[1]);
							try{
								int radius = Integer.parseInt(args[2]);
								try{
									int n = Integer.parseInt(args[3]);
									int f = 0;
									while(f<n){
										f++;
										target.getInventory().addItem(m.chestfinder(radius));
									}
									target.sendMessage(ChatColor.GREEN+"You have been given a chest finder");
									sender.sendMessage(ChatColor.GREEN+"Gave chest finder to "+target.getName());
								}catch(Exception ex){
									sender.sendMessage(ChatColor.RED+args[3]+" is not a valid integer.");
								}
							}catch(Exception ex){
								sender.sendMessage(ChatColor.RED+args[2]+" is not a valid integer.");
			    				return false;
							}
						}catch(Exception ex){
							sender.sendMessage(ChatColor.RED+"Player "+args[1]+" not found.");
							return false;
						}
					}else{
						sender.sendMessage(ChatColor.RED+"You do not have permission to perform this command.");
					}
				}else{
					sender.sendMessage(ChatColor.RED+"Strange Command. Do /chestfinder for help.");
				}
			}else{
				sender.sendMessage(ChatColor.RED+"Strange Command. Do /chestfinder for help.");
			}
		}
		return false;
	}
}
