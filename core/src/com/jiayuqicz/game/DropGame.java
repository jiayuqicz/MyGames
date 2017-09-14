package com.jiayuqicz.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * Created by wzq on 2017/9/8.
 */

public class DropGame extends Game {

    public SpriteBatch batch = null;
    public Label.LabelStyle labelStyle = null;



    @Override
    public void create() {
        initStyle();
        batch = new SpriteBatch();
        setScreen(new MainMenuScreen(this));
    }


    public void initStyle() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        parameter.borderWidth = 1;
        parameter.color = Color.YELLOW;
        BitmapFont font24 = generator.generateFont(parameter); // font size 24 pixels
        generator.dispose();

        labelStyle = new Label.LabelStyle();
        labelStyle.font = font24;

    }


    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
    }
}
