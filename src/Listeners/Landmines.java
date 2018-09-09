/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Listeners;

import oilrigs.OilRigs;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import sun.security.ssl.Debug;

/**
 *
 * @author knightmare
 */
public class Landmines implements Listener {

    private OilRigs plugin;

    public Landmines(OilRigs plugin) {
        this.plugin = plugin;
    }

    public ItemStack Landmineitem() {
        ItemStack landmine = new ItemStack(Material.GRASS);
        ItemMeta im = landmine.getItemMeta();
        im.setDisplayName(ChatColor.BLUE + "Landmine");
        landmine.setItemMeta(im);
        return landmine;
    }

    @EventHandler
    public void CheckOil(PrepareItemCraftEvent e) {
        ItemStack landmine = new ItemStack(Material.GRASS);
        ItemMeta im = landmine.getItemMeta();
        im.setDisplayName(ChatColor.BLUE + "Landmine");
        landmine.setItemMeta(im);
        ItemStack oil = new ItemStack(Material.POTION);
        PotionMeta pMeta = (PotionMeta) oil.getItemMeta();
        pMeta.setColor(Color.BLACK);
        pMeta.setDisplayName("OIL");
        oil.setItemMeta(pMeta);
        if (e.getRecipe() != null) {
            if (!e.getRecipe().getResult().hasItemMeta()) {
                return;
            }
            if (!e.getRecipe().getResult().getItemMeta().hasDisplayName()) {
                return;
            }
            if (!e.getRecipe().getResult().getItemMeta().getDisplayName().equals(ChatColor.BLUE + "Landmine")) {
                return;
            }
            if (e.getInventory().getMatrix()[7] == null) {
                // Debug.println("shouldnt be null", "maybe");
                return;

            }
            if (!e.getInventory().getMatrix()[7].hasItemMeta()) {
                e.getInventory().setResult(null);
                return;
            }
            if (!e.getInventory().getMatrix()[7].getItemMeta().hasDisplayName()) {
                e.getInventory().setResult(null);
                return;
            }
            if (!e.getInventory().getMatrix()[7].getItemMeta().getDisplayName().equals("OIL")) {
                e.getInventory().setResult(null);
                return;
            }
        }

    }

    @EventHandler
    public void setLocation(BlockPlaceEvent e) {
        if (!e.getItemInHand().hasItemMeta()) {
            return;
        }
        if (!e.getItemInHand().getItemMeta().hasDisplayName()) {
            return;
        }
        if (e.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.BLUE + "Landmine")) {
            oilrigs.OilRigs.LandMineLocs.add(e.getBlockPlaced().getLocation());

        }
    }

    @EventHandler
    public void Broken(BlockBreakEvent e) {
        for (int i = 0; i < oilrigs.OilRigs.LandMineLocs.size(); i++) {
            if (e.getBlock().getLocation().equals(oilrigs.OilRigs.LandMineLocs.get(i))) {
                breakLandmine(oilrigs.OilRigs.LandMineLocs.get(i));
            }
        }
    }

    @EventHandler
    public void Blowup(PlayerMoveEvent e) {
        if (!oilrigs.OilRigs.LandMineLocs.isEmpty()) {
            for (int i = 0; i < oilrigs.OilRigs.LandMineLocs.size(); i++) {
                if (e.getTo().getWorld() == oilrigs.OilRigs.LandMineLocs.get(i).getWorld()) {
                    float distance = (float) e.getTo().distance(oilrigs.OilRigs.LandMineLocs.get(i));
                    if (distance < plugin.getConfig().getDouble("LandMineRange")) {
                        if (oilrigs.OilRigs.LandMineLocs.get(i).getBlock() == null) {
                            oilrigs.OilRigs.LandMineLocs.remove(i);
                            return;
                        }
                        if (oilrigs.OilRigs.LandMineLocs.get(i).getBlock().getType() == null) {
                            oilrigs.OilRigs.LandMineLocs.remove(i);
                            return;
                        }
                        if (oilrigs.OilRigs.LandMineLocs.get(i).getBlock().getType().equals(Material.GRASS)) {
                            e.getTo().getWorld().createExplosion(e.getTo(), plugin.getConfig().getInt("LandMineExplosionSize"));
                            oilrigs.OilRigs.LandMineLocs.remove(i);
                        } else {
                            oilrigs.OilRigs.LandMineLocs.remove(i);
                        }

                    }
                }

            }
        }

    }

    public void breakLandmine(Location loc) {
        oilrigs.OilRigs.LandMineLocs.remove(loc);
        ItemStack landmine = new ItemStack(Material.GRASS);
        landmine.setDurability((short) 1);
        ItemMeta im = landmine.getItemMeta();
        im.setDisplayName(ChatColor.BLUE + "Landmine");
        landmine.setItemMeta(im);
        if (!loc.getBlock().getType().equals(Material.AIR)) {
            loc.getBlock().setType(Material.AIR);
            loc.getBlock().getLocation().getWorld().dropItemNaturally(loc, landmine);
        }

    }

}
