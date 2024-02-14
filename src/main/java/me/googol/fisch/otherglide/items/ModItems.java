package me.googol.fisch.otherglide.items;

import me.googol.fisch.otherglide.OtherGlide;
import me.googol.fisch.otherglide.items.custom.GlidingItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item BLOB = registerItem("blob",new Item(new FabricItemSettings()));
    public static final Item BOOST_BLOCK = registerItem("booster",new Item(new FabricItemSettings()));
    public static final Item CLIMB_BLOCK = registerItem("climber",new Item(new FabricItemSettings()));
    public static final Item GLIDE_ELYTRA = registerItem("glide_elytra",new GlidingItem(new FabricItemSettings()));

    private static Item registerItem(String name, Item item){
        return Registry.register(Registries.ITEM, new Identifier(OtherGlide.MOD_ID,name),item);
    }

    public static  void addItemToItemGroup(){
        addToItemGroup(ItemGroups.FUNCTIONAL, BOOST_BLOCK);
        addToItemGroup(ItemGroups.FUNCTIONAL, CLIMB_BLOCK);

        addToItemGroup(ModItemGroup.GLIDER_GROUP, BOOST_BLOCK);
        addToItemGroup(ModItemGroup.GLIDER_GROUP, CLIMB_BLOCK);
        addToItemGroup(ModItemGroup.GLIDER_GROUP, GLIDE_ELYTRA);
    }

    public static  void addToItemGroup(ItemGroup group,Item item){
        ItemGroupEvents.modifyEntriesEvent(group).register(entries -> entries.add(item));
    }
    public static void registerServerModItems() {}
    public static void registerModItems() {
        OtherGlide.LOGGER.info("items init");
        addItemToItemGroup();
    }
}
