package com.jiayuqicz.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Created by wzq on 2017/9/8.
 */

class MainMenuScreen implements Screen{

    private DropGame game;
    private OrthographicCamera camera;
    private Label.LabelStyle labelStyle;
    private Stage stage;

    private int row_height = Gdx.graphics.getHeight() / 12;
    private int col_width = Gdx.graphics.getWidth();


    public MainMenuScreen(DropGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        labelStyle = game.labelStyle;
        stage = new Stage(new ScreenViewport());
        labelStyle.fontColor = Color.WHITE;


        Label label2 = new Label("Touch anywhere to begin",labelStyle);
        label2.setAlignment(Align.center);
        label2.setSize(col_width/6,row_height);
        label2.setColor(Color.BLACK);
        label2.setPosition(col_width/2-col_width/12, row_height*6);
        stage.addActor(label2);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        stage.act();
        stage.draw();

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
