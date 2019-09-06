package me.TheTealViper.fishmod.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.TheTealViper.fishmod.CustomCatch;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;


public class ItemCreator
  implements Listener
{
  private static List<Material> durMats = new ArrayList();
  public static Map<ItemStack, Integer> damageInfo = new HashMap();
  public static Map<ItemStack, String> messageInfo = new HashMap();
  
  public static CustomCatch createItemFromConfiguration(ConfigurationSection sec) {
    if (durMats.isEmpty())
      loadDurMats();
    ItemStack item = (sec == null || !sec.contains("id")) ? null : new ItemStack(Material.valueOf(sec.getString("id").split(":")[0]), 1, Short.valueOf(sec.getString("id").split(":")[1]).shortValue());
    if (item == null)
      return null; 
    if (sec.contains("amount"))
      item.setAmount(sec.getInt("amount")); 
    ItemMeta meta = Bukkit.getItemFactory().getItemMeta(Material.STICK);
    if (sec.contains("name") && !sec.getString("name").equals("")) {
      meta.setDisplayName(StringUtils.makeColors(sec.getString("name")));
      item.setItemMeta(meta);
    } 
    List<String> enchantmentStrings = sec.contains("enchantments") ? sec.getStringList("enchantments") : new ArrayList();
    for (String enchantmentString : enchantmentStrings) {
      String enchantment = enchantmentString.split(":")[0];
      int level = Integer.valueOf(enchantmentString.split(":")[1]).intValue();
      if (enchantment.equalsIgnoreCase("arrowdamage")) {
        item.addEnchantment(Enchantment.ARROW_DAMAGE, level); continue;
      }  if (enchantment.equalsIgnoreCase("arrowfire")) {
        item.addUnsafeEnchantment(Enchantment.ARROW_FIRE, level); continue;
      }  if (enchantment.equalsIgnoreCase("arrowinfinite")) {
        item.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, level); continue;
      }  if (enchantment.equalsIgnoreCase("arrowknockback")) {
        item.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, level); continue;
      }  if (enchantment.equalsIgnoreCase("damage")) {
        item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, level); continue;
      }  if (enchantment.equalsIgnoreCase("digspeed")) {
        item.addUnsafeEnchantment(Enchantment.DIG_SPEED, level); continue;
      }  if (enchantment.equalsIgnoreCase("durability")) {
        item.addUnsafeEnchantment(Enchantment.DURABILITY, level); continue;
      }  if (enchantment.equalsIgnoreCase("fireaspect")) {
        item.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, level); continue;
      }  if (enchantment.equalsIgnoreCase("knockback")) {
        item.addUnsafeEnchantment(Enchantment.KNOCKBACK, level); continue;
      }  if (enchantment.equalsIgnoreCase("lootbonusblock")) {
        item.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, level); continue;
      }  if (enchantment.equalsIgnoreCase("lootbonusmob")) {
        item.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, level); continue;
      }  if (enchantment.equalsIgnoreCase("luck")) {
        item.addUnsafeEnchantment(Enchantment.LUCK, level); continue;
      }  if (enchantment.equalsIgnoreCase("protectionfall")) {
        item.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, level); continue;
      }  if (enchantment.equalsIgnoreCase("protectionfire")) {
        item.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, level); continue;
      }  if (enchantment.equalsIgnoreCase("silktouch")) {
        item.addUnsafeEnchantment(Enchantment.SILK_TOUCH, level);
      }
    } 
    List<String> tags = sec.contains("tags") ? sec.getStringList("tags") : new ArrayList();
    for (String s : tags) {
      if (s.startsWith("skullskin") && item.getType().equals(Material.PLAYER_HEAD)) {
        SkullMeta skull = (SkullMeta)item.getData();
        skull.setOwner(s.replace("skullskin:", ""));
        item.setData((MaterialData)skull); continue;
      }  if (s.startsWith("durability") && durMats.contains(item.getType())) {
        item.getData().setData(Byte.valueOf(s.replace("durability:", "")).byteValue());
        item.setDurability(Short.valueOf(s.replace("durability:", "")).shortValue()); continue;
      }  if (s.equalsIgnoreCase("unbreakable")) {
        meta = item.getItemMeta();
        meta.setUnbreakable(true);
        item.setItemMeta(meta); continue;
      }  s.startsWith("damage");
    } 

    
    List<String> lore = sec.contains("lore") ? sec.getStringList("lore") : new ArrayList();
    if (!lore.isEmpty()) {
      for (int i = 0; i < lore.size(); i++) {
        lore.set(i, StringUtils.makeColors((String)lore.get(i)));
      }
      meta = item.getItemMeta();
      meta.setLore(lore);
      item.setItemMeta(meta);
    } 
    if (sec.contains("flags")) {
      meta = item.getItemMeta();
      for (String s : sec.getStringList("flags")) {
        meta.addItemFlags(new ItemFlag[] { ItemFlag.valueOf(s) });
      } 
      item.setItemMeta(meta);
    } 
    for (String s : tags) {
      if (s.startsWith("damage")) {
        damageInfo.put(item, Integer.valueOf(s.replace("damage:", "")));
      }
    } 
    if (sec.contains("message")) {
      messageInfo.put(item, StringUtils.makeColors(sec.getString("message")));
    }
    List<String> biomeNames = new ArrayList<String>();
    if (sec.contains("biomes"))
      biomeNames = sec.getStringList("biomes"); 
    List<Biome> biomes = new ArrayList<Biome>();
    if (biomeNames.contains("ALL")) {
      for (Biome b : Biome.values()) { 
        biomes.add(b);
      }
    
    } else {
      for (String s : biomeNames) {
        biomes.add(Biome.valueOf(s));
      }
    } 
    double chance = 0.0D;
    if (sec.contains("chance"))
      chance = sec.getDouble("chance"); 
    return new CustomCatch(item, biomes, chance);
  }

  
  @EventHandler
  public void onDamage(EntityDamageByEntityEvent e) {
    if (e.getDamager() instanceof Player) {
      Player p = (Player)e.getDamager();
      ItemStack item = p.getItemInHand();
      if (p.getItemInHand() != null && !p.getItemInHand().getType().equals(Material.AIR)) {
        for (ItemStack i : damageInfo.keySet()) {
          if (item.isSimilar(i)) {
            e.setDamage(((Integer)damageInfo.get(i)).intValue());
            return;
          } 
        } 
      }
    } 
  }
  
  private static void loadDurMats() {
    durMats.add(Material.DIAMOND_SHOVEL); durMats.add(Material.GOLDEN_SHOVEL); durMats.add(Material.IRON_SHOVEL);
    durMats.add(Material.STONE_SHOVEL); durMats.add(Material.WOODEN_SHOVEL);
    durMats.add(Material.DIAMOND_PICKAXE); durMats.add(Material.GOLDEN_PICKAXE); durMats.add(Material.IRON_PICKAXE);
    durMats.add(Material.STONE_PICKAXE); durMats.add(Material.WOODEN_PICKAXE);
    durMats.add(Material.DIAMOND_AXE); durMats.add(Material.GOLDEN_AXE); durMats.add(Material.IRON_AXE);
    durMats.add(Material.STONE_AXE); durMats.add(Material.WOODEN_AXE);
    durMats.add(Material.DIAMOND_HOE); durMats.add(Material.GOLDEN_HOE); durMats.add(Material.IRON_HOE);
    durMats.add(Material.STONE_HOE); durMats.add(Material.WOODEN_HOE);
    durMats.add(Material.DIAMOND_SWORD); durMats.add(Material.GOLDEN_SWORD); durMats.add(Material.IRON_SWORD);
    durMats.add(Material.STONE_SWORD); durMats.add(Material.WOODEN_SWORD);
    durMats.add(Material.CHAINMAIL_HELMET); durMats.add(Material.DIAMOND_HELMET); durMats.add(Material.GOLDEN_HELMET);
    durMats.add(Material.IRON_HELMET); durMats.add(Material.LEATHER_HELMET);
    durMats.add(Material.CHAINMAIL_CHESTPLATE); durMats.add(Material.DIAMOND_CHESTPLATE); durMats.add(Material.GOLDEN_CHESTPLATE);
    durMats.add(Material.IRON_CHESTPLATE); durMats.add(Material.LEATHER_CHESTPLATE);
    durMats.add(Material.CHAINMAIL_LEGGINGS); durMats.add(Material.DIAMOND_LEGGINGS); durMats.add(Material.LEATHER_LEGGINGS);
    durMats.add(Material.IRON_LEGGINGS); durMats.add(Material.GOLDEN_LEGGINGS);
    durMats.add(Material.CHAINMAIL_BOOTS); durMats.add(Material.DIAMOND_BOOTS); durMats.add(Material.GOLDEN_BOOTS);
    durMats.add(Material.IRON_BOOTS); durMats.add(Material.LEATHER_BOOTS);
    durMats.add(Material.BOW);
  }
}
