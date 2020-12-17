package com.redemagic.cashmachine.utils;

import net.md_5.bungee.api.ChatColor;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.Color;
import com.mojang.authlib.properties.Property;
import java.util.Base64;
import com.mojang.authlib.GameProfile;
import java.util.UUID;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.Material;
import java.lang.reflect.Field;
import org.bukkit.inventory.ItemStack;

public class MakeItem
{
    private ItemStack ik;
    public static String green_light;
    public static String pink_light;
    public static String blue_light;
    public static String red_light;
    public static String black;
    public static String gray_dark;
    public static String orange;
    public static String blue;
    public static String yellow;
    public static String red;
    public static String yellow_green;
    public static String p_w;
    public static String d_w;
    public static String t_w;
    public static String s_w;
    private static Field profileField;
    
    static {
        MakeItem.green_light = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDI3Y2E0NmY2YTliYjg5YTI0ZmNhZjRjYzBhY2Y1ZTgyODVhNjZkYjc1MjEzNzhlZDI5MDlhZTQ0OTY5N2YifX19";
        MakeItem.pink_light = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2VmMGM1NzczZGY1NjBjYzNmYzczYjU0YjVmMDhjZDY5ODU2NDE1YWI1NjlhMzdkNmQ0NGYyZjQyM2RmMjAifX19";
        MakeItem.blue_light = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmVmN2I2ODI5ZmM0ODc1OGNiMjVhYjkzZjI4YmY3OTRkOTJhY2UwMTYxZjgwOWEyYWFkZDNiYjEyYjIzMDE0In19fQ==";
        MakeItem.red_light = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDI5MzJiNjZkZWNhZWZmNmViZGM3YzViZTZiMjQ2N2FhNmYxNGI3NDYzODhhMDZhMmUxZTFhODQ2M2U5ZTEyMiJ9fX0=";
        MakeItem.black = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTY3YTJmMjE4YTZlNmUzOGYyYjU0NWY2YzE3NzMzZjRlZjliYmIyODhlNzU0MDI5NDljMDUyMTg5ZWUifX19";
        MakeItem.gray_dark = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjA4ZjMyMzQ2MmZiNDM0ZTkyOGJkNjcyODYzOGM5NDRlZTNkODEyZTE2MmI5YzZiYTA3MGZjYWM5YmY5In19fQ==";
        MakeItem.orange = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmMyZTk3MmFmYTkxMTViNmQzMjA3NWIxZjFiN2ZlZDdhYTI5YTUzNDFjMTAyNDI4ODM2MWFiZThlNjliNDYifX19";
        MakeItem.blue = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTJjZDI3MmVlYjM4YmY3ODNhOThhNDZmYTFlMmU4ZDQ2MmQ4NTJmYmFhZWRlZjBkY2UyYzFmNzE3YTJhIn19fQ==";
        MakeItem.yellow = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzY0MTY4MmY0MzYwNmM1YzlhZDI2YmM3ZWE4YTMwZWU0NzU0N2M5ZGZkM2M2Y2RhNDllMWMxYTI4MTZjZjBiYSJ9fX0=";
        MakeItem.red = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWZkZTNiZmNlMmQ4Y2I3MjRkZTg1NTZlNWVjMjFiN2YxNWY1ODQ2ODRhYjc4NTIxNGFkZDE2NGJlNzYyNGIifX19";
        MakeItem.yellow_green = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjhmZmYyMmM2ZTY1NDZkMGQ4ZWI3Zjk3NjMzOTg0MDdkZDJhYjgwZjc0ZmUzZDE2YjEwYTk4M2VjYWYzNDdlIn19fQ==";
        MakeItem.p_w = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjU5M2E2ZjIzZjViMTEwZWMxYzdhYjY2YzQwZmJjOWI4MmY0ZjVjOTg3MjY1YWE2N2M5NWEyZDNmNGM5NyJ9fX0=";
        MakeItem.d_w = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODQxMWE3ZTFiYzdiOWI5NzYyM2UwNDc4MjE5YjFhMzIzODNlZTA3ZWIwZTZhNjZmNWVmOWY4NTU1OTNkZjAifX19";
        MakeItem.t_w = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzg5N2FlNTRjMWQzZGUyY2U0YTVmMGY0OTQzOTgyNTM4YThhN2M2OWYxNzRiZTU3ZDc0YTMzZWU5YmM1ZjY3In19fQ==";
        MakeItem.s_w = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjY1N2JkMTg2OGRhNjczNmNjNGM0MzUzOTg5ZTZhNDU3NDM3YmRkZDM5MmM4MmMyNDhkMTQyMDcwYThkZDgzIn19fQ==";
    }
    
