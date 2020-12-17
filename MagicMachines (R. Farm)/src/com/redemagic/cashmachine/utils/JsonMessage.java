package com.redemagic.cashmachine.utils;

import java.util.Locale;
import org.bukkit.ChatColor;
import org.json.simple.JSONObject;
import java.lang.reflect.InvocationTargetException;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;

public class JsonMessage
{
    private String msg;
    
    public JsonMessage() {
        this.msg = "[{\"text\":\"\",\"extra\":[{\"text\": \"\"}";
    }
    
    private static Class<?> getNmsClass(final String nmsClassName) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + getServerVersion() + "." + nmsClassName);
    }
    
    private static String getServerVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().substring(23);
    }
    
    public void send() {
        this.send((Player[])Bukkit.getOnlinePlayers().toArray(new Player[Bukkit.getOnlinePlayers().size()]));
    }
    
    public void send(final Player... player) {
        sendRawJson(String.valueOf(this.msg) + "]}]", player);
    }
    
    public static void sendRawJson(final String json, final Player... player) {
        final String nmsClass = String.valueOf(getServerVersion().startsWith("v1_7_R") ? "" : "IChatBaseComponent$") + "ChatSerializer";
        for (final Player p : player) {
            try {
                final Object comp = getNmsClass(nmsClass).getMethod("a", String.class).invoke(null, json);
                final Object packet = getNmsClass("PacketPlayOutChat").getConstructor(getNmsClass("IChatBaseComponent")).newInstance(comp);
                final Object handle = p.getClass().getMethod("getHandle", (Class<?>[])new Class[0]).invoke(p, new Object[0]);
                final Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
                playerConnection.getClass().getMethod("sendPacket", getNmsClass("Packet")).invoke(playerConnection, packet);
            }
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException | NoSuchFieldException | InstantiationException ex2) {
                final Exception ex = null;
                final Exception e = ex;
                e.printStackTrace();
            }
        }
    }
    
    public JsonStringBuilder append(final String text) {
        return new JsonStringBuilder(this, esc(text), null);
    }
    
    private static String esc(final String s) {
        return JSONObject.escape(s);
    }
    
    static /* synthetic */ void access$3(final JsonMessage jsonMessage, final String msg) {
        jsonMessage.msg = msg;
    }
    
    public static class JsonStringBuilder
    {
        private final JsonMessage message;
        private final String string = ",{\"text\":\"\",\"extra\":[";
        private final String[] strings;
        private String hover;
        private String click;
        
        private JsonStringBuilder(final JsonMessage jsonMessage, final String text, Object object) {
            this.hover = "";
            this.click = "";
            this.message = jsonMessage;
            final String[] colors = text.split(String.valueOf('§'));
            for (int i = 0; i < colors.length; ++i) {
                if (i == 0 && !text.startsWith(String.valueOf('§'))) {
                    colors[i] = "{\"text\":\"" + colors[i] + "\"}";
                }
                else if (colors[i].length() < 1) {
                    colors[i] = "{\"text\":\"\"}";
                }
                else {
                    final ChatColor color = ChatColor.getByChar(colors[i].substring(0, 1));
                    colors[i] = "{\"text\":\"" + colors[i].substring(1, colors[i].length()) + "\",\"color\":\"" + color.name().toLowerCase(Locale.US) + "\"}";
                }
                if (i + 1 != colors.length) {
                    colors[i] = String.valueOf(colors[i]) + ",";
                }
            }
            this.strings = colors;
        }
        
        public JsonStringBuilder setHoverAsTooltip(final String... lore) {
            final StringBuilder builder = new StringBuilder();
            for (int i = 0; i < lore.length; ++i) {
                if (i + 1 == lore.length) {
                    builder.append(lore[i]);
                }
                else {
                    builder.append(String.valueOf(lore[i]) + "\n");
                }
            }
            this.hover = ",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + esc(builder.toString()) + "\"}";
            return this;
        }
        
        @Deprecated
        public JsonStringBuilder setHoverAsAchievement(final String ach) {
            if (getServerVersion().startsWith("v1_12")) {
                this.hover = ",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + esc(ach) + "\"}";
            }
            else {
                this.hover = ",\"hoverEvent\":{\"action\":\"show_achievement\",\"value\":\"achievement." + esc(ach) + "\"}";
            }
            return this;
        }
        
        public JsonStringBuilder setClickAsURL(final String link) {
            this.click = ",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + esc(link) + "\"}";
            return this;
        }
        
        public JsonStringBuilder setClickAsSuggestCmd(final String cmd) {
            this.click = ",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"" + esc(cmd) + "\"}";
            return this;
        }
        
        public JsonStringBuilder setClickAsExecuteCmd(final String cmd) {
            this.click = ",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"" + esc(cmd) + "\"}";
            return this;
        }
        
        public JsonMessage save() {
            final StringBuilder builder = new StringBuilder(String.valueOf(this.message.msg) + ",{\"text\":\"\",\"extra\":[");
            String[] strings;
            for (int length = (strings = this.strings).length, i = 0; i < length; ++i) {
                final String string = strings[i];
                builder.append(string);
            }
            builder.append("]" + this.hover + this.click + "}");
            JsonMessage.access$3(this.message, builder.toString());
            return this.message;
        }
    }
}
