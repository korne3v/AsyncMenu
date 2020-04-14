package vk.com.korne3v.AsyncMenu.menu.interfaces;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public interface InventoryClick {

    void onClick(Player clickplayer, ClickType clickType);

}
