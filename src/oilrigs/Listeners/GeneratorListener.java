/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oilrigs.Listeners;

import Listeners.MachineGunBow;
import Util.RepeatingTask;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import oilrigs.OilRigs;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.ShulkerBox;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.scheduler.BukkitRunnable;
import sun.security.util.Debug;

/**
 *
 * @author knightmare
 */
public class GeneratorListener implements Listener {

    private OilRigs plugin;

    public GeneratorListener(OilRigs plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void CheckForOil(PrepareItemCraftEvent e) {
        if (e.getRecipe() == null) {
            return;
        }
        if (e.getRecipe().getResult() == null) {
            return;
        }
        if (!e.getRecipe().getResult().hasItemMeta()) {
            return;
        }
        if (!e.getRecipe().getResult().getItemMeta().hasDisplayName()) {
            return;
        }
        if (!e.getRecipe().getResult().getItemMeta().getDisplayName().equals(org.bukkit.ChatColor.BLUE + "Generator")) {
            return;
        }
        if (e.getInventory() == null) {
            return;
        }

        if (e.getInventory().getMatrix()[5] == null || e.getInventory().getMatrix()[6] == null) {
            // Debug.println("shouldnt be null", "maybe");
            return;

        }
        if (!e.getInventory().getMatrix()[5].hasItemMeta() || !e.getInventory().getMatrix()[6].hasItemMeta()) {
            e.getInventory().setResult(null);
            return;
        }
        if (!e.getInventory().getMatrix()[5].getItemMeta().hasDisplayName() || !e.getInventory().getMatrix()[6].getItemMeta().hasDisplayName()) {
            e.getInventory().setResult(null);
            return;
        }
        if (!e.getInventory().getMatrix()[5].getItemMeta().getDisplayName().equals("OIL") || !e.getInventory().getMatrix()[6].getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Gas")) {
            e.getInventory().setResult(null);
            return;
        }
    }

    @EventHandler
    public void PlaceGenerator(BlockPlaceEvent e) {
        if (!e.getItemInHand().hasItemMeta()) {
            return;
        }
        if (!e.getItemInHand().getItemMeta().hasDisplayName()) {
            return;
        }
        if (e.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.BLUE + "Generator")) {
            oilrigs.OilRigs.GeneratorLocs.add(e.getBlockPlaced().getLocation());
            //Debug.println("Place a ", "Generator" + e.getBlockPlaced().getLocation().toString());
        }
    }

    @EventHandler
    public void GeneratorBreak(BlockBreakEvent e) {
        ItemStack Generator = new ItemStack(Material.BLACK_SHULKER_BOX);
        ItemMeta GenMeta = Generator.getItemMeta();
        GenMeta.setDisplayName(ChatColor.BLUE + "Generator");
        Generator.setItemMeta(GenMeta);
        if (oilrigs.OilRigs.GeneratorLocs.contains(e.getBlock().getLocation())) {
            oilrigs.OilRigs.GeneratorLocs.remove(e.getBlock().getLocation());
        }
    }

    @EventHandler
    public void openGenerator(InventoryOpenEvent e) {
        ItemStack iss = new ItemStack(Material.LEVER);
        ItemMeta im = iss.getItemMeta();
        im.setDisplayName(ChatColor.GREEN + "Start Generator");
        iss.setItemMeta(im);
        if (e.getInventory().getType().equals(InventoryType.SHULKER_BOX)) {
            //Debug.println("This is not", "The problem" + e.getInventory().getLocation().toString());
            if (e.getInventory().getLocation() == null) {
                return;
            }
            if (e.getInventory().getLocation().getBlock() == null) {
                return;
            }
            ShulkerBox shulk = (ShulkerBox) e.getInventory().getLocation().getBlock().getState();
            if (oilrigs.OilRigs.GeneratorLocs.contains(shulk.getBlock().getLocation())) {
                //this is a generator
                //Debug.println("Opened inventory", "on the generator");
                if (!e.getInventory().contains(iss)) {
                    e.getInventory().addItem(iss);
                }

            }
        }
    }

    @EventHandler
    public void CanttakeLEver(InventoryClickEvent e) {
        
        if (e.getClickedInventory() == null) {
            return;
        }
        if (e.getClickedInventory().getType() == null) {
            return;
        }
        if (e.getClickedInventory().getType().equals(InventoryType.SHULKER_BOX)) {
            if (e.getClickedInventory().getLocation() == null) {
                return;
            }
            if (e.getClickedInventory().getLocation().getBlock() == null) {
                return;
            }
            if (oilrigs.OilRigs.GeneratorLocs.contains(e.getClickedInventory().getLocation())) {
                //this is a generator
                //Debug.println("clicked", "this");
                if (!e.getCurrentItem().hasItemMeta()) {
                    return;
                }
                if (!e.getCurrentItem().getItemMeta().hasDisplayName()) {
                    return;
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Start Generator")) {

                    e.setCancelled(true);
                    startGenerator(e.getClickedInventory().getLocation());
                }
            }
        }

    }
    HashMap<Location, Boolean> gens = new HashMap<Location, Boolean>();

