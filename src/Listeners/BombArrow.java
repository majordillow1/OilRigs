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
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

/**
 *
 * @author knightmare
 */
public class BombArrow implements Listener {

    private OilRigs plugin;

    public BombArrow(OilRigs plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void CheckArrow(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player) {
            if (e.getProjectile() instanceof Arrow) {
                Player p = (Player) e.getEntity();
                Arrow arrow = (Arrow) e.getProjectile();
                for (int i = 0; i < p.getInventory().getSize(); i++) {

                    if (p.getInventory().getItem(i) != null) {
                        if (p.getInventory().getItem(i).getType().equals(Material.ARROW) || p.getInventory().getItem(i).getType().equals(Material.TIPPED_ARROW)) {
                            if (!p.getInventory().getItem(i).hasItemMeta()) {
                                break;
                            }
                            if (!p.getInventory().getItem(i).getItemMeta().hasDisplayName()) {
                                break;
                            }
                            if (p.getInventory().getItem(i).getItemMeta().getDisplayName().equals(ChatColor.RED + "Bomb Arrow")) {
                                arrow.setMetadata("BombArrow", new FixedMetadataValue(OilRigs.getPlugin(), true));
                                break;
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void dispenserShootArrow(BlockDispenseEvent e) {

        if (!e.getItem().hasItemMeta()) {
            return;
        }
        if (!e.getItem().getItemMeta().hasDisplayName()) {
            return;
        }

        if (e.getItem().getItemMeta().getDisplayName().equals(ChatColor.RED + "Bomb Arrow")) {
            if (plugin.getConfig().getBoolean("BombArrowCanBeDispensed")) {
                if (e.getBlock().getType().equals(Material.DISPENSER)) {
                    MaterialData disp = e.getBlock().getState().getData();
                    Dispenser d = (Dispenser) e.getBlock().getState();
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            d.getInventory().removeItem(e.getItem());
                        }
                    }.runTaskLater(plugin, 1);
                    BlockFace targetFace = ((org.bukkit.material.Dispenser) e.getBlock().getState().getData()).getFacing();
                    Vector vec = e.getVelocity();
                    Location front = e.getBlock().getRelative(targetFace).getLocation().add(0.5, 0.5, 0.5);
                    Arrow arrow = e.getBlock().getWorld().spawn(front, Arrow.class);
                    arrow.setVelocity(vec.multiply(1.5));
                    arrow.setMetadata("BombArrow", new FixedMetadataValue(OilRigs.getPlugin(), true));
                    e.setCancelled(true);
                }

            }

        }
        if (e.getItem().getItemMeta().getDisplayName().equals(ChatColor.RED + "Molotov")) {
            if (plugin.getConfig().getBoolean("MolotovCanBeDispensed")) {
                if (e.getBlock().getType().equals(Material.DISPENSER)) {
                    MaterialData disp = e.getBlock().getState().getData();
                    Dispenser d = (Dispenser) e.getBlock().getState();
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            d.getInventory().removeItem(e.getItem());
                        }
                    }.runTaskLater(plugin, 1);
                    BlockFace targetFace = ((org.bukkit.material.Dispenser) e.getBlock().getState().getData()).getFacing();
                    Vector vec = e.getVelocity();
                    Location front = e.getBlock().getRelative(targetFace).getLocation().add(0.5, 0.5, 0.5);
                    ItemStack molotov = new ItemStack(Material.SPLASH_POTION);
                    PotionMeta meta = (PotionMeta) molotov.getItemMeta();
                    meta.setColor(Color.RED);
                    meta.setDisplayName(ChatColor.RED + "Molotov");
                    molotov.setItemMeta(meta);
                    ThrownPotion myMolotov = e.getBlock().getWorld().spawn(front, ThrownPotion.class);
                    myMolotov.setVelocity(vec.multiply(1.5));
                    myMolotov.setMetadata("molotov", new FixedMetadataValue(OilRigs.getPlugin(), true));
                    myMolotov.setItem(molotov);
                    e.setCancelled(true);
                }

            }

        }

    }

    @EventHandler

    public void onHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow) {
            if (event.getEntity().hasMetadata("BombArrow")) {
                event.getEntity().remove();
                event.getEntity().getWorld().createExplosion(event.getEntity().getLocation(), (float) plugin.getConfig().getDouble("bombArrowSize"));
            }
        }
    }

    @EventHandler
    public void craftRestrictor(PrepareItemCraftEvent event) {

        ItemStack OilArrow = new ItemStack(Material.TIPPED_ARROW);
        PotionMeta oilArrowMeta = (PotionMeta) OilArrow.getItemMeta();

        oilArrowMeta.setBasePotionData(new PotionData(PotionType.POISON));
        oilArrowMeta.setDisplayName(ChatColor.RED + "Bomb Arrow");
        OilArrow.setItemMeta(oilArrowMeta);
        if (event.getInventory() instanceof CraftingInventory) {
            CraftingInventory inv = (CraftingInventory) event.getInventory();
            if (event.getRecipe() != null) {
                if (event.getRecipe().getResult().equals(OilArrow)) {
                    boolean canMake;
                    canMake = false;
                    for (int i = 0; i < inv.getSize() - 1; i++) {
                        if (inv.getMatrix()[i] != null) {
                            if (inv.getMatrix()[i].getType().equals(Material.POTION)) {
                                if (inv.getMatrix()[i].hasItemMeta()) {
                                    if (inv.getMatrix()[i].getItemMeta().hasDisplayName()) {
                                        if (inv.getMatrix()[i].getItemMeta().getDisplayName().equals("OIL")) {
                                            canMake = true;
                                            break;

                                        }
                                    }

                                }

                            }
                        }
                    }
                    if (!canMake) {
                        event.getInventory().setResult(null);
                    }
                }
            }
        }
    }

}
