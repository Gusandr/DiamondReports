package me.gusandr.diamondreports.menu.command;

import me.gusandr.diamondreports.Plugin;
import me.gusandr.diamondreports.menu.ReportMenu;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ReportMenuCMD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        final FileConfiguration config = Plugin.getINSTANCE().getConfig();

        String noPlayerSend = "text.message.no-player-send";
        String noPermPlayer = "text.message.no-perm-player";
        String itWorked = "text.message.it-worked";
        String permModerator = "diamondreports.moderator";

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes(
                    '&', config.getString(noPlayerSend)));
            return true;
        }

        Player sender = (Player) commandSender;

        if (!sender.hasPermission(permModerator)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes(
                    '&', config.getString(noPermPlayer)));
            return true;
        }

        sender.openInventory(new ReportMenu(1).getInventory());
        sender.sendMessage(ChatColor.translateAlternateColorCodes(
                '&', config.getString(itWorked)));

        return true;
    }
}
