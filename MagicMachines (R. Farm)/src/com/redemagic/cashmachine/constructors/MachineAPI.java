package com.redemagic.cashmachine.constructors;

import java.util.Iterator;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import com.redemagic.cashmachine.utils.MakeItem;
import org.bukkit.Material;
import com.redemagic.cashmachine.Main;
import org.bukkit.Location;
import java.util.HashMap;
import java.util.List;
import org.bukkit.inventory.ItemStack;
import org.bukkit.configuration.file.FileConfiguration;
import com.redemagic.cashmachine.utils.Util;

public class MachineAPI
{
    static Util u;
    static FileConfiguration config;
    public static ItemStack item;
    public static List<Double> cashforlevel;
    public static HashMap<Location, Machine> cache;
    public static List<Integer> leveisnobuy;
    
    static {
        MachineAPI.u = new Util();
        MachineAPI.config = Main.config;
        MachineAPI.item = new MakeItem(Material.GOLD_ORE).setName("&6Máquina de Cash").addLoreList("", "&eNível: &f%level%", "&eQuantidade: &f%amount%", "").build();
        MachineAPI.cashforlevel = new ArrayList<Double>();
        MachineAPI.cache = new HashMap<Location, Machine>();
        MachineAPI.leveisnobuy = Lists.newArrayList();
    }
    
    public static void createMachine(final Machine m) {
        Main.getSQLite().insertTable(m);
        MachineAPI.cache.put(m.getLoc(), m);
    }
    
    public static boolean existsMachine(final Location loc) {
        return Main.getSQLite().hasRegister(loc);
    }
    
    public static Machine getMachine(final Location loc) {
        if (existsMachine(loc)) {
            return Main.getSQLite().getMachine(loc);
        }
        return null;
    }
    
    public static void setAmount(final Machine m, final double amount) {
        if (existsMachine(m.getLoc())) {
            m.setAmount(amount);
            Main.getSQLite().setAmount(m);
        }
    }
    
    public static double getAmount(final Machine m) {
        if (existsMachine(m.getLoc())) {
            return m.getAmount();
        }
        return 0.0;
    }
    
    public static void setCash(final Machine m, final double amount) {
        if (existsMachine(m.getLoc())) {
            m.setCash(amount);
            MachineAPI.cache.put(m.getLoc(), m);
        }
    }
    
    public static double getCash(final Machine m) {
        if (existsMachine(m.getLoc())) {
            return m.getCash();
        }
        return 0.0;
    }
    
    public static void setLevel(final Machine m, final int level) {
        if (existsMachine(m.getLoc())) {
            m.setLevel(level);
            Main.getSQLite().setLevel(m);
        }
    }
    
    public static Integer getLevel(final Machine m) {
        if (existsMachine(m.getLoc())) {
            return m.getLevel();
        }
        return 0;
    }
    
    public static void removeMachine(final Machine m) {
        if (existsMachine(m.getLoc())) {
            Main.getSQLite().remove(m.getLoc());
        }
    }
    
    public static void removeMachine(final Location loc) {
        if (existsMachine(loc)) {
            Main.getSQLite().remove(loc);
        }
    }
    
    public static Double getCashAmountForLevel(final Integer level) {
        if (MachineAPI.cashforlevel.isEmpty()) {
            return 0.0;
        }
        return MachineAPI.cashforlevel.get(level - 1);
    }
    
    public static void setCashAmountForLevel() {
        MachineAPI.cashforlevel.add(1000000.0);
        MachineAPI.cashforlevel.add(2000000.0);
        MachineAPI.cashforlevel.add(3000000.0);
        MachineAPI.cashforlevel.add(4000000.0);
        MachineAPI.cashforlevel.add(5000000.0);
        if (!MachineAPI.config.isSet("config.cashforlevel")) {
            return;
        }
        MachineAPI.cashforlevel = new ArrayList<Double>();
        for (final String x : MachineAPI.config.getConfigurationSection("config.cashforlevel").getKeys(false)) {
            MachineAPI.cashforlevel.add(MachineAPI.config.getDouble("config.cashforlevel." + x));
        }
    }
    
    public static double getMachinePrice(final int level) {
        double d = 0.0;
        if (MachineAPI.config.isSet("config.priceforlevel." + level)) {
            d = MachineAPI.config.getDouble("config.priceforlevel." + level);
        }
        return d;
    }
    
    public static void saveCash() {
        for (final Machine m : MachineAPI.cache.values()) {
            Main.getSQLite().setCash(m);
        }
    }
    
    public static ItemStack getMachineType(final Double amount, final Integer level) {
        final ItemStack i = new MakeItem(MachineAPI.item).build();
        final ArrayList<String> lore = new ArrayList<String>();
        for (String ll : i.getItemMeta().getLore()) {
            final String l = ll;
            if (l.contains("%level%")) {
                ll = ll.replace("%level%", new StringBuilder().append(level).toString());
            }
            if (l.contains("%amount%")) {
                ll = ll.replace("%amount%", new StringBuilder(String.valueOf(Util.formatVirgula(amount))).toString());
            }
            lore.add(ll);
        }
        final MakeItem item = new MakeItem(i).setLore(lore);
        return item.build();
    }
    
    public static void loadLeveisNoBuy() {
        if (MachineAPI.config.isSet("config.level_no_buy")) {
            MachineAPI.leveisnobuy = (List<Integer>)MachineAPI.config.getIntegerList("config.level_no_buy");
        }
    }
    
    public static boolean MachineCanBuy(final Machine machine) {
        return !MachineAPI.leveisnobuy.contains(machine.getLevel());
    }
}
