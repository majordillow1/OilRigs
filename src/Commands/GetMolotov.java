/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Commands;

import net.md_5.bungee.api.ChatColor;
import oilrigs.OilRigs;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

/**
 *
 * @author storm
 */
public class GetMolotov implements CommandExecutor {
    private OilRigs plugin;
 
public GetMolotov (OilRigs plugin) {
this.plugin = plugin;
}

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
         if(cs instanceof Player){
             Player p = (Player) cs;
             if(p.hasPermission("oilrigs.Commands")){
                 ItemStack molotov = new ItemStack(Material.SPLASH_POTION);
                PotionMeta meta = (PotionMeta) molotov.getItemMeta();
                meta.setColor(Color.RED);
                meta.setDisplayName(ChatColor.RED + "Molotov");
                molotov.setItemMeta(meta);
            p.getInventory().addItem(molotov);
            p.updateInventory();
             }
             
         }
        

        return true;
    }
}