    public void startGenerator(Location loc) {
        if (gens.containsKey(loc)) {
            gens.replace(loc, !gens.get(loc));
        } else {
            gens.put(loc, Boolean.TRUE);
        }

        if (gens.get(loc)) {
            //Debug.println("This generator started", loc.toString());
            startTask(loc, true);

        } else {
            //Debug.println("This generator stopped", loc.toString());

        }

    }
   
    public void startTask(Location loc, boolean b) {

        
        RepeatingTask repeatingTask = new RepeatingTask(plugin, 20, 20) {
            @Override
            public void run() {
                
                if (!gens.get(loc)) {
                    //Debug.println("WHY 1", "WHYYY");
                    canncel();
                    return;

                }
                if (loc.getBlock() == null) {
                    gens.remove(loc);
                    oilrigs.OilRigs.GeneratorLocs.remove(loc);
                    //Debug.println("WHY 2", "WHYYY");
                    canncel();
                }
                if (!loc.getBlock().getType().equals(Material.BLACK_SHULKER_BOX)) {
                    //Debug.println("WHY 3", "WHYYY");
                    gens.remove(loc);
                    oilrigs.OilRigs.GeneratorLocs.remove(loc);
                    canncel();
                }
                ShulkerBox gen = (ShulkerBox) loc.getBlock().getState();
                List<String> ls = new ArrayList<String>();
                ls.add(ChatColor.BLUE + "Used for powering Generators");
                ItemStack gas = new ItemStack(Material.POTION);
                PotionMeta im = (PotionMeta) gas.getItemMeta();
                im.setDisplayName(ChatColor.GREEN + "Gas");
                im.setLore(ls);
                im.setColor(Color.GREEN);
                gas.setItemMeta(im);
                ItemStack battery = new ItemStack(Material.NETHER_BRICK);
                ItemMeta batt = battery.getItemMeta();
                List<String> deadbatt = new ArrayList<String>();
                deadbatt.add(ChatColor.BLUE + "Dead Battery");
                batt.setDisplayName("Battery");
                batt.setLore(deadbatt);
                battery.setItemMeta(batt);
                ItemStack batteryfull = new ItemStack(Material.NETHER_BRICK);
                ItemMeta batts = batteryfull.getItemMeta();
                List<String> fullbatt = new ArrayList<String>();
                fullbatt.add(ChatColor.BLUE + "Full Battery");
                batts.setDisplayName("Battery");
                batts.setLore(fullbatt);
                batteryfull.setItemMeta(batts);

                ItemStack taser = new ItemStack(Material.LEVER);
                ItemMeta meta = taser.getItemMeta();
                List<String> taslist = new ArrayList<String>();
                taslist.add(org.bukkit.ChatColor.BLUE + "Full Taser");
                meta.setLore(taslist);
                meta.setDisplayName(ChatColor.RED + "TASER");
                taser.setItemMeta(meta);
                boolean hasFuel = false;
                boolean hasdedbatt = false;
                boolean hasdedTaser = false;
                boolean hasdedbow = false;
                int dedtaserp = 0;
                int dedbowp = 0;
                int fuelp = 0;
                int dedbattp = 0;
                int dedbattamt = 0;
                for (int i = 0; i < gen.getInventory().getSize(); i++) {
                    if (gen.getInventory().getItem(i) != null) {
                        if (gen.getInventory().getItem(i).hasItemMeta()) {
                            if (gen.getInventory().getItem(i).getType().equals(Material.POTION)) {
                                //check name
                                if (gen.getInventory().getItem(i).getItemMeta().hasDisplayName()) {
                                    if (gen.getInventory().getItem(i).getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Gas")) {
                                        hasFuel = true;
                                        fuelp = i;
                                    }
                                }
                            }
                            if (gen.getInventory().getItem(i).getType().equals(Material.NETHER_BRICK)) {
                                //check lore
                                if (gen.getInventory().getItem(i).getItemMeta().hasLore()) {
                                    if (gen.getInventory().getItem(i).getItemMeta().getLore().contains(ChatColor.BLUE + "Dead Battery")) {
                                        hasdedbatt = true;
                                        dedbattp = i;
                                        dedbattamt = gen.getInventory().getItem(i).getAmount();
                                    }
                                }
                            }
                            if (gen.getInventory().getItem(i).getType().equals(Material.LEVER)) {
                                if (gen.getInventory().getItem(i).getItemMeta().hasDisplayName()) {
                                    if (gen.getInventory().getItem(i).getItemMeta().hasLore()) {
                                        if (gen.getInventory().getItem(i).getItemMeta().getDisplayName().equals(ChatColor.RED + "TASER") && gen.getInventory().getItem(i).getItemMeta().getLore().contains(ChatColor.BLUE + "Dead Taser")) {
                                            hasdedTaser = true;
                                            dedtaserp = i;

                                        }
                                    }
                                }
                            }
                            
                            if (gen.getInventory().getItem(i).getType().equals(Material.BOW)) {
                                //check lore
                                if (gen.getInventory().getItem(i).getItemMeta().hasLore()) {
                                    if (gen.getInventory().getItem(i).getItemMeta().getDisplayName().contains(ChatColor.BLUE + "Machine gun bow")) {
                                        if(gen.getInventory().getItem(i).getItemMeta().hasLore()){
                                            
                                        String percent = gen.getInventory().getItem(i).getItemMeta().getLore().get(0);
                        NumberFormat defaultFormat = NumberFormat.getPercentInstance();
                    try {
                       Number value = defaultFormat.parse(ChatColor.translateAlternateColorCodes('&', percent));
                        Double f1 = value.doubleValue();
                        Double f2 = f1 * 100;
                        int f = f2.intValue();
                        if(f < 100){
                          //  Debug.println("charge me", "fiddy percent");
                            hasdedbow = true;
                            dedbowp = i;
                        }
                        
                            
                        
                         } catch (ParseException ex) {
                        Logger.getLogger(MachineGunBow.class.getName()).log(Level.SEVERE, null, ex);
                    }
                                        
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (!hasFuel) {
                    //startGenerator(loc);
                    //Debug.println("WHY 4", "WHYYY");
                    canncel();
                }

                if (hasFuel) {
                    
                                  //particles(loc);
                    
                    if (hasdedbatt) {
                        gen.getInventory().clear(fuelp);
                        if (gen.getInventory().getItem(dedbattp).getAmount() >= 2) {
                            gen.getInventory().getItem(dedbattp).setAmount(dedbattamt - 1);
                        } else {
                            gen.getInventory().clear(dedbattp);
                        }

                        gen.getInventory().addItem(batteryfull);
                        return;
                    }
                    if (hasdedTaser) {
                        gen.getInventory().clear(fuelp);
                        if (gen.getInventory().getItem(dedtaserp).getAmount() >= 2) {
                            gen.getInventory().getItem(dedtaserp).setAmount(gen.getInventory().getItem(dedtaserp).getAmount() - 1);
                        } else {
                            gen.getInventory().clear(dedtaserp);
                        }
                        gen.getInventory().addItem(taser);
                        return;
                    }
                    if (hasdedbow) {
                        gen.getInventory().clear(fuelp);
                        ItemStack bow = gen.getInventory().getItem(dedbowp);
                        ItemMeta bowmeta = bow.getItemMeta();
                        String percent = bowmeta.getLore().get(0);
                        NumberFormat defaultFormat = NumberFormat.getPercentInstance();
                    try {
                       Number value = defaultFormat.parse(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', percent)));
                        Double f1 = value.doubleValue();
                        Double f2 = f1 * 100;
                        int f = f2.intValue();
                       // Debug.println("f is", "this " + f);
                          //  Debug.println("charge me", "fiddy percent the second");
                            f += 25;
                            if(f >= 100){
                                f = 100;
                                 List<String> NewLore = new ArrayList<String>();
                            NewLore.add(f+"%");
                            bowmeta.setLore(NewLore);
                            bow.setItemMeta(bowmeta);
                            //gen.update();
                            }else{
                                List<String> NewLore = new ArrayList<String>();
                            NewLore.add(f+"%");
                            bowmeta.setLore(NewLore);
                            bow.setItemMeta(bowmeta);
                            //gen.update();
                            }
                        
                        
                            
                        
                         } catch (ParseException ex) {
                        Logger.getLogger(MachineGunBow.class.getName()).log(Level.SEVERE, null, ex);
                    }
                        //gen.getInventory().addItem(taser);
                        return;
                    }
                    gen.getInventory().clear(fuelp);

                }

            }

         
            

            
        };
        
         

    }

   
   

}
