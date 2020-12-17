package com.redemagic.cashmachine.listeners;

import com.redemagic.cashmachine.utils.MakeItem;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.Bukkit;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.command.CommandSender;
import dev.mateusamaral720.furioncash.Metodos;

import com.redemagic.cashmachine.utils.TitleAPI;
import com.redemagic.cashmachine.utils.ActionBar;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.EventHandler;
import com.redemagic.cashmachine.constructors.MachineAPI;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import com.redemagic.cashmachine.constructors.Machine;
import org.bukkit.entity.Player;
import java.util.HashMap;
import com.redemagic.cashmachine.utils.Util;
import org.bukkit.event.Listener;

public class MachineInventory implements Listener
{
    static Util u;
    public static HashMap<Player, Machine> save;
    public static HashMap<Player, Machine> savebuy;
    
    static {
        MachineInventory.u = new Util();
        MachineInventory.save = new HashMap<Player, Machine>();
        MachineInventory.savebuy = new HashMap<Player, Machine>();
    }
    
    @EventHandler
    public void onInteract(final PlayerInteractEvent e) {
        final Player p = e.getPlayer();
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (e.getClickedBlock().getType() == Material.AIR) {
            return;
        }
        if (!MachineAPI.existsMachine(e.getClickedBlock().getLocation())) {
            return;
        }
        if (MachineAPI.item.getType() != e.getClickedBlock().getType()) {
            return;
        }
        if (e.isCancelled()) {
            return;
        }
        menu(p, MachineAPI.getMachine(e.getClickedBlock().getLocation()));
    }
    
