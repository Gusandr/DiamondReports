package me.gusandr.diamondreports.menu;

import me.gusandr.diamondreports.menu.item.ItemReport;
import me.gusandr.diamondreports.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ReportMenu {
    private Inventory inventory;
    private static int slotItemUpdate;
    private static int slotItemSortBy;
    private static int slotNextMenu;
    private static int slotLastMenu;
    private static List<ItemReport> itemReports;

    public ReportMenu(int pageNumber) {
        slotItemUpdate = 40;
        slotItemSortBy = 38;
        slotNextMenu = 43;
        slotLastMenu = 42;
        if (itemReports != null)
            itemReports = new LinkedList<>(itemReports);
        else
            itemReports = new LinkedList<>();
        this.inventory = Bukkit.createInventory(null, 45, "Репорты."); // 45 - размер меню.

        fillMenu(inventory, pageNumber, false);
    }

    public static void addItemReport(ItemReport itemReport) {
        if (itemReports != null)
            itemReports = new LinkedList<>(itemReports);
        else
            itemReports = new LinkedList<>();
        itemReports.add(itemReport);
    }

    public static int getSlotItemUpdate() {
        return 40;
    }

    public static int getSlotItemSortBy() {
        return 38;
    }

    public static int getSlotNextMenu() {
        return 43;
    }

    public static int getSlotLastMenu() {
        return 42;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public static List<ItemReport> getItemReports() {
        if (itemReports != null)
            itemReports = new LinkedList<>(itemReports);
        else
            itemReports = new LinkedList<>();
        return itemReports;
    }

    public static ItemStack[] getItemStackReports(int pageNumber, boolean isSort) {
        if (itemReports != null)
            itemReports = new LinkedList<>(itemReports);
        else
            itemReports = new LinkedList<>();
        List<ItemReport> list = new LinkedList<>(itemReports);

        list.removeIf(item -> Bukkit.getPlayer(item.getSuspectPlayer()) == null);
        if (!isSort) {
            Collections.sort(list);
        }

        ItemStack[] reportsItem = new ItemStack[list.size()];

        for (int i = 36 * (pageNumber - 1); i < 36 && i < list.size(); i++) {
            if (list.get(i) != null)
                reportsItem[i - (36 * (pageNumber - 1))] = list.get(i).getItemStack();
            else
                break;
        }

        return reportsItem;
    }

    public static void fillMenu(Inventory menu, int pageNumber, boolean isSort) {
        menu.setContents(new ItemStack[]{}); // Удаляем все элементы меню.
        getItemStackReports(pageNumber, isSort);
        menu.setContents(getItemStackReports(pageNumber, isSort));

        menu.setItem(slotItemUpdate, Utils.createItemStack(Material.NETHER_STAR, "Обновить"));
        menu.setItem(slotNextMenu, Utils.createItemStack(Material.ARROW, "Следующая страница"));
        menu.setItem(slotLastMenu, Utils.createItemStack(Material.ARROW, "Прошлая страница"));

        if (!isSort) {
            menu.setItem(slotItemSortBy, Utils.createItemStack(Material.NETHER_STAR, "Сортировка", Arrays.asList(
                    ChatColor.translateAlternateColorCodes('&',"&7&l&nПо новизне.&f"),
                    ChatColor.translateAlternateColorCodes('&',"&7&lПо старости&f"))));
        } else {
            menu.setItem(slotItemSortBy, Utils.createItemStack(Material.NETHER_STAR, "Сортировка", Arrays.asList(
                    ChatColor.translateAlternateColorCodes('&',"&7&lПо новизне&f"),
                    ChatColor.translateAlternateColorCodes('&',"&7&l&nПо старости.&f"))));;
        }
        // Поясняю за магическое число 36 - это максимально возможное количество предметов с репортами на одной странице.
    }

    public static void setItemReports(List<ItemReport> itemReports) {
        ReportMenu.itemReports = itemReports;
    }

    public static boolean deleteReport(UUID playerSuspectUUID) {
        if (!containsReport(playerSuspectUUID)) {
            itemReports.removeIf(report -> report.getSuspectPlayer().equals(playerSuspectUUID));
            return true;
        } else {
            return false;
        }
    }

    public static boolean containsReport(UUID playerSuspectUUID) {
        if (itemReports != null) {
            List<ItemReport> list = new LinkedList<>(itemReports);
            list.removeIf(report -> !(report.getSuspectPlayer().equals(playerSuspectUUID)));
            return list.isEmpty();
        } else {
            return true;
        }
    }
}
