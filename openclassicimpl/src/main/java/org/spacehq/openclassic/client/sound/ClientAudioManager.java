package org.spacehq.openclassic.client.sound;

import de.cuina.fireandfuel.CodecJLayerMP3;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.openal.AL;
import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.level.Level;
import org.spacehq.openclassic.api.math.MathHelper;
import org.spacehq.openclassic.api.math.Vector;
import org.spacehq.openclassic.api.player.Player;
import org.spacehq.openclassic.api.sound.AudioManager;
import org.spacehq.openclassic.game.Main;
import paulscode.sound.Library;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.codecs.CodecJOrbis;
import paulscode.sound.codecs.CodecWav;
import paulscode.sound.libraries.LibraryJavaSound;
import paulscode.sound.libraries.LibraryLWJGLOpenAL;

import javax.sound.sampled.AudioSystem;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;
import java.util.zip.ZipFile;

public class ClientAudioManager implements AudioManager {

	private static final Random rand = new Random();
	private static int nextSoundId = 0;

	private final Map<String, List<URL>> sounds = new HashMap<String, List<URL>>();
	private final Map<String, List<URL>> music = new HashMap<String, List<URL>>();

	private SoundSystem system;
	public long nextMusic = System.currentTimeMillis();

	public ClientAudioManager() {
		try {
			this.system = new SoundSystem(findCompatibleLibrary());
			SoundSystemConfig.setCodec("ogg", CodecJOrbis.class);
			SoundSystemConfig.setCodec("wav", CodecWav.class);
			SoundSystemConfig.setCodec("mp3", CodecJLayerMP3.class);
		} catch(SoundSystemException e) {
			e.printStackTrace();
		}
		
		this.registerMusic("menu", Main.class.getResource("/audio/music/sweden.ogg"), true);
		
		this.registerMusic("bg", Main.class.getResource("/audio/music/clark.ogg"), true);
		this.registerMusic("bg", Main.class.getResource("/audio/music/danny.ogg"), true);
		this.registerMusic("bg", Main.class.getResource("/audio/music/dryhands.ogg"), true);
		this.registerMusic("bg", Main.class.getResource("/audio/music/haggstrom.ogg"), true);
		this.registerMusic("bg", Main.class.getResource("/audio/music/key.ogg"), true);
		this.registerMusic("bg", Main.class.getResource("/audio/music/livingmice.ogg"), true);
		this.registerMusic("bg", Main.class.getResource("/audio/music/miceonvenus.ogg"), true);
		this.registerMusic("bg", Main.class.getResource("/audio/music/minecraft.ogg"), true);
		this.registerMusic("bg", Main.class.getResource("/audio/music/oxygene.ogg"), true);
		this.registerMusic("bg", Main.class.getResource("/audio/music/subwooferlullaby.ogg"), true);
		this.registerMusic("bg", Main.class.getResource("/audio/music/sweden.ogg"), true);
		this.registerMusic("bg", Main.class.getResource("/audio/music/wethands.ogg"), true);
		
		this.registerSound("random.click", Main.class.getResource("/audio/sound/random/click.ogg"), true);
		this.registerSound("random.explode", Main.class.getResource("/audio/sound/random/explode.ogg"), true);
		this.registerSound("random.hurt", Main.class.getResource("/audio/sound/random/hurt.ogg"), true);
		this.registerSound("random.pop", Main.class.getResource("/audio/sound/random/pop.ogg"), true);
		this.registerSound("random.splash", Main.class.getResource("/audio/sound/random/splash.ogg"), true);
		this.registerSound("random.water", Main.class.getResource("/audio/sound/random/water.ogg"), true);
		this.registerSound("random.bow", Main.class.getResource("/audio/sound/random/bow.ogg"), true);
		this.registerSound("random.drr", Main.class.getResource("/audio/sound/random/drr.ogg"), true);
		this.registerSound("random.fizz", Main.class.getResource("/audio/sound/random/fizz.ogg"), true);
		
		this.registerSound("step.cloth", Main.class.getResource("/audio/sound/step/cloth1.ogg"), true);
		this.registerSound("step.cloth", Main.class.getResource("/audio/sound/step/cloth2.ogg"), true);
		this.registerSound("step.cloth", Main.class.getResource("/audio/sound/step/cloth3.ogg"), true);
		this.registerSound("step.cloth", Main.class.getResource("/audio/sound/step/cloth4.ogg"), true);
		
		this.registerSound("step.grass", Main.class.getResource("/audio/sound/step/grass1.ogg"), true);
		this.registerSound("step.grass", Main.class.getResource("/audio/sound/step/grass2.ogg"), true);
		this.registerSound("step.grass", Main.class.getResource("/audio/sound/step/grass3.ogg"), true);
		this.registerSound("step.grass", Main.class.getResource("/audio/sound/step/grass4.ogg"), true);
		
		this.registerSound("step.gravel", Main.class.getResource("/audio/sound/step/gravel1.ogg"), true);
		this.registerSound("step.gravel", Main.class.getResource("/audio/sound/step/gravel2.ogg"), true);
		this.registerSound("step.gravel", Main.class.getResource("/audio/sound/step/gravel3.ogg"), true);
		this.registerSound("step.gravel", Main.class.getResource("/audio/sound/step/gravel4.ogg"), true);
		
		this.registerSound("step.sand", Main.class.getResource("/audio/sound/step/sand1.ogg"), true);
		this.registerSound("step.sand", Main.class.getResource("/audio/sound/step/sand2.ogg"), true);
		this.registerSound("step.sand", Main.class.getResource("/audio/sound/step/sand3.ogg"), true);
		this.registerSound("step.sand", Main.class.getResource("/audio/sound/step/sand4.ogg"), true);
		
		this.registerSound("step.stone", Main.class.getResource("/audio/sound/step/stone1.ogg"), true);
		this.registerSound("step.stone", Main.class.getResource("/audio/sound/step/stone2.ogg"), true);
		this.registerSound("step.stone", Main.class.getResource("/audio/sound/step/stone3.ogg"), true);
		this.registerSound("step.stone", Main.class.getResource("/audio/sound/step/stone4.ogg"), true);
		
		this.registerSound("step.wood", Main.class.getResource("/audio/sound/step/wood1.ogg"), true);
		this.registerSound("step.wood", Main.class.getResource("/audio/sound/step/wood2.ogg"), true);
		this.registerSound("step.wood", Main.class.getResource("/audio/sound/step/wood3.ogg"), true);
		this.registerSound("step.wood", Main.class.getResource("/audio/sound/step/wood4.ogg"), true);
	}
	
