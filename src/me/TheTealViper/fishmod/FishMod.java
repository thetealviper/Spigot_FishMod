package me.TheTealViper.fishmod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import me.TheTealViper.fishmod.Utils.EnableShit;
import me.TheTealViper.fishmod.Utils.ItemCreator;


public class FishMod
  extends JavaPlugin
  implements Listener
{
  public Map<Biome, List<CustomCatch>> possibilitiesMap = new HashMap();
  private Map<Integer, ItemStack> fishMap = new HashMap();
  
  public void onEnable() {
    EnableShit.handleOnEnable(this, this, "-1");
    loadShit();
  }

  
  public void onDisable() { 
	  //Bukkit.getLogger().info(makeColors("FoodMod from TheTealViper shutting down. Bshzzzzzz")); 
  }

  
  @EventHandler
  public void onFish(PlayerFishEvent e) {
    if (e.getState().equals(PlayerFishEvent.State.CAUGHT_FISH)) {
      Biome b = e.getPlayer().getLocation().getBlock().getBiome();
      List<CustomCatch> catches = this.possibilitiesMap.containsKey(b) ? (List)this.possibilitiesMap.get(b) : new ArrayList();
      if (catches.isEmpty())
        return; 
      boolean completed = false;
      for (CustomCatch cc : catches) {
        if (completed)
          break; 
        if (Math.random() < cc.chance) {
          completed = true;
          ItemStack item = cc.itemstack;
          this.fishMap.put(Integer.valueOf(e.getCaught().getEntityId()), item);
        } 
      } 
    } 
  }
  
  @EventHandler
  public void onPickup(PlayerPickupItemEvent e) {
    if (this.fishMap.containsKey(Integer.valueOf(e.getItem().getEntityId()))) {
      e.setCancelled(true);
      e.getItem().remove();
      Player p = e.getPlayer();
      p.getWorld().dropItemNaturally(p.getLocation(), (ItemStack)this.fishMap.get(Integer.valueOf(e.getItem().getEntityId())));
      String message = (String)ItemCreator.messageInfo.get(this.fishMap.get(Integer.valueOf(e.getItem().getEntityId())));
      if (!message.equals(""))
        p.sendMessage(message); 
    } 
  }
  
  private void loadShit() {
    for (String id : getConfig().getConfigurationSection("Items").getKeys(false)) {
      CustomCatch cc = ItemCreator.createItemFromConfiguration(getConfig().getConfigurationSection("Items." + id));
      for (Biome bio : cc.biomes) {
        List<CustomCatch> catches = this.possibilitiesMap.containsKey(bio) ? (List)this.possibilitiesMap.get(bio) : new ArrayList();
        catches.add(cc);
        this.possibilitiesMap.put(bio, catches);
      } 
    } 
  }

  public static String makeColors(String s) { 
	  return s.replaceAll("&0", ChatColor.BLACK + "")
			  .replaceAll("&1", ChatColor.DARK_BLUE + "")
			  .replaceAll("&2", ChatColor.DARK_GREEN + "")
			  .replaceAll("&3", ChatColor.DARK_AQUA + "")
			  .replaceAll("&4", ChatColor.DARK_RED + "")
			  .replaceAll("&5", ChatColor.DARK_PURPLE + "")
			  .replaceAll("&6", ChatColor.GOLD + "")
			  .replaceAll("&7", ChatColor.GRAY + "")
			  .replaceAll("&8", ChatColor.DARK_GRAY + "")
			  .replaceAll("&9", ChatColor.BLUE + "")
			  .replaceAll("&a", ChatColor.GREEN + "")
			  .replaceAll("&b", ChatColor.AQUA + "")
			  .replaceAll("&c", ChatColor.RED + "")
			  .replaceAll("&d", ChatColor.LIGHT_PURPLE + "")
			  .replaceAll("&e", ChatColor.YELLOW + "")
			  .replaceAll("&f", ChatColor.WHITE + "")
			  .replaceAll("&r", ChatColor.RESET + "")
			  .replaceAll("&l", ChatColor.BOLD + "")
			  .replaceAll("&o", ChatColor.ITALIC + "")
			  .replaceAll("&k", ChatColor.MAGIC + "")
			  .replaceAll("&m", ChatColor.STRIKETHROUGH + "")
			  .replaceAll("&n", ChatColor.UNDERLINE + "")
			  .replaceAll("\\\\", " "); }
}
