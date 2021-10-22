package me.zowpy.quest.quest;

import lombok.Getter;

/**
 * This Project is property of Zowpy © 2021
 * Redistribution of this Project is not allowed
 *
 * @author Zowpy
 * Created: 10/22/2021
 * Project: Quest
 */

@Getter
public enum QuestType {

    MOB("Kill mobs"), BREAK("Break blocks"), PLACE("Place blocks"), COMMAND("Execute commands"), RUN("Run");

    String name;

    QuestType(String name) {
        this.name = name;
    }
}
