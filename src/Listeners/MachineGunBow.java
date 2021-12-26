/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Listeners;


import Util.RepeatingTask;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import oilrigs.OilRigs;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.TippedArrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionData;


/**
 *
 * @author storm
 */
public class MachineGunBow implements Listener {

    private OilRigs plugin;
    
    public MachineGunBow(OilRigs plugin) {
        this.plugin = plugin;
    }
    ArrayList<UUID> drawing = new ArrayList<UUID>();
    
    @EventHandler
    public void CheckRecharge(PrepareItemCraftEvent e){
         if (e.getRecipe() == null) {
            return;
        }
        if (e.getInventory() == null) {
            return;
        }
        if (e.getRecipe() instanceof ShapelessRecipe) {

            ShapelessRecipe s = (ShapelessRecipe) e.getRecipe();
            
            NamespacedKey key7 = new NamespacedKey(plugin, plugin.getDescription().getName() + "NYNE");
            if (s.getKey().equals(key7)) {
                boolean canMake = true;

                boolean isFullBatt = true;
                for (int i = 0; i < e.getInventory().getSize() - 1; i++) {
                    ItemStack item = e.getInventory().getMatrix()[i];
                    if (e.getInventory().getMatrix()[i] == null) {
                        continue;
                    }
                    if (e.getInventory().getMatrix()[i].getType().equals(Material.BOW)) {
                        //deal with dead taser
                        if (!item.hasItemMeta()) {
                            canMake = false;
                            break;
                        }
                        if (!item.getItemMeta().hasDisplayName()) {
                            canMake = false;
                            break;
                        }
                        if (!item.getItemMeta().hasLore()) {
                            canMake = false;
                            break;
                        }
                        if (!item.getItemMeta().getDisplayName().equals(ChatColor.BLUE + "Machine gun bow")) {
                            canMake = false;
                            break;
                        }
                        
                    }
                    if (e.getInventory().getMatrix()[i].getType().equals(Material.NETHER_BRICK)) {
                        //deal with battery
                        if (!item.hasItemMeta()) {
                            canMake = false;
                            break;
                        }
                        if (!item.getItemMeta().hasDisplayName()) {
                            canMake = false;
                            break;
                        }
                        if (!item.getItemMeta().hasLore()) {
                            canMake = false;
                            break;
                        }
                        if (!item.getItemMeta().getDisplayName().equals("Battery")) {
                            canMake = false;
                            break;
                        }
                        if (!item.getItemMeta().getLore().contains(ChatColor.BLUE + "Full Battery")) {
                            canMake = false;
                            break;
                        }
                    }
                }

                if (!canMake) {
                    e.getInventory().setResult(null);
                }

            }

        }

        
    }
    @EventHandler
    public void CheckCraft(PrepareItemCraftEvent e){
        //craft bow
        if(e.getRecipe() == null){
            return;
        }
        
        if(e.getInventory() == null){
            return;
        }
        
        if(e.getRecipe() instanceof ShapedRecipe){
            ShapedRecipe b = (ShapedRecipe) e.getRecipe();
            NamespacedKey key8 = new NamespacedKey(plugin, plugin.getDescription().getName() + "eight");
            if(b.getKey().equals(key8)){
                //Debug.println("is", "machine gun bow");
                boolean canCraft = true;
                boolean fullBatt = true;
               
        if (e.getRecipe().getResult() == null) {
           // Debug.println("is", "null");
            return;
        }
        if (!e.getRecipe().getResult().hasItemMeta()) {
            //Debug.println("is", "no meta");
            return;
        }
        
        
        if (e.getInventory().getSize() < 8) {
            //Debug.println("not big enough", "inventory");
            return;
        }
        if (e.getInventory().getMatrix()[1] == null || e.getInventory().getMatrix()[5] == null) {
            // Debug.println("shouldnt be null", "maybe");
             e.getInventory().setResult(null);
            return;

        }
        
        if (!e.getInventory().getMatrix()[1].hasItemMeta() || !e.getInventory().getMatrix()[5].hasItemMeta()) {
           // Debug.println("oil and battery", "no meta");
            e.getInventory().setResult(null);
            return;
        }
        
        if (!e.getInventory().getMatrix()[1].getItemMeta().hasDisplayName() || !e.getInventory().getMatrix()[5].getItemMeta().hasDisplayName()) {
           // Debug.println("not", "gas");
            e.getInventory().setResult(null);
            return;
        }
        
        if (e.getInventory().getMatrix()[1].getItemMeta().getDisplayName().equals("Battery") && e.getInventory().getMatrix()[5].getItemMeta().getDisplayName().equals("OIL")) {
           //differentiate between dead and full battery
            
            if(!e.getInventory().getMatrix()[1].getItemMeta().hasLore()){
                //Debug.println("not a", "Battery");
                e.getInventory().setResult(null);
                return;
            }
            //make dead one
            if(e.getInventory().getMatrix()[1].getItemMeta().getLore().contains(ChatColor.BLUE + "Dead Battery")){
                //e.getInventory().getResult().setDurability((short) 10);
               // Debug.println("dead", "Battery");
                 ItemStack MGB = new ItemStack(Material.BOW);
        ItemMeta MGBMETA = MGB.getItemMeta();
        List<String> MGBLORE = new ArrayList<String>();
        MGBLORE.add("0%");
        MGBMETA.setDisplayName(ChatColor.BLUE + "Machine gun bow");
        MGBMETA.setLore(MGBLORE);
        MGB.setItemMeta(MGBMETA);
        e.getInventory().setResult(MGB);
            }
            if(e.getInventory().getMatrix()[1].getItemMeta().getLore().contains(ChatColor.BLUE + "Full Battery")){
                //Debug.println("full", "Battery");
                return;
            }
            
           
            
        }else{
              //Debug.println("this is", "not right");
              e.getInventory().setResult(null);
              return;
        }
        
            }
        }
        
        
        
        
        
        //Check for battery and bow
        
        
    }
    
