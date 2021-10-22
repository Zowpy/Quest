package me.zowpy.quest.command;

import me.zowpy.quest.menu.QuestMenu;
import me.zowpy.quest.utils.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * This Project is property of Zowpy © 2021
 * Redistribution of this Project is not allowed
 *
 * @author Zowpy
 * Created: 10/22/2021
 * Project: Quest
 */

public class QuestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (commandSender instanceof Player) {
            new QuestMenu().openMenu(((Player) commandSender));
        }else {
            commandSender.sendMessage(CC.RED + "This is an in-game command only!");
        }

        return false;
    }
}
