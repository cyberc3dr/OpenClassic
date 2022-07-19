package org.spacehq.openclassic.game.network;

import java.util.HashMap;
import java.util.Map;

import org.spacehq.openclassic.game.network.codec.BlockChangeCodec;
import org.spacehq.openclassic.game.network.codec.IdentificationCodec;
import org.spacehq.openclassic.game.network.codec.LevelDataCodec;
import org.spacehq.openclassic.game.network.codec.LevelFinalizeCodec;
import org.spacehq.openclassic.game.network.codec.LevelInitializeCodec;
import org.spacehq.openclassic.game.network.codec.PingCodec;
import org.spacehq.openclassic.game.network.codec.PlayerChatCodec;
import org.spacehq.openclassic.game.network.codec.PlayerDespawnCodec;
import org.spacehq.openclassic.game.network.codec.PlayerDisconnectCodec;
import org.spacehq.openclassic.game.network.codec.PlayerOpCodec;
import org.spacehq.openclassic.game.network.codec.PlayerPositionCodec;
import org.spacehq.openclassic.game.network.codec.PlayerPositionRotationCodec;
import org.spacehq.openclassic.game.network.codec.PlayerRotationCodec;
import org.spacehq.openclassic.game.network.codec.PlayerSetBlockCodec;
import org.spacehq.openclassic.game.network.codec.PlayerSpawnCodec;
import org.spacehq.openclassic.game.network.codec.PlayerTeleportCodec;
import org.spacehq.openclassic.game.network.codec.custom.AudioPlayCodec;
import org.spacehq.openclassic.game.network.codec.custom.AudioRegisterCodec;
import org.spacehq.openclassic.game.network.codec.custom.CustomBlockCodec;
import org.spacehq.openclassic.game.network.codec.custom.CustomCodec;
import org.spacehq.openclassic.game.network.codec.custom.GameInfoCodec;
import org.spacehq.openclassic.game.network.codec.custom.KeyChangeCodec;
import org.spacehq.openclassic.game.network.codec.custom.LevelPropertyCodec;
import org.spacehq.openclassic.game.network.codec.custom.MusicStopCodec;
import org.spacehq.openclassic.game.network.codec.custom.PluginCodec;
import org.spacehq.openclassic.game.network.msg.Message;

public final class CodecLookup {

	private static final MessageCodec<?>[] opcodeTable = new MessageCodec<?>[256];
	private static final Map<Class<? extends Message>, MessageCodec<?>> classTable = new HashMap<Class<? extends Message>, MessageCodec<?>>();

	static {
		try {
			bind(IdentificationCodec.class);
			bind(PingCodec.class);
			bind(LevelInitializeCodec.class);
			bind(LevelDataCodec.class);
			bind(LevelFinalizeCodec.class);
			bind(PlayerSetBlockCodec.class);
			bind(BlockChangeCodec.class);
			bind(PlayerSpawnCodec.class);
			bind(PlayerTeleportCodec.class);
			bind(PlayerPositionRotationCodec.class);
			bind(PlayerPositionCodec.class);
			bind(PlayerRotationCodec.class);
			bind(PlayerDespawnCodec.class);
			bind(PlayerChatCodec.class);
			bind(PlayerDisconnectCodec.class);
			bind(PlayerOpCodec.class);

			// Custom
			bind(GameInfoCodec.class);
			bind(CustomBlockCodec.class);
			bind(KeyChangeCodec.class);
			bind(LevelPropertyCodec.class);
			bind(AudioRegisterCodec.class);
			bind(AudioPlayCodec.class);
			bind(MusicStopCodec.class);
			bind(PluginCodec.class);
			bind(CustomCodec.class);
		} catch(Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	private static <T extends Message, C extends MessageCodec<T>> void bind(Class<C> clazz) throws InstantiationException, IllegalAccessException {
		MessageCodec<T> codec = clazz.newInstance();
		opcodeTable[codec.getOpcode()] = codec;
		classTable.put(codec.getType(), codec);
	}

	public static MessageCodec<?> find(int opcode) {
		return opcodeTable[opcode];
	}

	@SuppressWarnings("unchecked")
	public static <T extends Message> MessageCodec<T> find(Class<T> clazz) {
		return (MessageCodec<T>) classTable.get(clazz);
	}

	private CodecLookup() {
	}

}
