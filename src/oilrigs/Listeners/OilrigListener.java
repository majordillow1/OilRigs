/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oilrigs.Listeners;

import java.util.HashMap;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import oilrigs.OilRigs;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Furnace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author storm
 */
public class OilrigListener implements Listener {

//Declares your plugin variable
    private OilRigs plugin;

    public OilrigListener(OilRigs plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void OpenFurnace(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getClickedBlock() != null) {
                if (e.getClickedBlock().getType().equals(Material.FURNACE)) {
                    Location furnloc = e.getClickedBlock().getLocation();
                    if (isOilRig(e.getClickedBlock())) {
                        // p.sendMessage("Created oil rig with " + getoilamtBlock(furnloc,100) + " oil pipes");
                        int ThisOilamt = getoilamtBlock(furnloc, 100);
                        CreateOilRig(ThisOilamt, furnloc);

                    } else {
                        if (hm.containsKey(furnloc)) {
                            hm.remove(furnloc);
                            oilamthash.remove(furnloc);
                        }
                    }

                }
            }
        }

    }
    public HashMap hm = new HashMap();

    public void CreateOilRig(int oilamt, Location oilloc) {

        if (hm.containsKey(oilloc)) {
            hm.replace(oilloc, oilamt);
        } else {
            hm.put(oilloc, oilamt);
        }

    }

    @EventHandler
    public void burned(FurnaceBurnEvent e) {
        if (isOilRig(e.getBlock())) {
            int ThisOilamt = getoilamtBlock(e.getBlock().getLocation(), 100);
            CreateOilRig(ThisOilamt, e.getBlock().getLocation());

        } else {
            if (hm.containsKey(e.getBlock().getLocation())) {
                hm.remove(e.getBlock().getLocation());
                oilamthash.remove(e.getBlock().getLocation());
            }
        }

        Block Furnace = e.getBlock();

        if (hm.containsKey(Furnace.getLocation())) {

            int thisResourcesAmt = (int) hm.get(Furnace.getLocation());
            //double random = Math.random() * thisResourcesAmt;

            //we have a winner furnace!
            //how much it costs
            int ThisOilamt = getoilamtBlock(e.getBlock().getLocation(), 100);
            if (ThisOilamt >= 2) {
                giveOil(Furnace.getLocation(), 1);
            }

            //how fast it goes
            e.setBurnTime((plugin.getConfig().getInt("fuelBurnOperator") / thisResourcesAmt - 1) - 20);

            //System.out.println("this has " + thisResourcesAmt);
        }
    }

    public HashMap oilamthash = new HashMap();

    public void giveOil(Location loc, float i) {
        
        
        float start = 0;
        //Block Chess = loc.getBlock().getLocation().add(0, 2, 0).getBlock();
        Furnace f = (Furnace) loc.getBlock().getState();
        Directional Furnaced = (Directional) f.getBlockData();
        BlockFace targetFace = Furnaced.getFacing();
        Location front = f.getBlock().getRelative(targetFace).getLocation();
        Block Chess = front.add(0,-1,0).getBlock();
        ItemStack oil = new ItemStack(Material.POTION);

        PotionMeta pMeta = (PotionMeta) oil.getItemMeta();
        pMeta.setColor(Color.BLACK);
        pMeta.setDisplayName("OIL");

        oil.setItemMeta(pMeta);

        if (oilamthash.containsKey(loc)) {
            float it = i + (float) oilamthash.get(loc);
            oilamthash.replace(loc, it);
            //System.out.println("replaced" + it);
            int x = (int) hm.get(loc);//oil pipes num
            float squared = x * x;//pipes squared
            float fuel = (float) plugin.getConfig().getDouble("fuelOperator");//fuel constant, could change this honestly this is casuing all the wierd
            float equation = fuel * ((1f / 200f) * squared + 4f);//total equation

            // System.out.println("has " + x+ " required " + equation);
            //last changed this was 1
            Block Sign = loc.getBlock().getLocation().add(0, 1, 1).getBlock();
            Block Sign2 = loc.getBlock().getLocation().add(0, 1, -1).getBlock();
            Block Sign3 = loc.getBlock().getLocation().add(1, 1, 0).getBlock();
            Block Sign4 = loc.getBlock().getLocation().add(-1, 1, 0).getBlock();
            int s = 0;
            if (Sign.getState() instanceof Sign) {
                s = 1;
            }
            if (Sign2.getState() instanceof Sign) {
                s = 2;
            }
            if (Sign3.getState() instanceof Sign) {
                s = 3;
            }
            if (Sign4.getState() instanceof Sign) {
                s = 4;
            }
            double check = Math.round((float) oilamthash.get(loc) / equation * 100f);
            if (s == 1) {
                Sign mysign = (Sign) Sign.getState();
                mysign.setLine(0, "This Process is ");
                mysign.setLine(1, check + "% Done");
                mysign.setLine(2, "I require " + equation);
                mysign.setLine(3, "fuel");
                mysign.update();
            }
            if (s == 2) {
                Sign mysign = (Sign) Sign2.getState();
                mysign.setLine(0, "This Process is ");
                mysign.setLine(1, check + "% Done");
                mysign.setLine(2, "I require " + equation);
                mysign.setLine(3, "fuel");
                mysign.update();
            }
            if (s == 3) {
                Sign mysign = (Sign) Sign3.getState();
                mysign.setLine(0, "This Process is ");
                mysign.setLine(1, check + "% Done");
                mysign.setLine(2, "I require " + equation);
                mysign.setLine(3, "fuel");
                mysign.update();
            }
            if (s == 4) {
                Sign mysign = (Sign) Sign4.getState();
                mysign.setLine(0, "This Process is ");
                mysign.setLine(1, check + "% Done");
                mysign.setLine(2, "I require " + equation);
                mysign.setLine(3, "fuel");
                mysign.update();
            }

            if ((float) oilamthash.get(loc) >= equation) {

                oilamthash.replace(loc, start);
                if (Chess.getType().equals(Material.CHEST)) {
                    //Bukkit.broadcastMessage("I may deposit");
                    Chest chest = (Chest) Chess.getState();
                    Inventory inv = chest.getInventory();
                    if (inv.getContents().length <= 27) {
                        inv.addItem(oil);
                        loc.getWorld().playSound(loc, Sound.ENTITY_LLAMA_EAT, 3F, 1F);
                        new BukkitRunnable(){
                            Location ploc = loc.subtract(0,1,0);
                            double t = 0;
                            double r = 1.5;
                            public void run(){
                                t = t + Math.PI/16;
                                double x = r * Math.cos(t);
                                double y = 0.5 * t;
                                double z = r*Math.sin(t);
                                ploc.add(x,y,z);
                                ploc.getWorld().spawnParticle(Particle.FLAME, ploc, 0,0,0,0,1);
                                ploc.subtract(x,y,z);
                                if(t > Math.PI*4){
                                    this.cancel();
                                }
                            }
                        }.runTaskTimer(plugin, 0, 1);
                    }

                }
            }
        } else {
            oilamthash.put(loc, start);
        }
    }

    public boolean isOilRig(Block b) {
        Furnace f = (Furnace) b.getState();
        Directional Furnaced = (Directional) f.getBlockData();
        BlockFace targetFace = Furnaced.getFacing();
        Location front = f.getBlock().getRelative(targetFace).getLocation();
        ArrayList<Block> pbrs = new ArrayList<Block>();
        Block Pbr1 = b.getLocation().add(1, 0, 0).getBlock();
        Block Pbr2 = b.getLocation().add(-1, 0, 0).getBlock();
        Block Pbr3 = b.getLocation().add(0, 0, 1).getBlock();
        Block Pbr4 = b.getLocation().add(0, 0, -1).getBlock();
        Block Anvil = b.getLocation().add(0, 1, 0).getBlock();
        Block Chest = front.add(0, -1, 0).getBlock();
        Block Hoppa = b.getLocation().add(0,-1,0).getBlock();
        
        int check = 0;

        pbrs.add(Pbr1);
        pbrs.add(Pbr2);
        pbrs.add(Pbr3);
        pbrs.add(Pbr4);
        int num = 0;
        for (int i = 0; i < pbrs.size(); i++) {
            if (pbrs.get(i).getType().equals(Material.POLISHED_BLACKSTONE_BRICK_WALL)) {
                num += 1;
            }
        }
        if (Chest.getType().equals(Material.CHEST) && Anvil.getType().equals(Material.HOPPER) && Hoppa.getType().equals(Material.HOPPER) && num >= 2) {
            return true;
        }
        //p.sendMessage(Chest.toString() + Anvil.toString());
        //p.sendMessage(b.getLocation().add(0,1,0).getBlock().getType().name()+ p.getWorld().getBlockAt(Iron1).getState().toString() + " " + Iron2.getBlock().getType().name() +" "+Iron3.getBlock().getType().name()+" "+Iron4.getBlock().getType().name() + Anvil.getBlock().getType().name() + " "+ Chest.getBlock().getType().name());
        return false;

    }

    private static final int getoilamtBlock(Location loc, int range) {
        int amt = 0;
        Vector vector = new Vector(0, -1, 0);
        BlockIterator iter = new BlockIterator(loc.getWorld(), loc.toVector(), vector, 0, 100);
        Block lastBlock = iter.next();
        iter.next();
        while (iter.hasNext()) {
            lastBlock = iter.next();
            if (lastBlock.getType() == Material.COBBLESTONE_WALL) {
                Block lastXup = lastBlock.getLocation().add(1, 0, 0).getBlock();
                Block lastXDown = lastBlock.getLocation().add(-1, 0, 0).getBlock();
                Block lastZup = lastBlock.getLocation().add(0, 0, 1).getBlock();
                Block lastzdown = lastBlock.getLocation().add(0, 0, -1).getBlock();
                if (!lastXup.getType().equals(Material.AIR) && !lastZup.getType().equals(Material.AIR) && !lastXDown.getType().equals(Material.AIR) && !lastzdown.getType().equals(Material.AIR)) {
                    amt += 1;
                }
                continue;
            }
            break;
        }
        //Bukkit.broadcastMessage("This has" + amt + "pipes");
        return amt;
        
    }

}
