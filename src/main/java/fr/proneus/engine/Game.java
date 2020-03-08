package fr.proneus.engine;

import fr.proneus.engine.audio.Sound;
import fr.proneus.engine.audio.SoundManager;
import fr.proneus.engine.data.DataManager;
import fr.proneus.engine.discord.DiscordRPCInfo;
import fr.proneus.engine.discord.DiscordRPCManager;
import fr.proneus.engine.graphic.Camera;
import fr.proneus.engine.graphic.Graphics;
import fr.proneus.engine.graphic.Image;
import fr.proneus.engine.input.*;
import fr.proneus.engine.utils.ByteBufferUtils;
import fr.themode.utils.file.FileUtils;
import fr.themode.utils.timer.Timer;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWImage.Buffer;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.Callback;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.function.Consumer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30C.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Game {

    protected int width, height;
    private static int cameraWidth, cameraHeight;
    private long window;
    private String title;
    private int monitorWidth, monitorHeight;

    private State state;
    private boolean shouldExitState;

    private Camera camera;
    private Graphics graphics;

    private int fps;
    private int maxfps;
    private int tps;
    private double nanoSecondsTicks;
    private double delta;

    private boolean allowResizeLoop;

    private Consumer<Game> close;

    private Callback debugProc;
    private boolean debug = false;
    private boolean resizable;

    private String icon;

    private Timer timer;

    private SoundManager soundManager;

    private Input input;
    private KeyboardManager keyboardManager;
    private MouseManager mouseManager;
    private MousePositionManager mousePositionManager;
    private MouseScrollManager mouseScrollManager;
    private ControllerManager controllerManager;

    private CharCallback charCallback;

    // Discord
    private DiscordRPCManager discordRPGManager;

    // Data
    private DataManager dataManager;

    public Game(String title, int windowsWidth, int windowsHeight, int cameraWidth, int cameraHeight, State state) {
        this.title = title;
        this.width = windowsWidth;
        this.height = windowsHeight;

        this.camera = new Camera();
        Game.cameraWidth = cameraWidth;
        Game.cameraHeight = cameraHeight;
        this.graphics = new Graphics(camera);

        // State
        this.state = state;

        this.timer = new Timer();

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

        this.charCallback = new CharCallback(this);
        this.charCallback.setListener(state);

        this.input = new Input(this, keyboardManager, mousePositionManager, mouseManager, controllerManager);

        this.resizable = true;

        // Data
        this.dataManager = new DataManager();

        // Fps
        this.maxfps = Integer.MAX_VALUE;
        setTPS(60);

        // File setup
        FileUtils.setClass(this.getClass());

    }

    public static float getAspectRatio() {
        return (float) getCameraWidth() / (float) getCameraHeight();
    }

    public static int getCameraWidth() {
        return cameraWidth;
    }

    public static int getCameraHeight() {
        return cameraHeight;
    }

    protected void start() {

        try {
            // TODO better way ?
            try {
                init();
                this.soundManager.init();

                setState(state);
                loop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            try {
                // Close callback
                if (close != null) {
                    close.accept(this);
                }

                destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void setCloseCallBack(Consumer<Game> callBack) {
        this.close = callBack;
    }

    public void setResizable(boolean resizable) {
        this.resizable = resizable;
    }

    protected void setIcon(String icon) {
        this.icon = icon;
    }

    protected void setMaxFPS(int fps) {
        this.maxfps = fps > 0 ? fps : this.maxfps;
    }

    public void setTPS(int tps) {
        this.tps = tps > 0 ? tps : this.tps;
        this.nanoSecondsTicks = 1000000000.0 / (double) tps;
    }

    protected void enableDiscordRPC(String applicationId) {
        this.discordRPGManager = new DiscordRPCManager(false, applicationId);
        this.discordRPGManager.connect(this);
    }

    private void initWindow() {

        GLFWErrorCallback.createPrint().set();
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_RESIZABLE, resizable ? GLFW_TRUE : GLFW_FALSE);
        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_SAMPLES, 4);

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        this.monitorWidth = vidmode.width();
        this.monitorHeight = vidmode.height();

        this.window = glfwCreateWindow(width, height, title, NULL, NULL);

        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        if (icon != null) {
            InputStream iconStream = FileUtils.getInternalFile(icon);
            Image image = new Image(iconStream);
            Buffer buff = GLFWImage.malloc(1).width(image.getImagePixelWidth()).height(image.getImagePixelHeight())
                    .pixels(ByteBufferUtils.convertImage(image.getBufferedImage()));
            glfwSetWindowIcon(window, buff);
        }

        glfwSetWindowSizeCallback(window, this::windowSizeChanged);

        glfwSetKeyCallback(window, keyboardManager);
        glfwSetMouseButtonCallback(window, mouseManager);
        glfwSetCursorPosCallback(window, mousePositionManager);
        glfwSetScrollCallback(window, mouseScrollManager);
        glfwSetJoystickCallback(controllerManager);

        glfwSetCharModsCallback(window, charCallback);

        // Resize Ratio
        glfwSetWindowAspectRatio(window, cameraWidth, cameraHeight);

        // Center window
        glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);

        glfwMakeContextCurrent(window);

        GL.createCapabilities();
        debugProc = debug ? GLUtil.setupDebugMessageCallback() : null;

        glfwSwapInterval(0);
        glfwShowWindow(window);
    }

    private void init() {
        initWindow();
    }

    private void loop() {

        // Timer
        timer.start();

        // Fps
        boolean tick, render;
        long timer = System.currentTimeMillis();

        long beforeTicks = System.nanoTime();
        long beforeFps = System.nanoTime();
        double elapsedTicks;
        double elapsedFps;
        double nanoSecondsFps = 1000000000.0 / (double) maxfps;
        double oldTimeSinceStart = 0;

        int frames = 0;

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        this.allowResizeLoop = true;

        while (!glfwWindowShouldClose(window)) {
            double timeSinceStart = glfwGetTime();
            delta = timeSinceStart - oldTimeSinceStart;
            oldTimeSinceStart = timeSinceStart;

            tick = false;
            render = false;

            long now = System.nanoTime();
            elapsedTicks = now - beforeTicks;
            elapsedFps = now - beforeFps;

            if (elapsedTicks > nanoSecondsTicks) {
                beforeTicks += nanoSecondsTicks;
                tick = true;
            }

            if (elapsedFps > nanoSecondsFps) {
                beforeFps += nanoSecondsFps;
                render = true;
                frames++;
            }

            // Update
            if (tick) {
                update();
            }

            // Render
            if (render) {
                render();
            }

            // Fps counter
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                fps = frames;
                frames = 0;
            }
        }
    }

    private void update() {
        // Discord
        if (hasDiscordRPCEnabled()) {
            discordRPGManager.getDiscordRPC().Discord_RunCallbacks();
        }
        // Reset input
        keyboardManager.resetKeys();
        mouseManager.resetKeys();

        glfwPollEvents();
        state.update();
    }

    private void render() {
        glClearColor(0f, 0f, 0f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        state.render(graphics);
        graphics.renderFrame();

        glfwSwapBuffers(window);
    }

    private void windowSizeChanged(long window, int width, int height) {
        this.width = width;
        this.height = height;
        glViewport(0, 0, width, height);
        // Redraw during resize for windows users
        if (this.allowResizeLoop) {
            render();
        }

    }

    private void destroy() {

        if (debugProc != null)
            debugProc.free();

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
        this.timer.stop();
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

    // GLFW's methods

    public long getTimeFromStart() {
        return (long) (glfwGetTime() * 1000);
    }

    public void lockMouse(boolean value) {
        glfwSetInputMode(window, GLFW_CURSOR, value ? GLFW_CURSOR_DISABLED : GLFW_CURSOR_NORMAL);
    }

    public void setTitle(String title) {
        glfwSetWindowTitle(window, title);
    }

    public void setCursor(Cursor cursor) {
        glfwSetCursor(window, glfwCreateStandardCursor(cursor.getValue()));
    }

    public String getClipboard() {
        return glfwGetClipboardString(window);
    }

    public void setClipboard(String clipboard) {
        glfwSetClipboardString(window, clipboard);
    }

    public int getMonitorWidth() {
        return monitorWidth;
    }

    public int getMonitorHeight() {
        return monitorHeight;
    }

    // Engine's methods

    public State getState() {
        return state;
    }

    public void setState(State state) {
        if (this.state != null && shouldExitState)
            this.state.exit();
        if (hasDiscordRPCEnabled()) {
            this.discordRPGManager.disconnect();
        }
        this.timer.reset();

        this.state = state;
        this.state.setGame(this);
        this.keyboardManager.setListener(state);
        this.mouseManager.setListener(state);
        this.mousePositionManager.setListener(state);
        this.mouseScrollManager.setListener(state);
        this.controllerManager.setListener(state);
        this.charCallback.setListener(state);
        this.state.create();
        this.shouldExitState = true;
    }

    public int getFps() {
        return fps;
    }

    public int getDeltaFps() {
        return (int) (1D / delta);
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

    public void exit() {
        glfwSetWindowShouldClose(glfwGetCurrentContext(), true);
    }

    public BufferedImage screenshot() {
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

    public Sound loadSound(File file) {
        try {
            return new Sound(this.soundManager.loadSound(new FileInputStream(file)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Sound loadSound(InputStream stream) {
        return new Sound(this.soundManager.loadSound(stream));
    }

    public Timer getTimer() {
        return this.timer;
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
