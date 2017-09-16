package com.jiayuqicz.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

/**
 * Created by wzq on 2017/9/8.
 */

class MainMenuScreen implements Screen{

    private DropGame game;
    private OrthographicCamera camera;
    private SpriteBatch batch = null;

    private Label label;

    private int height = 480;
    private int width = 800;


    public MainMenuScreen(DropGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, width, height);
        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);

        Label.LabelStyle labelStyle = game.getFontStyle(24, Color.BLUE);

        label = new Label("Touch anywhere to begin",labelStyle);
        label.setAlignment(Align.center);

//        Gdx.app.log("test", String.valueOf(width));

        label.setSize(width, height);
        label.setPosition(0, 0);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.begin();
        label.draw(batch, 1);
        batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new DropGameScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
    }
}
