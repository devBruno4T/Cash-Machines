package com.redemagic.cashmachine;

import java.util.Iterator;
import com.redemagic.cashmachine.constructors.Machine;
import com.redemagic.cashmachine.listeners.MachineInventory;
import com.redemagic.cashmachine.listeners.MachineBreak;
import org.bukkit.event.Listener;
import com.redemagic.cashmachine.listeners.MachinePlace;
import org.bukkit.command.CommandExecutor;
import com.redemagic.cashmachine.commands.CashMachine;
import org.bukkit.Bukkit;
import com.redemagic.cashmachine.constructors.MachineAPI;
import org.bukkit.configuration.file.YamlConfiguration;
import com.redemagic.cashmachine.constructors.SQLite;
import com.redemagic.cashmachine.utils.Util;
import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
{
    public static Plugin pl;
    public static Main main;
    public static FileConfiguration config;
    public static File machines1;
    public static FileConfiguration machines;
    static Util u;
    public static SQLite sqlite;
    public static Integer max_level;
    public static boolean transferirYMLparaSQL;
    
    static {
        Main.u = new Util();
        Main.max_level = 5;
        Main.transferirYMLparaSQL = false;
    }
    
    public void onEnable() {
    	Bukkit.getConsoleSender().sendMessage("");
      	Bukkit.getConsoleSender().sendMessage("§cMAGIC MACHINES: §fPlugin inicializando");
    	Bukkit.getConsoleSender().sendMessage("");  
    	Bukkit.getConsoleSender().sendMessage("§cMAGIC MACHINES: §fVersão: V1.0.6");
      	Bukkit.getConsoleSender().sendMessage("");
        Main.pl = (Plugin)this;
        Main.main = this;
        final File config1 = new File(this.getDataFolder(), "config.yml");
        if (!config1.exists()) {
            this.saveResource("config.yml", false);
        }
        Main.config = this.getConfig();
        Main.machines1 = new File(this.getDataFolder(), "machines.yml");
        if (!Main.machines1.exists()) {
            try {
                Main.machines1.createNewFile();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        Main.machines = (FileConfiguration)YamlConfiguration.loadConfiguration(Main.machines1);
        if (Main.config.isSet("config.max_level")) {
            Main.max_level = Main.config.getInt("config.max_level");
        }
        MachineAPI.setCashAmountForLevel();
        MachineAPI.loadLeveisNoBuy();
        Main.sqlite = new SQLite();
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.pl, (Runnable)new Runnable() {
            @Override
            public void run() {
                Main.getSQLite().loadDataBase();
            }
        }, 40L);
        if (Main.config.isSet("transferirYMLparaSQL")) {
            Main.transferirYMLparaSQL = true;
            Bukkit.getConsoleSender().sendMessage("[" + Main.pl.getName() + "] Est\u00e1 ativo a op\u00e7\u00e3o de transfer\u00eancia de arquivos de YML para SQLite. Iniciando transfer\u00eancias...");
        }
        transferirYMLparaSQL(Main.transferirYMLparaSQL);
        this.getCommand("cashmachine").setExecutor((CommandExecutor)new CashMachine());
        Bukkit.getPluginManager().registerEvents((Listener)new MachinePlace(), (Plugin)this);
        Bukkit.getPluginManager().registerEvents((Listener)new MachineBreak(), (Plugin)this);
        Bukkit.getPluginManager().registerEvents((Listener)new MachineInventory(), (Plugin)this);
        Bukkit.getPluginManager().registerEvents((Listener)new CashMachine(), (Plugin)this);
    }
    
    public void onDisable() {
        MachineAPI.saveCash();
    }
    
    public static SQLite getSQLite() {
        return Main.sqlite;
    }
    
    public static void transferirYMLparaSQL(final boolean active) {
        if (!active) {
            return;
        }
        if (!Main.machines.isSet("machines")) {
            Bukkit.getConsoleSender().sendMessage("[" + Main.pl.getName() + "] N\u00e3o foi possivel transferir arquivos para SQL, pois n\u00e3o havia nenhum.");
            return;
        }
        int i = 0;
        for (final String loc : Main.machines.getConfigurationSection("machines").getKeys(false)) {
            final String owner = Main.machines.getString("machines." + loc + ".owner");
            final Double amount = Main.machines.getDouble("machines." + loc + ".amount");
            final Double cash = Main.machines.getDouble("machines." + loc + ".cash");
            final Integer level = Main.machines.getInt("machines." + loc + ".level");
            final Machine m = new Machine(Main.u.deserialise(loc), owner, amount, cash, level);
            getSQLite().updateTableAmount(m);
            ++i;
        }
        Bukkit.getConsoleSender().sendMessage("[" + Main.pl.getName() + "] Foram transferidos " + i + " arquivos de YML para SQLite.");
    }
}
