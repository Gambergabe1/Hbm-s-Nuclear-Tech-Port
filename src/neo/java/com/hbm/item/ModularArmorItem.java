package com.hbm.item;

import java.util.List;
import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import com.hbm.armor.HbmArmorUtil;
import com.hbm.armor.RadiationResistanceProvider;
import com.hbm.util.LegacyTooltipUtil;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public class ModularArmorItem extends ArmorItem implements RadiationResistanceProvider {
    private final ResourceLocation outerTexture;
    private final ResourceLocation innerTexture;
    @Nullable
    private final ResourceLocation helmetOverlayTexture;
    private final float radiationResistance;

    public ModularArmorItem(
        Holder<ArmorMaterial> material,
        Type type,
        Properties properties,
        ResourceLocation outerTexture,
        ResourceLocation innerTexture,
        @Nullable ResourceLocation helmetOverlayTexture,
        float radiationResistance
    ) {
        super(material, type, properties);
        this.outerTexture = outerTexture;
        this.innerTexture = innerTexture;
        this.helmetOverlayTexture = helmetOverlayTexture;
        this.radiationResistance = radiationResistance;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        LegacyTooltipUtil.appendLegacyDescription(this, stack, tooltipComponents);
        HbmArmorUtil.appendInstalledModsTooltip(stack, tooltipComponents);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.isShiftKeyDown()) {
            if (!level.isClientSide()) {
                ItemStack removedUpgrade = HbmArmorUtil.popLastArmorMod(stack);
                if (!removedUpgrade.isEmpty()) {
                    HbmArmorUtil.giveBackToPlayer(player, removedUpgrade);
                    player.playSound(SoundEvents.ARMOR_EQUIP_CHAIN.value(), 0.8F, 0.85F);
                    return InteractionResultHolder.success(stack);
                }
            } else if (HbmArmorUtil.hasArmorMods(stack)) {
                return InteractionResultHolder.success(stack);
            }
        }

        return super.use(level, player, hand);
    }

    @Override
    public ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
        return innerModel ? innerTexture : outerTexture;
    }

    @Override
    public float getRadiationResistance(ItemStack stack) {
        return radiationResistance;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        if (helmetOverlayTexture == null) {
            return;
        }

        consumer.accept(new IClientItemExtensions() {
            @Override
            public void renderHelmetOverlay(ItemStack stack, Player player, GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
                guiGraphics.blit(
                    helmetOverlayTexture,
                    0,
                    0,
                    0.0F,
                    0.0F,
                    guiGraphics.guiWidth(),
                    guiGraphics.guiHeight(),
                    guiGraphics.guiWidth(),
                    guiGraphics.guiHeight()
                );
            }
        });
    }
}