    public MakeItem(final Material material) {
        this.ik = new ItemStack(material);
    }
    
    public MakeItem(final Material material, final byte data) {
        this.ik = new ItemStack(material, 1, (short)data);
    }
    
    public MakeItem(final String owner) {
        this.ik = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        final SkullMeta skullMeta = (SkullMeta)this.ik.getItemMeta();
        skullMeta.setOwner(owner);
        this.ik.setItemMeta((ItemMeta)skullMeta);
    }
    
    public MakeItem(final String owner, final String name) {
        this.ik = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        final SkullMeta skullMeta = (SkullMeta)this.ik.getItemMeta();
        skullMeta.setDisplayName(name);
        skullMeta.setOwner(owner);
        this.ik.setItemMeta((ItemMeta)skullMeta);
    }
    
    public MakeItem addEnchantment(final Enchantment enchant, final int level) {
        this.ik.addUnsafeEnchantment(enchant, level);
        return this;
    }
    
    public static ItemStack getCustomSkullURL(final String url) {
        final ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        final SkullMeta meta = (SkullMeta)item.getItemMeta();
        final GameProfile profile = new GameProfile(UUID.randomUUID(), (String)null);
        final byte[] data = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(data)));
        try {
            if (MakeItem.profileField == null) {
                MakeItem.profileField = meta.getClass().getDeclaredField("profile");
            }
            MakeItem.profileField.setAccessible(true);
            MakeItem.profileField.set(meta, profile);
            item.setItemMeta((ItemMeta)meta);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return item;
    }
    
    public static ItemStack getCustomSkullTexture(final String texture) {
        final ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        final SkullMeta meta = (SkullMeta)item.getItemMeta();
        final GameProfile profile = new GameProfile(UUID.randomUUID(), (String)null);
        profile.getProperties().put("textures", new Property("textures", new String(texture)));
        try {
            if (MakeItem.profileField == null) {
                MakeItem.profileField = meta.getClass().getDeclaredField("profile");
            }
            MakeItem.profileField.setAccessible(true);
            MakeItem.profileField.set(meta, profile);
            item.setItemMeta((ItemMeta)meta);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return item;
    }
    
    public MakeItem(final int material, final byte data) {
        this.ik = new ItemStack(material, 1, (short)data);
    }
    
    public MakeItem(final ItemStack ik) {
        this.ik = ik.clone();
    }
    
    public MakeItem(final int material) {
        this.ik = new ItemStack(material);
    }
    
    public MakeItem setAmout(final int amount) {
        this.ik.setAmount(amount);
        return this;
    }
    
    public MakeItem setType(final Material type) {
        this.ik.setType(type);
        return this;
    }
    
    public MakeItem setName(final String name) {
        final ItemMeta im = this.ik.getItemMeta();
        im.setDisplayName(name.replace("&", "§"));
        this.ik.setItemMeta(im);
        return this;
    }
    
    public MakeItem setDurability(final Short durability) {
        this.ik.setDurability((short)durability);
        return this;
    }
    
    public MakeItem addGlow() {
        final Glow glow = new Glow(200);
        final ItemMeta itemMeta = this.ik.getItemMeta();
        itemMeta.addEnchant((Enchantment)glow, 1, true);
        this.ik.setItemMeta(itemMeta);
        return this;
    }
    
    public MakeItem setColor(final Color color) {
        try {
            final LeatherArmorMeta meta = (LeatherArmorMeta)this.ik.getItemMeta();
            meta.setColor(color);
            this.ik.setItemMeta((ItemMeta)meta);
        }
        catch (Exception ex) {}
        return this;
    }
    
    public MakeItem setLore(final ArrayList<String> lore) {
        final ItemMeta im = this.ik.getItemMeta();
        final ArrayList<String> lorer = new ArrayList<String>();
        for (final String r : lore) {
            lorer.add(r.replace("&", "§"));
        }
        im.setLore((List)lorer);
        this.ik.setItemMeta(im);
        return this;
    }
    
    public MakeItem addLore(final ArrayList<String> lore, final ChatColor color) {
        final ItemMeta im = this.ik.getItemMeta();
        final ArrayList<String> lorer = new ArrayList<String>();
        for (final String r : lore) {
            lorer.add(color + r.replace("&", "§"));
        }
        im.setLore((List)lorer);
        this.ik.setItemMeta(im);
        return this;
    }
    
    public MakeItem addLoreList(final String... name) {
        final String[] arrayOfString = name;
        for (int j = name.length, i = 0; i < j; ++i) {
            final String s = arrayOfString[i];
            this.addLore(s);
        }
        return this;
    }
    
    public MakeItem addLore(final ArrayList<String> lore) {
        final ItemMeta im = this.ik.getItemMeta();
        final ArrayList<String> lorer = new ArrayList<String>();
        for (final String r : lore) {
            lorer.add(r.replace("&", "§"));
        }
        im.setLore((List)lorer);
        this.ik.setItemMeta(im);
        return this;
    }
    
    public MakeItem addLore(final String lore) {
        final ItemMeta im = this.ik.getItemMeta();
        List<String> lorer = new ArrayList<String>();
        if (im.hasLore()) {
            lorer = (List<String>)im.getLore();
        }
        if (lore.contains("/n")) {
            String[] arrayOfString;
            for (int j = (arrayOfString = lore.split("/n")).length, i = 0; i < j; ++i) {
                final String x = arrayOfString[i];
                lorer.add(x.replace("&", "§"));
            }
        }
        else {
            lorer.add(lore.replace("&", "§"));
        }
        im.setLore((List)lorer);
        this.ik.setItemMeta(im);
        return this;
    }
    
    public MakeItem remLore(final int amount) {
        final ItemMeta im = this.ik.getItemMeta();
        List<String> lorer = new ArrayList<String>();
        if (im.hasLore()) {
            lorer = (List<String>)im.getLore();
        }
        for (int i = 0; i < amount; ++i) {
            if (!lorer.isEmpty()) {
                lorer.remove(lorer.size() - 1);
            }
        }
        im.setLore((List)lorer);
        this.ik.setItemMeta(im);
        return this;
    }
    
    public MakeItem addLore(final String[] lore) {
        final ItemMeta im = this.ik.getItemMeta();
        List<String> lorer = new ArrayList<String>();
        if (im.hasLore()) {
            lorer = (List<String>)im.getLore();
        }
        final String[] arrayOfString = lore;
        for (int j = lore.length, i = 0; i < j; ++i) {
            final String x = arrayOfString[i];
            lorer.add(x.replace("&", "§"));
        }
        im.setLore((List)lorer);
        this.ik.setItemMeta(im);
        return this;
    }
    
    public ItemStack build() {
        return this.ik;
    }
    
    public static boolean checkIsSimilar(final ItemStack ik1, final ItemStack ik2) {
        return ik1.getType() == ik2.getType() && (ik1.hasItemMeta() && ik2.hasItemMeta()) && (ik1.getItemMeta().hasDisplayName() && ik2.getItemMeta().hasDisplayName()) && ik1.getItemMeta().getDisplayName().equals(ik2.getItemMeta().getDisplayName()) && (ik1.getItemMeta().hasLore() && ik2.getItemMeta().hasLore()) && ik1.getItemMeta().getLore().equals(ik2.getItemMeta().getLore());
    }
    
    public static boolean checkIsSimilar(final ItemStack ik1, final ItemStack ik2, final boolean use_lore) {
        return ik1.getType() == ik2.getType() && (ik1.hasItemMeta() && ik2.hasItemMeta()) && (ik1.getItemMeta().hasDisplayName() && ik2.getItemMeta().hasDisplayName()) && ik1.getItemMeta().getDisplayName().equals(ik2.getItemMeta().getDisplayName()) && (!use_lore || (ik1.getItemMeta().hasLore() && ik2.getItemMeta().hasLore() && ik1.getItemMeta().getLore().equals(ik2.getItemMeta().getLore())));
    }
}
