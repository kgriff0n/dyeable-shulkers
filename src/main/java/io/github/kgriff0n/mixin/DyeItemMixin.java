package io.github.kgriff0n.mixin;

import io.github.kgriff0n.Config;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static io.github.kgriff0n.DyeableShulkers.setColor;


@Mixin(DyeItem.class)
public class DyeItemMixin {
	@Final @Shadow private DyeColor color;
	@Inject(at = @At("HEAD"), method = "useOnEntity", cancellable = true)
	private void useOnShulker(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
		ShulkerEntity shulkerEntity;
		if (Config.canDyeMob && entity instanceof ShulkerEntity && (shulkerEntity = (ShulkerEntity) entity).isAlive() && ((ShulkerEntity) entity).getColor() != this.color) {
			user.playSound(SoundEvents.ITEM_DYE_USE);
			if (!user.getWorld().isClient) {
				setColor(shulkerEntity, this.color.getId());
				stack.decrement(1);
			}
			cir.setReturnValue(ActionResult.success(user.getWorld().isClient));
		}
	}
}