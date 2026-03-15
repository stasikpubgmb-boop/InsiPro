package com.insipro.display.screens.clickgui.components.implement.autobuy.originalitems;

import com.insipro.display.screens.clickgui.components.implement.autobuy.items.AutoBuyableItem;
import com.insipro.display.screens.clickgui.components.implement.autobuy.items.list.DonatorProvider;
import com.insipro.display.screens.clickgui.components.implement.autobuy.items.list.HolyWorldProvider;
import com.insipro.display.screens.clickgui.components.implement.autobuy.items.list.SpookyTimeProvider;
import com.insipro.display.screens.clickgui.components.implement.autobuy.util.krushprovider.KrushProvider;
import com.insipro.display.screens.clickgui.components.implement.autobuy.items.list.MiscProvider;
import com.insipro.display.screens.clickgui.components.implement.autobuy.items.list.PotionProvider;
import com.insipro.display.screens.clickgui.components.implement.autobuy.items.list.SphereProvider;
import com.insipro.display.screens.clickgui.components.implement.autobuy.items.list.TalismanProvider;

import java.util.ArrayList;
import java.util.List;

public class ItemRegistry {
    private static List<AutoBuyableItem> allItems = null;

    public static List<AutoBuyableItem> getAllItems() {
        if (allItems == null) {
            allItems = new ArrayList<>();
            allItems.addAll(getKrush());
            allItems.addAll(getTalismans());
            allItems.addAll(getSpheres());
            allItems.addAll(getMisc());
            allItems.addAll(getDonator());
            allItems.addAll(getPotions());
            allItems.addAll(getHolyWorld());
        }
        return allItems;
    }

    public static void reload() {
        allItems = null;
        HolyWorldProvider.reload();
        SpookyTimeProvider.reload();
    }

    public static List<AutoBuyableItem> getKrush() {
        return KrushProvider.getKrush();
    }

    public static List<AutoBuyableItem> getTalismans() {
        return TalismanProvider.getTalismans();
    }

    public static List<AutoBuyableItem> getSpheres() {
        return SphereProvider.getSpheres();
    }

    public static List<AutoBuyableItem> getMisc() {
        return MiscProvider.getMisc();
    }

    public static List<AutoBuyableItem> getDonator() {
        return DonatorProvider.getDonator();
    }

    public static List<AutoBuyableItem> getPotions() {
        return PotionProvider.getPotions();
    }

    public static List<AutoBuyableItem> getHolyWorld() {
        return HolyWorldProvider.getItems();
    }

    public static List<AutoBuyableItem> getSpookyTime() {
        return SpookyTimeProvider.getItems();
    }

    
    public static List<AutoBuyableItem> getFunTimeItems() {
        List<AutoBuyableItem> funTimeItems = new ArrayList<>();
        funTimeItems.addAll(getKrush());
        funTimeItems.addAll(getTalismans());
        funTimeItems.addAll(getSpheres());
        funTimeItems.addAll(getMisc());
        funTimeItems.addAll(getDonator());
        funTimeItems.addAll(getPotions());
        
        return funTimeItems;
    }
}