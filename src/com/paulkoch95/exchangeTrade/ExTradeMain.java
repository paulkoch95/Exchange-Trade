package com.paulkoch95.exchangeTrade;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;




public class ExTradeMain extends JavaPlugin implements Listener{
	
	public int allowedItem = 3;
	public String PATH_TO_PROPDATA = System.getProperty("user.dir")+"/plugins/ex_trade_dependencies/storeProperties.properties";
	
	public void onEnable(){
		getServer().getPluginManager().registerEvents(this, this);
		getLogger().info("Exchange Trade erfolgreich geladen!");
		loadPropData();
		
		
	}
	public void onDisable(){
		savePropData();
		saveConfig();
	}
	////////////
	//Commands//
	////////////
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		Player player = (Player) sender;//new instance of player is created
		if(cmd.getName().equalsIgnoreCase("trade")){ //Using Trade Cmd
			
			if (sender instanceof Player){ //Checks wether the user is a player or the console
				
				
				if(args[0].equalsIgnoreCase("setupStore")){ //Checks the first Argument
					
					player.sendMessage("Store is now at position: "+ ChatColor.GREEN + "x: "+ getPlayerPositionString(sender) +ChatColor.RESET+ " created by "+ChatColor.RED + player.getName()+" Item: "+ getPlayerItemStack(sender));//Prints message where the Store is
					
				}else if (args[0].equalsIgnoreCase(null)){
					
					sender.sendMessage("Gives you access to the beauty of Exchange Trade!");
				}
				if(args[0].equalsIgnoreCase("debug")){
					
					player.sendMessage("Your Balance is: "+getConfig().getInt(player.getName()+".Gulden"));
				}
				if(args[0].equalsIgnoreCase("info")){
					
					player.sendMessage("Information about the plugin will be provided here, eventually.... SPACEHOLDER TIME!");
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
	
	/////////////////
	//Event Handler//
	/////////////////
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		Player p = e.getPlayer();
		if(!getConfig().contains(p.getName())){
			getConfig().set(p.getName()+".Gulden", 0);
		}
	}
	@EventHandler
	public void onKill(EntityDeathEvent e){
		if(e.getEntity() instanceof Monster){
			Monster m = (Monster) e.getEntity();
			if(m.getKiller() instanceof Player){
				Player p = m.getKiller();
				giveMoney(p,100);
				p.sendMessage("§2§l$100 Gulden bekommen! ("+m.toString()+")");
			}
		}
		
		if(e.getEntity() instanceof Villager){
			Villager v = (Villager) e.getEntity();
			if(v.getKiller() instanceof Player){
				Player p = v.getKiller();
				removeMoney(p,100);
				p.sendMessage("§c§l$100 Gulden verloren!");
			}
		}
			
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event){
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
			if(event.getClickedBlock().getType().equals(Material.SIGN_POST)){
				Sign sign = (Sign) event.getClickedBlock().getState();
				readBankSign(sign,event.getPlayer());
				Player p = event.getPlayer();
				//p.sendMessage("Schild!!!!!!!");
			}
		}
	}
	
	public void readBankSign(Sign sign,Player p){
		
		if (sign.getLine(0).equalsIgnoreCase("serverbank")){
				ItemStack bankItem = new ItemStack(Material.getMaterial(Integer.parseInt(sign.getLine(2))),3);
				if (p.getInventory().getItemInHand().getTypeId() == bankItem.getTypeId()){
					int finalAmount = Integer.parseInt(sign.getLine(1).split(":")[1])*p.getItemInHand().getAmount();
					giveMoney(p,finalAmount);	
					takePlayerItem(bankItem,p); //TODO:Fix ITem amount system, clearly not works a the moment....
					p.sendMessage(finalAmount+" Gulden erhalten!");
				}else{
					p.sendMessage("Du scheinst leider Keine Diamanten in der Hand zu halten!");
				}
				
				
		}
	}
	
	public void giveMoney(Player p, int i){
		getConfig().set(p.getName()+".Gulden",getConfig().getInt(p.getName() + ".Gulden",0)+i);
		saveConfig();
		//p.sendMessage("§2§l$"+i+" Gulden bekommen!");
	}
	
	public void removeMoney(Player p, int i){
		getConfig().set(p.getName()+".Gulden",getConfig().getInt(p.getName() + ".Gulden",0)-i);
		saveConfig();
		//p.sendMessage("§c§l$"+i+" Gulden verloren!");
	}
	
	public void takePlayerItem(ItemStack item, Player p){
		p.getInventory().remove(item);
	}
	
	//////////////////////
	//Properties loading//
	//////////////////////
	public static Properties prop = new Properties();
	
	public void loadPropData(){
		try {
			prop.load(new FileInputStream(PATH_TO_PROPDATA));//load .properties Data by FIS
			getLogger().info("Store Data succesfully loaded!");
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
			prop.store(new FileOutputStream(PATH_TO_PROPDATA), null);//save .properties Data
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	
	
	
	
}
