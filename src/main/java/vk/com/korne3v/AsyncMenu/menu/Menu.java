package vk.com.korne3v.AsyncMenu.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import vk.com.korne3v.AsyncMenu.menu.interfaces.InventoryClick;
import vk.com.korne3v.AsyncMenu.utils.ItemUtil;

import java.util.HashMap;

public interface Menu {

    int getRows();

    String getTitle();

    Inventory getInventory();

    boolean isRegistered();
    void unregister();

    boolean isRemoveOnClose();
    Menu setRemoveOnClose(boolean removeOnClose);

    HashMap<Integer, InventoryClick> getItems();

    Menu addItem(ItemUtil gameItem, InventoryClick inventoryClick);
    Menu addItem(ItemStack itemStack, InventoryClick inventoryClick);

    Menu setItem(int slot, ItemUtil gameItem, InventoryClick inventoryClick);
    Menu setItem(int slot, ItemStack itemStack, InventoryClick inventoryClick);

    default void open(Player player) {
        if (getInventory() == null) {
            return;
        }

        player.openInventory(getInventory());
    }

}
