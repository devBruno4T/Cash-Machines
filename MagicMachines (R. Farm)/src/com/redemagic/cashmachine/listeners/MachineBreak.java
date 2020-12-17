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
                MachineBreak.u.message((CommandSender)p, "", "&cVoc� removeu um gerador, no modo administrador", "&cDono: " + m.getOwner(), "&cQuantidade: " + Util.format(m.getAmount()), "&cN�vel: " + m.getLevel(), "&cCash gerado: " + Util.format(m.getCash()), "");
                MachineAPI.removeMachine(m);
                return;
            }
            MachineBreak.u.message((CommandSender)p, "&cEsta m�quina pertence \u00e0 &n" + m.getOwner() + "&c. Voc� n�o pode remove-la.");
        }
        else {
            if (p.getInventory().firstEmpty() == -1) {
                MachineBreak.u.message((CommandSender)p, "&cEsvazie seu invent�rio antes de recolher uma m�quina.");
                return;
            }
            e.getBlock().breakNaturally((ItemStack)null);
            p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1.0f, 1.0f);
            p.playSound(p.getLocation(), Sound.NOTE_BASS, 1.0f, 1.0f);
            TitleAPI.sendFullTitle(p, 20, 20, 20, MachineBreak.u.color("&c&lOPA"), MachineBreak.u.color("&cVoc� removeu �e" + Util.format(m.getAmount()) + " �cM�quinas de cash"));
            p.getInventory().addItem(new ItemStack[] { MachineAPI.getMachineType(m.getAmount(), m.getLevel()) });
            MachineAPI.removeMachine(m);
            MachineStart.remove(m);
        }
    }
}
