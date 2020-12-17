package com.redemagic.cashmachine.constructors;

import org.bukkit.Location;

public class Machine
{
    private Location loc;
    private String owner;
    private Double amount;
    private Double cash;
    private Integer level;
    
    public Machine(final Location loc, final String owner, final Double amount, final Double cash, final Integer level) {
        this.setLoc(loc);
        this.setOwner(owner);
        this.setAmount(amount);
        this.setCash(cash);
        this.setLevel(level);
    }
    
    public Location getLoc() {
        return this.loc;
    }
    
    public void setLoc(final Location loc) {
        this.loc = loc;
    }
    
    public String getOwner() {
        return this.owner;
    }
    
    public void setOwner(final String owner) {
        this.owner = owner;
    }
    
    public Double getAmount() {
        return this.amount;
    }
    
    public void setAmount(final Double amount) {
        this.amount = amount;
    }
    
    public Double getCash() {
        return this.cash;
    }
    
    public void setCash(final Double cash) {
        this.cash = cash;
    }
    
    public Integer getLevel() {
        return this.level;
    }
    
    public void setLevel(final Integer level) {
        this.level = level;
    }
}
