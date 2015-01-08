package com.paulkoch95.exchangeTrade;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;




public class exTrade extends JavaPlugin{
	
	public int allowedItem = 3;
	public void onEnable(){
		getLogger().info("Exchange Trade erfolgreich geladen!");
		loadPropData();
		
	}
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(cmd.getName().equalsIgnoreCase("trade")){ //Using Trade Cmd
			
			if (sender instanceof Player){ //Checks wether the user is a player or the console
				
				
				if(args[0].equalsIgnoreCase("setupStore")){ //Checks the first Argument
					
					Player player = (Player) sender;//new instance of player is created
					player.sendMessage("Store is now at position: "+ ChatColor.GREEN + "x: "+ getPlayerPositionString(sender) +ChatColor.RESET+ " created by "+ChatColor.RED + player.getName()+" Item: "+ getPlayerItemStack(sender));//Prints message where the Store is
					
				}else if (args[0].equalsIgnoreCase(null)){
					sender.sendMessage("Gives you access to the beauty of Exchange Trade!");
				}
				if(args[0].equalsIgnoreCase("debug")){
					Player player = (Player) sender;
					player.sendMessage("Key Content quals: "+getKeyContent("player"));
				}
			}else{
				
				getLogger().info("As a console master all information is written in the /help");
				
			}
			
			
		}
		
		return false;
	}
	public String getPlayerPositionString(CommandSender sender){
		Player player = (Player) sender;
		String position = "X: " + (int)(player.getLocation().getX())+" Y: " + (int)(player.getLocation().getY())+" Z: " + (int)(player.getLocation().getZ());
		return position;
	}
	public ItemStack getPlayerItemStack(CommandSender sender){
		Player player = (Player) sender;
		ItemStack currentItem = player.getItemInHand();
		return currentItem;
	}
	public int getPlayerItem(CommandSender sender){
		Player player = (Player) sender;
		//ItemStack item = player.getInventory().getItemInHand().getTypeId();
		return (Integer) null;
	}
	
	
	
	
	//////////////////////
	//Properties loading//
	//////////////////////
	public static Properties prop = new Properties();
	public void loadPropData(){
		try {
			prop.load(new FileInputStream(System.getProperty("user.dir")+"/playerData.properties"));//load .properties Data by FIS
			getLogger().info("Player Data succesfully loaded!");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public String getKeyContent(String key){
		String readKey = prop.getProperty(key);
		return readKey;
	}
	public void setProp(String key, String keyContent){
		prop.setProperty(key,keyContent);
	}
	public void savePropData(){
		try {
			prop.store(new FileOutputStream("playerData.properties"), null);//save .properties Data
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	
	
	
	
}
