/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oilrigs.Listeners;

import java.util.ArrayList;
import oilrigs.OilRigs;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Furnace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.player.PlayerInteractEvent;


/**
 *
 * @author storm
 */
public class Refinery implements Listener {

    private OilRigs plugin;

    public Refinery(OilRigs plugin) {
        this.plugin = plugin;
    }
    Short cooktime = (short) 100;
    ArrayList<Block> Refinerys = new ArrayList<Block>();

    @EventHandler
    public void refine(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getClickedBlock() != null) {
                if (e.getClickedBlock().getType().equals(Material.FURNACE)) {
                    Location furnloc = e.getClickedBlock().getLocation();
                    if (checkRefinery(e.getClickedBlock().getLocation())) {
                        //Debug.println("this is a ", "refinery!");
                        if (!Refinerys.contains(e.getClickedBlock())) {
                            Refinerys.add(e.getClickedBlock());
                        }

                    } else {
                        //Debug.println("this is not a ", "refinery!");
                        if (Refinerys.contains(e.getClickedBlock())) {
                            Refinerys.remove(e.getClickedBlock());
                        }
                    }
                }
            }
        }

    }

    @EventHandler
    public void smelt(FurnaceSmeltEvent e) {
        if (Refinerys.contains(e.getBlock())) {

            if (e.getSource() == null) {
                e.setCancelled(true);
                return;
            }
            if (!e.getSource().hasItemMeta()) {
                e.setCancelled(true);
                return;
            }
            if (!e.getSource().getItemMeta().hasDisplayName()) {
                e.setCancelled(true);
                return;
            }
            if (!e.getSource().getItemMeta().getDisplayName().equals("OIL")) {
                e.setCancelled(true);;
            } else {
                return;

            }
        } else {
            if (e.getSource() == null) {

                return;
            }
            if (e.getSource().getType().equals(Material.POTION)) {
                e.setCancelled(true);
                return;
            }

        }

    }

    @EventHandler
    public void Refineing(FurnaceBurnEvent e) {
        if (checkRefinery(e.getBlock().getLocation())) {
            if (!Refinerys.contains(e.getBlock())) {
                Refinerys.add(e.getBlock());
            }

        } else {
            //Debug.println("this is not a ", "refinery!");
            if (Refinerys.contains(e.getBlock())) {
                Refinerys.remove(e.getBlock());
            }
        }

        if (Refinerys.contains(e.getBlock())) {
            e.setBurnTime(plugin.getConfig().getInt("RefineFuelBurnSpeed"));
        }

    }

    public boolean checkRefinery(Location loc) {
        Furnace f = (Furnace) loc.getBlock().getState();
        Directional FurnaceD = (Directional) f.getBlockData();
        BlockFace targetFace = FurnaceD.getFacing();
        Location front = f.getBlock().getRelative(targetFace).getLocation();
        int yaw = 0;
        switch (targetFace) {
            case NORTH:
                yaw = 0;
                break;
            case SOUTH:
                yaw = 180;
                break;
            case EAST:
                yaw = 90;
                break;
            case WEST:
                yaw = 270;
        }
        ArrayList<Block> blocks = new ArrayList<Block>();
        blocks.add(front.add(0, 1, 0).getBlock());
        blocks.add(front.add(0, 1, 0).getBlock());
        int amt = 1;
        final float newZ = (float) (loc.getZ() + (amt * Math.sin(Math.toRadians(yaw + 90 * 0))));
        final float newX = (float) (loc.getX() + (amt * Math.cos(Math.toRadians(yaw + 90 * 0))));
        Location Right = new Location(loc.getWorld(), newX, loc.getBlockY(), newZ);
        blocks.add(Right.getBlock());
        blocks.add(Right.add(0, 1, 0).getBlock());
        blocks.add(Right.add(0, 1, 0).getBlock());
        int otheramt = -1;
        final float LeftZ = (float) (loc.getZ() + (otheramt * Math.sin(Math.toRadians(yaw + 90 * 0))));
        final float LEftX = (float) (loc.getX() + (otheramt * Math.cos(Math.toRadians(yaw + 90 * 0))));
        Location Left = new Location(loc.getWorld(), LEftX, loc.getBlockY(), LeftZ);
        blocks.add(Left.getBlock());
        blocks.add(Left.add(0, 1, 0).getBlock());
        blocks.add(Left.add(0, 1, 0).getBlock());
        for (Block b : blocks) {
            if (!b.getType().equals(Material.IRON_BLOCK)) {
                return false;
            }

        }
        return true;
    }
}
