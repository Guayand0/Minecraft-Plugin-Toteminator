package totem.Guayando.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.potion.PotionEffect;
import totem.Guayando.Toteminator;
import totem.Guayando.managers.*;
import totem.Guayando.utils.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EntityResurrect implements Listener {

    private final Toteminator plugin;
    private final LanguageManager languageManager;

    public EntityResurrect(Toteminator plugin) {
        this.plugin = plugin;
        this.languageManager = plugin.getLanguageManager();
    }

    @EventHandler
    public void onEntityResurrect(EntityResurrectEvent event) {
        languageManager.reloadLanguage();
        plugin.reloadConfig();

        if (event.getEntity() instanceof Player) {

            boolean toteminatorUse = plugin.getConfig().getBoolean("config.toteminator-use");

            if (!toteminatorUse) {
                String messagePath = "config.toteminator-disabled";
                MessageUtils.sendMessageWithPlaceholdersAndColor((Player) event.getEntity(), messagePath, plugin, Toteminator.getPlaceholderAPI());
                return; // Si toteminator-use es false, no hacer nada
            }

            Player player = (Player) event.getEntity();

            // Verificar si el jugador tiene el permiso necesario
            if (!player.hasPermission("toteminator.use")) {
                String messagePath = "messages.no-perm";
                MessageUtils.sendMessageWithPlaceholdersAndColor(player, messagePath, plugin, Toteminator.getPlaceholderAPI());
                return; // Si el jugador no tiene el permiso, salir del evento
            }

            // Obtener la lista de mundos permitidos de la configuración
            List<String> allowedWorlds = plugin.getConfig().getStringList("config.allowed-worlds");

            // Verificar si el jugador está en uno de los mundos permitidos
            if (!allowedWorlds.contains(player.getWorld().getName())) {
                String messagePath = "messages.no-perm-world";
                MessageUtils.sendMessageWithPlaceholdersAndColor(player, messagePath, plugin, Toteminator.getPlaceholderAPI());
                return; // Si el mundo no está permitido, salir del evento
            }

            Collection<PotionEffect> effects = player.getActivePotionEffects();
            List<PotionEffect> effectsToRestore = new ArrayList<>(effects);

            // Esperar hasta que el jugador resucite completamente
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                for (PotionEffect effect : effectsToRestore) {
                    player.addPotionEffect(new PotionEffect(effect.getType(), effect.getDuration(), effect.getAmplifier(), effect.isAmbient(), effect.hasParticles()));
                }
            }, 1L);
        }
    }
}
