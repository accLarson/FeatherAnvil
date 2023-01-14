package com.zerek.featheranvil.listeners;

import com.zerek.featheranvil.FeatherAnvil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class PrepareAnvilListener implements Listener {

    private final FeatherAnvil plugin;

    Map<String,String> tagMap = new HashMap<>();

    public PrepareAnvilListener(FeatherAnvil plugin) {

        this.plugin = plugin;

        this.plugin.getConfig().getConfigurationSection("tags").getKeys(false).forEach(k -> tagMap.put(k,plugin.getConfig().getString("tags." + k)));
    }

    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent event){

        if (event.getResult() != null && event.getInventory().getFirstItem() != null){



            // 0 - current name | 1 - new name
            final String[] stringNames = new String[2];

            stringNames[0] = LegacyComponentSerializer.legacyAmpersand().serialize(event.getInventory().getFirstItem().displayName()).replaceAll("&[A-fK-o0-9]|\\[|\\]", "").replace("&x","");

            stringNames[1] = LegacyComponentSerializer.legacyAmpersand().serialize(event.getResult().displayName()).replaceAll("&[A-fK-o0-9]|\\[|\\]", "").replace("&x","");

            if (!stringNames[1].equals(stringNames[0])) {

                // Color Tag System -------------------------------------------------------------------------------------------------------------------------

                tagMap.keySet().forEach(k -> {

                    if(stringNames[1].startsWith(k) && event.getViewers().get(0).hasPermission("feather.anvil.rename." + k)){

                        stringNames[1] = stringNames[1].replace(k, tagMap.get(k));

                        ItemMeta resultMeta = event.getResult().getItemMeta();

                        resultMeta.displayName(MiniMessage.miniMessage().deserialize(stringNames[1]));

                        if (!PlainTextComponentSerializer.plainText().serialize(resultMeta.displayName()).trim().isEmpty()) event.getResult().setItemMeta(resultMeta);
                    }
                });

                // Named By: <player> Lore Record -----------------------------------------------------------------------------------------------------------

                String playerName = event.getViewers().get(0).getName();

                if (event.getResult().lore() != null) {

                    boolean hasNamedByLore = false;

                    List<Component> newLore = event.getResult().lore();

                    for (int i = 0; i < newLore.size(); i++) {

                        Component component = newLore.get(i);

                        if (MiniMessage.miniMessage().serialize(component).startsWith("Named By: ")) {

                            hasNamedByLore = true;

                            newLore.set(i, Component.text("Named By: " + playerName));

                        }
                    }

                    if (!hasNamedByLore) newLore.add(Component.text("Named By: " + playerName));

                    event.getResult().lore(newLore);

                }
                else event.getResult().lore(Collections.singletonList(Component.text("Named By: " + playerName)));
            }
        }
    }
}