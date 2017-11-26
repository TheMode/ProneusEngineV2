package fr.proneus.engine;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_STENCIL_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_STENCIL_TEST;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearStencil;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glReadBuffer;
import static org.lwjgl.opengl.GL11.glReadPixels;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWImage.Buffer;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.Callback;

import fr.proneus.engine.audio.Sound;
import fr.proneus.engine.audio.SoundManager;
import fr.proneus.engine.camera.Camera;
import fr.proneus.engine.data.DataManager;
import fr.proneus.engine.discord.DiscordRPCInfo;
import fr.proneus.engine.discord.DiscordRPCManager;
import fr.proneus.engine.graphic.Font;
import fr.proneus.engine.graphic.Graphics;
import fr.proneus.engine.graphic.Image;
import fr.proneus.engine.input.ControllerManager;
import fr.proneus.engine.input.Input;
import fr.proneus.engine.input.KeyboardManager;
import fr.proneus.engine.input.MouseManager;
import fr.proneus.engine.input.MousePositionManager;
import fr.proneus.engine.input.MouseScrollManager;
import fr.proneus.engine.state.State;
import fr.proneus.engine.timer.Timer;
import fr.proneus.engine.timer.TimerManager;
import fr.proneus.engine.timer.TimerRunnable;
import fr.proneus.engine.utils.ByteBufferUtils;

public class Game {

	private long window;
	private WindowType windowType;

	private String title;
	protected int width;
	protected int height;
	protected int virtualWidth, virtualHeight;

	private boolean scale;

	private State state;
	private Graphics graphic;

	private Camera camera;

	private int fps;
	private int maxfps;
	private int tps;
	private double delta;

	private CloseCallBack close;

	private Callback debugProc;
	private boolean debug;
	private boolean resizable;

	private String icon;

	private TimerManager timerManager;
	private TimerRunnable timerRunnable;

	private SoundManager soundManager;

	private Input input;
	private KeyboardManager keyboardManager;
	private MouseManager mouseManager;
	private MousePositionManager mousePositionManager;
	private MouseScrollManager mouseScrollManager;
	private ControllerManager controllerManager;

	// Discord
	private DiscordRPCManager discordRPGManager;

	// Data
	private DataManager dataManager;

	public Game(String title, int width, int height, State state) {
		this.windowType = WindowType.NORMAL;
		this.title = title;
		this.width = width;
		this.height = height;
		this.virtualWidth = 1920;
		this.virtualHeight = 1080;

		this.camera = new Camera(this);

		this.state = state;

		this.timerManager = new TimerManager();
		this.timerRunnable = new TimerRunnable(timerManager);

		this.soundManager = new SoundManager();

		this.keyboardManager = new KeyboardManager(this);
		this.keyboardManager.setListener(state);
		this.mouseManager = new MouseManager(this);
		this.mouseManager.setListener(state);
		this.mousePositionManager = new MousePositionManager(this);
		this.mousePositionManager.setListener(state);
		this.mouseScrollManager = new MouseScrollManager(this);
		this.mouseScrollManager.setListener(state);
		this.controllerManager = new ControllerManager(this);
		this.controllerManager.setListener(state);

		this.input = new Input(this, keyboardManager, mousePositionManager, mouseManager, controllerManager);

		this.scale = true;
		this.resizable = true;

		// Data
		this.dataManager = new DataManager();

		this.maxfps = Integer.MAX_VALUE;
		this.tps = 60;

	}

