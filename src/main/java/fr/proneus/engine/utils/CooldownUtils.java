package fr.proneus.engine.utils;

import java.util.HashMap;
import java.util.Map;

public class CooldownUtils {

    private static Map<String, Cooldown> cooldowns = new HashMap<>();

    public static Cooldown createCooldown(String identifier, long time) {
        Cooldown cooldown = new Cooldown(time);
        cooldowns.put(identifier, cooldown);
        return cooldown;
    }

    public static Cooldown getCooldown(String identifier) {
        return cooldowns.get(identifier);
    }
}
