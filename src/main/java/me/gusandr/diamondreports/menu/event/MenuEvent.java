package me.gusandr.diamondreports.menu.event;

import me.gusandr.diamondreports.Plugin;
import me.gusandr.diamondreports.menu.ReportMenu;
import me.gusandr.diamondreports.menu.item.ItemReport;
import me.gusandr.diamondreports.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class MenuEvent implements Listener {
    private Map<UUID, Integer> hashMap = new HashMap<>();
    private ItemReport currentItemReport;

    @EventHandler
    public void onOpenInventory(InventoryOpenEvent e) {
        UUID playerUUID = e.getView().getPlayer().getUniqueId();

        if (isReportMenu(e, "Репорты.")) {
            int updateValue = hashMap.getOrDefault(playerUUID, 0) + 1;
            hashMap.put(playerUUID, updateValue);
        }
    }

    @EventHandler
    public void onCloseInventory(InventoryCloseEvent e) {
        UUID playerUUID = e.getView().getPlayer().getUniqueId();

        if (isReportMenu(e, "Репорты.")) {
            hashMap.remove(playerUUID);
        }
    }

    @EventHandler
    public void onClickInventoryReport(InventoryClickEvent e) {
            Player p = (Player) e.getView().getPlayer();
            UUID playerUUID = e.getView().getPlayer().getUniqueId();

            if (e.getCurrentItem() != null) {
                if (isReportMenu(e, "Репорты.")) {
                    e.setCancelled(true);
                boolean isSortMenu = e.getInventory().getItem(ReportMenu.getSlotItemSortBy()).getItemMeta().getLore()
                        .contains(ChatColor.translateAlternateColorCodes('&',"&7&l&nПо старости.&f"));

                if (e.getSlot() == ReportMenu.getSlotItemSortBy()) {
                    ReportMenu.fillMenu(e.getInventory(), hashMap.getOrDefault(playerUUID, 1), !isSortMenu);
                    return;
                } else if (e.getSlot() == ReportMenu.getSlotItemUpdate()) {
                    ReportMenu.fillMenu(e.getInventory(),
                            hashMap.getOrDefault(playerUUID, 1),
                            isSortMenu);
                    return;
                } else if (e.getSlot() == ReportMenu.getSlotNextMenu()) {
                    if (ReportMenu.getItemStackReports(hashMap.getOrDefault(playerUUID, 1) + 1, isSortMenu).length < 1) {
                        ReportMenu.fillMenu(e.getInventory(), hashMap.getOrDefault(playerUUID, 1) + 1, isSortMenu);
                        int updateValue = hashMap.getOrDefault(playerUUID, 1) + 1;
                        hashMap.put(playerUUID, updateValue);
                    }
                    return;
                } else if (e.getSlot() == ReportMenu.getSlotLastMenu()) {
                    if (hashMap.getOrDefault(playerUUID, 1) - 1 > 0) {
                        ReportMenu.fillMenu(e.getInventory(), hashMap.getOrDefault(playerUUID, 1) - 1, isSortMenu);
                        int updateValue = hashMap.getOrDefault(playerUUID, 2) - 1;
                        hashMap.put(playerUUID, updateValue);
                    }
                    return;
                } else {
                    // При клике по репорту

                    List<ItemReport> list = ReportMenu.getItemReports();
                    ItemReport currentItemReport = null;
                    for (ItemReport itemReport : list)
                        if (itemReport.getItemStack().getItemMeta().getLore().equals(e.getCurrentItem().getItemMeta().getLore()))
                            currentItemReport = itemReport;
                    if (currentItemReport == null) {
                        e.getView().getPlayer().closeInventory();
                        e.getView().getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&cТакого репорта не существует!"));
                        return;
                    }
                    this.currentItemReport = currentItemReport;

                    if (!(isModHasReport(e)))
                        return;

                    for (ItemReport itemReport : ReportMenu.getItemReports()) {
                        if (!(itemReport.isModPlayerNull())
                                && itemReport.getModPlayer().equals(playerUUID)
                                && !(itemReport.equals(currentItemReport))) {
                            e.getView().getPlayer().closeInventory();
                            e.getView().getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    "&cВы уже взяли один репорт &n"
                                            + itemReport.getName()));
                                return;
                            }
                        }
                    }

                    Inventory inventory = Bukkit.createInventory(null, 27, "Меню действий");
                    inventory.setItem(12, Utils.createItemStack(Material.GREEN_BANNER, "Взять репорт на себя!"));
                    inventory.setItem(14, Utils.createItemStack(Material.YELLOW_CANDLE, "Удалить репорт!"));
                    p.openInventory(inventory);
                } else if (isReportMenu(e, "Меню действий")) {
                    e.setCancelled(true);
                    if (e.getCurrentItem() != null) {
                        List<ItemReport> list = ReportMenu.getItemReports();

                        if (e.getSlot() == 12 && currentItemReport.isModPlayerNull()) {

                            if (!(isModHasReport(e)))
                                return;

                            e.getViewers().removeIf(entity -> !(entity.getUniqueId() == playerUUID));
                            currentItemReport.takeUpReport(playerUUID);
                            e.getView().getPlayer().closeInventory();
                            teleportPlayer(e);
                            e.getView().getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    "&cУспех!"));
                            return;
                        } else if (e.getSlot() == 14) {
                            list.remove(currentItemReport);
                            e.getView().getPlayer().closeInventory();
                            e.getView().getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    "&cУспех!"));
                        }

                        ReportMenu.setItemReports(list);
                    }
                }
            }
        }

    private boolean isReportMenu(InventoryEvent e, String inventoryName) {
        String inventoryOpenName = e.getView().getTitle();
        return (inventoryOpenName.hashCode() == inventoryName.hashCode()) && inventoryOpenName.equals(inventoryName);
    }

    private void teleportPlayer(InventoryEvent e) {
        try {
            e.getView().getPlayer().teleport(Bukkit.getPlayer(currentItemReport.getSuspectPlayer()).getLocation());
        } catch (Exception exception) {
            e.getView().getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cТакого игрока сейчас нет на сервере!"));
            e.getView().getPlayer().closeInventory();
        }
    }

    private boolean isModHasReport(InventoryEvent e) {
        if (!(currentItemReport.isModPlayerNull())
                && !(Bukkit.getPlayer(currentItemReport.getModPlayer()).getName().equals(e.getView().getPlayer().getName()))) {
            e.getView().getPlayer().closeInventory();
            e.getView().getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cЭтот репорт занят другим администратором &n"
                            + Bukkit.getPlayer(currentItemReport.getModPlayer()).getName()));
            return false;
        }
        return true;
    }
}
