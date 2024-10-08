package com.github.sirblobman.shulker.listener;

import org.jetbrains.annotations.NotNull;

import org.bukkit.block.BlockState;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.sirblobman.api.language.LanguageManager;
import com.github.sirblobman.api.plugin.listener.PluginListener;
import com.github.sirblobman.api.utility.ItemUtility;
import com.github.sirblobman.shulker.ShulkerPlugin;
//import com.github.sirblobman.shulker.manager.ShopAccessManager;
import com.github.sirblobman.shulker.menu.ShulkerBoxMenu;
import com.github.sirblobman.api.shaded.xseries.XMaterial;

public final class ListenerMenu extends PluginListener<ShulkerPlugin> {
    public ListenerMenu(@NotNull ShulkerPlugin plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInteract(PlayerInteractEvent e) {
        EquipmentSlot hand = e.getHand();
        if (hand != EquipmentSlot.HAND) {
            return;
        }

        Result useItemResult = e.useItemInHand();
        if (useItemResult == Result.DENY) {
            return;
        }

        Action action = e.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Player player = e.getPlayer();
        // Changed to right-click air to open the shulker box
//        if (player.hasPermission("shulkerpackx.shift.place")) {
//            return;
//        }

        if (action != Action.RIGHT_CLICK_AIR) {
            return;
        }

        PlayerInventory playerInventory = player.getInventory();
        ItemStack item = playerInventory.getItemInMainHand();
        if (ItemUtility.isAir(item)) {
            return;
        }

        ItemMeta itemMeta = item.getItemMeta();
        if (!(itemMeta instanceof BlockStateMeta)) {
            return;
        }

        BlockStateMeta blockStateMeta = (BlockStateMeta) itemMeta;
        BlockState blockState = blockStateMeta.getBlockState();
        if (!(blockState instanceof ShulkerBox)) {
            return;
        }

        e.setUseItemInHand(Result.DENY);
        e.setUseInteractedBlock(Result.DENY);

        // Delete Shop
        ShulkerPlugin plugin = getPlugin();
//        if (plugin.isShopEnabled()) {
//            XMaterial material = XMaterial.matchXMaterial(item);
//            ShopAccessManager shopAccessManager = plugin.getShopAccessManager();
//            if (!shopAccessManager.hasAccess(player, material)) {
//                LanguageManager languageManager = plugin.getLanguageManager();
//                languageManager.sendMessage(player, "error.missing-type-access");
//                return;
//            }
//        }

        // Changed to right-click air to open the shulker box
        if (action == Action.RIGHT_CLICK_AIR) {
            ShulkerBoxMenu shulkerBoxMenu = new ShulkerBoxMenu(plugin, player, item);
            shulkerBoxMenu.open();
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onDrop(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        InventoryView openInventoryView = player.getOpenInventory();
        Inventory topInventory = openInventoryView.getTopInventory();

        InventoryHolder topInventoryHolder = topInventory.getHolder();
        if (topInventoryHolder instanceof ShulkerBoxMenu) {
            e.setCancelled(true);
        }
    }
}
