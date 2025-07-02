package me.gusandr.diamondreports.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

public class Utils {
    public static ItemStack createItemStack(Material material, String name) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack createItemStack(Material material, String name, List<String> lore) {
        ItemStack itemStack = createItemStack(material, name);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public static ItemStack createItemStack(Material material, String name, List<String> lore, SkullMeta skullMeta) {
        ItemStack itemStack = createItemStack(material,name);

        skullMeta.setLore(lore);
        itemStack.setItemMeta(skullMeta);

        return itemStack;
    }
}