	protected void start() {

		try {
			init();
			this.soundManager.init();
			loop();
		} finally {
			try {
				if (close != null) {
					close.onClose(this);
				}

				if (hasDiscordRPCEnabled()) {
					discordRPGManager.getDiscordRPC().Discord_Shutdown();
				}

				destroy();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	protected void setCloseCallBack(CloseCallBack callBack) {
		this.close = callBack;
	}

	public void setResizable(boolean resizable) {
		this.resizable = resizable;
	}

	protected void setIcon(String icon) {
		this.icon = icon;
	}

	protected void setBorderless() {
		this.windowType = WindowType.BORDERLESS;
	}

	protected void setFullScreen() {
		this.windowType = WindowType.FULLSCREEN;
	}

	protected void setScale(boolean scale) {
		this.scale = scale;
	}

	protected void setMaxFPS(int fps) {
		this.maxfps = fps > 0 ? fps : this.maxfps;
	}

	protected void setTPS(int tps) {
		this.tps = tps > 0 ? tps : this.tps;
	}

	protected void setDebug(boolean debug) {
		this.debug = debug;
	}

	protected void enableDiscordRPC(String applicationID) {
		this.discordRPGManager = new DiscordRPCManager();
		this.discordRPGManager.connect(this, applicationID);
	}

	private void initWindow(WindowType windowType) {
		boolean isFirstWindow = window == 0;
		boolean isBorderless = windowType.equals(WindowType.BORDERLESS);
		boolean isFullScreen = windowType.equals(WindowType.FULLSCREEN);

		GLFWErrorCallback.createPrint().set();
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, resizable ? GLFW_TRUE : GLFW_FALSE);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 2);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
		glfwWindowHint(GLFW_DECORATED, isBorderless ? GLFW_FALSE : GLFW_TRUE);

		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

		width = isBorderless ? vidmode.width() : width;
		height = isBorderless ? vidmode.height() : height;
		long newWindow = glfwCreateWindow(width, height, title, isFullScreen ? glfwGetPrimaryMonitor() : NULL, window);

		if (!isFirstWindow) {
			glfwDestroyWindow(window);
		}
		this.window = newWindow;

		if (window == NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		if (icon != null) {
			Image image = new Image(icon);
			Buffer buff = GLFWImage.malloc(1).width(image.getImageWidth()).height(image.getImageHeight())
					.pixels(ByteBufferUtils.convertImage(new Image(icon).getBufferedImage()));
			glfwSetWindowIcon(window, buff);
		}

		glfwSetWindowSizeCallback(window, this::windowSizeChanged);

		glfwSetKeyCallback(window, keyboardManager);
		glfwSetMouseButtonCallback(window, mouseManager);
		glfwSetCursorPosCallback(window, mousePositionManager);
		glfwSetScrollCallback(window, mouseScrollManager);
		glfwSetJoystickCallback(controllerManager);

		// Resize Ratio
		glfwSetWindowAspectRatio(window, 16, 9);

		// Center window
		glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);

		glfwMakeContextCurrent(window);

		GL.createCapabilities();
		debugProc = debug ? GLUtil.setupDebugMessageCallback() : null;

		glfwSwapInterval(0);
		glfwShowWindow(window);

		glEnable(GL_STENCIL_TEST);
		glClearStencil(0);
		
		// Borderless
		if (isBorderless) {
			glfwSetWindowSize(window, width, height);
			glfwSetWindowPos(window, 0, 0);
		}
	}

	private void init() {

		initWindow(windowType);

		// Graphics
		this.graphic = new Graphics(this);
	}

	private void loop() {

		state.create(this);

		// Timer
		new Thread(timerRunnable).start();

		// Fps
		boolean tick, render;
		long timer = System.currentTimeMillis();

		long beforeTicks = System.nanoTime();
		long beforeFps = System.nanoTime();
		double elapsedTicks;
		double elapsedFps;
		double nanoSecondsTicks = 1000000000.0 / (double) tps;
		double nanoSecondsFps = 1000000000.0 / (double) maxfps;
		double oldTimeSinceStart = 0;

		int frames = 0;

		glEnable(GL_STENCIL_TEST);
		glClearStencil(0);

		glEnable(GL_TEXTURE_2D);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, width, height, 0, -1, 1);
		glMatrixMode(GL_MODELVIEW);

		while (!glfwWindowShouldClose(window)) {
			double timeSinceStart = glfwGetTime();
			delta = timeSinceStart * 1000 - oldTimeSinceStart * 1000;
			oldTimeSinceStart = timeSinceStart;

			tick = false;
			render = false;

			long now = System.nanoTime();
			elapsedTicks = now - beforeTicks;
			elapsedFps = now - beforeFps;

			if (elapsedTicks > nanoSecondsTicks) {
				beforeTicks += nanoSecondsTicks;
				tick = true;
			} else if (elapsedFps > nanoSecondsFps) {
				beforeFps += nanoSecondsFps;
				render = true;
				frames++;
			}

			// Update
			if (tick) {

				// Discord
				if (hasDiscordRPCEnabled()) {
					discordRPGManager.getDiscordRPC().Discord_RunCallbacks();
				}

				glfwPollEvents();
				state.update(this);
				state.componentsUpdate(this);
			}
			// Update end

			// Render
			if (render) {
				glClear(GL_COLOR_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
				glEnable(GL_BLEND);
				glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

				glPushMatrix();

				glTranslatef(camera.getX(), camera.getY(), 0);
				state.render(this, graphic);
				// Change if issue
				state.getLightManager().render(this);
				state.componentsRender(this, graphic);

				glPopMatrix();

				glfwSwapBuffers(window);
			}
			// Render end

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				fps = frames;
				frames = 0;
			}

		}
		glDisable(GL_TEXTURE_2D);
	}

