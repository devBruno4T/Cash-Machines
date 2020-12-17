package com.redemagic.cashmachine.utils;

import java.util.Collection;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtils
{
    private static final String version;
    private static Method getHandle;
    private static Method sendPacket;
    private static Field playerConnectionField;
    
    static {
        version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }
    
    public static void loadUtils() {
        try {
            ReflectionUtils.getHandle = getOBClass("entity.CraftPlayer").getMethod("getHandle", (Class<?>[])new Class[0]);
            ReflectionUtils.playerConnectionField = getNMSClass("EntityPlayer").getField("playerConnection");
            ReflectionUtils.sendPacket = getNMSClass("PlayerConnection").getMethod("sendPacket", getNMSClass("Packet"));
        }
        catch (Throwable t) {}
    }
    
    public static Class<?> getNMSClass(final String name) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + ReflectionUtils.version + "." + name);
    }
    
    public static Class<?> getOBClass(final String name) throws ClassNotFoundException {
        return Class.forName("org.bukkit.craftbukkit." + ReflectionUtils.version + "." + name);
    }
    
    public static void sendPacket(final Player player, final Object packet) {
        try {
            final Object entityPlayer = ReflectionUtils.getHandle.invoke(player, new Object[0]);
            final Object playerConnection = ReflectionUtils.playerConnectionField.get(entityPlayer);
            ReflectionUtils.sendPacket.invoke(playerConnection, packet);
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }
    
    public static List<Class<?>> getProjectClasses(final String pckg) {
        try {
            final List<Class<?>> classes = new ArrayList<Class<?>>();
            final File directory = new File(Thread.currentThread().getContextClassLoader().getResource(pckg).getFile());
            File[] listFiles;
            for (int length = (listFiles = directory.listFiles()).length, i = 0; i < length; ++i) {
                final File file = listFiles[i];
                if (file.getName().endsWith(".class")) {
                    classes.add(Class.forName(String.valueOf(pckg.replace('/', '.')) + '.' + file.getName().replace(".class", "")));
                }
                else if (file.isDirectory()) {
                    classes.addAll(getProjectClasses(String.valueOf(pckg) + "/" + file.getName()));
                }
            }
            return classes;
        }
        catch (Throwable e) {
            return null;
        }
    }
}
