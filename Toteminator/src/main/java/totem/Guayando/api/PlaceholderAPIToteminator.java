/*package totem.Guayando.api;

import totem.Guayando.Toteminator;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class PlaceholderAPIToteminator extends PlaceholderExpansion {

    // We get an instance of the plugin later.
    private final Toteminator plugin;

    public PlaceholderAPIToteminator(Toteminator plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean persist(){
        return true;
    }
    @Override
    public boolean canRegister(){
        return true;
    }
    @Override
    public String getAuthor(){
        return "Guayando";
    }
    @Override
    public String getIdentifier() {
        return "toteminator"; // %toteminator_XXXXX%
    }
    @Override
    public String getVersion(){
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        plugin.getLanguageManager().reloadLanguage();
        plugin.reloadConfig();

        if (player == null) {
            return "";
        }

        String playerName = player.getName();

// ----------------------------------------------------- USER ----------------------------------------------------- //

        // %toteminator_player_name%
        if (identifier.equals("player_name")) {
            return playerName; // Retorna el nombre del jugador actual
        }

// --------------------------------------------------- -------- --------------------------------------------------- //
        return null;
    }
}*/