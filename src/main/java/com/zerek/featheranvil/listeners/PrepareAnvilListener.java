package com.zerek.featheranvil.listeners;

import com.zerek.featheranvil.FeatherAnvil;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class PrepareAnvilListener implements Listener {
    private final FeatherAnvil plugin;
    Map<String,String> tagMap = new HashMap<>();

    public PrepareAnvilListener(FeatherAnvil plugin) {
        this.plugin = plugin;
        this.plugin.getConfig().getConfigurationSection("tags").getKeys(false).forEach(k -> tagMap.put(k,plugin.getConfig().getString("tags." + k)));
    }

    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent event){
        if (event.getResult() != null){

            final String[] stringName = {LegacyComponentSerializer.legacyAmpersand().serialize(event.getResult().displayName()).replaceAll("&[A-fK-o0-9]|\\[|\\]", "")};

            tagMap.keySet().forEach(k -> {
                if(stringName[0].startsWith(k) && event.getViewers().get(0).hasPermission("feather.anvil.rename." + k)){
                    stringName[0] = stringName[0].replace(k, tagMap.get(k));
                    ItemMeta resultMeta = event.getResult().getItemMeta();
                    resultMeta.displayName(MiniMessage.miniMessage().deserialize(stringName[0]));
                    event.getResult().setItemMeta(resultMeta);
                }
            });
        }
    }
}
