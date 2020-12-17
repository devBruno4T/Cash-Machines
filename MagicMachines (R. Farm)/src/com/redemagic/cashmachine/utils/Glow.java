package com.redemagic.cashmachine.utils;

import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.bukkit.enchantments.Enchantment;

public class Glow extends Enchantment
{
    public Glow(final int id) {
        super(id);
    }
    
    public boolean canEnchantItem(final ItemStack arg0) {
        return false;
    }
    
    public boolean conflictsWith(final Enchantment arg0) {
        return false;
    }
    
    public EnchantmentTarget getItemTarget() {
        return null;
    }
    
    public int getMaxLevel() {
        return 2;
    }
    
    public String getName() {
        return "Glow I";
    }
    
    public int getStartLevel() {
        return 1;
    }
}