    @EventHandler
    public void pullbow(PlayerInteractEvent e){
        if(e.getAction() == Action.RIGHT_CLICK_AIR ){
            //right clicking
            if(e.getItem() == null){
                return;
            }
            if(e.getItem().getType().equals(Material.BOW)){
                //bow
                ItemStack Mybow = e.getItem();
                if(!Mybow.hasItemMeta()){
                    return;
                }
                if(!Mybow.getItemMeta().hasDisplayName()){
                    return;
                }
                if(!Mybow.getItemMeta().hasLore()){
                    return;
                }
                
                if(Mybow.getItemMeta().getDisplayName().equals(ChatColor.BLUE + "Machine gun bow")){
                //IS THE RIGHT BOW
                String percent = Mybow.getItemMeta().getLore().get(0);
                NumberFormat defaultFormat = NumberFormat.getPercentInstance();
                    try {
                        Number value = defaultFormat.parse(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', percent)));
                        Double f1 = value.doubleValue();
                        
                     //  Debug.println("Percent is ", "thist " + f1);
                       Double f2 =  f1*100;
                       int f = f2.intValue();
                        if(drawing.contains(e.getPlayer().getUniqueId())){
                            drawing.remove(e.getPlayer().getUniqueId());
                            //do nothing
                           
                            
                        }else{
                            drawing.add(e.getPlayer().getUniqueId());
                        }
                     //   Debug.println("this is", "at " + f);
                        if(f > 0){
                            StartShooting(e.getPlayer());
                        }
                        
                    } catch (ParseException ex) {
                        Logger.getLogger(MachineGunBow.class.getName()).log(Level.SEVERE, null, ex);
                    }
                   // e.getPlayer().launchProjectile(Arrow.class);
                }
                
                
            }
            
        }
        
    }
    @EventHandler
public void onShoot(EntityShootBowEvent e) {
     if(e.getEntity() instanceof Player) {
         //Making sure the shooter is not a Skeleton, etc.
         Player shooter = (Player) e.getEntity();
         //Variable to access that is a Player constructor.
         if(drawing.contains(shooter.getUniqueId())) {
             // drawing.remove(shooter.getUniqueId());
              e.setCancelled(true);
 
         }
     }
}
public void StartShooting(Player p){
 RepeatingTask repeatingTaskBow = new RepeatingTask(plugin, 0, plugin.getConfig().getInt("BowSpeed")) {
            @Override
            public void run() {
                if(plugin.getServer().getPlayer(p.getUniqueId()) == null){
                    drawing.remove(p.getUniqueId());
                    canncel();
                     
                }
                if(drawing.contains(p.getUniqueId())){
                    if(p.getInventory() != null){
                        if(p.getInventory().getItemInMainHand() == null){
                            drawing.remove(p.getUniqueId());
                            canncel();
                            return;
                        }
                        if(!p.getInventory().getItemInMainHand().hasItemMeta()){
                            drawing.remove(p.getUniqueId());
                            canncel();
                            return;
                        }
                        if(!p.getInventory().getItemInMainHand().getItemMeta().hasDisplayName()){
                            drawing.remove(p.getUniqueId());
                            canncel();
                            return;
                        }
                        if(!p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(ChatColor.BLUE + "Machine gun bow")){
                            drawing.remove(p.getUniqueId());
                            canncel();
                            return;
                        }
                        if(!p.getInventory().getItemInMainHand().getItemMeta().hasLore()){
                            drawing.remove(p.getUniqueId());
                            canncel();
                            return;
                        }
                        
                        if(p.getInventory().contains(Material.ARROW) || p.getInventory().contains(Material.TIPPED_ARROW)){
                            //Debug.println("Fire with:", "this player");
                           int i = p.getInventory().first(Material.ARROW);
                        int t = p.getInventory().first(Material.TIPPED_ARROW);
                        //Debug.println("i =", " = " + i);
                        //Debug.println("t =", " = " + t);
                        
                       String percent = p.getInventory().getItemInMainHand().getItemMeta().getLore().get(0);
                        NumberFormat defaultFormat = NumberFormat.getPercentInstance();
                    try {
                        Number value = defaultFormat.parse(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', percent)));
                        Double f1 = value.doubleValue();
                        Double f2 = f1 * 100;
                        int f = f2.intValue();
                        //change item meta
                      //  Debug.println("chekcing", "percent");
                        if(f > 0){
                           // Debug.println("take away", "percent was "+f);
                            f -= 1;
                            //Debug.println("take away", "now is "+f);
                             ItemStack newbow =  p.getInventory().getItemInMainHand();
                            ItemMeta newbowmeta = p.getInventory().getItemInMainHand().getItemMeta();
                            
                            List<String> NewLore = new ArrayList<String>();
                            NewLore.add(f+"%");
                            newbowmeta.setLore(NewLore);
                            newbow.setItemMeta(newbowmeta);
                            //p.getInventory().clear(p.getInventory().getHeldItemSlot());
                           /// p.getInventory().addItem(newbow);
                            p.getInventory().setItem(p.getInventory().getHeldItemSlot(), newbow);
                            p.updateInventory();
                            
                        }else{
                            //Debug.println("set", "percent");
                            f = 0;
                            ItemStack newbow =  p.getInventory().getItemInMainHand();
                            ItemMeta newbowmeta = p.getInventory().getItemInMainHand().getItemMeta();
                            
                            List<String> NewLore = new ArrayList<String>();
                            NewLore.add(f+"%");
                            newbowmeta.setLore(NewLore);
                            
                            p.getInventory().setItem(p.getInventory().getHeldItemSlot(), newbow);
                            p.updateInventory();
                            drawing.remove(p.getUniqueId());
                            canncel();
                            return;
                        }
                        
                    } catch (ParseException ex) {
                        Logger.getLogger(MachineGunBow.class.getName()).log(Level.SEVERE, null, ex);
                    }
                           
                            
                        
                        if(i < 0){
                            //no arrow
                             if(p.getInventory().getItem(t).hasItemMeta()){
                                 //Debug.println("this is a", "Bomb meta");
                            if(p.getInventory().getItem(t).getItemMeta().hasDisplayName()){
                               // Debug.println("this is a", "Bomb arrow display name");
                                if(p.getInventory().getItem(t).getItemMeta().getDisplayName().equals(ChatColor.RED + "Bomb Arrow")){
                                    Arrow arrow = p.launchProjectile(Arrow.class);
                                    arrow.setMetadata("BombArrow", new FixedMetadataValue(OilRigs.getPlugin(), true));
                                  //  Debug.println("this is a", "Bomb arrow");
                                  ItemStack stack = p.getInventory().getItem(t);
                                    if (stack.getAmount() > 1) stack.setAmount(stack.getAmount() - 1);
                                    else p.getInventory().clear(t);
                                    return;
                                }
                            }
                        }
                             ItemStack it = p.getInventory().getItem(t);
                            if(it.getItemMeta() == null){
                                drawing.remove(p.getUniqueId());
                                canncel();
                                return;
                            }
                            PotionMeta meta = (PotionMeta) it.getItemMeta();
                            
                           ShootTipped(p,meta.getBasePotionData(), t);
                            
                            return;
                        }
                        if(t <0){
                            //no tipped arrow
                           ShootArrow(p, i);
                           return;
                        }
                        if(i < t){
                            //look for arrow
                            ShootArrow(p, i);
                        }else{
                           
                             if(p.getInventory().getItem(t).hasItemMeta()){
                                // Debug.println("this is a", "Bomb meta");
                            if(p.getInventory().getItem(t).getItemMeta().hasDisplayName()){
                               // Debug.println("this is a", "Bomb arrow display name");
                                if(p.getInventory().getItem(t).getItemMeta().getDisplayName().equals(ChatColor.RED + "Bomb Arrow")){
                                    Arrow arrow = p.launchProjectile(Arrow.class);
                                    arrow.setMetadata("BombArrow", new FixedMetadataValue(OilRigs.getPlugin(), true));
                                    ItemStack stack = p.getInventory().getItem(t);
                                    if (stack.getAmount() > 1) stack.setAmount(stack.getAmount() - 1);
                                    else p.getInventory().clear(t);
                                  //  Debug.println("this is a", "Bomb arrow");
                                    return;
                                }
                            }
                        }
                            //look for tipped arrow
                            
                            ItemStack it = p.getInventory().getItem(t);
                            if(it.getItemMeta() == null){
                                drawing.remove(p.getUniqueId());
                                canncel();
                                return;
                            }
                            PotionMeta meta = (PotionMeta) it.getItemMeta();
                            
                           ShootTipped(p,meta.getBasePotionData(),t);
                           
                        }
                        
                        }else{
                            drawing.remove(p.getUniqueId());
                            canncel();
                        }
                        
                        
                        
                        
                    }else{
                        drawing.remove(p.getUniqueId());
                        canncel();
                    }
                    
                    
                    
                }else{
                    drawing.remove(p.getUniqueId());
                    canncel();
                }
            }
        };
}
public void ShootArrow(Player p, int space){
    p.launchProjectile(Arrow.class);
     ItemStack stack = p.getInventory().getItem(space);
    if (stack.getAmount() > 1) stack.setAmount(stack.getAmount() - 1);
    else p.getInventory().clear(space);
}

public void ShootTipped(Player p, PotionData b, int space){
    TippedArrow shootyArrow = p.launchProjectile(TippedArrow.class);
    shootyArrow.setBasePotionData(b);
    ItemStack stack = p.getInventory().getItem(space);
    if (stack.getAmount() > 1) stack.setAmount(stack.getAmount() - 1);
    else p.getInventory().clear(space);
}
}

