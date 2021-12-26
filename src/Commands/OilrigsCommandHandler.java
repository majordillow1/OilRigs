/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Commands;

import java.util.ArrayList;
import java.util.List;

import oilrigs.OilRigs;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

/**
 *
 * @author storm
 */
public class OilrigsCommandHandler implements CommandExecutor {

    private OilRigs plugin;
 
public OilrigsCommandHandler (OilRigs plugin) {
this.plugin = plugin;
}
    
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        if(cs instanceof Player){
            Player p = (Player) cs;
        if (alias.equalsIgnoreCase("or")) {
            if(args.length == 0){
                cs.sendMessage("Hey this is the base command for oilrigs! Use /or help for help or, /or get to get options or reload to reload the plugin!");
                return true;
            }
            
             if(p.hasPermission("oilrigs.Commands")){
            if(args[0].equalsIgnoreCase("reload")){
                p.sendMessage("reload plugin!");
                ReloadPlugin();
                return true;
            }
            if(args[0].equalsIgnoreCase("help")){
                p.sendMessage("You can use the /or get command or use the /or reload command for this plugin");
                p.sendMessage("when you use /or get without specifying an item, it will tell you what items are availiable");
                return true;
            }
            if (args[0].equalsIgnoreCase("get")) {
                if(args.length <= 1){
                    cs.sendMessage("Get what?");
                    cs.sendMessage("Options you can choose:");
                    cs.sendMessage("Molotov");
                    cs.sendMessage("bombarrow");
                    cs.sendMessage("landmine");
                    cs.sendMessage("generator");
                    cs.sendMessage("tazer");
                    cs.sendMessage("gas");
                    cs.sendMessage("oil");
                    return true;  
                }else{
                    String getInput = args[1];
                    switch(getInput.toLowerCase()){
                        case "molotov":
                            giveMolotov(p);
                            break;
                        case "bombarrow":
                            giveBombArrow(p);
                            p.sendMessage("I gave you your bomb arrow! Now do with it what you will....");
                            break;
                        case "landmine":
                            giveLandmine(p);
                            p.sendMessage("pssst! Put this somewhere safe!");
                            break;
                        case "generator":
                            giveGenerator(p);
                            break;
                        case "tazer":
                            giveTazer(p);
                            break;
                        case "battery":
                            giveBattery(p);
                            break;
                        case "oil":
                            giveOil(p);
                            break;
                        case "deadbattery":
                            givedeadBattery(p);
                            break;
                        case "gas":
                            giveGas(p);
                            break;
                        case "machinegunbow":
                            giveMachineGunBow(p);
                            break;
                        case "oilrig":
                            giveOilRig(p);
                            break;
                        default:
                            p.sendMessage("did you spell that right?");
                            break;
                    }
                }
              
            }
             }else{
                 p.sendMessage("Oy you don't have the right permissions mate! Get yourself outta here!");
             }
        }

      
    }
        
        
        return true;
    }

    private void giveMolotov(Player p) {
        ItemStack molotov = new ItemStack(Material.SPLASH_POTION);
                PotionMeta meta = (PotionMeta) molotov.getItemMeta();
                meta.setColor(Color.RED);
                meta.setDisplayName(ChatColor.RED + "Molotov");
                molotov.setItemMeta(meta);
            p.getInventory().addItem(molotov);
            p.updateInventory();
    }

    private void giveBombArrow(Player p) {
        ItemStack OilArrow = new ItemStack(Material.TIPPED_ARROW);
            PotionMeta oilArrowMeta =(PotionMeta) OilArrow.getItemMeta();
            oilArrowMeta.setBasePotionData(new PotionData(PotionType.POISON));
            oilArrowMeta.setDisplayName(ChatColor.RED + "Bomb Arrow");
            OilArrow.setItemMeta(oilArrowMeta);
            p.getInventory().addItem(OilArrow);
            p.updateInventory();
    }

    private void giveLandmine(Player p) {
       ItemStack landmine = new ItemStack(Material.GRASS);
        ItemMeta im = landmine.getItemMeta();
        im.setDisplayName(org.bukkit.ChatColor.BLUE + "Landmine");
        landmine.setItemMeta(im);
        p.getInventory().addItem(landmine);
        p.updateInventory();
    }

    private void giveGenerator(Player p) {
         ItemStack Generator = new ItemStack(Material.BLACK_SHULKER_BOX);
        ItemMeta GenMeta = Generator.getItemMeta();
        GenMeta.setDisplayName(org.bukkit.ChatColor.BLUE + "Generator");
        Generator.setItemMeta(GenMeta);
        p.getInventory().addItem(Generator);
        p.updateInventory();
    }

    private void giveTazer(Player p) {
        ItemStack taser = new ItemStack(Material.LEVER);
                ItemMeta meta = taser.getItemMeta();
                List<String> taslist = new ArrayList<String>();
                taslist.add(org.bukkit.ChatColor.BLUE + "Full Taser");
                meta.setLore(taslist);
                meta.setDisplayName(org.bukkit.ChatColor.RED + "TASER");
                taser.setItemMeta(meta);
                p.getInventory().addItem(taser);
                p.updateInventory();
    }

    private void giveBattery(Player p) {
        ItemStack batteryfull = new ItemStack(Material.NETHER_BRICK);
                ItemMeta batts = batteryfull.getItemMeta();
                List<String> fullbatt = new ArrayList<String>();
                fullbatt.add(org.bukkit.ChatColor.BLUE + "Full Battery");
                batts.setDisplayName("Battery");
                batts.setLore(fullbatt);
                batteryfull.setItemMeta(batts);
                
                p.getInventory().addItem(batteryfull);
                p.updateInventory();
    }

    private void ReloadPlugin() {
        plugin.reloadConfig();
    }

    private void giveOil(Player p) {
        ItemStack oil = new ItemStack(Material.POTION);

        PotionMeta pMeta = (PotionMeta) oil.getItemMeta();
        pMeta.setColor(Color.BLACK);
        pMeta.setDisplayName("OIL");

        oil.setItemMeta(pMeta);
        
        p.getInventory().addItem(oil);
        p.updateInventory();
    }

    private void givedeadBattery(Player p) {
     ItemStack batteryfull = new ItemStack(Material.NETHER_BRICK);
                ItemMeta batts = batteryfull.getItemMeta();
                List<String> fullbatt = new ArrayList<String>();
                fullbatt.add(org.bukkit.ChatColor.BLUE + "Dead Battery");
                batts.setDisplayName("Battery");
                batts.setLore(fullbatt);
                batteryfull.setItemMeta(batts);
                
                p.getInventory().addItem(batteryfull);
                p.updateInventory();
    }

    private void giveGas(Player p) {
        List<String> ls = new ArrayList<String>();
        ls.add(ChatColor.BLUE + "Used for powering Generators");
        ItemStack gas = new ItemStack(Material.POTION);
        PotionMeta im = (PotionMeta) gas.getItemMeta();
        im.setDisplayName(ChatColor.GREEN + "Gas");
        im.setLore(ls);
        im.setColor(Color.GREEN);
        gas.setItemMeta(im);
        p.getInventory().addItem(gas);
        p.updateInventory();
    }

    private void giveMachineGunBow(Player p) {
        ItemStack MGB = new ItemStack(Material.BOW);
        ItemMeta MGBMETA = MGB.getItemMeta();
        List<String> MGBLORE = new ArrayList<String>();
        MGBLORE.add("100%");
        MGBMETA.setDisplayName(ChatColor.BLUE + "Machine gun bow");
        MGBMETA.setLore(MGBLORE);
        MGB.setItemMeta(MGBMETA);
        p.getInventory().addItem(MGB);
        p.updateInventory();
    }

    private void giveOilRig(Player p) {
       ItemStack cobblestoneWall = new ItemStack(Material.COBBLESTONE_WALL,64);
       ItemStack chest = new ItemStack(Material.CHEST,1);
       ItemStack hopper = new ItemStack(Material.HOPPER,1);
       ItemStack furnace = new ItemStack(Material.FURNACE,1);
       ItemStack ironBars = new ItemStack(Material.IRON_BARS,3);
       ItemStack sign = new ItemStack(Material.DARK_OAK_SIGN,1);
       p.getInventory().addItem(cobblestoneWall);
       p.getInventory().addItem(chest);
       p.getInventory().addItem(hopper);
       p.getInventory().addItem(furnace);
       p.getInventory().addItem(ironBars);
       p.getInventory().addItem(sign);
       p.updateInventory();
    }

}