    @EventHandler
    public void onClick(final InventoryClickEvent e) {
        final Player p = (Player)e.getWhoClicked();
        final Inventory inv = e.getInventory();
        if (!inv.getTitle().contains("Menu - Sua Máquina:")) {
            return;
        }
        if (e.getCurrentItem() == null) {
            return;
        }
        e.setCancelled(true);
        if (e.getCurrentItem().getType() == Material.AIR) {
            return;
        }
        if (!MachineInventory.save.containsKey(p)) {
            p.closeInventory();
            return;
        }
        if (!MachineAPI.existsMachine(MachineInventory.save.get(p).getLoc())) {
            p.closeInventory();
            return;
        }
        final ItemStack item = e.getCurrentItem();
        if (item.getType().equals((Object)Material.DOUBLE_PLANT) && item.getItemMeta().getDisplayName().contains("Recolher Cash")) {
            if (!MachineAPI.existsMachine(MachineInventory.save.get(p).getLoc())) {
                p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1.0f, 1.0f);
                p.closeInventory();
                ActionBar.sendActionText(p, MachineInventory.u.color("§c&lERRO: §cNão foi possivel recolher o cash da sua máquina."));
                return;
            }
            if (!MachineInventory.save.get(p).getOwner().toLowerCase().equals(p.getName().toLowerCase())) {
                p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1.0f, 1.0f);
                p.closeInventory();
                ActionBar.sendActionText(p, MachineInventory.u.color("§c&lERRO: §cVocê não é o dono desta máquina."));
                return;
            }
            if (MachineInventory.save.get(p).getCash() <= 0.0) {
                p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1.0f, 1.0f);
                p.closeInventory();
                ActionBar.sendActionText(p, MachineInventory.u.color("§c&lERRO: §cNão possui cash para recolher"));
                return;
            }
            p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
            p.closeInventory();
            TitleAPI.sendFullTitle(p, 20, 20, 20, MachineInventory.u.color("§6§lCASH RECEBIDO"), MachineInventory.u.color("§e+" + Util.format(MachineInventory.save.get(p).getCash()) + ""));
            CashAPI.addCash(p.getName(), MachineInventory.save.get(p).getCash());
            MachineAPI.setCash(MachineInventory.save.get(p), 0.0);
        }
        if (item.getType().equals((Object)Material.GOLDEN_CARROT) && item.getItemMeta().getDisplayName().contains("Comprar mais máquinas")) {
            if (!MachineInventory.save.get(p).getOwner().toLowerCase().equals(p.getName().toLowerCase())) {
                p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1.0f, 1.0f);
                p.closeInventory();
                ActionBar.sendActionText(p, MachineInventory.u.color("§c&lERRO: §cVocê não é o dono desta maquina."));
                return;
            }
            final Machine m = MachineInventory.save.get(p);
            MachineInventory.savebuy.put(p, m);
            MachineInventory.u.message((CommandSender)p, "", "§2§lPROCESSO DE COMPRA");
            MachineInventory.u.message((CommandSender)p, "", "");
            MachineInventory.u.message((CommandSender)p, "", "§aDigite a quantidade de máquinas de cash que deseja comprar.", "§7Para cancelar, digite §ccancelar&7.", "");
            MachineInventory.u.message((CommandSender)p, "", "");
            p.closeInventory();
        }
    }
    
    public static double getCost(final int level, final double amount) {
        final double cost = level;
        return cost * amount;
    }
    
    public static boolean hasCost(final Player p, final double amount, final int level) {
        return CashAPI.getCash(p.getName()) >= getCost(level, amount);
    }
    
    public static boolean hasCost(final Player p, final double amount) {
        return CashAPI.getCash(p.getName()) >= amount;
    }
    
    public static void takeCost(final Player p, final double amount) {
        if (hasCost(p, amount)) {
        	CashAPI.removeCash(p.getName(), Double.valueOf(amount));
        }
    }
    
    @EventHandler
    public void onChat(final AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        final String msg = e.getMessage();
        if (!MachineInventory.savebuy.containsKey(p)) {
            return;
        }
        if (msg.toLowerCase().equals("cancelar")) {
            e.setCancelled(true);
            MachineInventory.u.message((CommandSender)p, "§cProcesso de compra cancelado.");
            MachineInventory.savebuy.remove(p);
            return;
        }
        if (!isDouble(msg)) {
            MachineInventory.u.message((CommandSender)p, "§cA quantidade deve ser um número.");
            e.setCancelled(true);
            return;
        }
        if (msg.contains(".") || msg.toLowerCase().contains("nan")) {
            MachineInventory.u.message((CommandSender)p, "§cA quantidade deve ser um número.");
            e.setCancelled(true);
            return;
        }
        if (Double.parseDouble(msg) < 1.0) {
            MachineInventory.u.message((CommandSender)p, "§cSua quantidade deve ser positiva.");
            e.setCancelled(true);
            return;
        }
        shop(p, MachineInventory.savebuy.get(p), Double.parseDouble(msg), MachineInventory.savebuy.get(p).getLevel());
        e.setCancelled(true);
        MachineInventory.savebuy.remove(p);
    }
    
    public static void shop(final Player p, final Machine m, final double amount, final int level) {
        if (!MachineInventory.savebuy.containsKey(p)) {
            MachineInventory.u.message((CommandSender)p, "§c§lOPS!");
            return;
        }
        if (MachineAPI.getMachinePrice(level) == 0.0) {
            MachineInventory.u.message((CommandSender)p, "§cEssa máquina não existe");
            MachineInventory.savebuy.remove(p);
            p.closeInventory();
            return;
        }
        if (CashAPI.getCash(p.getName()) < MachineAPI.getMachinePrice(level) * amount) {
            MachineInventory.savebuy.remove(p);
            MachineInventory.u.message((CommandSender)p, "§cVocê precisa §f" + Util.format(MachineAPI.getMachinePrice(level) * amount) + " §cde Cash para conseguir comprar essa quantidade de máquinas de cash.");
            p.closeInventory();
            return;
        }
        takeCost(p, MachineAPI.getMachinePrice(level) * amount);
        MachineAPI.setAmount(m, m.getAmount() + amount);
        TitleAPI.sendFullTitle(p, 20, 20, 20, MachineInventory.u.color("§f+§e" + Util.format(amount)), MachineInventory.u.color("§fMáquinas de Cash"));
        p.closeInventory();
        p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
    }
    
    public static void menu(final Player p, final Machine m) {
        final Inventory inv = Bukkit.createInventory((InventoryHolder)null, 27, MachineInventory.u.color("§8Sua máquina:"));
        MachineInventory.save.put(p, m);
        MakeItem buy = new MakeItem(1);
        if (!MachineAPI.MachineCanBuy(m)) {
            buy.setType(Material.BARRIER);
            buy.setName("§c§lOPS!");
            buy.addLoreList("", " §7Esta máquina não permite que você", " §7adquira mais do mesmo tipo", "");
        }
        else {
            buy = new MakeItem(Material.GOLDEN_CARROT).setName("&aComprar mais máquinas").addLore("&7Clique aqui iniciar o processo de compra");
        }
        inv.setItem(11, buy.build());
        final MakeItem info = new MakeItem(Material.ARMOR_STAND).setName("&eInformações");
        info.addLore("&7Visualize aqui as informações");
        info.addLore("&7sobre sua máquina");
        info.addLore("");
        info.addLore(" &fQuantidade: &7" + Util.format(m.getAmount()));
        info.addLore(" &fNível: &7" + m.getLevel());
        info.addLore(" &fCash: &7" + Util.format(m.getCash()));
        info.addLore("");
        info.addLore(" &fDono: &7" + m.getOwner());
        info.addLore("");
        inv.setItem(15, new MakeItem(Material.DOUBLE_PLANT).setName("&eRecolher Cash").addLore("&7Clique aqui para recolher &e" + Util.format(m.getCash()) + "&7 de Cash.").build());
        inv.setItem(13, info.build());
        p.openInventory(inv);
    }
    
    public static boolean isDouble(final String s) {
        try {
            Double.parseDouble(s);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }
}
