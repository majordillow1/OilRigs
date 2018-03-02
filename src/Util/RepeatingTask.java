/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

/**
 *
 * @author storm
 */


import oilrigs.OilRigs;
import org.bukkit.Bukkit;

public abstract class RepeatingTask implements Runnable {

        private int taskId;

        public RepeatingTask(OilRigs plugin, int arg1, int arg2) {
            taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, arg1, arg2);
        }

        public void canncel() {
            Bukkit.getScheduler().cancelTask(taskId);
        }

    }