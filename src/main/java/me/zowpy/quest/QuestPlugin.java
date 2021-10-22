package me.zowpy.quest;

import io.github.zowpy.menu.MenuAPI;
import lombok.Getter;
import me.zowpy.quest.command.QuestCommand;
import me.zowpy.quest.database.Database;
import me.zowpy.quest.player.Profile;
import me.zowpy.quest.player.ProfileListener;
import me.zowpy.quest.quest.Quest;
import me.zowpy.quest.utils.ConfigFile;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class QuestPlugin extends JavaPlugin {

    @Getter private static QuestPlugin instance;

    private Database mongoHandler;
    private ConfigFile settingsFile, questsFile;

    @Override
    public void onEnable() {
        instance = this;

        settingsFile = new ConfigFile(this, "settings");
        questsFile = new ConfigFile(this, "quests");

        mongoHandler = new Database(settingsFile.getString("mongo.uri"));

        Quest.init();

        new MenuAPI(this);
        getServer().getPluginManager().registerEvents(new ProfileListener(), this);
        getCommand("quests").setExecutor(new QuestCommand());
    }

    @Override
    public void onDisable() {
        Profile.getProfiles().forEach(Profile::save);

        instance = null;
    }
}
