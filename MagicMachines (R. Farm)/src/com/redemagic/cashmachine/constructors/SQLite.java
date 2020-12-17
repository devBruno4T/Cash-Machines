package com.redemagic.cashmachine.constructors;

import java.sql.ResultSet;
import java.sql.PreparedStatement;
import com.redemagic.cashmachine.utils.Util;
import java.sql.SQLException;
import java.sql.Statement;
import com.redemagic.cashmachine.Main;
import org.bukkit.Bukkit;
import java.sql.DriverManager;
import org.bukkit.Location;
import java.util.HashMap;
import java.sql.Connection;

public class SQLite
{
    public Connection connection;
    public HashMap<Location, Machine> cache;
    
    public SQLite() {
        this.cache = new HashMap<Location, Machine>();
        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:plugins/MagicMachines/machines.db");
            final Statement stm = this.connection.createStatement();
            stm.execute("CREATE TABLE IF NOT EXISTS machines (location TXT, owner TXT, level INTEGER, amount BIGINT, cash BIGINT);");
            stm.close();
            Bukkit.getConsoleSender().sendMessage("[ " + Main.pl.getName() + "] Conex\u00e3o com o SQLite executada com sucesso.");
        }
        catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("[ " + Main.pl.getName() + "] N\u00e3o foi poss\u00edvel estabelecer conex\u00e3o com o SQLite.");
            Bukkit.getPluginManager().disablePlugin(Main.pl);
            e.printStackTrace();
        }
    }
    
    public Connection getConnection() throws SQLException {
        if (this.connection.isClosed()) {
            this.connection = DriverManager.getConnection("jdbc:sqlite:plugins/MagicMachines/machines.db");
        }
        return this.connection;
    }
    
    public void insertTable(final Machine machine) {
        PreparedStatement stm = null;
        try {
            final Connection connection = this.getConnection();
            stm = connection.prepareStatement("INSERT INTO `machines` (`location`, `owner`, `level`, `amount`, `cash`) VALUES (?,?,?,?,?)");
            stm.setString(1, new Util().serialize(machine.getLoc()));
            stm.setString(2, machine.getOwner());
            stm.setInt(3, machine.getLevel());
            stm.setDouble(4, machine.getAmount());
            stm.setDouble(5, machine.getCash());
            stm.executeUpdate();
            this.cache.put(machine.getLoc(), machine);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void setOwner(final Machine machine) {
        this.updateTableOwner(machine);
    }
    
    public void updateTableOwner(final Machine machine) {
        if (!this.hasRegister(machine.getLoc())) {
            this.insertTable(machine);
        }
        else {
            PreparedStatement stm = null;
            try {
                final Connection connection = this.getConnection();
                stm = connection.prepareStatement("UPDATE `machines` SET `owner` = ? WHERE `location` = ?");
                stm.setString(1, machine.getOwner());
                stm.setString(2, new Util().serialize(machine.getLoc()));
                stm.executeUpdate();
                this.cache.put(machine.getLoc(), machine);
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void setLevel(final Machine machine) {
        this.updateTableLevel(machine);
    }
    
    public void updateTableLevel(final Machine machine) {
        if (!this.hasRegister(machine.getLoc())) {
            this.insertTable(machine);
        }
        else {
            PreparedStatement stm = null;
            try {
                final Connection connection = this.getConnection();
                stm = connection.prepareStatement("UPDATE `machines` SET `level` = ? WHERE `location` = ?");
                stm.setInt(1, machine.getLevel());
                stm.setString(2, new Util().serialize(machine.getLoc()));
                stm.executeUpdate();
                this.cache.put(machine.getLoc(), machine);
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void setAmount(final Machine machine) {
        this.updateTableAmount(machine);
    }
    
    public void updateTableAmount(final Machine machine) {
        if (!this.hasRegister(machine.getLoc())) {
            this.insertTable(machine);
        }
        else {
            PreparedStatement stm = null;
            try {
                final Connection connection = this.getConnection();
                stm = connection.prepareStatement("UPDATE `machines` SET `amount` = ? WHERE `location` = ?");
                stm.setDouble(1, machine.getAmount());
                stm.setString(2, new Util().serialize(machine.getLoc()));
                stm.executeUpdate();
                this.cache.put(machine.getLoc(), machine);
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void setCash(final Machine machine) {
        this.updateTableCash(machine);
    }
    
    public void updateTableCash(final Machine machine) {
        if (!this.hasRegister(machine.getLoc())) {
            this.insertTable(machine);
        }
        else {
            PreparedStatement stm = null;
            try {
                final Connection connection = this.getConnection();
                stm = connection.prepareStatement("UPDATE `machines` SET `cash` = ? WHERE `location` = ?");
                stm.setDouble(1, machine.getCash());
                stm.setString(2, new Util().serialize(machine.getLoc()));
                stm.executeUpdate();
                this.cache.put(machine.getLoc(), machine);
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void remove(final Location loc) {
        if (!this.hasRegister(loc)) {
            return;
        }
        PreparedStatement stm = null;
        try {
            final Connection connection = this.getConnection();
            stm = connection.prepareStatement("DELETE FROM `machines` WHERE `location` = ?");
            stm.setString(1, new Util().serialize(loc));
            stm.executeUpdate();
            MachineStart.remove(this.getMachine(loc));
            this.cache.remove(loc);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void loadDataBase() {
        try {
            final Connection connection = this.getConnection();
            final Statement stm = connection.createStatement();
            final ResultSet rs = stm.executeQuery("SELECT * FROM machines;");
            int i = 0;
            while (rs.next()) {
                final Location loc = new Util().deserialise(rs.getString("location"));
                final String owner = rs.getString("owner");
                final Integer level = rs.getInt("level");
                final Double amount = rs.getDouble("amount");
                final Double cash = rs.getDouble("cash");
                final Machine m = new Machine(loc, owner, amount, cash, level);
                this.cache.put(loc, m);
                MachineStart.Start(m);
                ++i;
            }
            rs.close();
            stm.close();
            Bukkit.getConsoleSender().sendMessage("[ " + Main.pl.getName() + "] Foram carregados " + i + " arquivos de m\u00e1quinas.");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public boolean hasRegister(final Location machine_location) {
        return this.cache.containsKey(machine_location);
    }
    
    public Machine getMachine(final Location machine_location) {
        return this.cache.get(machine_location);
    }
}
