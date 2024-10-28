package totem.Guayando.commands;

import totem.Guayando.Toteminator;
import totem.Guayando.managers.*;
import totem.Guayando.utils.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ComandoPrincipal implements CommandExecutor {

    private final Toteminator plugin;
    private final LanguageManager languageManager;

    public ComandoPrincipal(Toteminator plugin) {
        this.plugin = plugin;
        this.languageManager = plugin.getLanguageManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        languageManager.reloadLanguage();
        plugin.reloadConfig();

        Player player = (Player) sender;

        if (!(player instanceof Player)) {
            // Consola
            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    languageManager.reloadLanguage();
                    plugin.reloadConfig();
                    String messagePath = "messages.reload";
                    MessageUtils.sendMessageWithPlaceholdersAndColor(player, messagePath, plugin, Toteminator.getPlaceholderAPI());
                    return true;
                }
                String messagePath = "messages.console-error";
                MessageUtils.sendMessageWithPlaceholdersAndColor(player, messagePath, plugin, Toteminator.getPlaceholderAPI());
                return true;
            }
            String messagePath = "messages.console-error";
            MessageUtils.sendMessageWithPlaceholdersAndColor(player, messagePath, plugin, Toteminator.getPlaceholderAPI());
            return true;
        }

        if (!player.hasPermission("toteminator.admin")) {
            String messagePath = "messages.no-perm";
            MessageUtils.sendMessageWithPlaceholdersAndColor(player, messagePath, plugin, Toteminator.getPlaceholderAPI());
            return true;
        }

        // /toteminator args[0] args[1] args[2]
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                // toteminator reload
                languageManager.reloadLanguage();
                plugin.reloadConfig();
                String messagePath = "messages.reload";
                MessageUtils.sendMessageWithPlaceholdersAndColor(player, messagePath, plugin, Toteminator.getPlaceholderAPI());
            } else if (args[0].equalsIgnoreCase("help")) {
                // toteminator help
                String messagePath = "messages.help";
                MessageUtils.sendMessageListWithPlaceholdersAndColor(player, messagePath, plugin, Toteminator.getPlaceholderAPI());
            } else if (args[0].equalsIgnoreCase("version")) {
                // toteminator version
                String messagePath = "messages.version";
                MessageUtils.sendMessageWithPlaceholdersAndColor(player, messagePath, plugin, Toteminator.getPlaceholderAPI());
            } else if (args[0].equalsIgnoreCase("author")) {
                // toteminator author
                String messagePath = "messages.author";
                MessageUtils.sendMessageWithPlaceholdersAndColor(player, messagePath, plugin, Toteminator.getPlaceholderAPI());
            }else if (args[0].equalsIgnoreCase("plugin")) {
                // toteminator plugin
                player.sendMessage(MessageUtils.applyPlaceholdersAndColor(player, "%plugin% &7%link%", plugin,  Toteminator.getPlaceholderAPI()));
            } else if (args[0].equalsIgnoreCase("permissions")) {
                // toteminator permissions
                String messagePath = "messages.permissions";
                MessageUtils.sendMessageListWithPlaceholdersAndColor(player, messagePath, plugin, Toteminator.getPlaceholderAPI());
            } else {
                // toteminator qwewe
                String messagePath = "messages.command-no-argument";
                MessageUtils.sendMessageWithPlaceholdersAndColor(player, messagePath, plugin, Toteminator.getPlaceholderAPI());
            }
        } else {
            // toteminator
            String messagePath = "messages.command-no-argument";
            MessageUtils.sendMessageWithPlaceholdersAndColor(player, messagePath, plugin, Toteminator.getPlaceholderAPI());
        }
        return true;
    }
}