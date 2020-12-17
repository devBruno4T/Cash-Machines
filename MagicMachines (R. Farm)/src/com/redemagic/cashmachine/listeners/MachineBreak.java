package com.redemagic.cashmachine.listeners;

import org.bukkit.event.EventHandler;
import com.redemagic.cashmachine.constructors.Machine;
import org.bukkit.entity.Player;
import com.redemagic.cashmachine.constructors.MachineStart;
import com.redemagic.cashmachine.utils.TitleAPI;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.command.CommandSender;
import org.bukkit.GameMode;
import com.redemagic.cashmachine.constructors.MachineAPI;
import org.bukkit.event.block.BlockBreakEvent;
import com.redemagic.cashmachine.utils.Util;
import org.bukkit.event.Listener;

public class MachineBreak implements Listener
{
    static Util u;
    
    static {
        MachineBreak.u = new Util();
    }
    
    @EventHandler
    public void onBreak(final BlockBreakEvent e) {
        final Player p = e.getPlayer();
        if (e.isCancelled()) {
            return;
        }
        if (e.getBlock().getType() != MachineAPI.item.getType()) {
            return;
        }
        if (!MachineAPI.existsMachine(e.getBlock().getLocation())) {
            return;
        }
        e.setCancelled(true);
        final Machine m = MachineAPI.getMachine(e.getBlock().getLocation());
        if (!m.getOwner().toLowerCase().equals(p.getName().toLowerCase())) {
            if (p.getGameMode().equals((Object)GameMode.CREATIVE) && p.hasPermission("magicmachine.admin") && p.isSneaking()) {
                e.setCancelled(false);
                MachineBreak.u.message((CommandSender)p, "", "&cVocê removeu um gerador, no modo administrador", "&cDono: " + m.getOwner(), "&cQuantidade: " + Util.format(m.getAmount()), "&cNível: " + m.getLevel(), "&cCash gerado: " + Util.format(m.getCash()), "");
                MachineAPI.removeMachine(m);
                return;
            }
            MachineBreak.u.message((CommandSender)p, "&cEsta máquina pertence \u00e0 &n" + m.getOwner() + "&c. Você não pode remove-la.");
        }
        else {
            if (p.getInventory().firstEmpty() == -1) {
                MachineBreak.u.message((CommandSender)p, "&cEsvazie seu inventário antes de recolher uma máquina.");
                return;
            }
            e.getBlock().breakNaturally((ItemStack)null);
            p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1.0f, 1.0f);
            p.playSound(p.getLocation(), Sound.NOTE_BASS, 1.0f, 1.0f);
            TitleAPI.sendFullTitle(p, 20, 20, 20, MachineBreak.u.color("&c&lOPA"), MachineBreak.u.color("&cVocê removeu §e" + Util.format(m.getAmount()) + " §cMáquinas de cash"));
            p.getInventory().addItem(new ItemStack[] { MachineAPI.getMachineType(m.getAmount(), m.getLevel()) });
            MachineAPI.removeMachine(m);
            MachineStart.remove(m);
        }
    }
}
