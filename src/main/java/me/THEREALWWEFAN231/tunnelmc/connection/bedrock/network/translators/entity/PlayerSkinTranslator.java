package me.THEREALWWEFAN231.tunnelmc.connection.bedrock.network.translators.entity;

import com.nukkitx.protocol.bedrock.packet.PlayerSkinPacket;
import me.THEREALWWEFAN231.tunnelmc.connection.bedrock.BedrockConnection;
import me.THEREALWWEFAN231.tunnelmc.connection.java.FakeJavaConnection;
import me.THEREALWWEFAN231.tunnelmc.translator.packet.PacketIdentifier;
import me.THEREALWWEFAN231.tunnelmc.translator.packet.PacketTranslator;

@PacketIdentifier(PlayerSkinPacket.class)
public class PlayerSkinTranslator extends PacketTranslator<PlayerSkinPacket> {

    @Override
    public void translate(PlayerSkinPacket packet, BedrockConnection bedrockConnection, FakeJavaConnection javaConnection) {
        bedrockConnection.addSerializedSkin(packet.getUuid(), packet.getSkin());
    }
}
