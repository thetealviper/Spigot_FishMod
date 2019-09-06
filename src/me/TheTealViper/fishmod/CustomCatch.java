package me.TheTealViper.fishmod;

import java.util.List;
import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;

public class CustomCatch {
  public CustomCatch(ItemStack itemstack, List<Biome> biome, double chance) {
    this.itemstack = null;
    this.biomes = null;
    this.chance = 0.0D;

    
    this.itemstack = itemstack;
    this.biomes = biome;
    this.chance = chance;
  }
  
  public ItemStack itemstack;
  public List<Biome> biomes;
  public double chance;
}
