/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Listeners;


import oilrigs.OilRigs;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ShapedRecipe;

/**
 *
 * @author storm
 */
public class MachineGunBow implements Listener {

    private OilRigs plugin;

    public MachineGunBow(OilRigs plugin) {
        this.plugin = plugin;
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
                boolean canCraft = true;
                boolean fullBatt = true;
               
        if (e.getRecipe().getResult() == null) {
            return;
        }
        if (!e.getRecipe().getResult().hasItemMeta()) {
            return;
        }
        
        
        if (e.getInventory().getSize() < 8) {
            return;
        }
        if (e.getInventory().getMatrix()[1] == null || e.getInventory().getMatrix()[5] == null) {
            // Debug.println("shouldnt be null", "maybe");
            return;

        }
        
        if (!e.getInventory().getMatrix()[1].hasItemMeta() || !e.getInventory().getMatrix()[5].hasItemMeta()) {
            e.getInventory().setResult(null);
            return;
        }
        
        if (!e.getInventory().getMatrix()[1].getItemMeta().hasDisplayName() || !e.getInventory().getMatrix()[5].getItemMeta().hasDisplayName()) {
            e.getInventory().setResult(null);
            return;
        }
        
        if (!e.getInventory().getMatrix()[1].getItemMeta().getDisplayName().equals("Battery") || !e.getInventory().getMatrix()[5].getItemMeta().getDisplayName().equals("OIL")) {
           //differentiate between dead and full battery
            if(!e.getInventory().getMatrix()[1].getItemMeta().hasLore()){
                return;
            }
            //make dead one
            if(e.getInventory().getMatrix()[1].getItemMeta().getLore().contains(ChatColor.BLUE + "Dead Battery")){
                e.getInventory().getResult().setDurability((short) 10);
            }
            if(e.getInventory().getMatrix()[1].getItemMeta().getLore().contains(ChatColor.BLUE + "Full Battery")){
                return;
            }
            
           
            
        }
        
            }
        }
        
    }
    
}
