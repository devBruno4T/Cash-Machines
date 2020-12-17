package com.redemagic.cashmachine.commands;

import com.redemagic.cashmachine.utils.MakeItem;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import com.redemagic.cashmachine.utils.TitleAPI;
import org.bukkit.ChatColor;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import com.redemagic.cashmachine.constructors.MachineAPI;
import com.redemagic.cashmachine.Main;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import com.redemagic.cashmachine.utils.Util;

import dev.mateusamaral720.furioncash.Metodos;

import org.bukkit.event.Listener;
import org.bukkit.command.CommandExecutor;

public class CashMachine implements CommandExecutor, Listener
{
    static Util u;
    
    static {
        CashMachine.u = new Util();
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!sender.hasPermission("magicmachine.admin")) {
        	CashMachine.u.message(sender, "");
            CashMachine.u.message(sender, "§c§lOPS!");
            CashMachine.u.message(sender, "");
            CashMachine.u.message(sender, "§cVocê precisa ser Gerente ou superior para acessar.");
            CashMachine.u.message(sender, "");
            return true;
        }
        if (args.length == 0) {
            CashMachine.u.message(sender, "", "&c/" + label + " give (nickname) (amount) (level) ", "");
            return true;
        }
        if (args[0].equalsIgnoreCase("cheque")) {
            if (args.length < 3) {
                CashMachine.u.message(sender, "", "&c/" + label + " cheque (nickname) (amount)", "");
                return true;
            }
            final String nickname = args[1];
            final Player p = Bukkit.getPlayerExact(nickname);
            if (p == null) {
            	CashMachine.u.message(sender, "");
                CashMachine.u.message(sender, "§c§lERRO");
                CashMachine.u.message(sender, "");
                CashMachine.u.message(sender, "§cJogador não encontrado.");
                CashMachine.u.message(sender, "");
                return true;
            }
            if (!p.isOnline()) {
            	CashMachine.u.message(sender, "");
                CashMachine.u.message(sender, "§c§lERRO");
                CashMachine.u.message(sender, "");
                CashMachine.u.message(sender, "§cJogador não encontrado.");
                CashMachine.u.message(sender, "");
                return true;
            }
            final Double amount = isDouble(args[2]) ? Double.parseDouble(args[2]) : 0.0;
            if (amount <= 0.0) {
                CashMachine.u.message(sender, "", "&c/" + label + " cheque (nickname) (amount)", "");
                return true;
            }
            if (p.getInventory().firstEmpty() == -1) {
            	CashMachine.u.message(sender, "");
                CashMachine.u.message(sender, "§c§lINVENTÁRIO CHEIO!");
                CashMachine.u.message(sender, "");
                CashMachine.u.message(sender, "&cJogador " + p.getName() + " n\u00e3o recebeu seu cheque de cash, pois seu invent\u00e1rio estava cheio.");
                CashMachine.u.message(sender, "");
                return true;
            }
            CashMachine.u.message(sender, "");
            CashMachine.u.message(sender, "§6§lCASH");
            CashMachine.u.message(sender, "");
            CashMachine.u.message(sender, "&aEntrege cheque de cash " + amount + " para " + p.getName() + " com sucesso.");
            CashMachine.u.message(sender, "");
            p.getInventory().addItem(new ItemStack[] { getItem(amount) });
            return true;
        }
        else {
            if (!args[0].equalsIgnoreCase("give")) {
                CashMachine.u.message(sender, "", "&c/" + label + " give (nickname) (id) (amount)", "");
                return false;
            }
            if (args.length < 4) {
                CashMachine.u.message(sender, "", "&c/" + label + " give (nickname) (amount) (level)", "");
                return true;
            }
            final String nickname = args[1];
            final Player p = Bukkit.getPlayerExact(nickname);
            if (p == null) {
            	CashMachine.u.message(sender, "");
                CashMachine.u.message(sender, "§c§lERRO");
                CashMachine.u.message(sender, "");
                CashMachine.u.message(sender, "§cJogador não encontrado.");
                CashMachine.u.message(sender, "");
                return true;
            }
            if (!p.isOnline()) {
            	CashMachine.u.message(sender, "");
                CashMachine.u.message(sender, "§c§lERRO");
                CashMachine.u.message(sender, "");
                CashMachine.u.message(sender, "§cJogador não encontrado.");
                CashMachine.u.message(sender, "");
                return true;
            }
            final Double amount = isDouble(args[2]) ? Double.parseDouble(args[2]) : 0.0;
            if (amount <= 0.0) {
                CashMachine.u.message(sender, "", "&c/" + label + " give (nickname) (amount) (level)", "");
                return true;
            }
            final Integer level = isInteger(args[3]) ? Integer.parseInt(args[3]) : 0;
            if (level <= 0) {
                CashMachine.u.message(sender, "", "&c/" + label + " give (nickname) (amount) (level)", "");
                return true;
            }
            if (level > Main.max_level) {
            	CashMachine.u.message(sender, "");
                CashMachine.u.message(sender, "§6§lOPA!");
                CashMachine.u.message(sender, "");
                CashMachine.u.message(sender, "&eO level escolhido passa do limite atual!");
                CashMachine.u.message(sender, "");
                return true;
            }
            p.getInventory().addItem(new ItemStack[] { MachineAPI.getMachineType(amount, level) });
            CashMachine.u.message(sender, "");
            CashMachine.u.message(sender, "§2§lSUCESSO!");
            CashMachine.u.message(sender, "");
            CashMachine.u.message(sender, " §eEnviando §f" + Util.format(amount) + " §eMáquinas de Cash para o jogador §f" + p.getName());
            CashMachine.u.message(sender, "");
            if (sender instanceof Player) {
                ((Player)sender).playSound(((Player)sender).getLocation(), Sound.NOTE_PLING, 1.0f, 1.0f);
            }
            return true;
        }
    }
    
    @EventHandler
    public void onInteract(final PlayerInteractEvent e) {
        final Player p = e.getPlayer();
        final ItemStack item = p.getItemInHand();
        if (item.getType() != getItem(1.0).getType()) {
            return;
        }
        if (!item.hasItemMeta()) {
            return;
        }
        if (!item.getItemMeta().hasDisplayName()) {
            return;
        }
        if (!item.getItemMeta().getDisplayName().equals(CashMachine.u.color("&6&lCHEQUE DE CASH"))) {
            return;
        }
        e.setCancelled(true);
        final Double amount = Double.parseDouble(ChatColor.stripColor(item.getItemMeta().getLore().get(2).replace(".", "").replace("+", ""))) * p.getItemInHand().getAmount();
        CashAPI.addCash(p.getName(), amount);
        TitleAPI.sendFullTitle(p, 20, 20, 20, CashMachine.u.color("§e§l+&f" + Util.format(amount) + " §eCash"), "");
        p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1.0f, 1.0f);
        p.setItemInHand((ItemStack)null);
    }
    
    public static ItemStack getItem(final Double amount) {
        final MakeItem item = new MakeItem(Material.GOLD_INGOT).addGlow();
        item.setName("§6§lCheque de Cash");
        item.addLore("§7Cheque de §e" + Util.format(amount) + " de Cash.");
        item.addLore("");
        item.addLore("§a§l+" + Util.formatVirgula(amount));
        item.addLore("");
        return item.build();
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
    
    public static boolean isInteger(final String s) {
        try {
            Integer.parseInt(s);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }
}
