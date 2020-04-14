package vk.com.korne3v.AsyncMenu.commands.interfaces;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.jetbrains.annotations.Async;
import vk.com.korne3v.AsyncMenu.AsyncMenu;

import java.lang.reflect.Field;
import java.util.*;

public abstract class AbstractCommand {

    public static CommandMap commandMap = getCommandMap();
    public static List<AbstractCommand> commands = new ArrayList<>();

    public AbstractCommand(final String command, final String... aliases){
        try {

            if (commandMap == null) {
                System.out.println("Failed to register command "+command+" because CommandMap is empty");
                return;
            }

            this.command = command;

            Command executor = new Command(command, "", "/" + command, Arrays.asList(aliases)) {
                @Override
                public boolean execute(CommandSender sender, String s, String[] args) {
                    try {
                        return AbstractCommand.this.execute(sender, args);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return true;
                    }
                }
            };

            commandMap.register(AsyncMenu.getApi().getPlugin().getDescription().getName(), executor);

            commands.add(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static SimpleCommandMap getCommandMap() {
        try {
            String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            Class<?> craftServerClass = Class.forName("org.bukkit.craftbukkit." + version + ".CraftServer");
            Object craftServerObject = craftServerClass.cast((Object) Bukkit.getServer());
            Field commandMapField = craftServerClass.getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            return (SimpleCommandMap) commandMapField.get(craftServerObject);
        } catch (IllegalAccessException | ClassNotFoundException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String command;

    public static void unregisterAll() {

        Iterator iterator = commands.iterator();
        while (iterator.hasNext()) {

            AbstractCommand abstractCommand = (AbstractCommand) iterator.next();

            if(!abstractCommand.command.equalsIgnoreCase("asyncmenu")) {
                try {

                    Command command = getKnownCommandMap().get(abstractCommand.command);
                    command.unregister(commandMap);

                    getKnownCommandMap().remove(abstractCommand.command);
                    getKnownCommandMap().remove(AsyncMenu.getApi().getPlugin().getDescription().getName() +":"+abstractCommand.command);

                    iterator.remove();

                } catch (Exception e) {
                    System.out.println("Could not unregister command " + abstractCommand.command);
                }
            }

        }
    }

    private static Map<String, Command> getKnownCommandMap() throws NoSuchFieldException, IllegalAccessException {

        Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
        knownCommandsField.setAccessible(true);

        return  (Map<String, Command>) knownCommandsField.get(commandMap);
    }

    public abstract boolean execute(CommandSender sender, String[] args);
}
