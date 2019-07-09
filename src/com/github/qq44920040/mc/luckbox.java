package com.github.qq44920040.mc;

import com.github.qq44920040.mc.Entity.luckboxpool;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public class luckbox extends JavaPlugin  implements Listener {
    public List<ItemStack> AchieveAwardPool = new ArrayList<>();
    public List<luckboxpool> boxpoll = new ArrayList<>();
    public String Boxhavename;
    public String Keyhavename;
    public int LuckValueMax;
    public String AchieveAwardMsg;
    public String luckboxpoolMag;
    private int MaxChance;
    private String AllMsg;
    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        File file = new File(getDataFolder(),"config.yml");
        if (!file.exists()){
            saveDefaultConfig();
        }
        Bukkit.getServer().getPluginManager().registerEvents(this,this);
        ReloadConfig();
        super.onEnable();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(label.equalsIgnoreCase("box")){
            if (args.length==1){
                if (args[0].equalsIgnoreCase("reload")&&sender.isOp()){
                    ReloadConfig();
                    sender.sendMessage("§e§l重载插件成功");
                }else if (args[0].equalsIgnoreCase("luckinfo")){
                    String luck = getConfig().getString("PlayerData." + sender.getName());
                    if (luck==null){
                        getConfig().set("PlayerData." + sender.getName(),0);
                    }else {
                        sender.sendMessage("§b玩家：§a"+sender.getName()+"的幸运值为§e"+luck);
                    }
                }
            }else if (args.length==2){
                if (args[0].equalsIgnoreCase("luckinfo")){
                    String luck = getConfig().getString("PlayerData." + args[1]);
                    if (luck==null){
                        getConfig().set("PlayerData." + args[1],0);
                    }else {
                        sender.sendMessage("§b玩家：§a"+args[1]+"的幸运值为§e"+luck);
                    }
                }
            }else if (args.length==3){
                if (args[0].equalsIgnoreCase("luckadd")&&sender.isOp()){
                    int luck = getConfig().getInt("PlayerData." + args[1]);
                    int addvalue = Integer.parseInt(args[2]);
                    getConfig().set("PlayerData." + args[1],luck+addvalue);
                    sender.sendMessage("§d§l添加幸运值成功");
                }
            }
        }
        return super.onCommand(sender, command, label, args);
    }

    @EventHandler
    public void PlayerUseEvent(PlayerInteractEvent event){
        Action action = event.getAction();
        if (action== Action.RIGHT_CLICK_AIR||action==Action.RIGHT_CLICK_BLOCK){
            Player player = event.getPlayer();
            ItemStack itemInHand = player.getItemInHand();
            if (itemInHand!=null){
                if (itemInHand.hasItemMeta()){
                    ItemMeta itemMeta = itemInHand.getItemMeta();
                    String displayName = itemMeta.getDisplayName();
                    System.out.println("进入了");
                    System.out.println(displayName+Boxhavename);
                    if (displayName.contains(Boxhavename)){
                        System.out.println("验证宝箱");
                        int a =0;
                        for (ItemStack itemStack:player.getInventory().getContents()){
                            if (itemStack!=null&&itemStack.getType()!=Material.AIR){
                                a++;
                            }
                        }
                        if (a==36){
                            player.sendMessage("§c背包已满，无法进行此操作");
                            return;
                        }
                        ItemStack item = player.getInventory().getItem(0);
                        if (item==null){
                            player.sendMessage("§d§l物品栏一号位请放置钥匙");
                            return;
                        }
                        if (!item.getItemMeta().getDisplayName().contains(Keyhavename)){
                            player.sendMessage("§d§l物品栏一号位请放置钥匙");
                            return;
                        }
                        int stacknumkey = item.getAmount();
                        int stacknumbox = itemInHand.getAmount();
                        if (stacknumbox==1){
                            player.getInventory().setItem(0,null);
                        }else {
                            item.setAmount(stacknumbox-1);
                        }
                        if (stacknumkey==1){
                            player.setItemInHand(null);
                        }else {
                            itemInHand.setAmount(stacknumkey-1);
                        }
                        player.updateInventory();
                        int luckvalue = getConfig().getInt("PlayerData." + player.getName());
                        if (luckvalue>=LuckValueMax){
                            getConfig().set("PlayerData."+player.getName(),luckvalue-LuckValueMax);
                            Random random = new Random();
                            int i = random.nextInt(AchieveAwardPool.size());
                            ItemStack itemStack = AchieveAwardPool.get(i);
                            player.getInventory().addItem(itemStack);
                            player.sendMessage(AchieveAwardMsg.replace("{name}",itemStack.getItemMeta().getDisplayName()).replace("{player}",player.getName()).split("\\|"));
                            Bukkit.getServer().getConsoleSender().sendMessage(AllMsg.replace("{name}",itemStack.getItemMeta().getDisplayName()).replace("{player}",player.getName()));
                            player.updateInventory();
                        }else {
                            Random random = new Random();
                            int i = random.nextInt(MaxChance);
                            int nowchance =0;
                            for (luckboxpool luckboxpool:boxpoll){
                                nowchance += luckboxpool.getChance();
                                if (i<=nowchance){
                                    getConfig().set("PlayerData."+player.getName(),luckvalue+luckboxpool.getLuck());
                                    player.getInventory().addItem(luckboxpool.getItem());
                                    player.sendMessage(luckboxpoolMag.replace("{name}",luckboxpool.getItem().getItemMeta().getDisplayName()).replace("{player}",player.getName()).split("\\|"));
                                    Bukkit.getServer().getConsoleSender().sendMessage(AllMsg.replace("{name}",luckboxpool.getItem().getItemMeta().getDisplayName()).replace("{player}",player.getName()));
                                    player.updateInventory();
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }




    private void ReloadConfig() {
        reloadConfig();
        Set<String> mines = getConfig().getConfigurationSection("AchieveAwardPool").getKeys(false);
        for (String temp:mines){
            int id = getConfig().getInt("AchieveAwardPool."+temp+".id");
            String name = getConfig().getString("AchieveAwardPool."+temp+".name");
            //System.out.println(id+name);
            ItemStack itemStack = new ItemStack(Material.APPLE,id);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(name);
            itemStack.setTypeId(id);
            itemStack.setItemMeta(itemMeta);
            itemStack.setAmount(1);
            AchieveAwardPool.add(itemStack);
        }

        Set<String> luckboxpools = getConfig().getConfigurationSection("luckboxpool").getKeys(false);
        for (String temp:luckboxpools){
            int chance = getConfig().getInt("luckboxpool."+temp+".chance");
            int luck = getConfig().getInt("luckboxpool."+temp+".luck");
            int id = getConfig().getInt("luckboxpool."+temp+".items.id");
            String name = getConfig().getString("luckboxpool."+temp+".items.name");
            //System.out.println(chance+luck+id+name);
            ItemStack itemStack = new ItemStack(Material.APPLE,id);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(name);
            itemStack.setTypeId(id);
            itemStack.setItemMeta(itemMeta);
            itemStack.setAmount(1);
            boxpoll.add(new luckboxpool(temp,chance,luck,itemStack));
        }
        LuckValueMax = getConfig().getInt("LuckValueMax");
        Boxhavename = getConfig().getString("BoxAndKey.Box");
        Keyhavename = getConfig().getString("BoxAndKey.Key");
        luckboxpoolMag =getConfig().getString("luckboxpoolMag");
        AchieveAwardMsg = getConfig().getString("AchieveAwardMsg");
        AllMsg = getConfig().getString("AllMsg");
        for (luckboxpool luckboxpool:boxpoll){
            MaxChance += luckboxpool.getChance();
        }
    }

}