	private void windowSizeChanged(long window, int width, int height) {
		this.width = width;
		this.height = height;
		
		glViewport(0, 0, width, height);

	}

	private void destroy() {

		if (debugProc != null)
			debugProc.free();

		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);
		glfwTerminate();
		glfwSetErrorCallback(null).free();
		timerRunnable.stop();
		soundManager.delete();

		System.exit(0);
	}

	// Game's methods

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getVirtualWidth() {
		return virtualWidth;
	}

	public int getVirtualHeight() {
		return virtualHeight;
	}

	public boolean isScalable() {
		return scale;
	}

	public Graphics getGraphic() {
		return graphic;
	}

	// GLFW's methods

	public long getTimeFromStart() {
		return (long) (glfwGetTime() * 1000);
	}

	public void setTitle(String title) {
		glfwSetWindowTitle(window, title);
	}

	public void setCursor(Cursor cursor) {
		long c = glfwCreateStandardCursor(cursor.getValue());
		glfwSetCursor(window, c);
	}

	public String getClipboard() {
		return glfwGetClipboardString(window);
	}

	public void setClipboard(String clipboard) {
		glfwSetClipboardString(window, clipboard);
	}

	// Engine's methods

	public State getState() {
		return state;
	}

	public int getFps() {
		return fps;
	}

	public int getTps() {
		return tps;
	}

	public double getDelta() {
		return delta;
	}

	public long getWindowID() {
		return window;
	}

	public Camera getCamera() {
		return camera;
	}

	public void setState(State state) {
		this.state.exit(this);
		this.timerManager.reset();
		this.state = state;
		this.keyboardManager.setListener(state);
		this.mouseManager.setListener(state);
		this.mousePositionManager.setListener(state);
		this.mouseScrollManager.setListener(state);
		this.controllerManager.setListener(state);
		this.state.create(this);
	}

	public WindowType getWindowType() {
		return windowType;
	}
	
	public void changeWindow(WindowType windowType) {
		initWindow(windowType);
	}

	public enum WindowType {
		NORMAL, BORDERLESS, FULLSCREEN;
	}

	public void exit() {
		glfwSetWindowShouldClose(glfwGetCurrentContext(), true);
	}

	public BufferedImage getScreenshot() {
		glReadBuffer(GL_FRONT);
		int bpp = 4;
		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);

		glReadPixels(0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int i = (x + (width * y)) * bpp;
				int r = buffer.get(i) & 0xFF;
				int g = buffer.get(i + 1) & 0xFF;
				int b = buffer.get(i + 2) & 0xFF;
				image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
			}
		}

		return image;
	}

	public Font getCurrentFont() {
		return graphic.getFont();
	}

	public Sound loadSound(String path) {
		return new Sound(this.soundManager.loadSound(path));
	}

	public Timer getTimer() {
		return timerManager.getTimer();
	}

	public Input getInput() {
		return input;
	}

	// Discord
	public boolean hasDiscordRPCEnabled() {
		return discordRPGManager != null;
	}

	public void updateDiscordRPC(DiscordRPCInfo info) {
		this.discordRPGManager.update(info);
	}

	// Data
	public DataManager getDataManager() {
		return dataManager;
	}

}
