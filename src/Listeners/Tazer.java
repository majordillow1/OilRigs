package Listeners;

import java.util.ArrayList;
import java.util.List;

import oilrigs.OilRigs;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LlamaSpit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;


/**
 *
 * @author knightmare
 */
public class Tazer implements Listener {

    //taser is crafted with either full or dead battery resulting in ded or full battery
    //taser can be recharged in generator or with new full battery in crafting inv
    //set tazer craft events
    private OilRigs plugin;

    public Tazer(OilRigs plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void CheckCraft(PrepareItemCraftEvent e) {
        //craft taser'
        if (e.getRecipe() == null) {
            return;
        }
        if (e.getInventory() == null) {
            return;
        }
        if (e.getRecipe() instanceof ShapelessRecipe) {

            ShapelessRecipe s = (ShapelessRecipe) e.getRecipe();
            NamespacedKey key6 = new NamespacedKey(plugin, plugin.getDescription().getName() + "seven");
            if (s.getKey().equals(key6)) {
                
                boolean canCraft = true;
                boolean FullBatt = true;
                for (int i = 0; i < e.getInventory().getSize() - 1; i++) {
                    if (e.getInventory().getMatrix()[i] == null) {
                        continue;
                    }
                    ItemStack it = e.getInventory().getMatrix()[i];
                    if (it.getType().equals(Material.NETHER_BRICK)) {
                        if (it.hasItemMeta()) {
                            if (it.getItemMeta().hasDisplayName()) {
                                if (it.getItemMeta().hasLore()) {
                                    if (it.getItemMeta().getDisplayName().equals("Battery")) {
                                        //Debug.println("Should be null", "isnt");
                                        //canCraft = false;
                                        if (it.getItemMeta().getLore().contains(ChatColor.BLUE + "Dead Battery")) {
                                            //return dead tazer
                                            //Debug.println("Should be dead", "isnt");
                                            FullBatt = false;
                                        }

                                    } else {
                                        canCraft = false;
                                        //Debug.println("Should be null", "isnt");
                                    }
                                } else {
                                    canCraft = false;
                                    //Debug.println("Should be null", "isnt");
                                }
                            } else {
                                canCraft = false;
                                //Debug.println("Should be null", "isnt");
                            }
                        } else {
                            canCraft = false;
                            //Debug.println("Should be null", "isnt");
                        }

                    }
                    ItemStack taser = new ItemStack(Material.LEVER);
                    ItemMeta meta = taser.getItemMeta();
                    List<String> taslist = new ArrayList<String>();
                    taslist.add(ChatColor.BLUE + "Dead Taser");
                    meta.setLore(taslist);
                    meta.setDisplayName(ChatColor.RED + "TASER");
                    taser.setItemMeta(meta);
                    if (!canCraft) {
                        e.getInventory().setResult(null);
                        return;
                    }
                    if (!FullBatt) {

                        e.getInventory().setResult(taser);
                        return;
                    }
                }
            }
            NamespacedKey key7 = new NamespacedKey(plugin, plugin.getDescription().getName() + "seeven");
            if (s.getKey().equals(key7)) {
                boolean canMake = true;

                boolean isFullBatt = true;
                for (int i = 0; i < e.getInventory().getSize() - 1; i++) {
                    ItemStack item = e.getInventory().getMatrix()[i];
                    if (e.getInventory().getMatrix()[i] == null) {
                        continue;
                    }
                    if (e.getInventory().getMatrix()[i].getType().equals(Material.LEVER)) {
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
                        if (!item.getItemMeta().getDisplayName().equals(ChatColor.RED + "TASER")) {
                            canMake = false;
                            break;
                        }
                        if (!item.getItemMeta().getLore().contains(ChatColor.BLUE + "Dead Taser")) {
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
    //set tazer go off and get player and add player to list with task to take them off list 
    //set tazer in hand to dead tazer. 

    @EventHandler
    public void ShootTaser(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getItem() == null) {
                return;
            }
            if (!e.getItem().hasItemMeta()) {
                return;
            }
            if (!e.getItem().getItemMeta().hasDisplayName()) {
                return;
            }
            if (!e.getItem().getItemMeta().hasLore()) {
                return;
            }
            ItemStack taser = new ItemStack(Material.LEVER);
            ItemMeta meta = taser.getItemMeta();
            List<String> taslist = new ArrayList<String>();
            taslist.add(ChatColor.BLUE + "Dead Taser");
            meta.setLore(taslist);
            meta.setDisplayName(ChatColor.RED + "TASER");
            taser.setItemMeta(meta);
            if (e.getItem().getItemMeta().getDisplayName().equals(ChatColor.RED + "TASER") && e.getItem().getItemMeta().getLore().contains(ChatColor.BLUE + "Full Taser")) {
                //shoot taser here
                LlamaSpit llama = e.getPlayer().launchProjectile(LlamaSpit.class);
                llama.setMetadata("taze", new FixedMetadataValue(OilRigs.getPlugin(), true));
                if (e.getItem().getAmount() >= 2) {
                    e.getItem().setAmount(e.getItem().getAmount() - 1);
                } else {
                    e.getPlayer().getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                }

                e.getPlayer().getInventory().addItem(taser);
                //Debug.println("Should", "shoot taser");
            }
        }

    }
    ArrayList<Player> freezes = new ArrayList<Player>();

    @EventHandler
    public void hitPlayer(ProjectileHitEvent e) {
        if (e.getEntity() instanceof LlamaSpit) {
            if (!e.getEntity().hasMetadata("taze")) {
                return;
            }
            if (e.getHitEntity() == null) {
                return;
            }
            if (e.getHitEntity() instanceof Player) {
                Player p = (Player) e.getHitEntity();
                PotionEffect pe = new PotionEffect(PotionEffectType.CONFUSION, plugin.getConfig().getInt("TazerTime"), 2);
                p.sendMessage(ChatColor.RED + "You got Tazed!");
                p.addPotionEffect(pe);
                freezes.add(p);
                BukkitTask task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        freezes.remove(p);
                    }

                }.runTaskLater(plugin, plugin.getConfig().getInt("TazerTime"));
            }
        }
    }

    //set tazer not to be placed
    @EventHandler
    public void placeLever(BlockPlaceEvent e) {
        if (e.getItemInHand() == null) {
            return;
        }
        if (!e.getItemInHand().getType().equals(Material.LEVER)) {
            return;
        }
        if (!e.getItemInHand().hasItemMeta()) {
            return;
        }
        if (!e.getItemInHand().getItemMeta().hasDisplayName()) {
            return;
        }
        if (e.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.RED + "TASER")) {
            e.setCancelled(true);
        }
    }
    //get move event and if on the list cancel event

    @EventHandler
    public void playerMove(PlayerMoveEvent e) {
        if (e.getPlayer() == null) {
            return;
        }
        if (freezes.contains(e.getPlayer())) {
            e.setCancelled(true);
        }
    }
}
