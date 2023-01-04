package me.THEREALWWEFAN231.tunnelmc.connection.bedrock.network.caches.container.containers;

import me.THEREALWWEFAN231.tunnelmc.TunnelMC;
import me.THEREALWWEFAN231.tunnelmc.translator.item.ItemTranslator;
import net.minecraft.item.ItemStack;

public class PlayerInventoryContainer extends GenericContainer {
	private static final int SIZE = 36;

	public PlayerInventoryContainer() {
		super(PlayerInventoryContainer.SIZE);
	}
	
	@Override
	protected int convertJavaSlotIdToBedrockSlotId(int javaSlotId) {
		if(javaSlotId >= 36) {//if it's a java hotbar slot 36->44
			return javaSlotId - 36;//convert to bedrock slot, 0-8
		}
		
		//this check *isn't* needed *if* we are in the correct container, which we should be, for now I'm keeping this if statement, and return 0 for debugging purposes
		if(javaSlotId >= 9) {
			return javaSlotId;//java main inventory, the 27 slots have the same id on bedrock
		}
		
		return super.convertJavaSlotIdToBedrockSlotId(javaSlotId);
	}

	@Override
	public boolean isStatic() {
		return true;
	}

	@Override
	public void updateInventory() {
		for (int i = 0; i < this.getSize(); i++) {
			ItemStack stack = ItemTranslator.itemDataToItemStack(this.getItemFromSlot(i));

			TunnelMC.mc.player.playerScreenHandler.getSlot(convertJavaSlotIdToBedrockSlotId(i)).setStack(stack);
		}
	}
}
