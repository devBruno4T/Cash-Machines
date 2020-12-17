package com.redemagic.cashmachine.constructors;

import com.redemagic.cashmachine.Main;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.ArrayList;

public class MachineStart
{
    public static ArrayList<Machine> starts;
    
    static {
        MachineStart.starts = new ArrayList<Machine>();
    }
    
    public static void Start(final Machine m) {
        if (MachineStart.starts.contains(m)) {
            return;
        }
        MachineStart.starts.add(m);
        new BukkitRunnable() {
            int i = 0;
            
            public void run() {
                ++this.i;
                if (!MachineAPI.existsMachine(m.getLoc())) {
                    this.cancel();
                    MachineStart.starts.remove(m);
                    return;
                }
                if (!MachineStart.starts.contains(m)) {
                    this.cancel();
                    return;
                }
                if (this.i >= 60) {
                    this.i = 0;
                    MachineAPI.setCash(m, MachineAPI.getCash(m) + MachineAPI.getAmount(m) * MachineAPI.getCashAmountForLevel(m.getLevel()));
                }
            }
        }.runTaskTimerAsynchronously(Main.pl, 1L, 20L);
    }
    
    public static boolean isStart(final Machine m) {
        return MachineStart.starts.contains(m);
    }
    
    public static void remove(final Machine m) {
        MachineStart.starts.remove(m);
    }
}
