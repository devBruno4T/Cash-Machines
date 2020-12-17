package com.redemagic.cashmachine.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.redemagic.cashmachine.Main;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.Player;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Constructor;
import com.google.common.io.BaseEncoding;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import org.bukkit.inventory.ItemStack;
import java.util.Iterator;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.ChatColor;

public class Util
{
    private static String[] charlist;
    
    static {
        Util.charlist = new String[] { "K", "M", "B", "T", "Q", "QQ", "S", "SS", "OC", "N", "D", "UN", "DD", "TR", "QT", "QN", "SD", "SSD", "OD", "ND", "VG", "UVG", "DVG", "TVG", "QVG", "QVN", "SEV", "SPV", "OVG", "NVG", "TG" };
    }
    
    public Util get() {
        return this;
    }
    
    public String color(final String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
    
    public String cor(final String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
    
    public String serialize(final Location l) {
        return (String.valueOf(l.getWorld().getName()) + "," + l.getX() + "," + l.getY() + "," + l.getZ() + "," + l.getYaw() + "," + l.getPitch()).replace(".", "/");
    }
    
    public Location deserialise(final String l) {
        try {
            final String[] L = l.replace("/", ".").split(",");
            return new Location(Bukkit.getWorld(L[0]), Double.parseDouble(L[1]), Double.parseDouble(L[2]), Double.parseDouble(L[3]), Float.parseFloat(L[4]), Float.parseFloat(L[5]));
        }
        catch (Exception e) {
            return null;
        }
    }
    
    public void message(final CommandSender sender, final String... txt) {
        for (final String x : txt) {
            sender.sendMessage(this.color(x));
        }
    }
    
    public void message(final CommandSender sender, final List<String> txt) {
        for (final String x : txt) {
            sender.sendMessage(this.color(x));
        }
    }
    
    public static String serializeItem(final ItemStack itemStack) {
        if (itemStack == null) {
            return null;
        }
        ByteArrayOutputStream outputStream = null;
        try {
            final Class<?> nbtTagCompoundClass = getNMSClass("NBTTagCompound");
            final Constructor<?> nbtTagCompoundConstructor = nbtTagCompoundClass.getConstructor((Class<?>[])new Class[0]);
            final Object nbtTagCompound = nbtTagCompoundConstructor.newInstance(new Object[0]);
            final Object nmsItemStack = getObfuscationClass("inventory.CraftItemStack").getMethod("asNMSCopy", ItemStack.class).invoke(null, itemStack);
            getNMSClass("ItemStack").getMethod("save", nbtTagCompoundClass).invoke(nmsItemStack, nbtTagCompound);
            outputStream = new ByteArrayOutputStream();
            getNMSClass("NBTCompressedStreamTools").getMethod("a", nbtTagCompoundClass, OutputStream.class).invoke(null, nbtTagCompound, outputStream);
        }
        catch (Exception ex) {}
        return BaseEncoding.base64().encode(outputStream.toByteArray());
    }
    
    public static ItemStack deserializeItem(final String itemStackString) {
        if (itemStackString.equals(null)) {
            return null;
        }
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(BaseEncoding.base64().decode((CharSequence)itemStackString));
        final Class<?> nbtTagCompoundClass = getNMSClass("NBTTagCompound");
        final Class<?> nmsItemStackClass = getNMSClass("ItemStack");
        Object nbtTagCompound = null;
        ItemStack itemStack = null;
        try {
            nbtTagCompound = getNMSClass("NBTCompressedStreamTools").getMethod("a", InputStream.class).invoke(null, inputStream);
            final Object craftItemStack = nmsItemStackClass.getMethod("createStack", nbtTagCompoundClass).invoke(null, nbtTagCompound);
            itemStack = (ItemStack)getObfuscationClass("inventory.CraftItemStack").getMethod("asBukkitCopy", nmsItemStackClass).invoke(null, craftItemStack);
        }
        catch (Exception ex) {}
        return itemStack;
    }
    
    private static Class<?> getNMSClass(final String className) {
        final String packageName = Bukkit.getServer().getClass().getPackage().getName();
        final String version = packageName.replace(".", ",").split(",")[3];
        final String classLocation = "net.minecraft.server." + version + "." + className;
        Class<?> nmsClass = null;
        try {
            nmsClass = Class.forName(classLocation);
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return nmsClass;
    }
    
    private static Class<?> getObfuscationClass(final String className) {
        final String packageName = Bukkit.getServer().getClass().getPackage().getName();
        final String version = packageName.replace(".", ",").split(",")[3];
        final String classLocation = "org.bukkit.craftbukkit." + version + "." + className;
        Class<?> nmsClass = null;
        try {
            nmsClass = Class.forName(classLocation);
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return nmsClass;
    }
    
    public static String formatVirgula(final int valor) {
        String formated = "";
        final DecimalFormat decimalFormat = new DecimalFormat("###,###,###,###,###,###,###,###,###", new DecimalFormatSymbols(Locale.GERMAN));
        formated = String.valueOf(formated) + decimalFormat.format(valor);
        return formated;
    }
    
    public static String formatVirgula(final double valor) {
        String formated = "";
        final DecimalFormat decimalFormat = new DecimalFormat("###,###,###,###,###,###,###,###,###", new DecimalFormatSymbols(Locale.GERMAN));
        formated = String.valueOf(formated) + decimalFormat.format(valor);
        return formated;
    }
    
    public static String format(final double d) {
        final DecimalFormat df = new DecimalFormat("###.##");
        if (d < 1000.0) {
            return df.format(d);
        }
        if (d < 1000000.0) {
            return String.valueOf(df.format(d / 1000.0)) + Util.charlist[0];
        }
        if (d < 1.0E9) {
            return String.valueOf(df.format(d / 1000000.0)) + Util.charlist[1];
        }
        if (d < 1.0E12) {
            return String.valueOf(df.format(d / 1.0E9)) + Util.charlist[2];
        }
        if (d < 1.0E15) {
            return String.valueOf(df.format(d / 1.0E12)) + Util.charlist[3];
        }
        if (d < 1.0E18) {
            return String.valueOf(df.format(d / 1.0E15)) + Util.charlist[4];
        }
        if (d < 1.0E21) {
            return String.valueOf(df.format(d / 1.0E18)) + Util.charlist[5];
        }
        if (d < 1.0E24) {
            return String.valueOf(df.format(d / 1.0E21)) + Util.charlist[6];
        }
        if (d < 1.0E27) {
            return String.valueOf(df.format(d / 1.0E24)) + Util.charlist[7];
        }
        if (d < 1.0E30) {
            return String.valueOf(df.format(d / 1.0E27)) + Util.charlist[8];
        }
        if (d < 1.0E33) {
            return String.valueOf(df.format(d / 1.0E30)) + Util.charlist[9];
        }
        if (d < 1.0E36) {
            return String.valueOf(df.format(d / 1.0E33)) + Util.charlist[10];
        }
        if (d < 1.0E39) {
            return String.valueOf(df.format(d / 1.0E36)) + Util.charlist[11];
        }
        if (d < 1.0E42) {
            return String.valueOf(df.format(d / 1.0E39)) + Util.charlist[12];
        }
        if (d < 1.0E45) {
            return String.valueOf(df.format(d / 1.0E42)) + Util.charlist[13];
        }
        if (d < 1.0E48) {
            return String.valueOf(df.format(d / 1.0E45)) + Util.charlist[14];
        }
        if (d < 1.0E51) {
            return String.valueOf(df.format(d / 1.0E48)) + Util.charlist[15];
        }
        if (d < 1.0E54) {
            return String.valueOf(df.format(d / 1.0E51)) + Util.charlist[16];
        }
        if (d < 1.0E57) {
            return String.valueOf(df.format(d / 1.0E54)) + Util.charlist[17];
        }
        if (d < 1.0E60) {
            return String.valueOf(df.format(d / 1.0E57)) + Util.charlist[18];
        }
        if (d < 1.0E63) {
            return String.valueOf(df.format(d / 1.0E60)) + Util.charlist[19];
        }
        if (d < 1.0E66) {
            return String.valueOf(df.format(d / 1.0E63)) + Util.charlist[20];
        }
        if (d < 1.0E69) {
            return String.valueOf(df.format(d / 1.0E66)) + Util.charlist[21];
        }
        if (d < 1.0E72) {
            return String.valueOf(df.format(d / 1.0E69)) + Util.charlist[22];
        }
        if (d < 1.0E75) {
            return String.valueOf(df.format(d / 1.0E72)) + Util.charlist[23];
        }
        if (d < 1.0E78) {
            return String.valueOf(df.format(d / 1.0E75)) + Util.charlist[24];
        }
        if (d < 1.0E81) {
            return String.valueOf(df.format(d / 1.0E78)) + Util.charlist[25];
        }
        if (d < 1.0E84) {
            return String.valueOf(df.format(d / 1.0E81)) + Util.charlist[26];
        }
        if (d < 1.0E87) {
            return String.valueOf(df.format(d / 1.0E84)) + Util.charlist[27];
        }
        if (d < 1.0E90) {
            return String.valueOf(df.format(d / 1.0E87)) + Util.charlist[28];
        }
        if (d < 1.0E93) {
            return String.valueOf(df.format(d / 1.0E90)) + Util.charlist[29];
        }
        if (d < 1.0E96) {
            return String.valueOf(df.format(d / 1.0E93)) + Util.charlist[30];
        }
        return String.valueOf(df.format(d / 1.0E96)) + Util.charlist[31];
    }
    
    public static void performCommand(final Player player, final String command, final boolean console) {
        new BukkitRunnable() {
            public void run() {
                if (!console) {
                    player.chat("/" + command);
                }
                else {
                    Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), command);
                }
            }
        }.runTaskLater(Main.pl, 1L);
    }
    
    public static String DateFormat(final Date d) {
        final SimpleDateFormat s = new SimpleDateFormat("hh:mm aaa (dd/MMM/yyyy)");
        return s.format(d);
    }
    
    public String DataReminingFormat(final Date d) {
        final Date vencimento = d;
        final Date atual = new Date();
        final long variacao = atual.getTime() - vencimento.getTime();
        final long varsegundos = variacao / 1000L % 60L;
        final long varminutos = variacao / 60000L % 60L;
        final long varhoras = variacao / 3600000L % 24L;
        final String segundos = String.valueOf(varsegundos).replaceAll("-", "");
        final String minutos = String.valueOf(varminutos).replaceAll("-", "");
        final String horas = String.valueOf(varhoras).replaceAll("-", "");
        if (horas.equals("0") && minutos.equals("0")) {
            return this.color("&f" + segundos);
        }
        if (horas.equals("0")) {
            return this.color("&f" + minutos + "&7:&f" + segundos);
        }
        return this.color(String.valueOf(horas) + "&7:&f" + minutos + "&7:&f" + segundos);
    }
}