	private static Class<? extends Library> findCompatibleLibrary() {
		Class<? extends Library> ret = Library.class;
		OpenClassic.getLogger().info("Checking if LWJGL OpenAL is compatible...");
		boolean al = false;
		String debug = "";
        if(AL.isCreated()) {
        	al = true;
        } else {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			PrintStream stream = new PrintStream(out);
			PrintStream old = System.err;
			System.setErr(stream);
			setLWJGLDebug(true);
			try {
				AL.create();
				al = true;
				AL.destroy();
			} catch(Throwable t) {
				System.err.println("");
				t.printStackTrace();
			}
			
			setLWJGLDebug(false);
			System.setErr(old);
			stream.close();
			debug = out.toString();
        }
        
		if(al) {
			ret = LibraryLWJGLOpenAL.class;
			OpenClassic.getLogger().info("      ...yes, AL was successfully created.");
		} else {
			OpenClassic.getLogger().info("      ...no, AL could not be created.");
			OpenClassic.getLogger().info("Debug:");
			OpenClassic.getLogger().info(debug);
			
			OpenClassic.getLogger().info("Checking if Java Sound is compatible...");
			if(AudioSystem.getMixer(null) != null) {
				ret = LibraryJavaSound.class;
				OpenClassic.getLogger().info("      ...yes, default mixer was found.");
			} else {
				OpenClassic.getLogger().info("      ...no, default mixer could be found.");
			}
		}
		
		return ret;
	}
	
