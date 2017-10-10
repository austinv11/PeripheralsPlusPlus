package com.austinv11.peripheralsplusplus.villagers;

import com.austinv11.collectiveframework.language.translation.TranslationException;
import com.austinv11.collectiveframework.minecraft.utils.Colors;
import com.austinv11.collectiveframework.minecraft.utils.MinecraftTranslator;
import com.austinv11.collectiveframework.minecraft.utils.NBTHelper;
import com.austinv11.peripheralsplusplus.reference.Reference;
import com.austinv11.peripheralsplusplus.utils.Util;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VillagerProfessionPPP extends VillagerRegistry.VillagerProfession {
    private VillagerRegistry.VillagerCareer career;

    public VillagerProfessionPPP(String name, String texture, String zombie) {
        super(name, texture, zombie);
        career = new VillagerRegistry.VillagerCareer(this, Reference.MOD_ID + ".cc_upgrader");
        career.addTrade(1, new CcTrades());
        career.addTrade(2, new Upgrades());
        career.addTrade(3, new DungeonDisk());
    }

    @Override
    public VillagerRegistry.VillagerCareer getCareer(int id) {
        return career;
    }

    private class DungeonDisk implements EntityVillager.ITradeList {
        @Override
        public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random) {
            // Empty floppy disk + 3 emeralds = dungeon disk
            ItemStack emeralds = new ItemStack(Items.EMERALD, 3);
            ItemStack floppy = getFloppyFromInt(random.nextInt(10));
            recipeList.add(
                    new MerchantRecipe(GameRegistry.makeItemStack("computercraft:disk_expanded",
                    0, 1, ""),
                    emeralds, floppy));
            recipeList.add(
                    new MerchantRecipe(GameRegistry.makeItemStack("computercraft:disk",
                    0, 1, ""),
                    emeralds, floppy));
        }

        private ItemStack getFloppyFromInt(int t) {
            ItemStack stack = GameRegistry.makeItemStack("computercraft:treasure_disk", 0, 1, "");
            switch (t) {
                case 0:
                    break;
                case 1:
                    NBTHelper.setInteger(stack, "colour", 3368652);
                    NBTHelper.setString(stack, "subPath", "fredthead/protector");
                    NBTHelper.setString(stack, "title", "\"protector\" by fredthead");
                    break;
                case 2:
                    NBTHelper.setInteger(stack, "colour", 3368652);
                    NBTHelper.setString(stack, "subPath", "GopherAtl/battleship");
                    NBTHelper.setString(stack, "title", "\"battleship\" by GopherAtl");
                    break;
                case 3:
                    NBTHelper.setInteger(stack, "colour", 3368652);
                    NBTHelper.setString(stack, "subPath", "GravityScore/LuaIDE");
                    NBTHelper.setString(stack, "title", "\"LuaIDE\" by GravityScore");
                    break;
                case 4:
                    NBTHelper.setInteger(stack, "colour", 3368652);
                    NBTHelper.setString(stack, "subPath", "JTK/maze3d");
                    NBTHelper.setString(stack, "title", "\"maze3d\" by JTK");
                    break;
                case 5:
                    NBTHelper.setInteger(stack, "colour", 3368652);
                    NBTHelper.setString(stack, "subPath", "Lyqyd/nsh");
                    NBTHelper.setString(stack, "title", "\"nsh\" by Lyqyd");
                    break;
                case 6:
                    NBTHelper.setInteger(stack, "colour", 3368652);
                    NBTHelper.setString(stack, "subPath", "nitrogenfingers/goldrunner");
                    NBTHelper.setString(stack, "title", "\"goldrunner\" by nitrogenfingers");
                    break;
                case 7:
                    NBTHelper.setInteger(stack, "colour", 3368652);
                    NBTHelper.setString(stack, "subPath", "nitrogenfingers/npaintpro");
                    NBTHelper.setString(stack, "title", "\"npaintpro\" by nitrogenfingers");
                    break;
                case 8:
                    NBTHelper.setInteger(stack, "colour", 3368652);
                    NBTHelper.setString(stack, "subPath", "vilsol/gameoflife");
                    NBTHelper.setString(stack, "title", "\"gameoflife\" by vilsol");
                    break;
                case 9:
                    NBTHelper.setInteger(stack, "colour", 3368652);
                    NBTHelper.setString(stack, "subPath", "TheOriginalBIT/tictactoe");
                    NBTHelper.setString(stack, "title", "\"tictactoe\" by TheOriginalBIT");
                    break;
            }
            stack.setItemDamage(0);
            return stack;
        }
    }

    private class Upgrades implements EntityVillager.ITradeList {
        @Override
        public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random) {
            // Normal comp + emerald = advanced comp
            recipeList.add(new MerchantRecipe(
                    GameRegistry.makeItemStack("computercraft:computer",
                    0, 1, ""),
                    new ItemStack(Items.EMERALD),
                    GameRegistry.makeItemStack("computercraft:computer",
                            16384, 1, "")));
            // Normal monitor + emerald = advanced monitor
            recipeList.add(new MerchantRecipe(
                    GameRegistry.makeItemStack("computercraft:peripheral",
                    2, 1, ""),
                    new ItemStack(Items.EMERALD),
                    GameRegistry.makeItemStack("computercraft:peripheral",
                            4, 1, "")));
            // Normal turtle + emerald = advanced turtle
            recipeList.add(new MerchantRecipe(GameRegistry.makeItemStack("computercraft:turtle",
                    0, 1, ""),
                    new ItemStack(Items.EMERALD),
                    GameRegistry.makeItemStack("computercraft:turtle_advanced",
                            0, 1, "")));
            // Normal comp + 2 emeralds = normal turtle
            recipeList.add(new MerchantRecipe(
                    GameRegistry.makeItemStack("computercraft:computer", 0, 1, ""),
                    new ItemStack(Items.EMERALD, 2),
                    GameRegistry.makeItemStack("computercraft:turtle", 0, 1, "")));
            // Advanced comp + 2 emeralds = advanced turtle
            recipeList.add(new MerchantRecipe(
                    GameRegistry.makeItemStack("computercraft:computer", 16384, 1, ""),
                    new ItemStack(Items.EMERALD, 2),
                    GameRegistry.makeItemStack("computercraft:turtle_advanced", 0, 1, "")));
        }
    }

    public static class CcTrades implements EntityVillager.ITradeList {
        @Override
        public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random) {
            // 64 emeralds (+64 diamonds) = portable computer (an IPhone basically)
            ItemStack iPhone = GameRegistry.makeItemStack("computercraft:pocket_computer",
                    1, 1, "");
            try {
                iPhone.setStackDisplayName(
                        MinecraftTranslator.translateToLocal("item.peripheralsplusplus:iphone.name"));
            } catch (TranslationException | IOException e) {
                e.printStackTrace();
                iPhone.setStackDisplayName("item.peripheralsplusplus:iphone.name");
            }
            recipeList.add(new MerchantRecipe(
                    new ItemStack(Items.EMERALD, 64),
                    new ItemStack(Items.DIAMOND, 64),
                    iPhone));
            // Paper + emerald = book w/ lore TODO:More than 3 books
            recipeList.add(new MerchantRecipe(
                    new ItemStack(Items.BOOK),
                    new ItemStack(Items.EMERALD),
                    getBookFromInt(random.nextInt(3))));
            // 1 emerald = basic computer
            recipeList.add(new MerchantRecipe(
                    new ItemStack(Items.EMERALD),
                    GameRegistry.makeItemStack("computercraft:computer",
                            0, 1, "")
            ));
            // 2 emeralds = basic pocket computer
            recipeList.add(new MerchantRecipe(
                    new ItemStack(Items.EMERALD, 2),
                    GameRegistry.makeItemStack("computercraft:pocket_computer",
                            0, 1, "")
            ));
        }

        public static ItemStack getBookFromInt(int type) {
            ItemStack stack = new ItemStack(Items.WRITTEN_BOOK);
            switch (type) {
                case 0:
                        stack.setTagCompound(Util.writeToBookNBT("peripheralsplusplus.lore.1.title",
                            Colors.MAGIC+"dan200", getTextFromInt(type)));
                    break;
                case 1:
                    stack.setTagCompound(Util.writeToBookNBT("peripheralsplusplus.lore.2.title",
                            Colors.MAGIC+"dan200", getTextFromInt(type)));
                    break;
                case 2:
                    stack.setTagCompound(Util.writeToBookNBT("peripheralsplusplus.lore.3.title",
                            Colors.MAGIC+"dan200", getTextFromInt(type)));
                    break;
            }
            return stack;
        }

        public static List<String> getTextFromInt(int type) {
            List<String> list = new ArrayList<>();
            switch (type) {
                case 0:
                    list.add("peripheralsplusplus.lore.1.header");
                    list.add("peripheralsplusplus.lore.1.pg1");
                    list.add("peripheralsplusplus.lore.1.pg2");
                    list.add("peripheralsplusplus.lore.1.pg3");
                    list.add("peripheralsplusplus.lore.1.pg4");
                    break;
                case 1:
                    list.add("peripheralsplusplus.lore.2.header");
                    list.add("peripheralsplusplus.lore.2.pg1");
                    list.add("peripheralsplusplus.lore.2.pg2");
                    list.add("peripheralsplusplus.lore.2.pg3");
                    list.add("peripheralsplusplus.lore.2.pg4");
                    list.add("peripheralsplusplus.lore.2.pg5");
                    list.add("peripheralsplusplus.lore.2.pg6");
                    break;
                case 2:
                    list.add("peripheralsplusplus.lore.3.header");
                    list.add("peripheralsplusplus.lore.3.pg1");
                    list.add("peripheralsplusplus.lore.3.pg2");
                    list.add("peripheralsplusplus.lore.3.pg3");
                    break;
            }
            return list;
        }
    }
}
