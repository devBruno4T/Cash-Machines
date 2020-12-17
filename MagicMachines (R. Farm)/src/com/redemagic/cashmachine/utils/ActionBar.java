package com.redemagic.cashmachine.utils;

import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import org.bukkit.entity.Player;

public final class ActionBar
{
    public static void sendActionText(final Player player, final String message) {
        final PacketPlayOutChat packet = new PacketPlayOutChat((IChatBaseComponent)new ChatComponentText(new Util().color(message)), (byte)2);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)packet);
    }
}
