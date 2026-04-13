package com.hbm.registry;

import com.hbm.HbmNuclearTech;
import com.hbm.item.HbmFluidBucketItem;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class HbmFluidItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(HbmNuclearTech.MODID);
    private static final List<DeferredItem<Item>> BUCKET_ITEMS = new ArrayList<>();

    private HbmFluidItems() {
    }

    public static DeferredItem<Item> registerBucket(String name, Supplier<? extends Fluid> fluidSupplier) {
        DeferredItem<Item> item = ITEMS.register(
            name,
            () -> new HbmFluidBucketItem(
                fluidSupplier.get(),
                new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)
            )
        );
        BUCKET_ITEMS.add(item);
        return item;
    }

    public static Item[] bucketItems() {
        return BUCKET_ITEMS.stream().map(DeferredItem::get).toArray(Item[]::new);
    }
}
