package fr.proneus.engine.demo;

import fr.proneus.engine.State;
import fr.proneus.engine.graphic.*;
import fr.proneus.engine.graphic.animation.Animation;
import fr.proneus.engine.graphic.animation.AnimationFrame;
import fr.themode.utils.file.FileUtils;

public class DemoState extends State {

    private float caseWidth = 52f / 512f;
    private float caseHeight = 52f / 288f;

    private Sprite board;
    private Sprite object;
    private Sprite cursor;

    private FontTexture fontTexture;
    private Font fontObject;

    @Override
    public void create() {

        Texture boardTexture = new Texture(FileUtils.getInternalFile("plateau.png"));
        this.board = new Sprite(boardTexture);
        board.setPosition(0.5f, 0.5f); // Centered

        Texture animTexture = new Texture(FileUtils.getInternalFile("chasseur_vol_idle.png"));
        this.object = new Sprite(animTexture);
        float posx = (3 + 1) * caseWidth - caseWidth / 2;
        float posy = (4 + 1) * caseHeight - caseHeight / 2;
        this.object.setPosition(posx, posy, 1);
        Animation animation = new Animation();
        for (int i = 0; i < 24; i++) {
            AnimationFrame frame = new AnimationFrame(animTexture, i, 0, 24, 1);
            animation.append(frame);
        }
        object.addAnimation("idle", animation);
        object.setAnimation("idle", 50);

        Texture cursorTexture = new Texture(FileUtils.getInternalFile("cursor.png"));
        Texture cursorAnimTexture = new Texture(FileUtils.getInternalFile("cursor_anim.png"));
        this.cursor = new Sprite(cursorTexture);
        Animation cursorAnim = new Animation();
        for (int i = 0; i < 8; i++) {
            AnimationFrame frame = new AnimationFrame(cursorAnimTexture, i, 0, 8, 1);
            cursorAnim.append(frame);
        }
        cursor.addAnimation("cursor", cursorAnim);
        cursor.setAnimation("cursor", 75);

        // TEXT
        this.fontTexture = new FontTexture("fonts/Noto.ttf", 64);
        this.fontObject = new Font(fontTexture);
        this.fontObject.setPosition(0.5f, 0.5f, 1);
        this.fontObject.setColor(Color.WHITE);

    }

    @Override
    public void update() {
        this.fontObject.setText("Test à'éçàé' " + getGame().getFps());
        System.out.println(fontObject.getTextWidth() + ": " + fontObject.getTextHeight());
    }

    @Override
    public void render() {
        board.draw();
        object.draw();
        cursor.draw();

        fontObject.draw();
    }

    @Override
    public void exit() {

    }

    @Override
    public void onMouseMove(float x, float y) {
        float offsetX = 49f / 512f;
        float offsetY = 53f / 288f;

        int caseX = (int) (x / caseWidth);
        int caseY = (int) (y / caseHeight);

        float posx = caseX * caseWidth - caseWidth / 2 + offsetX;
        float posy = caseY * caseHeight - caseHeight / 2 + offsetY;
        this.cursor.setPosition(posx, posy, 1);

        if (caseX == 1 && caseY == 1) {
            cursor.disableAnimation();
        } else if (cursor.getCurrentAnimationName() == null) {
            cursor.setAnimation("cursor", 75);
        }
    }
}
