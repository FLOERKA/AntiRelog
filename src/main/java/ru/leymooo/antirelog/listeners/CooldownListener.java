package ru.leymooo.antirelog.listeners;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import ru.leymooo.antirelog.config.Settings;
import ru.leymooo.antirelog.manager.CooldownManager;
import ru.leymooo.antirelog.manager.CooldownManager.CooldownType;
import ru.leymooo.antirelog.manager.PvPManager;
import ru.leymooo.antirelog.util.Utils;
import ru.leymooo.antirelog.util.VersionUtils;

import java.util.concurrent.TimeUnit;

public class CooldownListener implements Listener {

    private final CooldownManager cooldownManager;
    private final PvPManager pvpManager;
    private final Settings settings;

    public CooldownListener(Plugin plugin, CooldownManager cooldownManager, PvPManager pvpManager, Settings settings) {
        this.cooldownManager = cooldownManager;
        this.pvpManager = pvpManager;
        this.settings = settings;
        registerEntityResurrectEvent(plugin);
    }

    private void registerEntityResurrectEvent(Plugin plugin) {
        if (VersionUtils.isVersion(11)) {
            plugin.getServer().getPluginManager().registerEvents(new Listener() {
                @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
                public void onResurrect(EntityResurrectEvent event) {
                    if (event.getEntityType() != EntityType.PLAYER) {
                        return;
                    }
                    Player player = (Player) event.getEntity();
                    long cooldownTime = settings.getTotemCooldown();
                    if (cooldownTime == 0 || pvpManager.isBypassed(player)) {
                        return;
                    }
                    if (cooldownTime <= -1) {
                        cancelEventIfInPvp(event, CooldownType.TOTEM, player);
                        return;
                    }
                    cooldownTime = cooldownTime * 1000;
                    if (checkCooldown(player, CooldownType.TOTEM, cooldownTime)) {
                        event.setCancelled(true);
                        return;
                    }
                    cooldownManager.addCooldown(player, CooldownType.TOTEM);
                }
            }, plugin);
        }
    }

    @EventHandler
    public void onItemEat(PlayerItemConsumeEvent event) {
        ItemStack consumeItem = event.getItem();

        CooldownType cooldownType = null;

        long cooldownTime = 0;

        if (isChorus(consumeItem)) {
            cooldownType = CooldownType.CHORUS;
            cooldownTime = settings.getСhorusCooldown();
        }
        if (isGoldenOrEnchantedApple(consumeItem)) {
            boolean enchanted = isEnchantedGolderApple(consumeItem);
            cooldownType = enchanted ? CooldownType.ENC_GOLDEN_APPLE : CooldownType.GOLDEN_APPLE;
            cooldownTime = enchanted ? settings.getEnchantedGoldenAppleCooldown() : settings.getGoldenAppleCooldown();
        }

        if (cooldownType != null) {
            if (cooldownTime == 0 || pvpManager.isBypassed(event.getPlayer())) {
                return;
            }
            if (cooldownTime <= -1) {
                cancelEventIfInPvp(event, cooldownType, event.getPlayer());
                return;
            }
            cooldownTime = cooldownTime * 1000;
            if (checkCooldown(event.getPlayer(), cooldownType, cooldownTime)) {
                event.setCancelled(true);
                return;
            }
            cooldownManager.addCooldown(event.getPlayer(), cooldownType);
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPerlLaunch(ProjectileLaunchEvent e) {
        if (settings.getEnderPearlCooldown() > 0 && e.getEntityType() == EntityType.ENDER_PEARL && e.getEntity().getShooter() instanceof Player) {
            Player p = (Player) e.getEntity().getShooter();
            if (!pvpManager.isBypassed(p)) {
                cooldownManager.addCooldown(p, CooldownType.ENDER_PEARL);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onInteract(PlayerInteractEvent event) {
        if (settings.getEnderPearlCooldown() == 0 || !event.hasItem() || event.getItem().getType() != Material.ENDER_PEARL || pvpManager.isBypassed(event.getPlayer())) {
            return;
        }

        if (settings.getEnderPearlCooldown() <= -1) {
            cancelEventIfInPvp(event, CooldownType.ENDER_PEARL, event.getPlayer());
            return;
        }

        if (checkCooldown(event.getPlayer(), CooldownType.ENDER_PEARL, settings.getEnderPearlCooldown() * 1000)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        cooldownManager.remove(event.getPlayer());
    }

    private boolean isChorus(ItemStack itemStack) {
        return VersionUtils.isVersion(9) && itemStack.getType() == Material.CHORUS_FRUIT;
    }

    private boolean isGoldenOrEnchantedApple(ItemStack itemStack) {
        return isGoldenApple(itemStack) || isEnchantedGolderApple(itemStack);
    }

    private boolean isGoldenApple(ItemStack itemStack) {
        return itemStack.getType() == Material.GOLDEN_APPLE;
    }

    private boolean isEnchantedGolderApple(ItemStack itemStack) {
        return (VersionUtils.isVersion(13) && itemStack.getType() == Material.ENCHANTED_GOLDEN_APPLE)
                || (isGoldenApple(itemStack) && itemStack.getDurability() >= 1);
    }

    private void cancelEventIfInPvp(Cancellable event, CooldownType type, Player player) {
        if (pvpManager.isInPvP(player)) {
            ((Cancellable) event).setCancelled(true);
            String message = type == CooldownType.TOTEM ? settings.getMessages().getTotemDisabledInPvp() :
                    settings.getMessages().getItemDisabledInPvp();
            if (!message.isEmpty()) {
                player.sendMessage(Utils.color(message));
            }
        }
        return;
    }

    private boolean checkCooldown(Player player, CooldownType cooldownType, long cooldownTime) {
        boolean cooldownActive = !pvpManager.isPvPModeEnabled() || pvpManager.isInPvP(player);
        if (cooldownActive && cooldownManager.hasCooldown(player, cooldownType, cooldownTime)) {
            long remaining = cooldownManager.getRemaining(player, cooldownType, cooldownTime);
            int remainingInt = (int) TimeUnit.MILLISECONDS.toSeconds(remaining);
            String message = cooldownType == CooldownType.TOTEM ? settings.getMessages().getTotemCooldown() :
                    settings.getMessages().getItemCooldown();
            if (!message.isEmpty()) {
                player.sendMessage(Utils.color(Utils.replaceTime(message.replace("%time%",
                        Math.round(remaining / 1000) + ""), remainingInt)));
            }
            return true;
        }
        return false;
    }

}
