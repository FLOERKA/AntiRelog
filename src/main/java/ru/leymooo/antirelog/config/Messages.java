package ru.leymooo.antirelog.config;

import ru.leymooo.annotatedyaml.ConfigOptions;
import ru.leymooo.annotatedyaml.Configuration;

@ConfigOptions.Comment("Для того, чтобы отключить сообщение, оставьте его пустым")
public class Messages extends Configuration {
    @ConfigOptions.ConfigKey("pvp-started")
    private String pvpStarted = "&bВы начали &e&lPVP&b!";
    @ConfigOptions.ConfigKey("pvp-started-title")
    private String pvpStartedTitle = "&bAntiRelog";
    @ConfigOptions.ConfigKey("pvp-started-subtitle")
    private String pvpStartedSubtitle = "Вы вошли в режим &ePVP&a!";
    @ConfigOptions.ConfigKey("pvp-stopped")
    private String pvpStopped = "&e&lPVP &bокончено";
    @ConfigOptions.ConfigKey("pvp-stopped-title")
    private String pvpStoppedTitle = "&bAntiRelog";
    @ConfigOptions.ConfigKey("pvp-stopped-subtitle")
    private String pvpStoppedSubtitle = "Вы вышли из режима &ePVP&a!";
    @ConfigOptions.ConfigKey("pvp-stopped-actionbar")
    private String pvpStoppedActionbar = "&e&lPVP &aокончено, Вы снова можете использовать команды и выходить из игры!";
    @ConfigOptions.ConfigKey("in-pvp-bossbar")
    private String inPvpBossbar = "&r&lРежим &c&lPVP &r&l- &a&l%time% &r&l%formated-sec%.";
    @ConfigOptions.ConfigKey("in-pvp-actionbar")
    private String inPvpActionbar = "&r&lРежим &c&lPVP&r&l, не выходите из игры &a&l%time% &r&l%formated-sec%.";
    @ConfigOptions.ConfigKey("pvp-leaved")
    private String pvpLeaved = "&aИгрок &c&l%player% &aпокинул игру во время &b&lПВП&a и был наказан.";
    @ConfigOptions.ConfigKey("commands-disabled")
    private String commandsDisabled = "&b&lВы не можете использовать команды в &e&lPvP&b&l. &b&lПодождите &a&l%time% &b&l%formated-sec%.";
    @ConfigOptions.ConfigKey("apple-disabled")
    private String appleDisabled = "&b&lВы сможете использовать золотое яблоко через &a&l%time% &b&l%formated-sec%.";
    @ConfigOptions.ConfigKey("enderpearl-disabled")
    private String enderPearlDisabled = "&b&lВы сможете использовать Жемчуг Края через &a&l%time% &b&lсек.";

    public String getPvpStarted() {
        return pvpStarted;
    }

    public String getPvpStartedTitle() {
        return pvpStartedTitle;
    }

    public String getPvpStartedSubtitle() {
        return pvpStartedSubtitle;
    }

    public String getPvpStopped() {
        return pvpStopped;
    }

    public String getPvpStoppedTitle() {
        return pvpStoppedTitle;
    }

    public String getPvpStoppedSubtitle() {
        return pvpStoppedSubtitle;
    }

    public String getPvpStoppedActionbar() {
        return pvpStoppedActionbar;
    }

    public String getInPvpBossbar() {
        return inPvpBossbar;
    }

    public String getInPvpActionbar() {
        return inPvpActionbar;
    }

    public String getPvpLeaved() {
        return pvpLeaved;
    }

    public String getCommandsDisabled() {
        return commandsDisabled;
    }

    public String getAppleDisabled() {
        return appleDisabled;
    }

    public String getEnderPearlDisabled() {
        return enderPearlDisabled;
    }


    @Override
    public String toString() {
        return "Messages{" +
                "pvpStarted='" + pvpStarted + '\'' +
                ", pvpStartedTitle='" + pvpStartedTitle + '\'' +
                ", pvpStartedSubtitle='" + pvpStartedSubtitle + '\'' +
                ", pvpStopped='" + pvpStopped + '\'' +
                ", pvpStoppedTitle='" + pvpStoppedTitle + '\'' +
                ", pvpStoppedSubtitle='" + pvpStoppedSubtitle + '\'' +
                ", pvpStoppedActionbar='" + pvpStoppedActionbar + '\'' +
                ", inPvpBossbar='" + inPvpBossbar + '\'' +
                ", inPvpActionbar='" + inPvpActionbar + '\'' +
                ", pvpLeaved='" + pvpLeaved + '\'' +
                ", commandsDisabled='" + commandsDisabled + '\'' +
                ", appleDisabled='" + appleDisabled + '\'' +
                ", enderPearlDisabled='" + enderPearlDisabled + '\'' +
                '}';
    }
}