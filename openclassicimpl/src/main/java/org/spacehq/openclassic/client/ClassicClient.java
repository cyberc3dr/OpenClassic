package org.spacehq.openclassic.client;

import com.mojang.minecraft.entity.Entity;
import com.mojang.minecraft.entity.object.Arrow;
import com.mojang.minecraft.entity.player.LocalPlayer;
import com.zachsthings.onevent.EventManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.spacehq.openclassic.api.Client;
import org.spacehq.openclassic.api.OpenClassic;
import org.spacehq.openclassic.api.Position;
import org.spacehq.openclassic.api.ProgressBar;
import org.spacehq.openclassic.api.block.*;
import org.spacehq.openclassic.api.block.model.QuadFactory;
import org.spacehq.openclassic.api.block.model.TextureFactory;
import org.spacehq.openclassic.api.event.block.BlockPlaceEvent;
import org.spacehq.openclassic.api.event.level.LevelCreateEvent;
import org.spacehq.openclassic.api.event.level.LevelLoadEvent;
import org.spacehq.openclassic.api.event.level.LevelSaveEvent;
import org.spacehq.openclassic.api.event.player.PlayerKeyChangeEvent;
import org.spacehq.openclassic.api.event.player.PlayerQuitEvent;
import org.spacehq.openclassic.api.event.player.PlayerRespawnEvent;
import org.spacehq.openclassic.api.gui.GuiComponent;
import org.spacehq.openclassic.api.gui.HUDComponent;
import org.spacehq.openclassic.api.gui.base.ComponentHelper;
import org.spacehq.openclassic.api.input.InputHelper;
import org.spacehq.openclassic.api.input.Keyboard;
import org.spacehq.openclassic.api.input.Mouse;
import org.spacehq.openclassic.api.level.Level;
import org.spacehq.openclassic.api.level.LevelInfo;
import org.spacehq.openclassic.api.level.generator.FlatLandGenerator;
import org.spacehq.openclassic.api.level.generator.Generator;
import org.spacehq.openclassic.api.level.generator.NormalGenerator;
import org.spacehq.openclassic.api.math.BoundingBox;
import org.spacehq.openclassic.api.math.MathHelper;
import org.spacehq.openclassic.api.math.Vector;
import org.spacehq.openclassic.api.player.Player;
import org.spacehq.openclassic.api.plugin.Plugin;
import org.spacehq.openclassic.api.plugin.PluginManager.LoadOrder;
import org.spacehq.openclassic.api.plugin.RemotePluginInfo;
import org.spacehq.openclassic.api.settings.BooleanSetting;
import org.spacehq.openclassic.api.settings.IntSetting;
import org.spacehq.openclassic.api.settings.Settings;
import org.spacehq.openclassic.api.settings.bindings.Bindings;
import org.spacehq.openclassic.api.settings.bindings.KeyBinding;
import org.spacehq.openclassic.api.sound.AudioManager;
import org.spacehq.openclassic.api.util.Constants;
import org.spacehq.openclassic.client.block.physics.TNTPhysics;
import org.spacehq.openclassic.client.command.ClientCommands;
import org.spacehq.openclassic.client.gamemode.CreativeGameMode;
import org.spacehq.openclassic.client.gamemode.GameMode;
import org.spacehq.openclassic.client.gamemode.SurvivalGameMode;
import org.spacehq.openclassic.client.gui.*;
import org.spacehq.openclassic.client.gui.base.ClientComponentHelper;
import org.spacehq.openclassic.client.gui.hud.ClientHUDScreen;
import org.spacehq.openclassic.client.gui.hud.HeldBlock;
import org.spacehq.openclassic.client.input.ClientInputHelper;
import org.spacehq.openclassic.client.input.Input;
import org.spacehq.openclassic.client.input.KeyboardEvent;
import org.spacehq.openclassic.client.input.MouseEvent;
import org.spacehq.openclassic.client.level.ClientLevel;
import org.spacehq.openclassic.client.level.particle.RainParticle;
import org.spacehq.openclassic.client.math.Intersection;
import org.spacehq.openclassic.client.math.RayTracer;
import org.spacehq.openclassic.client.network.ClientSession;
import org.spacehq.openclassic.client.player.ClientPlayer;
import org.spacehq.openclassic.client.render.ClientQuadFactory;
import org.spacehq.openclassic.client.render.ClientTextureFactory;
import org.spacehq.openclassic.client.render.RenderHelper;
import org.spacehq.openclassic.client.render.level.Chunk;
import org.spacehq.openclassic.client.settings.*;
import org.spacehq.openclassic.client.sound.ClientAudioManager;
import org.spacehq.openclassic.client.util.HTTPUtil;
import org.spacehq.openclassic.client.util.LWJGLNatives;
import org.spacehq.openclassic.client.util.ServerDataStore;
import org.spacehq.openclassic.client.util.Timer;
import org.spacehq.openclassic.game.ClassicGame;
import org.spacehq.openclassic.game.Main;
import org.spacehq.openclassic.game.io.OpenClassicLevelFormat;
import org.spacehq.openclassic.game.network.ClassicSession.State;
import org.spacehq.openclassic.game.network.msg.PlayerSetBlockMessage;
import org.spacehq.openclassic.game.network.msg.PlayerTeleportMessage;
import org.spacehq.openclassic.game.network.msg.custom.KeyChangeMessage;
import org.spacehq.openclassic.game.scheduler.ClassicScheduler;
import org.spacehq.openclassic.game.util.DateOutputFormatter;
import org.spacehq.openclassic.game.util.EmptyMessageFormatter;
import org.spacehq.openclassic.game.util.InternalConstants;
import org.spacehq.openclassic.game.util.LoggerOutputStream;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

public class ClassicClient extends ClassicGame implements Client {

	private static final Random rand = new Random();

	private volatile boolean running;
	private boolean ingame;
	private Settings settings;
	private Settings hackSettings;
	private Bindings bindings;
	private ClientPlayer player;
	private GuiComponent baseGUI;
	private ClientProgressBar progressBar;
	private ClientAudioManager audio;
	private ClientLevel level;
	private ClientHUDScreen hud;
	private GameMode mode;
	private Intersection selected;
	private HeldBlock heldBlock = new HeldBlock();

	private int prevWidth;
	private int prevHeight;
	private int ticks;
	private int schedTicks;
	private int blockHitTime;
	private int lastClick;
	private boolean displayActive;
	private boolean wasActive;
	private long lastUpdate;
	private int fps;
	private int waterDelay = 200;
	private boolean hacks = true;
	private boolean openclassicServer = false;
	private String openclassicVersion = "";
	private List<RemotePluginInfo> serverPlugins = new ArrayList<RemotePluginInfo>();

