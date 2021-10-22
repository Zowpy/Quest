package me.zowpy.quest.menu;

import io.github.zowpy.menu.Button;
import io.github.zowpy.menu.ItemBuilder;
import io.github.zowpy.menu.pagination.PaginatedMenu;
import me.zowpy.quest.player.Profile;
import me.zowpy.quest.quest.Quest;
import me.zowpy.quest.quest.QuestType;
import me.zowpy.quest.utils.CC;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * This Project is property of Zowpy © 2021
 * Redistribution of this Project is not allowed
 *
 * @author Zowpy
 * Created: 10/22/2021
 * Project: Quest
 */

public class QuestMenu extends PaginatedMenu {

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "&7Quests";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Profile profile = Profile.getByUUID(player.getUniqueId());
        final Map<Integer, Button> toReturn = new HashMap<>();

        int i = 0;
        for (Quest quest : Quest.getQuests()) {
            if (quest == null) continue;
            if (profile.getFinishedQuests().contains(quest.getName())) continue;

            toReturn.put(i, new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    ItemBuilder itemBuilder = new ItemBuilder(Material.PAPER);

                    if (profile.getQuest() == quest) {
                        itemBuilder.enchantment(Enchantment.DURABILITY, 1);
                    }

                    ItemStack item = itemBuilder.name(CC.GREEN + quest.getName())
                            .lore(Arrays.asList(
                                    "&cType: &f" + quest.getType().getName(),
                                    "&cRequirements: &f" + (profile.getQuest() == quest ? profile.getProgress() : "0") + "/" + quest.getAmount(),
                                    quest.getType() == QuestType.BREAK || quest.getType() == QuestType.PLACE ? "&cBlock: &f" + quest.getBlock().name() :
                                    quest.getType() == QuestType.MOB ? "&cMob: &f" + quest.getEntityType().getName() :
                                    quest.getType() == QuestType.COMMAND ? "&cCommand: " + quest.getCommand() : ""
                            )).build();

                    ItemMeta itemMeta = item.getItemMeta();
                    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    item.setItemMeta(itemMeta);

                    return item;
                }

                @Override
                public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                    if (profile.getQuest() != null) {
                        player.sendMessage(CC.RED + "You already have a running quest. finish it then you can start a new one");
                        return;
                    }

                    profile.setQuest(quest);
                    profile.setProgress(0);
                    profile.save();

                    player.sendMessage(CC.GREEN + "Successfully started " + quest.getName() + "!");
                }
            });
            i++;
        }

        return toReturn;
    }
}
