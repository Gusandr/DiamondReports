package me.gusandr.diamondreports.report.command;

import me.gusandr.diamondreports.Plugin;
import me.gusandr.diamondreports.menu.ReportMenu;
import me.gusandr.diamondreports.menu.item.ItemReport;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ReportCMD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        final FileConfiguration config = Plugin.getINSTANCE().getConfig();
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes(
                    '&', config.getString("text.message.no-player-send")));
            return true;
        }

        Player sender = (Player) commandSender;

        if (!sender.hasPermission("diamondreports.send.report")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes(
                    '&', config.getString("text.message.no-perm-player")));
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes(
                    '&', config.getString("text.message.misuse")));
            return true;
        }

        Player playerReceiver = Bukkit.getPlayer(args[0]);

        if (playerReceiver == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes(
                    '&', config.getString("text.message.error-args")));
            return true;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            sb.append(args[i]);
            if (i < args.length - 1)
                sb.append(" ");
        }
        if (sb.length() > 21) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes(
                    '&', "&cТы превысил макс. количество символов в репорте - 21 символ!"));
            return true;
        }

        if (ReportMenu.containsReport(playerReceiver.getUniqueId())) {
            if (args.length == 1)
                ReportMenu.addItemReport(new ItemReport(
                        ((Player) commandSender).getUniqueId(),
                        playerReceiver.getUniqueId(),
                        new Date()));
            else
                ReportMenu.addItemReport(new ItemReport(
                        ((Player) commandSender).getUniqueId(),
                        playerReceiver.getUniqueId(),
                        new Date(),
                        sb.toString()));

            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.hasPermission("diamondreports.moderator")) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes(
                            '&', config.getString("text.message.new-report-send")));
                }
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cНа такого игрока уже есть репорт!"));
            return true;
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes(
                '&', config.getString("text.message.it-worked")));

        return true;
    }
}