	public ClassicClient() {
		super(getMinecraftDirectory());
	}

	private static File getMinecraftDirectory() {
		File result = null;
		String os = System.getProperty("os.name").toLowerCase();
		if(os.contains("win")) {
			result = new File(System.getenv("APPDATA"), ".minecraft_classic/");
		} else if(os.contains("mac")) {
			result = new File(System.getProperty("user.home"), "/Library/Application Support/minecraft_classic");
		} else if(os.contains("linux") || os.contains("solaris")) {
			result = new File(System.getProperty("user.home"), ".minecraft_classic/");
		} else {
			result = new File(System.getProperty("user.home"), "minecraft_classic/");
		}

		if(!result.exists()) {
			try {
				result.mkdirs();
			} catch(SecurityException e) {
				throw new RuntimeException(OpenClassic.getGame().getTranslator().translate("core.fail-working-dir"), e);
			}
		}

		return result;
	}

	public void start(String[] args) {
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				OpenClassic.getLogger().severe("Uncaught exception in thread \"" + t.getName() + "\"");
				handleException(e);
			}
		});

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				shutdown();
			}
		});

		System.setProperty("java.protocol.handler.pkgs", "org.spacehq.openclassic.client.util.protocol");

		ConsoleHandler console = new ConsoleHandler();
		console.setFormatter(new DateOutputFormatter(new SimpleDateFormat("HH:mm:ss"), new EmptyMessageFormatter()));

		Logger logger = Logger.getLogger("");
		for(Handler handler : logger.getHandlers()) {
			logger.removeHandler(handler);
		}

		logger.addHandler(console);

		try {
			FileHandler handler = new FileHandler(this.getDirectory().getPath() + "/client.log");
			handler.setFormatter(new DateOutputFormatter(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"), new EmptyMessageFormatter()));
			OpenClassic.getLogger().addHandler(handler);
		} catch(IOException e) {
			OpenClassic.getLogger().severe(this.getTranslator().translate("log.create-fail"));
			e.printStackTrace();
		}

		System.setOut(new PrintStream(new LoggerOutputStream(java.util.logging.Level.INFO), true));
		System.setErr(new PrintStream(new LoggerOutputStream(java.util.logging.Level.SEVERE), true));

		OpenClassic.getLogger().info(String.format(this.getTranslator().translate("core.startup.client"), Constants.VERSION));
		this.running = true;

		File lib = new File(this.getDirectory(), "lib");
		if(!lib.exists()) {
			try {
				lib.mkdirs();
			} catch(SecurityException e) {
				e.printStackTrace();
			}
		}

		LWJGLNatives.load(lib);
		File levels = new File(this.getDirectory(), "levels");
		if(!levels.exists()) {
			try {
				levels.mkdirs();
			} catch(SecurityException e) {
				e.printStackTrace();
			}
		}

		File screenshots = new File(this.getDirectory(), "screenshots");
		if(!screenshots.exists()) {
			try {
				screenshots.mkdirs();
			} catch(SecurityException e) {
				e.printStackTrace();
			}
		}

		File resourcepacks = new File(this.getDirectory(), "resourcepacks");
		if(!resourcepacks.exists()) {
			try {
				resourcepacks.mkdirs();
			} catch(SecurityException e) {
				e.printStackTrace();
			}
		}

		InputHelper.setHelper(new ClientInputHelper());
		QuadFactory.setFactory(new ClientQuadFactory());
		TextureFactory.setFactory(new ClientTextureFactory());
		ComponentHelper.setHelper(new ClientComponentHelper());

		this.progressBar = new ClientProgressBar();
		this.player = new ClientPlayer();
		this.audio = new ClientAudioManager();
		this.bindings = new Bindings();
		this.bindings.registerBinding(new KeyBinding("options.keys.forward", Keyboard.KEY_W));
		this.bindings.registerBinding(new KeyBinding("options.keys.left", Keyboard.KEY_A));
		this.bindings.registerBinding(new KeyBinding("options.keys.back", Keyboard.KEY_S));
		this.bindings.registerBinding(new KeyBinding("options.keys.right", Keyboard.KEY_D));
		this.bindings.registerBinding(new KeyBinding("options.keys.jump", Keyboard.KEY_SPACE));
		this.bindings.registerBinding(new KeyBinding("options.keys.blocks", Keyboard.KEY_B));
		this.bindings.registerBinding(new KeyBinding("options.keys.chat", Keyboard.KEY_T));
		this.bindings.registerBinding(new KeyBinding("options.keys.toggle-fog", Keyboard.KEY_F));
		this.bindings.registerBinding(new KeyBinding("options.keys.save-loc", Keyboard.KEY_RETURN));
		this.bindings.registerBinding(new KeyBinding("options.keys.load-loc", Keyboard.KEY_R));
		this.bindings.registerBinding(new KeyBinding("options.keys.speedhack", Keyboard.KEY_LCONTROL));
		this.bindings.registerBinding(new KeyBinding("options.keys.fly-down", Keyboard.KEY_LSHIFT));
		this.settings = new Settings();
		this.settings.registerSetting(new MusicSetting("options.music"));
		this.settings.getBooleanSetting("options.music").setDefault(true);
		this.settings.registerSetting(new BooleanSetting("options.sound"));
		this.settings.getBooleanSetting("options.sound").setDefault(true);
		this.settings.registerSetting(new IntSetting("options.graphics", new String[] {
				this.getTranslator().translate("options.graphics-options.normal"),
				this.getTranslator().translate("options.graphics-options.fancy") }));
		this.settings.getIntSetting("options.graphics").setDefault(1);
		this.settings.registerSetting(new BooleanSetting("options.invert-mouse"));
		this.settings.registerSetting(new BooleanSetting("options.show-info"));
		this.settings.registerSetting(new IntSetting("options.render-distance", new String[] {
				this.getTranslator().translate("options.render-distance-options.far"),
				this.getTranslator().translate("options.render-distance-options.normal"),
				this.getTranslator().translate("options.render-distance-options.short"),
				this.getTranslator().translate("options.render-distance-options.tiny") }));

		this.settings.registerSetting(new BooleanSetting("options.view-bobbing"));
		this.settings.getBooleanSetting("options.view-bobbing").setDefault(true);
		this.settings.registerSetting(new TextureRefreshSetting("options.3d-anaglyph"));
		this.settings.registerSetting(new BooleanSetting("options.limit-fps"));
		this.settings.registerSetting(new SurvivalSetting("options.survival", new String[] {
				this.getTranslator().translate("options.off"),
				this.getTranslator().translate("options.survival-options.peaceful"),
				this.getTranslator().translate("options.survival-options.normal") }));

		this.settings.registerSetting(new SmoothingSetting("options.smoothing"));
		this.settings.registerSetting(new NightSetting("options.night"));
		this.settings.registerSetting(new IntSetting("options.sensitivity", new String[] {
				this.getTranslator().translate("options.sensitivity-options.slow"),
				this.getTranslator().translate("options.sensitivity-options.normal"),
				this.getTranslator().translate("options.sensitivity-options.fast"),
				this.getTranslator().translate("options.sensitivity-options.faster"),
				this.getTranslator().translate("options.sensitivity-options.fastest") }));

		this.settings.getIntSetting("options.sensitivity").setDefault(1);
		this.settings.registerSetting(new MinimapSetting("options.minimap"));

		this.getConfig().applyDefault("options.resource-pack", "none");
		this.getConfig().save();

		ServerDataStore.loadFavorites(this.getDirectory());

		this.initRender();
		this.baseGUI = new GuiComponent("base", 0, 0, Display.getWidth(), Display.getHeight());
		this.baseGUI.setFocused(true);

		this.registerExecutor(this, new ClientCommands());
		this.registerGenerator("normal", new NormalGenerator());
		this.registerGenerator("flat", new FlatLandGenerator());

		VanillaBlock.TNT.setPhysics(new TNTPhysics());
		this.hackSettings = new Settings();
		this.hackSettings.registerSetting(new BooleanSetting("hacks.speed"));
		this.hackSettings.registerSetting(new BooleanSetting("hacks.flying"));
		this.mode = this.getSettings().getIntSetting("options.survival").getValue() > 0 ? new SurvivalGameMode() : new CreativeGameMode();

		this.getPluginManager().loadPlugins(LoadOrder.PREWORLD);
		this.getPluginManager().loadPlugins(LoadOrder.POSTWORLD);
		this.setActiveComponent(new LoginScreen());

		Timer timer = new Timer(InternalConstants.TICKS_PER_SECOND);
		while(this.isRunning() && !Display.isCloseRequested()) {
			timer.update();
			Input.update();
			for(int tick = 0; tick < timer.elapsedTicks; tick++) {
				this.tick(timer.delta);
			}

			this.render(timer.delta);
		}

		if(this.isInGame()) {
			this.level = null;
			this.stopGame(false);
		}

		this.unregisterExecutors(this);
		((ClassicScheduler) this.getScheduler()).shutdown();
		this.audio.cleanup();
		OpenClassic.setGame(null);
		System.exit(0);
		this.destroyRender();
		return;
	}

	private void initRender() {
		try {
			Display.setDisplayMode(new DisplayMode(854, 480));
			Display.setResizable(true);
			try {
				Display.setIcon(new ByteBuffer[] { this.loadIcon(Main.class.getResourceAsStream("/icon.png")) });
			} catch(IOException e) {
				OpenClassic.getLogger().severe("Failed to load icon!");
				e.printStackTrace();
			}
		} catch(LWJGLException e) {
			this.handleException(e);
			return;
		}

		Display.setTitle("OpenClassic " + Constants.VERSION);
		try {
			Display.create();
		} catch(LWJGLException e) {
			this.handleException(e);
			return;
		}

		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glClearDepth(GL11.GL_CLIENT_PIXEL_STORE_BIT);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glLineWidth(2);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());

		RenderHelper.getHelper().init();
	}

	private ByteBuffer loadIcon(InputStream in) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocateDirect(4 * 128 * 128);
		buffer.clear();
		byte[] data = (byte[]) ImageIO.read(in).getRaster().getDataElements(0, 0, 128, 128, null);
		buffer.put(data);
		buffer.rewind();
		return buffer;
	}

	private void destroyRender() {
		Display.destroy();
	}

	private void handleException(Throwable e) {
		e.printStackTrace();
		if(this.isRunning() && this.baseGUI != null && !(e instanceof LWJGLException) && !(e instanceof RuntimeException)) {
			this.getProgressBar().setVisible(false);
			this.setActiveComponent(new ErrorScreen(this.getTranslator().translate("core.client-error"), String.format(this.getTranslator().translate("core.game-broke"), e)));
		} else {
			String msg = "Exception occured";
			if(this.getTranslator() != null) {
				msg = this.getTranslator().translate("core.exception");
			}

			JOptionPane.showMessageDialog(null, "See .minecraft_classic/client.log for more details.\n" + e.toString(), msg, 0);
			System.exit(0);
		}
	}

	public void initGame(String server, int port, String mppass) {
		this.audio.stopMusic();
		this.audio.setMusicTime(System.currentTimeMillis() + rand.nextInt(900000));
		if(server != null) {
			this.player.setSession(new ClientSession(this.player, mppass, server, port));
		}

		this.hud = new ClientHUDScreen();
		this.hud.onAttached(null);
		this.mode = this.settings.getIntSetting("options.survival").getValue() > 0 && !this.isInMultiplayer() ? new SurvivalGameMode() : new CreativeGameMode();
		if(this.level != null) {
			this.mode.apply(this.level);
			this.mode.apply(this.player);
		}

		this.ingame = true;
	}

	public void stopGame(boolean menu) {
		this.audio.stopMusic();
		this.serverPlugins.clear();
		if(menu) {
			this.setActiveComponent(new MainMenuScreen());
		}

		this.hud = null;
		if(this.player.getData() != null && !this.isInMultiplayer()) {
			this.player.getData().save(this.getDirectory().getPath() + "/player.nbt");
		}

		if(this.isInMultiplayer()) {
			EventManager.callEvent(new PlayerQuitEvent(this.player, "Quit"));
			this.player.getSession().disconnect(null);
			this.player.setSession(null);
		}

		for(BlockType block : Blocks.getBlocks()) {
			if(block != null) {
				Blocks.unregister(block.getId());
			}
		}

		this.openclassicServer = false;
		this.ingame = false;
		this.player.setHandle(null);
		this.hacks = true;
	}

	public boolean getHacks() {
		return this.hacks;
	}

	public void setHacks(boolean hacks) {
		this.hacks = hacks;
	}

	public GameMode getMode() {
		return this.mode;
	}

	public void setMode(GameMode mode) {
		this.mode = mode;
	}

	public Intersection getSelected() {
		return this.selected;
	}

	@Override
	public void shutdown() {
		this.running = false;
	}

	@Override
	public Level createLevel(LevelInfo info, Generator generator) {
		if(this.isInGame()) {
			this.exitGameSession();
		}

		VanillaBlock.registerAll();
		ClientLevel level = new ClientLevel(info);
		byte[] data = new byte[info.getWidth() * info.getHeight() * info.getDepth()];
		level.setData(info.getWidth(), info.getHeight(), info.getDepth(), data);
		level.setGenerating(true);
		generator.generate(level, data);
		level.setGenerating(false);
		level.setData(info.getWidth(), info.getHeight(), info.getDepth(), data);
		level.setSpawn(generator.findSpawn(level));
		if(info.getSpawn() != null) {
			level.setSpawn(info.getSpawn());
		}

		this.setLevel(level);
		this.mode.prepareLevel(level);
		EventManager.callEvent(new LevelCreateEvent(level));
		return level;
	}

	@Override
	public boolean isRunning() {
		return this.running;
	}

	@Override
	public Player getPlayer() {
		return this.player;
	}

	@Override
	public Level getLevel() {
		if(this.level == null) {
			return null;
		}
		return this.level;
	}

	@Override
	public Level openLevel(String name) {
		if(this.level != null && this.level.getName().equals(name)) {
			return this.level;
		}

		this.getProgressBar().setVisible(true);
		this.getProgressBar().setTitle(this.getTranslator().translate("progress-bar.singleplayer"));
		this.getProgressBar().setSubtitle(this.getTranslator().translate("level.loading"));
		this.getProgressBar().setText(this.getTranslator().translate("level.reading"));
		this.getProgressBar().setProgress(-1);
		this.getProgressBar().render();
		ClientLevel level = null;
		VanillaBlock.registerAll();
		try {
			level = new ClientLevel();
			level = (ClientLevel) OpenClassicLevelFormat.load(level, name, false);
			EventManager.callEvent(new LevelLoadEvent(level));
			this.setLevel(level);
			this.getProgressBar().setVisible(false);
			return level;
		} catch(IOException e) {
			VanillaBlock.unregisterAll();
			this.getProgressBar().setText(String.format(this.getTranslator().translate("level.load-fail"), name));
			e.printStackTrace();
			try {
				Thread.sleep(1000L);
			} catch(InterruptedException e1) {
			}

			this.getProgressBar().setVisible(false);
			return null;
		}
	}

	public void setLevel(ClientLevel level) {
		if(this.level != null && this.level == level) {
			return;
		}

		this.level = level;
		if(level == null) {
			this.setActiveComponent(null);
			return;
		}

		LocalPlayer player = (LocalPlayer) this.player.getHandle();
		this.mode.apply(level);
		if(player != null) {
			level.addEntity(player);
		} else {
			player = new LocalPlayer(level, this.player);
			this.player.setHandle(player);
		}

		player.resetPos();
		// Fix for a strange bug where you can't select blocks in a certain direction.
		player.move(0.001f, 0.001f, 0.001f);
		this.mode.preparePlayer(this.player);

		player.input.resetKeys();
		this.mode.apply(this.player);

		this.initGame(null, 0, null);
		this.setActiveComponent(null);
	}

	@Override
	public boolean saveLevel() {
		if(this.level == null) {
			return false;
		}
		if(EventManager.callEvent(new LevelSaveEvent(this.level)).isCancelled()) {
			return false;
		}

		try {
			OpenClassicLevelFormat.save(this.level);
			if(this.level.getData() != null) {
				this.level.getData().save(this.getDirectory().getPath() + "/levels/" + this.level.getName() + ".nbt");
			}

			return true;
		} catch(IOException e) {
			OpenClassic.getLogger().severe("Failed to save level \"" + this.level.getName() + "\"");
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean saveLevel(String name) {
		if(this.level == null) {
			return false;
		}
		String old = this.level.getName();
		this.level.setName(name);
		boolean ret = this.saveLevel();
		this.level.setName(old);
		return ret;
	}

	@Override
	public void exitGameSession() {
		this.level = null;
		this.stopGame(true);
	}

	@Override
	public AudioManager getAudioManager() {
		return this.audio;
	}

	@Override
	public GuiComponent getActiveComponent() {
		if(this.baseGUI.getComponents().isEmpty()) {
			return null;
		}

		return this.baseGUI.getComponents().get(0);
	}

	@Override
	public void setActiveComponent(GuiComponent component) {
		this.baseGUI.clearComponents();
		if(component == null && this.isInGame() && this.mode instanceof SurvivalGameMode && this.player.getHealth() <= 0) {
			component = new GameOverScreen();
		}

		if(component != null) {
			this.baseGUI.attachComponent(component);
			component.setFocused(true);
			if(this.player.getHandle() != null) {
				((LocalPlayer) this.player.getHandle()).input.resetKeys();
			}

			Input.setMouseGrabbed(false);
		} else {
			Input.setMouseGrabbed(true);
		}
	}

	@Override
	public boolean isInGame() {
		return this.ingame;
	}

	@Override
	public HUDComponent getHUD() {
		return this.hud;
	}

	@Override
	public boolean isInMultiplayer() {
		return this.player.getSession() != null;
	}

	@Override
	public String getServerIP() {
		return ((InetSocketAddress) this.player.getAddress()).getHostName();
	}

	@Override
	public boolean isConnectedToOpenClassic() {
		return this.openclassicServer;
	}

	@Override
	public String getServerVersion() {
		return this.openclassicVersion;
	}

	public void setConnectedToOpenClassic(boolean openclassic) {
		this.openclassicServer = openclassic;
	}

	public void setServerVersion(String version) {
		this.openclassicVersion = version;
	}

	public List<RemotePluginInfo> getPluginInfo() {
		return new ArrayList<RemotePluginInfo>(this.serverPlugins);
	}

	public void addPluginInfo(RemotePluginInfo info) {
		this.serverPlugins.add(info);
	}

	@Override
	public ProgressBar getProgressBar() {
		return this.progressBar;
	}

	@Override
	public Settings getSettings() {
		return this.settings;
	}

	@Override
	public Bindings getBindings() {
		return this.bindings;
	}

	@Override
	public boolean isInSurvival() {
		return this.mode instanceof SurvivalGameMode && !this.isInMultiplayer();
	}

	@Override
	public void joinServer(String url) {
		if(this.isInGame()) {
			this.exitGameSession();
		}

		this.getProgressBar().setVisible(true);
		this.getProgressBar().setSubtitleScaled(false);
		this.getProgressBar().setTitle(this.getTranslator().translate("progress-bar.multiplayer"));
		this.getProgressBar().setSubtitle(this.getTranslator().translate("connecting.connect"));
		this.getProgressBar().setText(this.getTranslator().translate("connecting.getting-info"));
		this.getProgressBar().setProgress(-1);
		this.getProgressBar().render();
		String play = HTTPUtil.fetchUrl(url, "", InternalConstants.MINECRAFT_URL_HTTPS + "classic/list");
		String mppass = HTTPUtil.getParameterOffPage(play, "mppass");

		if(mppass.length() > 0) {
			String server = HTTPUtil.getParameterOffPage(play, "server");
			int port = 0;
			try {
				port = Integer.parseInt(HTTPUtil.getParameterOffPage(play, "port"));
			} catch(NumberFormatException e) {
				this.setActiveComponent(new ErrorScreen(this.getTranslator().translate("connecting.fail-connect"), this.getTranslator().translate("connecting.invalid-page")));
				this.getProgressBar().setVisible(false);
				this.getProgressBar().setSubtitleScaled(true);
				return;
			}

			this.getProgressBar().setVisible(false);
			this.setActiveComponent(null);
			this.initGame(server, port, mppass);
		} else {
			this.setActiveComponent(new ErrorScreen(this.getTranslator().translate("connecting.fail-connect"), this.getTranslator().translate("connecting.check")));
			this.getProgressBar().setVisible(false);
			this.getProgressBar().setSubtitleScaled(true);
			return;
		}
	}

	@Override
	public Settings getHackSettings() {
		return this.hackSettings;
	}

	@Override
	public List<Player> getPlayers() {
		return this.getLevel().getPlayers();
	}

	@Override
	public Player getPlayer(String name) {
		for(Player player : this.getPlayers()) {
			if(player.getName().equalsIgnoreCase(name)) {
				return player;
			}
		}

		return null;
	}

	@Override
	public List<Player> matchPlayer(String name) {
		List<Player> result = new ArrayList<Player>();
		for(Player player : this.getPlayers()) {
			if(player.getName().toLowerCase().contains(name.toLowerCase()) && !result.contains(player)) {
				result.add(player);
			}
		}

		return result;
	}

	public void render(float delta) {
		if(this.prevWidth != Display.getWidth() || this.prevHeight != Display.getHeight()) {
			this.prevWidth = Display.getWidth();
			this.prevHeight = Display.getHeight();
			if(this.getHUD() != null) {
				this.getHUD().setSize(Display.getWidth(), Display.getHeight());
				this.getHUD().clearComponents();
				this.getHUD().onAttached(this.getHUD().getParent());
			}

			this.baseGUI.setSize(Display.getWidth(), Display.getHeight());
			GuiComponent component = this.getActiveComponent();
			if(component != null) {
				component.clearComponents();
				component.onAttached(component.getParent());
			}

			GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
		}

		this.displayActive = Display.isActive();
		this.mode.applyBlockCracks(delta);
		((ClientTextureFactory) TextureFactory.getFactory()).renderUpdateTextures();
		if(this.getLevel() != null) {
			ClientLevel level = (ClientLevel) this.getLevel();
			LocalPlayer player = (LocalPlayer) ((ClientPlayer) this.getPlayer()).getHandle();
			if(Input.isMouseGrabbed()) {
				int x = Input.getMouseDX();
				int y = Input.getMouseDY();
				byte direction = 1;
				if(this.getSettings().getBooleanSetting("options.invert-mouse").getValue()) {
					direction = -1;
				}

				player.turn(x, (y * direction));
				Input.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
			} else {
				player.turn(0, 0);
			}

			float pitch = this.getPlayer().getPosition().getInterpolatedPitch(delta);
			float yaw = this.getPlayer().getPosition().getInterpolatedYaw(delta);
			Vector pVec = this.getPlayer().getPosition().toPosVector(delta);
			float ycos = MathHelper.cos(-yaw * MathHelper.DEG_TO_RAD - MathHelper.PI);
			float ysin = MathHelper.sin(-yaw * MathHelper.DEG_TO_RAD - MathHelper.PI);
			float pcos = MathHelper.cos(-pitch * MathHelper.DEG_TO_RAD);
			float psin = MathHelper.sin(-pitch * MathHelper.DEG_TO_RAD);
			float mx = ysin * pcos;
			float mz = ycos * pcos;
			float reach = this.mode.getReachDistance();
			this.selected = RayTracer.rayTrace((ClientLevel) this.getLevel(), pVec, pVec.clone().add(mx * reach, psin * reach, mz * reach), true);
			if(this.selected != null) {
				reach = this.selected.getPos().distance(pVec);
			}

			if(this.mode instanceof CreativeGameMode) {
				reach = 32;
			}

			Entity selectedEntity = null;
			List<Entity> entities = ((ClientLevel) this.getLevel()).getEntities(player, player.bb.expand(mx * reach, psin * reach, mz * reach));

			float distance = 0;
			for(int count = 0; count < entities.size(); count++) {
				Entity entity = entities.get(count);
				if(entity.isPickable()) {
					Intersection pos = RayTracer.rayTrace(entity.bb.grow(0.1F, 0.1F, 0.1F), pVec, pVec.clone().add(mx * reach, psin * reach, mz * reach));
					if(pos != null && (pVec.distance(pos.getPos()) < distance || distance == 0)) {
						selectedEntity = entity;
						distance = pVec.distance(pos.getPos());
					}
				}
			}

			if(selectedEntity != null && !(this.mode instanceof CreativeGameMode)) {
				this.selected = new Intersection(selectedEntity);
			}

			this.waterDelay++;
			if(this.waterDelay > 200 && rand.nextInt(1000) < 250) {
				this.waterDelay = 0;
				Block block = null;
				float dist = 10000;
				for(int x = this.getPlayer().getPosition().getBlockX() - 50; x < this.getPlayer().getPosition().getBlockX() + 50; x++) {
					for(int y = this.getPlayer().getPosition().getBlockY() - 20; y < this.getPlayer().getPosition().getBlockY() + 20; y++) {
						for(int z = this.getPlayer().getPosition().getBlockZ() - 50; z < this.getPlayer().getPosition().getBlockZ() + 50; z++) {
							BlockType b = this.getLevel().getBlockTypeAt(x, y, z);
							float d = this.getPlayer().getPosition().distance(x, y, z);
							if(b != null && b.getLiquidName() != null && b.getLiquidName().equals("water") && d < dist) {
								block = this.getLevel().getBlockAt(x, y, z);
								dist = d;
							}
						}
					}
				}

				if(block != null) {
					this.getAudioManager().playSound(block.getLevel(), "random.water", block.getPosition().getX() + 0.5f, block.getPosition().getY() + 0.5f, block.getPosition().getZ() + 0.5f, rand.nextFloat() * 0.25f + 0.75f, rand.nextFloat() + 0.5f);
				}
			}

			for(int pass = 0; pass < 2; pass++) {
				if(this.getSettings().getBooleanSetting("options.3d-anaglyph").getValue()) {
					if(pass == 0) {
						GL11.glColorMask(false, true, true, false);
					} else {
						GL11.glColorMask(true, false, false, false);
					}
				}


				level.render(delta, pass);
				GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
				GL11.glLoadIdentity();
				if(this.getSettings().getBooleanSetting("options.3d-anaglyph").getValue()) {
					GL11.glTranslatef(((pass << 1) - 1) * 0.1F, 0, 0);
				}

				RenderHelper.getHelper().hurtEffect(player.openclassic, delta);
				if(this.getSettings().getBooleanSetting("options.view-bobbing").getValue()) {
					RenderHelper.getHelper().applyBobbing(player.openclassic, delta);
				}

				this.heldBlock.render(delta);
				if(!this.getSettings().getBooleanSetting("options.3d-anaglyph").getValue()) {
					break;
				}
			}

			GL11.glColorMask(true, true, true, false);
			RenderHelper.getHelper().ortho();
			if(this.getHUD() != null) {
				this.getHUD().render(delta, Input.getMouseX(), Display.getHeight() - Input.getMouseY());
			}
		} else {
			GL11.glClearColor(0, 0, 0, 0);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			RenderHelper.getHelper().ortho();
		}

		this.baseGUI.render(delta, Input.getMouseX(), Display.getHeight() - Input.getMouseY());
		if(this.getProgressBar().isVisible()) {
			((ClientProgressBar) this.getProgressBar()).render(false);
		}

		Display.update();
		this.fps++;
		if(this.getSettings().getBooleanSetting("options.limit-fps").getValue()) {
			Display.sync(Display.getDesktopDisplayMode().getFrequency());
		}

		if(System.currentTimeMillis() - this.lastUpdate >= 1000) {
			if(this.getHUD() != null) {
				((ClientHUDScreen) this.getHUD()).setDebugInfo(this.fps + " fps, " + Chunk.chunkUpdates + " chunk updates");
			}

			Chunk.chunkUpdates = 0;
			this.fps = 0;
			this.lastUpdate = System.currentTimeMillis();
		}
	}

	public void tick(float delta) {
		this.ticks++;
		((ClientAudioManager) this.getAudioManager()).update();
		((ClassicScheduler) this.getScheduler()).tick(this.schedTicks);
		if(this.getActiveComponent() != null) {
			this.lastClick = this.ticks + 10000;
		}

		LocalPlayer player = (LocalPlayer) ((ClientPlayer) this.getPlayer()).getHandle();
		MouseEvent mouse = null;
		while((mouse = Input.nextMouseEvent()) != null) {
			if(this.isInGame() && this.getActiveComponent() == null) {
				ClientLevel level = (ClientLevel) this.getLevel();
				if(mouse.getDWheel() != 0) {
					player.inventory.scrollSelection(mouse.getDWheel());
				}

				if(!Input.isMouseGrabbed() && mouse.getState()) {
					this.lastClick = this.ticks + 10000;
					Input.setMouseGrabbed(true);
				} else {
					if(mouse.getState()) {
						if(mouse.getButton() == Mouse.LEFT_BUTTON || mouse.getButton() == Mouse.RIGHT_BUTTON) {
							this.onMouseClick(mouse.getButton());
							this.lastClick = this.ticks;
						}

						if(mouse.getButton() == Mouse.MIDDLE_BUTTON && this.selected != null) {
							int block = level.getBlockTypeAt(this.selected.getX(), this.selected.getY(), this.selected.getZ()).getId();
							if(block == VanillaBlock.GRASS.getId()) {
								block = VanillaBlock.DIRT.getId();
							}

							if(block == VanillaBlock.DOUBLE_SLAB.getId()) {
								block = VanillaBlock.SLAB.getId();
							}

							if(block == VanillaBlock.BEDROCK.getId()) {
								block = VanillaBlock.STONE.getId();
							}

							player.inventory.selectBlock(block, this.mode instanceof CreativeGameMode);
						}
					}
				}
			} else if(mouse.getState()) {
				int x = mouse.getX();
				int y = Display.getHeight() - mouse.getY();
				this.baseGUI.onMouseClick(x, y, mouse.getButton());
			}
		}

		KeyboardEvent keyboard = null;
		while((keyboard = Input.nextKeyEvent()) != null) {
			if(keyboard.getState()) {
				if(keyboard.getKey() == Keyboard.KEY_F6) {
					if(Display.isFullscreen()) {
						try {
							Display.setFullscreen(false);
							Display.setDisplayMode(new DisplayMode(854, 480));
						} catch(LWJGLException e) {
							e.printStackTrace();
						}
					} else {
						try {
							Display.setDisplayMode(Display.getDesktopDisplayMode());
							Display.setFullscreen(true);
						} catch(LWJGLException e) {
							e.printStackTrace();
						}
					}
				}
			}

			if(this.isInGame()) {
				if(this.getActiveComponent() == null) {
					ClientLevel level = (ClientLevel) this.getLevel();
					this.getHUD().onKeyPress(keyboard.getCharacter(), keyboard.getKey());
					player.input.setKeyState(keyboard.getKey(), keyboard.getState());
					if(keyboard.getState() && !keyboard.isRepeat()) {
						player.input.keyPress(keyboard.getKey());
					}

					if(keyboard.getState()) {
						if(keyboard.getKey() == Keyboard.KEY_F1) {
							this.getHUD().setVisible(!this.getHUD().isVisible());
						}

						if(keyboard.getKey() == Keyboard.KEY_F2) {
							GL11.glReadBuffer(GL11.GL_FRONT);
							int width = Display.getWidth();
							int height = Display.getHeight();
							ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);
							GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
							File file = new File(this.getDirectory(), "screenshots/" + (new Date(System.currentTimeMillis()).toString().replaceAll(" ", "-").replaceAll(":", "-")) + ".png");
							BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
							for(int x = 0; x < width; x++) {
								for(int y = 0; y < height; y++) {
									int i = (x + (width * y)) * 4;
									int r = buffer.get(i) & 0xFF;
									int g = buffer.get(i + 1) & 0xFF;
									int b = buffer.get(i + 2) & 0xFF;
									image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
								}
							}

							try {
								ImageIO.write(image, "PNG", file);
								if(this.getHUD() != null) {
									this.getPlayer().sendMessage("screenshot.saved", file.getName());
								}
							} catch(IOException e) {
								e.printStackTrace();
								if(this.getHUD() != null) {
									this.getPlayer().sendMessage("screenshot.error", file.getName());
								}
							}
						}

						if(keyboard.getKey() == Keyboard.KEY_ESCAPE) {
							if(this.getActiveComponent() == null && this.isInGame() && (!this.isInMultiplayer() || ((ClientPlayer) this.getPlayer()).getSession().isConnected() && ((ClientPlayer) this.getPlayer()).getSession().getState() == State.GAME)) {
								this.setActiveComponent(new IngameMenuScreen());
							}
						}

						if(this.mode instanceof CreativeGameMode) {
							if(keyboard.getKey() == this.getBindings().getBinding("options.keys.load-loc").getKey()) {
								PlayerRespawnEvent event = new PlayerRespawnEvent(this.getPlayer(), new Position(this.getLevel(), level.getSpawn().getX(), level.getSpawn().getY(), level.getSpawn().getZ(), level.getSpawn().getYaw(), level.getSpawn().getPitch()));
								if(!event.isCancelled()) {
									player.resetPos(event.getPosition());
								}
							}

							if(keyboard.getKey() == this.getBindings().getBinding("options.keys.save-loc").getKey()) {
								level.setSpawn(new Position(level, this.getPlayer().getPosition().getX(), this.getPlayer().getPosition().getY(), this.getPlayer().getPosition().getZ(), this.getPlayer().getPosition().getYaw(), this.getPlayer().getPosition().getPitch()));
								player.resetPos();
							}
						}

						keyboard.getKey();
						if(keyboard.getKey() == Keyboard.KEY_F5 && !this.isConnectedToOpenClassic()) {
							this.getLevel().setRaining(!this.getLevel().isRaining());
						}

						if(keyboard.getKey() == Keyboard.KEY_TAB && this.mode instanceof SurvivalGameMode && player.arrows > 0) {
							this.getAudioManager().playSound("random.bow", this.getPlayer().getPosition().getX(), this.getPlayer().getPosition().getY(), this.getPlayer().getPosition().getZ(), 1, 1 / (rand.nextFloat() * 0.4f + 0.8f));
							level.addEntity(new Arrow(level, player, this.getPlayer().getPosition().getX(), this.getPlayer().getPosition().getY(), this.getPlayer().getPosition().getZ(), this.getPlayer().getPosition().getYaw(), this.getPlayer().getPosition().getPitch(), 1.2F));
							this.getPlayer().setArrows(this.getPlayer().getArrows() - 1);
						}

						if(keyboard.getKey() == Keyboard.KEY_Q && this.mode instanceof SurvivalGameMode) {
							player.dropHeldItem();
						}

						if(keyboard.getKey() == this.getBindings().getBinding("options.keys.blocks").getKey()) {
							this.mode.openInventory();
						}

						if(keyboard.getKey() == this.getBindings().getBinding("options.keys.chat").getKey()) {
							player.input.resetKeys();
							this.setActiveComponent(new ChatInputScreen());
						}

						for(int selection = 0; selection < 9; selection++) {
							if(keyboard.getKey() == selection + 2) {
								player.inventory.selected = selection;
							}
						}

						if(keyboard.getKey() == this.getBindings().getBinding("options.keys.toggle-fog").getKey()) {
							this.getSettings().getSetting("options.render-distance").toggle();
						}
					}

					EventManager.callEvent(new PlayerKeyChangeEvent(this.getPlayer(), keyboard.getKey(), keyboard.getState()));
					if(this.isInMultiplayer() && ((ClientPlayer) this.getPlayer()).getSession().isConnected() && this.isConnectedToOpenClassic()) {
						((ClientPlayer) this.getPlayer()).getSession().send(new KeyChangeMessage(keyboard.getKey(), keyboard.getState()));
					}
				} else if(keyboard.getState() && keyboard.getKey() == Keyboard.KEY_ESCAPE) {
					this.setActiveComponent(null);
				}
			} else if(keyboard.getState()) {
				this.baseGUI.onKeyPress(keyboard.getCharacter(), keyboard.getKey());
			}
		}

		this.baseGUI.update(Input.getMouseX(), Display.getHeight() - Input.getMouseY());
		if(this.getHUD() != null) {
			this.getHUD().update(Input.getMouseX(), Display.getHeight() - Input.getMouseY());
		}

		if(this.getActiveComponent() instanceof ErrorScreen) {
			this.getProgressBar().setVisible(false);
		}

		if(this.displayActive && !this.wasActive && !Input.isButtonDown(Mouse.LEFT_BUTTON) && !Input.isButtonDown(Mouse.RIGHT_BUTTON) && !Input.isButtonDown(Mouse.MIDDLE_BUTTON)) {
			if(this.getActiveComponent() == null && this.isInGame() && (!this.isInMultiplayer() || ((ClientPlayer) this.getPlayer()).getSession().isConnected() && ((ClientPlayer) this.getPlayer()).getSession().getState() == State.GAME)) {
				this.setActiveComponent(new IngameMenuScreen());
			}

			this.wasActive = true;
		} else if(!this.displayActive) {
			this.wasActive = false;
		}

		((ClientTextureFactory) TextureFactory.getFactory()).updateTextures();
		if(!this.isInGame()) {
			return;
		}

		if(System.currentTimeMillis() > ((ClientAudioManager) this.getAudioManager()).getMusicTime() && this.getAudioManager().playMusic("bg")) {
			((ClientAudioManager) this.getAudioManager()).setMusicTime(System.currentTimeMillis() + rand.nextInt(900000) + 300000L);
		}

		this.mode.spawnMobs();
		if(this.isInMultiplayer()) {
			if(!(this.getActiveComponent() instanceof ErrorScreen)) {
				if(!((ClientPlayer) this.getPlayer()).getSession().isConnected()) {
					this.getProgressBar().setVisible(true);
					this.getProgressBar().setTitle(this.getTranslator().translate("progress-bar.multiplayer"));
					this.getProgressBar().setSubtitle(this.getTranslator().translate("connecting.connect"));
					this.getProgressBar().setProgress(-1);
					this.getProgressBar().render();
				} else {
					if(((ClientPlayer) this.getPlayer()).getSession().connectSuccess()) {
						try {
							((ClientPlayer) this.getPlayer()).getSession().tick();
						} catch(Exception e) {
							e.printStackTrace();
							((ClientPlayer) this.getPlayer()).getSession().disconnect(e.toString());
							((ClientPlayer) this.getPlayer()).setSession(null);
						}
					}

					if(this.isInMultiplayer() && ((ClientPlayer) this.getPlayer()).getSession().getState() == State.GAME) {
						((ClientPlayer) this.getPlayer()).getSession().send(new PlayerTeleportMessage((byte) -1, this.getPlayer().getPosition().getX(), this.getPlayer().getPosition().getY(), this.getPlayer().getPosition().getZ(), this.getPlayer().getPosition().getYaw(), this.getPlayer().getPosition().getPitch()));
					}
				}
			}
		}

		if(this.getActiveComponent() == null && player != null && this.mode instanceof SurvivalGameMode && player.health <= 0) {
			this.setActiveComponent(new GameOverScreen());
		}

		if(this.getActiveComponent() == null) {
			if(this.blockHitTime > 0) {
				this.blockHitTime--;
			}

			if(this.getActiveComponent() == null) {
				if(Input.isButtonDown(Mouse.LEFT_BUTTON) && (this.ticks - this.lastClick) >= InternalConstants.TICKS_PER_SECOND / 4 && Input.isMouseGrabbed()) {
					this.onMouseClick(Mouse.LEFT_BUTTON);
					this.lastClick = this.ticks;
				}

				if(Input.isButtonDown(Mouse.RIGHT_BUTTON) && (this.ticks - this.lastClick) >= InternalConstants.TICKS_PER_SECOND / 4 && Input.isMouseGrabbed()) {
					this.onMouseClick(Mouse.RIGHT_BUTTON);
					this.lastClick = this.ticks;
				}
			}

			if(!this.mode.creative && this.blockHitTime <= 0) {
				if(this.getActiveComponent() == null && Input.isButtonDown(Mouse.LEFT_BUTTON) && Input.isMouseGrabbed() && this.selected != null && this.selected.getEntity() == null) {
					this.mode.hitBlock(this.selected.getX(), this.selected.getY(), this.selected.getZ(), this.selected.getFace());
				} else {
					this.mode.resetHits();
				}
			}
		}

		if(this.getLevel() != null) {
			int id = this.getPlayer().getInventoryContents()[this.getPlayer().getSelectedSlot()];
			BlockType block = null;
			if(id > 0) {
				block = Blocks.fromId(id);
			}

			BlockType placemode = this.getPlayer().getPlaceMode();
			block = placemode != null ? placemode : block;
			this.heldBlock.tick(block);
			if(this.getLevel().isRaining()) {
				for(int count = 0; count < 50; count++) {
					int x = this.getPlayer().getPosition().getBlockX() + rand.nextInt(9) - 4;
					int z = this.getPlayer().getPosition().getBlockZ() + rand.nextInt(9) - 4;
					int y = this.getLevel().getHighestBlockY(x, z) + 1;
					if(y <= this.getPlayer().getPosition().getBlockY() + 4 && y >= this.getPlayer().getPosition().getBlockY() - 4) {
						float xOffset = rand.nextFloat();
						float zOffset = rand.nextFloat();
						((ClientLevel) this.getLevel()).getParticleManager().spawnParticle(new RainParticle(new Position(this.getLevel(), x + xOffset, y + 0.1f, z + zOffset)));
					}
				}
			}

			((ClientLevel) this.getLevel()).tick();
		}

		for(Plugin plugin : this.getPluginManager().getPlugins()) {
			plugin.tick();
		}

		this.schedTicks++;
	}

	private void onMouseClick(int button) {
		if(button != 0 || this.blockHitTime <= 0) {
			if(button == 0) {
				this.heldBlock.move();
			}

			int selected = this.getPlayer().getInventoryContents()[this.getPlayer().getSelectedSlot()];
			if(button == 1 && selected > 0 && this.mode.useItem(this.getPlayer(), selected)) {
				this.heldBlock.setHeldPosition(0);
			} else if(this.selected == null) {
				if(button == 0 && !(this.mode instanceof CreativeGameMode)) {
					this.blockHitTime = 10;
				}
			} else {
				if(this.selected.getEntity() != null) {
					if(button == 0) {
						this.selected.getEntity().hurt(((ClientPlayer) this.getPlayer()).getHandle(), 4);
						return;
					}
				} else {
					int x = this.selected.getX();
					int y = this.selected.getY();
					int z = this.selected.getZ();
					if(button == 0) {
						if(this.getLevel() != null && (!this.getLevel().getBlockTypeAt(x, y, z).isUnbreakable() || this.getPlayer().canBreakUnbreakables())) {
							this.mode.hitBlock(x, y, z);
							return;
						}
					} else if(selected > 0) {
						x += this.selected.getFace().getModX();
						y += this.selected.getFace().getModY();
						z += this.selected.getFace().getModZ();
						BlockType type = Blocks.fromId(selected);
						if(type == null) {
							return;
						}

						if(this.getPlayer().getPlaceMode() != null) {
							type = this.getPlayer().getPlaceMode();
						}

						BlockType block = this.getLevel().getBlockTypeAt(x, y, z);
						BoundingBox collision = type.getModel(this.getLevel(), x, y, z).getCollisionBox(x, y, z);
						if((block == null || block.canPlaceIn()) && (collision == null || (!((ClientPlayer) this.getPlayer()).getHandle().bb.intersects(collision) && ((ClientLevel) this.getLevel()).isFree(collision)))) {
							if(!this.mode.canPlace(type.getId())) {
								return;
							}

							if(this.isInMultiplayer()) {
								((ClientPlayer) this.getPlayer()).getSession().send(new PlayerSetBlockMessage((short) x, (short) y, (short) z, button == 1, type.getId()));
							} else if(EventManager.callEvent(new BlockPlaceEvent(this.getLevel().getBlockAt(x, y, z), this.getPlayer(), this.heldBlock.getBlock())).isCancelled()) {
								return;
							}

							this.getLevel().setBlockAt(x, y, z, type);
							this.heldBlock.setHeldPosition(0);
							if(type != null && type.getPhysics() != null) {
								type.getPhysics().onPlace(this.getLevel().getBlockAt(x, y, z));
							}

							if(type != null && type.getStepSound() != StepSound.NONE) {
								this.getAudioManager().playSound(type.getStepSound().getSound(), x, y, z, (type.getStepSound().getVolume() + 1) / 2, type.getStepSound().getPitch() * 0.8F);
							}
						}
					}
				}
			}
		}
	}

}
