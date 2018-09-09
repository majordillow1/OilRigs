/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oilrigs;

import Commands.GetMolotov;
import Commands.ReloadConfigCommand;
import Commands.getBombArrow;
import Listeners.BombArrow;
import Listeners.Landmines;
import Listeners.Tazer;
import Listeners.deadBattery;
import Listeners.molotov;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.md_5.bungee.api.ChatColor;
import oilrigs.Listeners.GeneratorListener;

import oilrigs.Listeners.OilrigListener;
import oilrigs.Listeners.Refinery;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitScheduler;

/**
 *
 * @author knightmare
 */
public class OilRigs extends JavaPlugin {

    public static Plugin plugin;
    FileConfiguration config = this.getConfig();

    @Override
    public void onEnable() {
        plugin = this;
        this.getCommand("oilReload").setExecutor(new ReloadConfigCommand(this));
        this.getCommand("getmolotov").setExecutor(new GetMolotov(this));
        this.getCommand("getbombarrow").setExecutor(new getBombArrow(this));
        this.getServer().getPluginManager().registerEvents(new OilrigListener(this), this);
        this.getServer().getPluginManager().registerEvents(new Refinery(this), this);
        this.getServer().getPluginManager().registerEvents(new BombArrow(this), this);
        this.getServer().getPluginManager().registerEvents(new molotov(this), this);
        this.getServer().getPluginManager().registerEvents(new Landmines(this), this);
        this.getServer().getPluginManager().registerEvents(new GeneratorListener(this), this);
        this.getServer().getPluginManager().registerEvents(new deadBattery(), this);
        this.getServer().getPluginManager().registerEvents(new Tazer(this), this);
        setupRecipie();

        ItemStack oil = new ItemStack(Material.POTION);
        PotionMeta pMeta = (PotionMeta) oil.getItemMeta();
        pMeta.setColor(Color.BLACK);
        pMeta.setDisplayName("OIL");
        oil.setItemMeta(pMeta);

        createConfig();
        try {
            readLocations();
        } catch (IOException ex) {
            Logger.getLogger(OilRigs.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(OilRigs.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            readGeneratorLocations();
        } catch (IOException ex) {
            Logger.getLogger(OilRigs.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(OilRigs.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void onDisable() {
        plugin = null;
        try {
            writeLocations();
        } catch (IOException ex) {
            Logger.getLogger(OilRigs.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            writeGeneratorLocations();
        } catch (IOException ex) {
            Logger.getLogger(OilRigs.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Plugin getPlugin() {
        return plugin;
    }

    public static ShapelessRecipe OilArrow(NamespacedKey k) {
        ItemStack OilArrow = new ItemStack(Material.TIPPED_ARROW);
        PotionMeta oilArrowMeta = (PotionMeta) OilArrow.getItemMeta();

        oilArrowMeta.setBasePotionData(new PotionData(PotionType.POISON));
        oilArrowMeta.setDisplayName(ChatColor.RED + "Bomb Arrow");
        OilArrow.setItemMeta(oilArrowMeta);
        ShapelessRecipe OilArrowRec = new ShapelessRecipe(k, OilArrow);
        OilArrowRec.addIngredient(Material.POTION);
        OilArrowRec.addIngredient(Material.ARROW);
        return OilArrowRec;
    }

    public static ShapelessRecipe Molotov(NamespacedKey k) {
        ItemStack molotov = new ItemStack(Material.SPLASH_POTION);
        PotionMeta meta = (PotionMeta) molotov.getItemMeta();
        meta.setColor(Color.RED);
        meta.setDisplayName(ChatColor.RED + "Molotov");
        molotov.setItemMeta(meta);
        ShapelessRecipe Molotovrev = new ShapelessRecipe(k, molotov);
        Molotovrev.addIngredient(Material.POTION);
        Molotovrev.addIngredient(Material.PAPER);
        return Molotovrev;
    }

    public static ShapelessRecipe Taser(NamespacedKey k) {

        ItemStack taser = new ItemStack(Material.LEVER);
        ItemMeta meta = taser.getItemMeta();
        List<String> taslist = new ArrayList<String>();
        taslist.add(ChatColor.BLUE + "Full Taser");
        meta.setLore(taslist);
        meta.setDisplayName(ChatColor.RED + "TASER");
        taser.setItemMeta(meta);
        ShapelessRecipe Molotovrev = new ShapelessRecipe(k, taser);
        Molotovrev.addIngredient(Material.LEVER);
        Molotovrev.addIngredient(Material.REDSTONE);
        Molotovrev.addIngredient(Material.NETHER_BRICK);

        return Molotovrev;
    }

    public static ShapelessRecipe TaserRecharge(NamespacedKey k) {
        ItemStack taser = new ItemStack(Material.LEVER);
        ItemMeta meta = taser.getItemMeta();
        List<String> taslist = new ArrayList<String>();
        taslist.add(org.bukkit.ChatColor.BLUE + "Full Taser");
        meta.setLore(taslist);
        meta.setDisplayName(ChatColor.RED + "TASER");
        taser.setItemMeta(meta);
        ShapelessRecipe Molotovrev = new ShapelessRecipe(k, taser);
        Molotovrev.addIngredient(Material.LEVER);
        Molotovrev.addIngredient(Material.NETHER_BRICK);

        return Molotovrev;
    }

    public static ShapedRecipe Landmine(NamespacedKey k) {
        ItemStack landmine = new ItemStack(Material.GRASS);
        landmine.setDurability((short) 1);
        ItemMeta im = landmine.getItemMeta();
        im.setDisplayName(org.bukkit.ChatColor.BLUE + "Landmine");
        landmine.setItemMeta(im);
        ShapedRecipe Landmine = new ShapedRecipe(k, landmine);
        Landmine.shape("   ", "LM ", "COR");
        Landmine.setIngredient('C', Material.COMPARATOR);
        Landmine.setIngredient('R', Material.REDSTONE);
        Landmine.setIngredient('O', Material.POTION);
        Landmine.setIngredient('L', Material.LEVER);
        Landmine.setIngredient('M', new MaterialData(Material.GRASS, (byte) 1));
        return Landmine;

    }

    public static ShapedRecipe Generator(NamespacedKey k) {
        ItemStack generator = new ItemStack(Material.BLACK_SHULKER_BOX);

        ItemMeta im = generator.getItemMeta();
        im.setDisplayName(org.bukkit.ChatColor.BLUE + "Generator");
        generator.setItemMeta(im);
        ShapedRecipe genrep = new ShapedRecipe(k, generator);
        genrep.shape("RS ", "CJO", "GSL");
        genrep.setIngredient('S', Material.SHULKER_SHELL);
        genrep.setIngredient('C', Material.COMPARATOR);
        genrep.setIngredient('J', Material.CHEST);
        genrep.setIngredient('O', Material.POTION);
        genrep.setIngredient('G', Material.POTION);
        genrep.setIngredient('R', Material.REDSTONE);
        genrep.setIngredient('L', Material.LEVER);
        return genrep;

    }

    public static ShapedRecipe dedBattery(NamespacedKey k) {
        ItemStack battery = new ItemStack(Material.NETHER_BRICK);
        ItemMeta batt = battery.getItemMeta();
        List<String> deadbatt = new ArrayList<String>();
        deadbatt.add(org.bukkit.ChatColor.BLUE + "Dead Battery");
        batt.setDisplayName("Battery");
        batt.setLore(deadbatt);
        battery.setItemMeta(batt);
        ShapedRecipe genrep = new ShapedRecipe(k, battery);
        genrep.shape("L L", "SSS", "ORO");
        genrep.setIngredient('S', Material.NETHER_BRICK);
        genrep.setIngredient('L', Material.LEVER);
        genrep.setIngredient('O', Material.POTION);
        genrep.setIngredient('R', Material.REPEATER);
        return genrep;

    }

    public void setupRecipie() {
        if (this.getConfig().getBoolean("canCraftBombArrow")) {
            NamespacedKey key1 = new NamespacedKey(this, this.getDescription().getName() + "_two");
            getServer().addRecipe(OilArrow(key1));
        }
        if (this.getConfig().getBoolean("canCraftMolotov")) {
            NamespacedKey key2 = new NamespacedKey(this, this.getDescription().getName() + "three");
            getServer().addRecipe(Molotov(key2));
        }
        NamespacedKey key3 = new NamespacedKey(this, this.getDescription().getName() + "five");
        getServer().addRecipe(Generator(key3));
        NamespacedKey key4 = new NamespacedKey(this, this.getDescription().getName() + "six");
        getServer().addRecipe(dedBattery(key4));
        if (this.getConfig().getBoolean("canCraftLandmine")) {
            NamespacedKey key2 = new NamespacedKey(this, this.getDescription().getName() + "four");
            getServer().addRecipe(Landmine(key2));
        }
        NamespacedKey key6 = new NamespacedKey(this, this.getDescription().getName() + "seven");
        getServer().addRecipe(Taser(key6));
        NamespacedKey key7 = new NamespacedKey(this, this.getDescription().getName() + "seeven");
        getServer().addRecipe(TaserRecharge(key7));

        List<String> ls = new ArrayList<String>();
        ls.add(ChatColor.BLUE + "Used for powering Generators");
        ItemStack gas = new ItemStack(Material.POTION);
        PotionMeta im = (PotionMeta) gas.getItemMeta();
        im.setDisplayName(ChatColor.GREEN + "Gas");
        im.setLore(ls);
        im.setColor(Color.GREEN);
        gas.setItemMeta(im);
        FurnaceRecipe f = new FurnaceRecipe(gas, Material.POTION);
        getServer().addRecipe(f);

    }

    private void createConfig() {
        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }
            File file = new File(getDataFolder(), "config.yml");
            if (!file.exists()) {
                getLogger().info("Config.yml not found, creating!");
                saveDefaultConfig();
            } else {
                getLogger().info("Config.yml found, loading!");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }
    public static ArrayList<Location> LandMineLocs = new ArrayList<Location>();

    public void writeLocations() throws IOException {
        if (!LandMineLocs.isEmpty()) {

            File locsf = new File(getDataFolder() + File.separator + "LandMines.dat");
            if (!locsf.exists()) {
                getDataFolder().mkdirs();
                locsf.getParentFile().mkdirs();
                locsf.createNewFile();
            }
            ObjectOutputStream output = new ObjectOutputStream((new FileOutputStream(locsf)));
            ArrayList<Map<String, Object>> rawLocations = new ArrayList<Map<String, Object>>();
            for (Location loc : LandMineLocs) {
                Map<String, Object> rawLoc = loc.serialize();
                rawLocations.add(rawLoc);
            }
            output.writeObject(rawLocations);
            output.flush();
            output.close();
        }
    }

    @SuppressWarnings("unchecked")
    public void readLocations() throws FileNotFoundException, IOException, ClassNotFoundException {

        File locsf = new File(getDataFolder() + File.separator + "LandMines.dat");

        if (locsf.exists()) {
            ObjectInputStream input = new ObjectInputStream((new FileInputStream(locsf)));
            //Reads the first object in
            Object readObject = input.readObject();
            input.close();

            ArrayList<Map<String, Object>> rawLocations = new ArrayList<Map<String, Object>>();
            rawLocations = (ArrayList<Map<String, Object>>) readObject;
            for (Map<String, Object> rawLoc : rawLocations) {
                Location loc = Location.deserialize(rawLoc);
                LandMineLocs.add(loc);
            }
            for (int i = 0; i < LandMineLocs.size(); i++) {
                if (LandMineLocs.get(i).getBlock() == null) {
                    LandMineLocs.remove(i);
                }
                if (LandMineLocs.get(i).getBlock().getType() == null) {
                    LandMineLocs.remove(i);
                }
                if (!LandMineLocs.get(i).getBlock().getType().equals(Material.GRASS)) {
                    LandMineLocs.remove(i);
                }
            }
        }

    }
    public static ArrayList<Location> GeneratorLocs = new ArrayList<Location>();

    public void writeGeneratorLocations() throws IOException {
        if (!GeneratorLocs.isEmpty()) {

            File locsf = new File(getDataFolder() + File.separator + "GenLocs.dat");
            if (!locsf.exists()) {
                getDataFolder().mkdirs();
                locsf.getParentFile().mkdirs();
                locsf.createNewFile();
            }
            ObjectOutputStream output = new ObjectOutputStream((new FileOutputStream(locsf)));
            ArrayList<Map<String, Object>> rawLocations = new ArrayList<Map<String, Object>>();
            for (Location loc : GeneratorLocs) {
                Map<String, Object> rawLoc = loc.serialize();
                rawLocations.add(rawLoc);
            }
            output.writeObject(rawLocations);
            output.flush();
            output.close();
        }
    }

    @SuppressWarnings("unchecked")
    public void readGeneratorLocations() throws FileNotFoundException, IOException, ClassNotFoundException {

        File locsf = new File(getDataFolder() + File.separator + "GenLocs.dat");

        if (locsf.exists()) {
            ObjectInputStream input = new ObjectInputStream((new FileInputStream(locsf)));
            //Reads the first object in
            Object readObject = input.readObject();
            input.close();

            ArrayList<Map<String, Object>> rawLocations = new ArrayList<Map<String, Object>>();
            rawLocations = (ArrayList<Map<String, Object>>) readObject;
            for (Map<String, Object> rawLoc : rawLocations) {
                Location loc = Location.deserialize(rawLoc);
                GeneratorLocs.add(loc);
            }
        }

    }
}
