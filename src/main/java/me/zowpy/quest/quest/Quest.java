package me.zowpy.quest.quest;

import lombok.Getter;
import lombok.Setter;
import me.zowpy.quest.QuestPlugin;
import me.zowpy.quest.utils.ConfigFile;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

import java.util.LinkedList;
import java.util.List;

/**
 * This Project is property of Zowpy © 2021
 * Redistribution of this Project is not allowed
 *
 * @author Zowpy
 * Created: 10/22/2021
 * Project: Quest
 */

@Getter @Setter
public class Quest {

    @Getter private static List<Quest> quests = new LinkedList<>();

    private String name, rewardCommand;
    private QuestType type;
    private int amount;
    private EntityType entityType;
    private Material block;
    private String command;

    public Quest(String name) {
        this.name = name;
    }

    public static void init() {
        ConfigFile configFile = QuestPlugin.getInstance().getQuestsFile();

        if (configFile.getConfigurationSection("quests") == null) return;

        for (String key : configFile.getConfigurationSection("quests").getKeys(false)) {
            if (key == null) continue;
            ConfigurationSection section = configFile.getConfigurationSection("quests." + key);

            Quest quest = new Quest(key);
            quest.setAmount(section.getInt("amount"));
            quest.setType(QuestType.valueOf(section.getString("type").toUpperCase()));
            quest.setRewardCommand(section.getString("reward"));

            switch (quest.getType()) {
                case BREAK:
                case PLACE: {
                    quest.setBlock(Material.valueOf(section.getString("block").toUpperCase()));
                    break;
                }

                case MOB: {
                    quest.setEntityType(EntityType.valueOf(section.getString("mob")));
                    break;
                }

                case COMMAND: {
                    quest.setCommand(section.getString("command"));
                    break;
                }
            }

            quests.add(quest);
        }
    }

    public static Quest getByName(String name) {
        return quests.stream().filter(quest -> quest.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }
}
