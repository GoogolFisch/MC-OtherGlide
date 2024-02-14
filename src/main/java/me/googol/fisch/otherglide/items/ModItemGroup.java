package me.googol.fisch.otherglide.items;

import me.googol.fisch.otherglide.OtherGlide;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroup {
    public static ItemGroup GLIDER_GROUP;
    public static void registerItemClientGroup(){
        GLIDER_GROUP = FabricItemGroup.builder(new Identifier(OtherGlide.MOD_ID,"glide"))
                .displayName(Text.translatable("itemgroup.other_glide.glide"))
                .icon(() -> new ItemStack(ModItems.BOOST_BLOCK)).build();
    }
    public static void registerItemServerGroup(){
        GLIDER_GROUP = FabricItemGroup.builder(new Identifier(OtherGlide.MOD_ID,"glide"))
                .icon(() -> new ItemStack(ModItems.BOOST_BLOCK)).build();
    }
}
