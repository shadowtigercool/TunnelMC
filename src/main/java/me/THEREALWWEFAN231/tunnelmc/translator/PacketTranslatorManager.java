package me.THEREALWWEFAN231.tunnelmc.translator;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.nukkitx.api.event.Listener;
import com.nukkitx.protocol.bedrock.BedrockPacket;

import me.THEREALWWEFAN231.tunnelmc.TunnelMC;
import me.THEREALWWEFAN231.tunnelmc.events.EventPlayerTick;
import me.THEREALWWEFAN231.tunnelmc.translator.packet.*;
import me.THEREALWWEFAN231.tunnelmc.translator.packet.entity.*;
import me.THEREALWWEFAN231.tunnelmc.translator.packet.inventory.ContainerOpenPacketTranslator;
import me.THEREALWWEFAN231.tunnelmc.translator.packet.inventory.InventoryContentPacketTranslator;
import me.THEREALWWEFAN231.tunnelmc.translator.packet.inventory.InventorySlotPacketTranslator;
import me.THEREALWWEFAN231.tunnelmc.translator.packet.world.*;

public class PacketTranslatorManager {

	private final Map<Class<?>, PacketTranslator<?>> packetTranslatorsByPacketClass = new HashMap<>();
	private final CopyOnWriteArrayList<IdlePacket> idlePackets = new CopyOnWriteArrayList<>();

	public PacketTranslatorManager() {
		this.addTranslator(new StartGameTranslator());
		this.addTranslator(new ChunkRadiusUpdatedTranslator());
		this.addTranslator(new LevelChunkTranslator());
		this.addTranslator(new NetworkStackLatencyPacketTranslator());
		this.addTranslator(new ResourcePacksInfoPacketTranslator());
		this.addTranslator(new ResourcePackStackPacketTranslator());
		this.addTranslator(new AddPlayerTranslator());
		this.addTranslator(new PlayerListPacketTranslator());
		this.addTranslator(new TextTranslator());
		this.addTranslator(new AddEntityPacketTranslator());
		this.addTranslator(new SetTimePacketTranslator());
		this.addTranslator(new RemoveEntityPacketTranslator());
//		this.addTranslator(new InventorySlotPacketTranslator()); // TODO: REDO INVENTORIES/CONTAINERS
		this.addTranslator(new AddItemEntityPacketTranslator());
		this.addTranslator(new MovePlayerPacketTranslator());
		this.addTranslator(new MoveEntityAbsolutePacketTranslator());
		this.addTranslator(new ServerToClientHandshakePacketTranslator());
		this.addTranslator(new UpdateBlockTranslator());
		this.addTranslator(new SetEntityMotionTranslator());
		this.addTranslator(new TakeItemEntityPacketTranslator());
		this.addTranslator(new NetworkChunkPublisherUpdateTranslator());
		this.addTranslator(new SetEntityDataPacketTranslator());
//		this.addTranslator(new ContainerOpenPacketTranslator()); // TODO: REDO INVENTORIES/CONTAINERS
//		this.addTranslator(new InventoryContentPacketTranslator()); // TODO: REDO INVENTORIES/CONTAINERS
		this.addTranslator(new DisconnectTranslator());
		this.addTranslator(new SetPlayerGameTypeTranslator());
		this.addTranslator(new AdventureSettingsTranslator());
		this.addTranslator(new AnimateTranslator());
		this.addTranslator(new MobEquipmentTranslator());
		this.addTranslator(new MobArmorEquipmentTranslator());
		this.addTranslator(new BlockEntityDataTranslator());
		this.addTranslator(new GameRulesChangedTranslator());
		this.addTranslator(new UpdatePlayerGameTypeTranslator());
		this.addTranslator(new LevelEventTranslator());
		this.addTranslator(new LevelSoundEvent2Translator());
		this.addTranslator(new LevelSoundEventTranslator());
		this.addTranslator(new BlockEntityDataPacketTranslator());
		this.addTranslator(new RespawnPacketTranslator());
		this.addTranslator(new PlayStatusTranslator());
		this.addTranslator(new EntityEventPacketTranslator());

		TunnelMC.instance.eventManager.registerListeners(this, this);
	}

	private void addTranslator(PacketTranslator<?> translator) {
		this.packetTranslatorsByPacketClass.put(translator.getPacketClass(), translator);
	}

	@SuppressWarnings("unchecked")
	public void translatePacket(BedrockPacket bedrockPacket) {
		PacketTranslator<BedrockPacket> packetTranslator = (PacketTranslator<BedrockPacket>) this.packetTranslatorsByPacketClass.get(bedrockPacket.getClass());
		if (packetTranslator != null) {
			if (packetTranslator.idleUntil()) {
				this.idlePackets.add(new IdlePacket(packetTranslator, bedrockPacket));
				return;
			}

			packetTranslator.translate(bedrockPacket);
		} else {
			System.out.println("Couldn't find a packet translator for the packet: " + bedrockPacket.getClass());
		}
	}

	@Listener
	public void onEvent(EventPlayerTick event) {
		for (int i = 0; i < this.idlePackets.size(); i++) {
			IdlePacket idlePacket = this.idlePackets.get(i);
			if (idlePacket.packetTranslator.idleUntil()) {
				continue;
			}

			idlePacket.packetTranslator().translate(idlePacket.bedrockPacket());
			this.idlePackets.remove(i);
			i--;
		}
	}

	private record IdlePacket(PacketTranslator<BedrockPacket> packetTranslator, BedrockPacket bedrockPacket) {
	}
}