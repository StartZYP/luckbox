package com.github.qq44920040.mc.Entity;

import org.bukkit.inventory.ItemStack;

public class luckboxpool {
    private String poolid;
    private int chance;
    private int luck;
    private ItemStack item;
    private String cmd;

    @Override
    public String toString() {
        return "luckboxpool{" +
                "poolid='" + poolid + '\'' +
                ", chance=" + chance +
                ", luck=" + luck +
                ", item=" + item +
                ", cmd='" + cmd + '\'' +
                '}';
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

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public luckboxpool(String poolid, int chance, int luck, ItemStack item, String cmd) {
        this.poolid = poolid;
        this.chance = chance;
        this.luck = luck;
        this.item = item;
        this.cmd = cmd;
    }
}
