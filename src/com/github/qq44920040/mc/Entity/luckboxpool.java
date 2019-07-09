package com.github.qq44920040.mc.Entity;

import org.bukkit.inventory.ItemStack;

public class luckboxpool {
    private String poolid;
    private int chance;
    private int luck;
    private ItemStack item;

    public luckboxpool(String poolid, int chance, int luck, ItemStack item) {
        this.poolid = poolid;
        this.chance = chance;
        this.luck = luck;
        this.item = item;
    }

    public String getPoolid() {
        return poolid;
    }

    public void setPoolid(String poolid) {
        this.poolid = poolid;
    }

    public int getChance() {
        return chance;
    }

    public void setChance(int chance) {
        this.chance = chance;
    }

    public int getLuck() {
        return luck;
    }

    public void setLuck(int luck) {
        this.luck = luck;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return "luckboxpool{" +
                "poolid='" + poolid + '\'' +
                ", chance=" + chance +
                ", luck=" + luck +
                ", item=" + item +
                '}';
    }
}
