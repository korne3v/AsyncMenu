package vk.com.korne3v.AsyncMenu.commands;

import org.bukkit.command.CommandSender;
import vk.com.korne3v.AsyncMenu.AsyncMenu;
import vk.com.korne3v.AsyncMenu.commands.interfaces.AbstractCommand;
import vk.com.korne3v.AsyncMenu.configs.Messages;

public class MenuCommand extends AbstractCommand {

    public MenuCommand() {
        super("asyncmenu", "am");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {

        if(args.length == 0){
            sender.sendMessage(" ");
            sender.sendMessage("             §c§lAsyncMenu "+AsyncMenu.getApi().getPlugin().getDescription().getVersion());
            sender.sendMessage("           §fAuthor: §akorne3v");
            sender.sendMessage(" ");
            sender.sendMessage("§8 * §7§o/asyncmenu reload");
            sender.sendMessage(" ");
            return true;
        }

        if(!sender.hasPermission("asyncmenu.*") && !sender.isOp()){
            sender.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        if(args[0].equalsIgnoreCase("reload")){

            AsyncMenu.getCfg().load();
            AsyncMenu.getMsgCfg().load();

            sender.sendMessage(Messages.RELOAD);
            return true;
        }

        return true;
    }
}
