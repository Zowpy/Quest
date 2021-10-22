package me.zowpy.quest.player;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import lombok.Setter;
import me.zowpy.quest.QuestPlugin;
import me.zowpy.quest.quest.Quest;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * This Project is property of Zowpy © 2021
 * Redistribution of this Project is not allowed
 *
 * @author Zowpy
 * Created: 10/22/2021
 * Project: Quest
 */

@Getter @Setter
public class Profile {

    @Getter private static List<Profile> profiles = new LinkedList<>();

    private final UUID uuid;
    private List<String> finishedQuests = new ArrayList<>();
    private Quest quest;
    private int progress = 0;

    public Profile(UUID uuid) {
        this.uuid = uuid;
    }

    public void save() {
        CompletableFuture.runAsync(() -> {
            QuestPlugin.getInstance().getMongoHandler().getProfiles().replaceOne(Filters.eq("uuid", uuid.toString()), toBson(), new ReplaceOptions().upsert(true));
        });
    }

    public void load() {
        Document document = QuestPlugin.getInstance().getMongoHandler().getProfiles().find(Filters.eq("uuid", uuid.toString())).first();

        if (document == null) {
            save();
            return;
        }

        finishedQuests = document.getList("finishedQuests", String.class);

        String questName = document.getString("quest");

        if (!questName.equals("null")) {
            quest = Quest.getByName(questName);
        }

        progress = document.getInteger("progress");
    }

    public Document toBson() {
        return new Document("uuid", uuid.toString())
                .append("finishedQuests", finishedQuests)
                .append("quest", quest == null ? "null" : quest.getName())
                .append("progress", progress);
    }

    public void incrementProgress() {
        progress++;
        if (progress >= quest.getAmount()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), quest.getRewardCommand().replace("<player>", getPlayer().getName()));
            progress = 0;
            finishedQuests.add(quest.getName());
            quest = null;
            save();
        }
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public static Profile getByUUID(UUID uuid) {
        return profiles.stream().filter(profile -> profile.getUuid() == uuid)
                .findFirst().orElse(null);
    }
}
