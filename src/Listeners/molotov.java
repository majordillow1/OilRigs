/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Listeners;

import java.util.ArrayList;
import java.util.List;
import net.md_5.bungee.api.ChatColor;
import oilrigs.OilRigs;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.SplashPotion;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Vector;
import sun.security.ssl.Debug;

/**
 *
 * @author storm
 */
public class molotov implements Listener {

    private OilRigs plugin;

    public molotov(OilRigs plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void craftRestriction(PrepareItemCraftEvent event) {

        ItemStack molotov = new ItemStack(Material.SPLASH_POTION);
        PotionMeta meta = (PotionMeta) molotov.getItemMeta();
        meta.setColor(Color.RED);
        meta.setDisplayName(ChatColor.RED + "Molotov");
        molotov.setItemMeta(meta);
        if (event.getInventory() instanceof CraftingInventory) {
            CraftingInventory inv = (CraftingInventory) event.getInventory();
            if (event.getRecipe() != null) {
                if (event.getRecipe().getResult().hasItemMeta() && event.getRecipe().getResult().getItemMeta().getDisplayName().equals(ChatColor.RED + "Molotov")) {
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

    @EventHandler
    public void onfuelbomb(PlayerInteractEvent e) {
        final Player p = e.getPlayer();
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        ItemStack item = p.getInventory().getItemInMainHand();
        if (item == null) {
            return;
        }
        if (item.getType() != Material.SPLASH_POTION) {
            return;
        }
        if ((!item.hasItemMeta()) || (!item.getItemMeta().hasDisplayName())) {
            return;
        }
        if (!item.getItemMeta().getDisplayName().equals(ChatColor.RED + "Molotov")) {
            return;
        }

        ItemStack molotov = new ItemStack(Material.SPLASH_POTION);
        PotionMeta meta = (PotionMeta) molotov.getItemMeta();
        meta.setColor(Color.RED);
        meta.setDisplayName(ChatColor.RED + "Molotov");
        molotov.setItemMeta(meta);
        ThrownPotion thrownPotion = (ThrownPotion) e.getPlayer().launchProjectile(SplashPotion.class);
//e.getPlayer().getWorld().spawnEntity(e.getPlayer().getLocation().add(e.getPlayer().getLocation().getDirection().multiply(1).add(new Vector(0,1.5,0))), EntityType.SPLASH_POTION);
        thrownPotion.setMetadata("molotov", new FixedMetadataValue(OilRigs.getPlugin(), true));
        thrownPotion.setVelocity(e.getPlayer().getLocation().getDirection().multiply(1.5));
        thrownPotion.setItem(molotov);
        if (e.getPlayer().getGameMode().equals(GameMode.SURVIVAL) || e.getPlayer().getGameMode().equals(GameMode.ADVENTURE)) {
            e.getPlayer().getInventory().setItemInMainHand(null);
        }
        e.setCancelled(true);

//Debug.println("test", "test");
    }

    @EventHandler
    public void onSplash(PotionSplashEvent e) {
        if (e.getEntity().hasMetadata("molotov")) {
            surroundFlames(e.getEntity().getLocation(), plugin.getConfig().getInt("MolotovSize"));
            /*Fireball fireball = e.getEntity().getWorld().spawn(e.getEntity().getLocation(), Fireball.class);
            fireball.setDirection(e.getEntity().getVelocity().multiply(10));
                    fireball.setBounce(false);
                    fireball.setIsIncendiary(true);
                    fireball.setYield(0F);
                    fireball.setMetadata("molotovexp", new FixedMetadataValue(OilRigs.getPlugin(), true));*/
//e.getEntity().getWorld().createExplosion(e.getEntity().getLocation(), 4.0f);
        }
    }

    public List<Block> getBlocks(Location center, int radius,
            boolean hollow, boolean sphere) {
        List<Location> locs = circle(center, radius, radius, hollow, sphere, 0);
        List<Block> blocks = new ArrayList<Block>();

        for (Location loc : locs) {
            blocks.add(loc.getBlock());
        }

        return blocks;
    }

    public List<Location> circle(Location loc, int radius, int height,
            boolean hollow, boolean sphere, int plusY) {
        List<Location> circleblocks = new ArrayList<Location>();
        int cx = loc.getBlockX();
        int cy = loc.getBlockY();
        int cz = loc.getBlockZ();

        for (int x = cx - radius; x <= cx + radius; x++) {
            for (int z = cz - radius; z <= cz + radius; z++) {
                for (int y = (sphere ? cy - radius : cy); y < (sphere ? cy
                        + radius : cy + height); y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z)
                            + (sphere ? (cy - y) * (cy - y) : 0);

                    if (dist < radius * radius
                            && !(hollow && dist < (radius - 1) * (radius - 1))) {
                        Location l = new Location(loc.getWorld(), x, y + plusY,
                                z);
                        circleblocks.add(l);
                    }
                }
            }
        }

        return circleblocks;
    }

    public void surroundFlames(Location l, int r) {
        for (Block b : getBlocks(l, r, false, true)) {
            Block under = b.getLocation().add(0, -1, 0).getBlock();
            if (b.getType().equals(Material.AIR) && !under.getType().equals(Material.AIR) && !under.isLiquid() && !under.getType().equals(Material.FIRE)) {
                b.setType(Material.FIRE);
            }
            if (under.getType().equals(Material.LONG_GRASS)) {
                under.setType(Material.FIRE);
            }

        }

    }
}
