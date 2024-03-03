package net.cleocyde.newrpgtest.utils;

import net.cleocyde.newrpgtest.NewRPGTest;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ItemUtils {

    public static final Item VITALITY = registerItem("vitality", new Item(new FabricItemSettings()));
    public static final Item AGILITY = registerItem("agility", new Item(new FabricItemSettings()));
    public static final Item STRENGTH = registerItem("strength", new Item(new FabricItemSettings()));
    public static final Item INTELLIGENCE = registerItem("intelligence", new Item(new FabricItemSettings()));
    public static final Item LUCK = registerItem("luck", new Item(new FabricItemSettings()));

    private static void addItemsToToolsItemGroup(FabricItemGroupEntries entries) {
        entries.add(VITALITY);
        entries.add(AGILITY);
        entries.add(STRENGTH);
        entries.add(INTELLIGENCE);
        entries.add(LUCK);

    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(NewRPGTest.MOD_ID, name), item);
    }

    public static void registerModItems() {
        NewRPGTest.LOGGER.info("Registering Mod Items for " + NewRPGTest.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(ItemUtils::addItemsToToolsItemGroup);
    }

}