	private static void setLWJGLDebug(boolean debug) {
		try {
			Field f = LWJGLUtil.class.getDeclaredField("DEBUG");
			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);
			f.set(null, debug);
		} catch(Exception e) {
			OpenClassic.getLogger().warning("Failed to set LWJGL debug value for audio compatibility checks!");
			e.printStackTrace();
		}
	}

	public void update() {
		Player player = OpenClassic.getClient().getPlayer();
		if(player != null && OpenClassic.getClient().isInGame()) {
			this.system.setListenerPosition(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ());
			Vector vec = MathHelper.toForwardVec(player.getPosition().getYaw(), player.getPosition().getPitch());
			this.system.setListenerOrientation(vec.getX(), vec.getY(), vec.getZ(), (float) Math.sin(Math.toRadians(player.getPosition().getPitch())), (float) Math.sin(Math.toRadians(player.getPosition().getYaw())), 1);
		} else {
			this.system.setListenerPosition(0, 0, 0);
			this.system.setListenerOrientation(0, 0, -1, 0, 1, 0);
		}
	}

	public void cleanup() {
		this.system.cleanup();
	}

	@Override
	public void registerSound(String sound, URL file, boolean included) {
		Validate.notNull(sound, "Sound cannot be null.");
		Validate.notNull(file, "URL cannot be null.");
		if(!included) {
			this.download(file);
		}

		if(!this.sounds.containsKey(sound)) this.sounds.put(sound, new ArrayList<URL>());
		this.sounds.get(sound).add(file);
	}

	@Override
	public void registerMusic(String music, URL file, boolean included) {
		Validate.notNull(music, "Music cannot be null.");
		Validate.notNull(file, "URL cannot be null.");
		if(!included) {
			this.download(file);
		}

		if(!this.music.containsKey(music)) this.music.put(music, new ArrayList<URL>());
		this.music.get(music).add(file);
	}

	private void download(URL url) {
		File file = new File(OpenClassic.getClient().getDirectory(), "cache/" + (OpenClassic.getClient().isInMultiplayer() ? OpenClassic.getClient().getServerIP() : "local") + "/" + url.getFile());
		if(!file.exists()) {
			if(!file.getParentFile().exists()) {
				try {
					file.getParentFile().mkdirs();
				} catch(SecurityException e) {
					e.printStackTrace();
				}
			}

			try {
				file.createNewFile();
			} catch(SecurityException e) {
				e.printStackTrace();
			} catch(IOException e) {
				e.printStackTrace();
			}

			OpenClassic.getLogger().info(String.format(OpenClassic.getGame().getTranslator().translate("http.downloading"), file.getName()));

			byte[] data = new byte[4096];
			DataInputStream in = null;
			DataOutputStream out = null;

			try {
				in = new DataInputStream(url.openStream());
				out = new DataOutputStream(new FileOutputStream(file));

				int length = 0;
				while(OpenClassic.getClient().isRunning()) {
					length = in.read(data);
					if(length < 0) break;
					out.write(data, 0, length);
				}
			} catch(IOException e) {
				e.printStackTrace();
			} finally {
				IOUtils.closeQuietly(in);
				IOUtils.closeQuietly(out);
			}

			OpenClassic.getLogger().info(String.format(OpenClassic.getGame().getTranslator().translate("http.downloaded"), file.getName()));
		}
	}
	
	private URL getURLFor(String audio, Map<String, List<URL>> map) {
		List<URL> files = map.get(audio);
		URL url = files.get(rand.nextInt(files.size()));
		String pack = OpenClassic.getGame().getConfig().getString("options.resource-pack");
		if(pack != null && !pack.equals("none")) {
			File f = new File(OpenClassic.getClient().getDirectory(), "resourcepacks/" + pack);
			ZipFile zip = null;
			try {
				zip = new ZipFile(f);
				String p = url.getPath();
				if(p.contains("!")) {
					p = p.substring(p.indexOf("!") + 1);
				}
				
				p = p.startsWith("/") ? p.substring(1, p.length()) : p;
				if(zip.getEntry(p) != null) {
					url = new URL("zip:" + f.toURI().toURL().toString() + "!/" + p);
				}
			} catch(IOException e) {
				OpenClassic.getLogger().severe("Failed to read resource pack.");
				e.printStackTrace();
			} finally {
				IOUtils.closeQuietly(zip);
			}
		}
		
		return url;
	}

	@Override
	public boolean playSound(String sound, float x, float y, float z, float volume, float pitch) {
		if(!OpenClassic.getClient().getSettings().getBooleanSetting("options.sound").getValue()) {
			return true;
		}

		URL file = this.getURLFor(sound, this.sounds);
		if(file != null) {
			String source = "sound_" + nextSoundId++;

			float attenuation = 16;
			if(volume > 1) {
				attenuation = volume * 16;
			}

			this.system.newSource(volume > 1, source, file, file.getFile(), false, x, y, z, SoundSystemConfig.ATTENUATION_LINEAR, attenuation);
			if(volume > 1) {
				volume = 1;
			}

			this.system.setVolume(source, volume);
			this.system.setPitch(source, pitch);

			this.system.play(source);
			return true;
		}

		return false;
	}

	@Override
	public boolean playSound(String sound, float volume, float pitch) {
		return this.playSound(sound, this.system.getListenerData().position.x, this.system.getListenerData().position.y, this.system.getListenerData().position.z, volume, pitch);
	}

	@Override
	public boolean playMusic(String music) {
		return this.playMusic(music, false);
	}

	@Override
	public boolean playMusic(String music, boolean loop) {
		if(!OpenClassic.getClient().getSettings().getBooleanSetting("options.music").getValue()) {
			return true;
		}
		
		URL file = this.getURLFor(music, this.music);
		if(file != null) {
			if(this.isPlaying(music)) {
				return true;
			}
			
			if(this.isPlayingMusic()) {
				this.stopMusic();
			}

			this.system.backgroundMusic(music, file, file.getFile(), loop);
			this.system.play(music);
			return true;
		}

		return false;
	}

	@Override
	public boolean isPlayingMusic() {
		for(String music : this.music.keySet()) {
			if(this.isPlaying(music)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void stopMusic() {
		for(String music : this.music.keySet()) {
			if(this.isPlaying(music)) this.stop(music);
		}
	}

	@Override
	public boolean isPlaying(String music) {
		return this.system.playing(music);
	}

	@Override
	public void stop(String music) {
		this.system.stop(music);
	}

	@Override
	public boolean playSound(Player player, String sound, float volume, float pitch) {
		return this.playSound(sound, volume, pitch);
	}

	@Override
	public boolean playSound(Player player, String sound, float x, float y, float z, float volume, float pitch) {
		return this.playSound(sound, x, y, z, volume, pitch);
	}

	@Override
	public boolean playMusic(Player player, String music) {
		return this.playMusic(music);
	}

	@Override
	public boolean playMusic(Player player, String music, boolean loop) {
		return this.playMusic(music, loop);
	}
	
	@Override
	public boolean playSound(Level level, String sound, float volume, float pitch) {
		return this.playSound(sound, volume, pitch);
	}

	@Override
	public boolean playSound(Level level, String sound, float x, float y, float z, float volume, float pitch) {
		return this.playSound(sound, x, y, z, volume, pitch);
	}

	@Override
	public boolean playMusic(Level level, String music) {
		return this.playMusic(music);
	}

	@Override
	public boolean playMusic(Level level, String music, boolean loop) {
		return this.playMusic(music, loop);
	}

	@Override
	public void stopMusic(Player player) {
		this.stopMusic();
	}

	@Override
	public void stop(Player player, String music) {
		this.stop(music);
	}
	
	public long getMusicTime() {
		return this.nextMusic;
	}
	
	public void setMusicTime(long time) {
		this.nextMusic = time;
	}

}