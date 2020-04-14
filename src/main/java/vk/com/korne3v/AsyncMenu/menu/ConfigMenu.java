package vk.com.korne3v.AsyncMenu.menu;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.jline.internal.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import vk.com.korne3v.AsyncMenu.AsyncMenu;
import vk.com.korne3v.AsyncMenu.api.MenuAPI;
import vk.com.korne3v.AsyncMenu.commands.interfaces.AbstractCommand;
import vk.com.korne3v.AsyncMenu.configs.Messages;
import vk.com.korne3v.AsyncMenu.hooks.PlaceholderAPIHook;
import vk.com.korne3v.AsyncMenu.proxy.BungeeMessageListener;
import vk.com.korne3v.AsyncMenu.utils.ItemUtil;

import java.util.ArrayList;
import java.util.List;

public class ConfigMenu {

    private AbstractCommand abstractCommand;

    public ConfigMenu(final AsyncMenu asyncMenu, final ConfigurationSection section, final String command){

        abstractCommand = new AbstractCommand(command) {
            @Override
            public boolean execute(CommandSender sender, String[] args) {


                if(sender instanceof Player) {

                    Menu menu = AsyncMenu.getApi().create(section.getString("title"), section.getInt("rows"))
                            .setRemoveOnClose(true);

                    generate(section, menu, (Player) sender);

                    BukkitRunnable task = new BukkitRunnable() {
                        @Override
                        public void run() {

                            if(!menu.isRegistered()){
                                this.cancel();
                                return;
                            }

                            generate(section, menu, (Player) sender);

                        }
                    };

                    task.runTaskTimerAsynchronously(asyncMenu, 20L, 20L);

                    menu.open((Player) sender);

                }else {
                    sender.sendMessage(Messages.CONSOLE_USE_COMMAND);
                }

                return true;
            }
        };
    }

    public void generate(final ConfigurationSection section, final Menu menu, final Player player) {

        for (String item : section.getConfigurationSection("items").getKeys(false)) {

            List<String> lore = new ArrayList<>();

            for (String string : section.getStringList("items." + item + ".lore")) {
                lore.add(PlaceholderAPIHook.replace(player, string.replace("{player}", player.getName())));
            }

            menu.setItem(section.getInt("items." + item + ".slot"),
                    new ItemUtil(section.getString("items." + item + ".icon"))
                            .setNamed(section.getString("items." + item + ".name"))
                            .setLore(lore)
                    , (clickplayer, type) -> {

                        for (String command : section.getStringList("items." + item + ".execute")) {

                            if (command.startsWith("[close]")) {

                                clickplayer.closeInventory();

                            } else {

                                command = command.replace("{player}", clickplayer.getName());

                                if (command.startsWith("[message]")) {
                                    clickplayer.sendMessage(command
                                            .replace('&', '§')
                                            .replace("[message] ", "")
                                            .replace("[message]", "")
                                    );
                                } else if (command.startsWith("[server]")) {
                                    BungeeMessageListener.send(clickplayer, command
                                            .replace("[server]", "")
                                            .replace("[server] ", "")
                                    );
                                } else if (command.startsWith("[console]")) {
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command
                                            .replace("[console]", "")
                                            .replace("[console] ", "")
                                    );
                                } else {
                                    Bukkit.dispatchCommand(clickplayer, command.replaceFirst("/", ""));
                                }
                            }

                        }

                    });
        }

    }

    public AbstractCommand getCommand() {
        return abstractCommand;
    }
}
