package me.gusandr.diamondreports.menu.item;

import club.minnced.discord.webhook.WebhookClient;
import me.gusandr.diamondreports.Plugin;
import me.gusandr.diamondreports.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

public class ItemReport implements Comparable<ItemReport> {
    private UUID whoSendReport;
    private UUID suspectPlayer;
    private UUID modPlayer; // Модератор, который принял репорт.
    private String moreInfo;
    private Date dateSendReport;

    public ItemReport(UUID whoSendReport, UUID suspectPlayer, Date dateSendReport) {
        this.whoSendReport = whoSendReport;
        this.suspectPlayer = suspectPlayer;
        this.dateSendReport = dateSendReport;
        this.modPlayer = null;
    }

    public ItemReport(UUID whoSendReport, UUID suspectPlayer, Date dateSendReport, String moreInfo) {
        this.whoSendReport = whoSendReport;
        this.suspectPlayer = suspectPlayer;
        this.dateSendReport = dateSendReport;
        this.moreInfo = moreInfo;
        this.modPlayer = null;
    }

    public ItemStack getItemStack() {
        SkullMeta itemMeta = (SkullMeta) new ItemStack(Material.PLAYER_HEAD).getItemMeta();
        itemMeta.setOwningPlayer(Bukkit.getOfflinePlayer(suspectPlayer));
        return Utils.createItemStack(Material.PLAYER_HEAD, getName(), getLore(), itemMeta);
    }

    public String getName() {
        return replaceHolders(Plugin.getINSTANCE().getConfig().getString(("item-report.name")));
    }

    public List<String> getLore() {
        List<String> lore = Plugin.getINSTANCE().getConfig().getStringList(("item-report.lore"));

        lore.replaceAll(this::replaceHolders);
        return lore;
    }

    public void takeUpReport(UUID modPlayer) {
        this.modPlayer = modPlayer;

        SimpleDateFormat sdf = new SimpleDateFormat(Plugin.getINSTANCE().getConfig().getString("date.format"));
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        WebhookClient client = WebhookClient.withUrl(Plugin.getINSTANCE().getConfig().getString("discord.webhooks"));
        client.send("Модератор **" + Bukkit.getOfflinePlayer(modPlayer).getName() + "** принял жалобу на игрока **"
                + Bukkit.getOfflinePlayer(this.suspectPlayer).getName() + "**\nПричина жалобы: *" + moreInfo
                + "*\nТекущая дата: **" + sdf.format(new Date())
                + "**\nДата отправки репорта: *" + sdf.format(new Date()) + "*"
                + "\n**Анархия:** " + Plugin.getINSTANCE().getConfig().getString("anarchy.number")
                + "\n-----------------------------------------------------------");
    }

    private String replaceHolders(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat(Plugin.getINSTANCE().getConfig().getString("date.format"));
        sdf.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        boolean complaintTaken = modPlayer != null;
        String complainTaken = complaintTaken ? "Занят" : "Свободен";
        if (moreInfo == null) moreInfo = "-";

        return ChatColor.translateAlternateColorCodes('&', str
                .replace("%whoSendReport", Bukkit.getOfflinePlayer(this.whoSendReport).getName())
                .replace("%suspectPlayer", Bukkit.getOfflinePlayer(this.suspectPlayer).getName())
                .replace("%complaintTaken", complainTaken)
                .replace("%date", sdf.format(this.dateSendReport))
                .replace("%moreInfo", moreInfo));
    }

    public UUID getSuspectPlayer() {
        return suspectPlayer;
    }

    public UUID getModPlayer() {
        return modPlayer;
    }
    public boolean isModPlayerNull() {
        return modPlayer == null;
    }

    @Override
    public int compareTo(ItemReport o) {
        return Long.compare(o.dateSendReport.getTime(), this.dateSendReport.getTime());
    }

    @Override
    public String toString() {
        return "ItemReport{" +
                "whoSendReport=" + whoSendReport +
                ", suspectPlayer=" + suspectPlayer +
                ", modPlayer=" + modPlayer +
                ", moreInfo='" + moreInfo + '\'' +
                ", dateSendReport=" + dateSendReport +
                '}';
    }
}
