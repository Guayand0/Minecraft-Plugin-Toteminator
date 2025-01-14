package totem.Guayando;

import totem.Guayando.api.*;
import totem.Guayando.commands.*;
import totem.Guayando.events.EntityResurrect;
import totem.Guayando.managers.*;
import totem.Guayando.utils.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Toteminator extends JavaPlugin implements Listener {
    public static String prefix = "&4[&6&lToteminator&4]";
    public String version = getDescription().getVersion();
    public String latestversion;
    public boolean updateCheckerWork = true;
    public static boolean PlaceholderAPI = true;
    //public static boolean Vault = true;
    private Economy economy;
    private LanguageManager languageManager;
    public static int spigotID = 120470; // Spigot 119147
    public static int bstatsID = 23748; // Bstats 23748

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage("&9<------------------------------------>"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(prefix + " &fEnabled, (&aVersion: &b" + version + "&f)"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(prefix + " &6Thanks for using my plugin :)"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(prefix + " &eMade by &dGuayando"));
        // Verificar si existe un plugin de economia
        /*if (setupEconomy()) {
            Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(prefix + " &aVault found and linked successfully."));
        } else {
            Vault = false;
        }*/

        saveDefaultConfig();
        this.languageManager = new LanguageManager(this);
        registrarComandos();
        registrarEventos();
        startPlaceholderUpdateTask();

        // Crear variables PlaceholderAPI
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(prefix + " &bPlaceholderAPI detected."));
            /*Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(prefix + " &bPlaceholderAPI detected. Registering placeholders..."));
            try {
                new PlaceholderAPIToteminator(this).register();
                Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(prefix + " &aToteminator placeholders registered successfully."));
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(prefix + " &cError registering placeholders: " + e.getMessage()));
                e.printStackTrace();
            }*/
        }  else {
            PlaceholderAPI = false;
        }
        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage("&9<------------------------------------>"));

        // Ejecutar comprobarActualizaciones() después de que el servidor haya iniciado completamente
        new BukkitRunnable() {
            @Override
            public void run() {
                comprobarActualizaciones();
            }
        }.runTask(this); // Ejecuta la tarea en el siguiente tick

        Metrics metrics = new Metrics(this, bstatsID); // Bstats
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(prefix + " &fDisabled, (&aVersion: &b" + version + "&f)"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(prefix + " &6Thanks for using my plugin :)"));
    }

    public void registrarComandos() {
        this.getCommand("toteminator").setExecutor(new ComandoPrincipal(this));
        // TabComplete
        this.getCommand("toteminator").setTabCompleter(new TabComplete());
    }

    public void registrarEventos() {
        getServer().getPluginManager().registerEvents(new UpdateChecker(this), this);
        getServer().getPluginManager().registerEvents(new EntityResurrect(this), this);
    }

    public LanguageManager getLanguageManager() {
        return languageManager;
    }

    // Método para comprobar ultima version
    public void comprobarActualizaciones() {
        try {
            HttpURLConnection con = (HttpURLConnection) new URL("https://api.spigotmc.org/legacy/update.php?resource=" + spigotID).openConnection();
            int timed_out = 5000;
            con.setConnectTimeout(timed_out);
            con.setReadTimeout(timed_out);
            latestversion = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();

            if (compareVersions(version, latestversion) < 0) {
                Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(prefix + "&bThere is a new version available. &f(&7" + latestversion + "&f)"));
                Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(prefix + "&bYou can download it at:&f https://www.spigotmc.org/resources/" + spigotID + "/"));
                updateCheckerWork = true;
            } else {
                Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(prefix + "&aYou are using the last version. &f(&b" + version + "&f)"));
            }
        } catch (Exception ex) {
            updateCheckerWork = false;
            Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(prefix + "&cError while checking update."));
        }
    }
    // Método para comparar versiones
    public static int compareVersions(String currentVersion, String latestVersion) {
        String[] currentParts = currentVersion.split("\\.");
        String[] latestParts = latestVersion.split("\\.");

        for (int i = 0; i < Math.min(currentParts.length, latestParts.length); i++) {
            int currentPart = Integer.parseInt(currentParts[i]);
            int latestPart = Integer.parseInt(latestParts[i]);
            if (latestPart > currentPart) {
                return -1; // La versión más reciente es mayor
            } else if (latestPart < currentPart) {
                return 1; // La versión actual es mayor
            }
        }
        return Integer.compare(latestParts.length, currentParts.length); // Comparar longitud si son iguales hasta el mínimo
    }
    // Por si falla la comprobación de actualización
    public boolean getUpdateCheckerWork(){
        return updateCheckerWork;
    }
    // Version actual del plugin
    public String getVersion() {
        return this.version;
    }
    // Ultima version según en spigot
    public String getLatestVersion() {
        return this.latestversion;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) { return false; }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) { return false; } economy = rsp.getProvider(); return economy != null;
    }

    public Economy getEconomy() {
        return economy;
    }

    public void startPlaceholderUpdateTask() {
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                try {
                    // Aquí llamamos al método que aplica los placeholders para cada jugador conectado
                    MessageUtils.applyPlaceholdersAndColor(player, null, this, Toteminator.getPlaceholderAPI());
                } catch (NullPointerException e) {
                    // Manejar el error y registrar la excepción para depuración
                    //Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(prefix + " &cError al aplicar placeholders para el jugador " + player.getName() + ": " + e.getMessage()));
                    //e.printStackTrace();
                } catch (Exception e) {
                    // Manejar cualquier otra excepción que pueda ocurrir
                    Bukkit.getConsoleSender().sendMessage(MessageUtils.getColoredMessage(prefix + " &cError Toteminator startPlaceholderUpdateTask"));
                    e.printStackTrace();
                }
            }
        }, 0L, 40L); // 40 ticks son 2 segundos (20 ticks = 1 segundo)
    }

    public static boolean getPlaceholderAPI(){
        return PlaceholderAPI;
    }
}