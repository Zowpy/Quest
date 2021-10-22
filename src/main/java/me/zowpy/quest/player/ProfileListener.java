package me.zowpy.quest.player;

import me.zowpy.quest.quest.Quest;
import me.zowpy.quest.quest.QuestType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.print.DocFlavor;

/**
 * This Project is property of Zowpy © 2021
 * Redistribution of this Project is not allowed
 *
 * @author Zowpy
 * Created: 10/22/2021
 * Project: Quest
 */

public class ProfileListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Profile profile = new Profile(player.getUniqueId());
        profile.load();
        Profile.getProfiles().add(profile);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        Profile profile = Profile.getByUUID(player.getUniqueId());

        if (profile != null) {
            profile.save();
            Profile.getProfiles().remove(profile);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        Profile profile = Profile.getByUUID(player.getUniqueId());

        if (profile.getQuest() == null) return;

        Quest quest = profile.getQuest();

        if (quest.getType() != QuestType.RUN) return;

        if (event.getTo() == null || event.getTo() == event.getFrom()) return;
        if (event.getTo().getX() == event.getFrom().getX() && event.getTo().getZ() == event.getFrom().getZ()) return;

        double distance = Math.hypot(event.getTo().getX() - event.getFrom().getX(), event.getTo().getZ() - event.getFrom().getZ());

        if (distance >= 0.14D) {
            profile.incrementProgress();
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        Profile profile = Profile.getByUUID(player.getUniqueId());

        if (profile.getQuest() == null) return;
        if (profile.getQuest().getType() != QuestType.COMMAND) return;

        Quest quest = profile.getQuest();

        if (event.getMessage().equalsIgnoreCase(quest.getCommand())) {
            profile.incrementProgress();
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        Profile profile = Profile.getByUUID(player.getUniqueId());

        if (profile.getQuest() == null) return;
        if (profile.getQuest().getType() != QuestType.PLACE) return;

        if (event.getBlockPlaced().getType() == profile.getQuest().getBlock()) {
            profile.incrementProgress();
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        Profile profile = Profile.getByUUID(player.getUniqueId());

        if (profile.getQuest() == null) return;
        if (profile.getQuest().getType() != QuestType.BREAK) return;

        if (event.getBlock().getType() == profile.getQuest().getBlock()) {
            profile.incrementProgress();
        }
    }

    @EventHandler
    public void onMobKill(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            Profile profile = Profile.getByUUID(player.getUniqueId());

            if (profile.getQuest() == null) return;
            if (profile.getQuest().getType() != QuestType.MOB) return;

            if (event.getEntity().getType() == profile.getQuest().getEntityType()) {
                profile.incrementProgress();
            }
        }
    }
}
