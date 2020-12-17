package com.redemagic.cashmachine.listeners;

import java.util.Iterator;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import com.redemagic.cashmachine.constructors.MachineStart;
import org.bukkit.inventory.ItemStack;
import com.redemagic.cashmachine.utils.TitleAPI;
import org.bukkit.Sound;
import org.bukkit.event.block.BlockPlaceEvent;
import com.redemagic.cashmachine.constructors.MachineAPI;
import com.redemagic.cashmachine.constructors.Machine;
import org.bukkit.block.Block;
import com.redemagic.cashmachine.utils.Util;
import org.bukkit.event.Listener;

public class MachinePlace implements Listener
{
    static Util u;
    
    static {
        MachinePlace.u = new Util();
    }
    
    public Machine getNearbyMachine(final Block start, final int radius, final String owner, final double amount, final int level) {
        for (int x = -radius; x <= radius; ++x) {
            for (int y = -radius; y <= radius; ++y) {
                for (int z = -radius; z <= radius; ++z) {
                    if (x != 0 || y != 0 || z != 0) {
                        final Block block = start.getRelative(x, y, z);
                        if (block.getType() == MachineAPI.item.getType()) {
                            if (MachineAPI.existsMachine(block.getLocation())) {
                                final Machine m = MachineAPI.getMachine(block.getLocation());
                                if (m.getLevel() == level) {
                                    if (m.getOwner().equals(owner)) {
                                        return m;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
    
    @EventHandler
    public void onPlace(final BlockPlaceEvent e) {
        final Player p = e.getPlayer();
        final ItemStack item = p.getItemInHand();
        if (e.isCancelled()) {
            return;
        }
        if (item.getTypeId() != MachineAPI.item.getTypeId()) {
            return;
        }
        if (!item.getItemMeta().hasDisplayName()) {
            return;
        }
        if (!item.getItemMeta().getDisplayName().equals(MachineAPI.item.getItemMeta().getDisplayName())) {
            return;
        }
        try {
            e.setCancelled(true);
            final double qnt = getMachineAmount(item) * p.getItemInHand().getAmount();
            final int level = getMachineLevel(item);
            final Machine m = new Machine(e.getBlockPlaced().getLocation(), p.getName(), qnt, 0.0, level);
            p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
            TitleAPI.sendFullTitle(p, 20, 10, 20, MachinePlace.u.color("&6&lMÁQUINA DE CASH"), MachinePlace.u.color("&eForam colocada(s) §f" + Util.format(qnt) + " §eMáquina(s) de Cash"));
            final Machine nearby = this.getNearbyMachine(e.getBlockPlaced(), 10, p.getName(), m.getAmount(), m.getLevel());
            if (nearby != null) {
                MachineAPI.setAmount(nearby, nearby.getAmount() + qnt);
                p.setItemInHand((ItemStack)null);
                return;
            }
            e.setCancelled(false);
            p.setItemInHand((ItemStack)null);
            MachineAPI.createMachine(m);
            MachineStart.Start(m);
        }
        catch (Exception e2) {
            e.setCancelled(true);
            MachinePlace.u.message((CommandSender)p, "&cNão foi possivel adicionar sua maquina de cash, tente novamente!");
        }
    }
    
    public static Double getMachineAmount(final ItemStack i) {
        Double gerador = 1.0;
        if (i.getTypeId() == MachineAPI.item.getTypeId() && i.getItemMeta().hasDisplayName() && i.getItemMeta().getDisplayName().contains(MachineAPI.item.getItemMeta().getDisplayName()) && i.getItemMeta().hasLore()) {
            for (final String l : i.getItemMeta().getLore()) {
                if (l.contains("Quantidade")) {
                    gerador = Double.parseDouble(l.split(MachinePlace.u.color("Quantidade: &f"))[1].replace(".", ""));
                }
            }
        }
        return gerador;
    }
    
    public static Integer getMachineLevel(final ItemStack i) {
        Integer gerador = 1;
        if (i.getTypeId() == MachineAPI.item.getTypeId() && i.getItemMeta().hasDisplayName() && i.getItemMeta().getDisplayName().contains(MachineAPI.item.getItemMeta().getDisplayName()) && i.getItemMeta().hasLore()) {
            for (final String l : i.getItemMeta().getLore()) {
                if (l.contains("N\u00edvel")) {
                    gerador = Integer.parseInt(l.split(MachinePlace.u.color("N\u00edvel: &f"))[1].replace(".", ""));
                }
            }
        }
        return gerador;
    }
}
