package me.googol.fisch.otherglide.blocks;

import me.googol.fisch.otherglide.OtherGlide;
import me.googol.fisch.otherglide.items.ModItemGroup;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static Block TESTING;

    private static Block  registerBlock(String name, Block block,ItemGroup group){
        registerBlockItem(name, block,group);
        return Registry.register(Registries.BLOCK, new Identifier(OtherGlide.MOD_ID, name),block);
    }

    private static Item registerBlockItem(String name, Block block, ItemGroup group){
        Item item = Registry.register(Registries.ITEM, new Identifier(OtherGlide.MOD_ID,name),
                new BlockItem(block,new FabricItemSettings()));
        ItemGroupEvents.modifyEntriesEvent(group).register((entries -> entries.add(item)));
        return item;
    }

    public static void registerModClientBlocks(){TESTING = registerBlock("tester_block",
            new Block(FabricBlockSettings.of(Material.METAL).strength(4.0f).requiresTool()), ModItemGroup.GLIDER_GROUP);}
    public static void registerModServerBlocks(){}
}
