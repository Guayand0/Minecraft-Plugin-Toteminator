package totem.Guayando.utils;

import totem.Guayando.Toteminator;
import totem.Guayando.managers.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UpdateChecker implements Listener{
    private final Toteminator plugin;
    private final LanguageManager languageManager;
    public UpdateChecker(Toteminator plugin){
        this.plugin = plugin;
        this.languageManager = plugin.getLanguageManager();
    }
    @EventHandler
    public void CheckUpdate(PlayerJoinEvent event) {
        try{
            Player player = event.getPlayer();
            boolean updateChecker = plugin.getConfig().getBoolean("config.update-checker");
            boolean updateCheckerWork = plugin.getUpdateCheckerWork();

            // Verificar si el mensaje de actualización debe enviarse
            if (updateChecker && (Toteminator.compareVersions(plugin.version, plugin.latestversion) < 0)) {
                if (player.isOp() || player.hasPermission("toteminator.updatechecker") || player.hasPermission("toteminator.admin")) {
                    languageManager.reloadLanguage(); // Recargar el archivo de idioma
                    if(!updateCheckerWork){
                        plugin.comprobarActualizaciones();
                    }
                    String messagePath = "config.update-checker";
                    MessageUtils.sendMessageWithPlaceholdersAndColor(player, messagePath, plugin, Toteminator.getPlaceholderAPI());
                }
            }
        }catch (NullPointerException e){
            Bukkit.getConsoleSender().sendMessage(e.getMessage());
            e.printStackTrace();
        }
    }
}