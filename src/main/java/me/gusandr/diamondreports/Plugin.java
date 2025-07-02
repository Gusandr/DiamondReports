package me.gusandr.diamondreports;

import me.gusandr.diamondreports.menu.ReportMenu;
import me.gusandr.diamondreports.menu.event.MenuEvent;
import me.gusandr.diamondreports.menu.item.ItemReport;
import me.gusandr.diamondreports.report.command.ReportCMD;
import me.gusandr.diamondreports.report.command.ReportCompleter;
import me.gusandr.diamondreports.menu.command.ReportMenuCMD;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public final class Plugin extends JavaPlugin {

    private static Plugin INSTANCE;

    public static Plugin getINSTANCE() {
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        saveDefaultConfig();
        registerEvents();

        getCommand("reports").setExecutor(new ReportMenuCMD());
        getCommand("report").setExecutor(new ReportCMD());
        getCommand("report").setTabCompleter(new ReportCompleter());
        getServer().getPluginManager().registerEvents(new MenuEvent(), this);

        getServer().getLogger().info(ChatColor.AQUA +
                "\n ____    __  __  ____    ______  __  __  ____    ____       \n" +
                "/\\  _`\\ /\\ \\/\\ \\/\\  _`\\ /\\  _  \\/\\ \\/\\ \\/\\  _`\\ /\\  _`\\     \n" +
                "\\ \\ \\L\\_\\ \\ \\ \\ \\ \\,\\L\\_\\ \\ \\L\\ \\ \\ `\\\\ \\ \\ \\/\\ \\ \\ \\L\\ \\   \n" +
                " \\ \\ \\L_L\\ \\ \\ \\ \\/_\\__ \\\\ \\  __ \\ \\ , ` \\ \\ \\ \\ \\ \\ ,  /   \n" +
                "  \\ \\ \\/, \\ \\ \\_\\ \\/\\ \\L\\ \\ \\ \\/\\ \\ \\ \\`\\ \\ \\ \\_\\ \\ \\ \\\\ \\  \n" +
                "   \\ \\____/\\ \\_____\\ `\\____\\ \\_\\ \\_\\ \\_\\ \\_\\ \\____/\\ \\_\\ \\_\\\n" +
                "    \\/___/  \\/_____/\\/_____/\\/_/\\/_/\\/_/\\/_/\\/___/  \\/_/\\/ /\n" +
                "                                                            \n" +
                "                                                            ");
        getServer().getLogger().info( ChatColor.GREEN +
                "Плагин был разработан Gusandr!\nТекущая версия плагина: " + this.getDescription().getVersion()
                + "\nТех. поддержка: ebli (discord)");
    }

    public void registerEvents() {
        litebans.api.Events.get().register(new litebans.api.Events.Listener() {
            @Override
            public void entryAdded(litebans.api.Entry entry) {
                if (entry.getType().equals("ban")) {
                    UUID playerSuspectUUID = UUID.fromString(entry.getUuid());
                    ReportMenu.deleteReport(playerSuspectUUID);
                }
            }
        });
    }
}
