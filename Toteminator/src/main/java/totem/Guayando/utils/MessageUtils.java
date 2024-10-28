package totem.Guayando.utils;

import totem.Guayando.Toteminator;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import java.util.List;

public class MessageUtils {

    private static String playerName;

    public static String getColoredMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String applyPlaceholdersAndColor(Player player, String message, Toteminator plugin, boolean PlaceholderAPIWorks) {
        if (player != null) {
            playerName = player.getName();
        }

        // Reemplazar los placeholders
        message = message
                .replaceAll("%plugin%", Toteminator.prefix)
                .replaceAll("%version%", plugin.getVersion())
                .replaceAll("%latestversion%", plugin.getLatestVersion())
                .replaceAll("%link%", "https://www.spigotmc.org/resources/" + Toteminator.spigotID + "/")
                .replaceAll("%author%", plugin.getDescription().getAuthors().toString())
                // ------------------------------------------ //
                .replaceAll("%playerName%", playerName)
                ;

        if (PlaceholderAPIWorks) {
            // Aplicar color y placeholders con PlaceholderAPI
            return getColoredMessage(PlaceholderAPI.setPlaceholders(player, message));
        } else {
            // Aplicar color y placeholders
            return getColoredMessage(message);
        }
    }

    // String messagePath = "messages.version";
    // MessageUtils.sendMessageWithPlaceholdersAndColor(player, messagePath, plugin, Toteminator.getPlaceholderAPI());
    public static void sendMessageWithPlaceholdersAndColor(Player player, String messagePath, Toteminator plugin, boolean PlaceholderAPIWorks) {
        String message = plugin.getLanguageManager().getMessage(messagePath);
        if (player != null) {
            message = applyPlaceholdersAndColor(player, message, plugin, PlaceholderAPIWorks);
            player.sendMessage(message);
        }
    }

    // String messagePath = "messages.help";
    // MessageUtils.sendMessageListWithPlaceholdersAndColor(player, messagePath, plugin, Toteminator.getPlaceholderAPI());
    public static void sendMessageListWithPlaceholdersAndColor(Player player, String messagePath, Toteminator plugin, boolean PlaceholderAPIWorks) {
        List<String> messages = plugin.getLanguageManager().getStringList(messagePath);
        if (player != null) {
            for (String msg : messages) {
                String processedMessage = applyPlaceholdersAndColor(player, msg, plugin, PlaceholderAPIWorks);
                player.sendMessage(processedMessage);
            }
        }
    }
}