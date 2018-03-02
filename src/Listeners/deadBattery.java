/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.InventoryView;

/**
 *
 * @author storm
 */
public class deadBattery implements Listener {

    @EventHandler
    public void StopRename(InventoryClickEvent event) {
        if (!(event.getInventory() instanceof AnvilInventory)) {
            return;
        }

        if (event.getSlotType() != SlotType.RESULT) {
            return;
        }
        if (event.getClickedInventory().getItem(0) != null) {
            if (event.getClickedInventory().getItem(0).hasItemMeta()) {
                if (event.getClickedInventory().getItem(0).getItemMeta().hasDisplayName()) {
                    if (event.getClickedInventory().getItem(0).getItemMeta().getDisplayName().equals(ChatColor.BLUE + "Generator")) {
                        event.setCancelled(true);
                    }
                }
            }
        }

        if (event.getCurrentItem() == null) {
            return;
        }
        if (!event.getCurrentItem().hasItemMeta()) {
            return;
        }
        if (!event.getCurrentItem().getItemMeta().hasDisplayName()) {
            return;
        }
        if (event.getCurrentItem().getItemMeta().getDisplayName().equals("OIL")) {
            event.setCancelled(true);
        }
        if (event.getCurrentItem().getItemMeta().getDisplayName().equals("Battery")) {
            event.setCancelled(true);
        }
        if (event.getCurrentItem().getItemMeta().getDisplayName().equals(org.bukkit.ChatColor.BLUE + "Landmine")) {
            event.setCancelled(true);
        }
        if (event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RED + "Molotov")) {
            event.setCancelled(true);
        }
        if (event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RED + "Bomb Arrow")) {
            event.setCancelled(true);
        }
        if (event.getCurrentItem().getItemMeta().getDisplayName().equals(net.md_5.bungee.api.ChatColor.GREEN + "Gas")) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void CheckInventory(PrepareItemCraftEvent e) {
        if (e.getRecipe() == null) {
            return;
        }
        if (e.getRecipe().getResult() == null) {
            return;
        }
        if (!e.getRecipe().getResult().hasItemMeta()) {
            return;
        }
        if (!e.getRecipe().getResult().getItemMeta().hasLore()) {
            return;
        }
        if (!e.getRecipe().getResult().getItemMeta().getLore().contains(org.bukkit.ChatColor.BLUE + "Dead Battery")) {
            return;
        }
        if (e.getInventory() == null) {
            return;
        }
        if (e.getInventory().getSize() < 8) {
            return;
        }

        if (e.getInventory().getMatrix()[6] == null || e.getInventory().getMatrix()[8] == null) {
            // Debug.println("shouldnt be null", "maybe");
            return;

        }
        if (!e.getInventory().getMatrix()[6].hasItemMeta() || !e.getInventory().getMatrix()[8].hasItemMeta()) {
            e.getInventory().setResult(null);
            return;
        }
        if (!e.getInventory().getMatrix()[6].getItemMeta().hasDisplayName() || !e.getInventory().getMatrix()[8].getItemMeta().hasDisplayName()) {
            e.getInventory().setResult(null);
            return;
        }
        if (!e.getInventory().getMatrix()[6].getItemMeta().getDisplayName().equals("OIL") || !e.getInventory().getMatrix()[8].getItemMeta().getDisplayName().equals("OIL")) {
            e.getInventory().setResult(null);
            return;
        }

    }
}
