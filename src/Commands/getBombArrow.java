/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Commands;

import net.md_5.bungee.api.ChatColor;
import oilrigs.OilRigs;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

/**
 *
 * @author storm
 */
public class getBombArrow implements CommandExecutor{
 private OilRigs plugin;
 
public getBombArrow (OilRigs plugin) {
this.plugin = plugin;
}
    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        if(cs instanceof Player){
            Player p = (Player) cs;
            if(p.hasPermission("oilrigs.Commands")){
                ItemStack OilArrow = new ItemStack(Material.TIPPED_ARROW);
                    PotionMeta oilArrowMeta =(PotionMeta) OilArrow.getItemMeta();
                    oilArrowMeta.setBasePotionData(new PotionData(PotionType.POISON));
                    oilArrowMeta.setDisplayName(ChatColor.RED + "Bomb Arrow");
                    OilArrow.setItemMeta(oilArrowMeta);
                p.getInventory().addItem(OilArrow);
                p.updateInventory();
            }
        }
        return true;
    }
    
}
