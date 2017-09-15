package com.jiayuqicz.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class DropGameScreen implements Screen, InputProcessor {

    private DropGame game;
	private Texture bucketImage = null;
	private Texture rainImage = null;
	private Sound dropSound = null;
	private Music rainMusic = null;

    private boolean flag = true;
    private final int left = 0;
    private final int center = 1;
    private final int right = 2;

    private int row_height = Gdx.graphics.getHeight();
    private int col_width = Gdx.graphics.getWidth();

    private Label.LabelStyle labelStyle;
    private Label score = null;

    private int direction = center;

    private OrthographicCamera camera = null;
    private SpriteBatch batch = null;
    private Rectangle bucket = null;

    private Vector3 touch = null;
    private Rectangle abnormal = null;
    private Rectangle normalDrop = null;

    private Array<Rectangle> drops = null;
    private long lastDropTime = 0;
    private long lastShadow = 0;

    public DropGameScreen (DropGame game) {
        this.game = game;
        //初始化界面
        initView();

    }

    private void initView() {

        labelStyle = game.labelStyle;
        labelStyle.fontColor = Color.BLUE;


        score = new Label("Score：",labelStyle);
        score.setAlignment(Align.top);
        score.setSize(16,16);
        score.setPosition(0, 400);

        batch = new SpriteBatch();
        bucketImage = new Texture(Gdx.files.internal("bucket.png"));
        rainImage = new Texture(Gdx.files.internal("raindrop.png"));

        dropSound = Gdx.audio.newSound(Gdx.files.internal("dropSound.wav"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rainSound.mp3"));
        rainMusic.setLooping(true);
        rainMusic.play();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        bucket = new Rectangle();
        bucket.setX(800/2 - 64/2);
        bucket.setY(20);
        bucket.setWidth(64);
        bucket.setHeight(64);

        touch = new Vector3();
        drops = new Array<Rectangle>();

        Gdx.input.setInputProcessor(this);
        spawnDrop();
    }

	@Override
	public void render (float delta) {
		Gdx.gl.glClearColor(255,255,255, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        //初始化batch
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (Rectangle drop: drops) {
            batch.draw(rainImage, drop.x, drop.y);
        }
        batch.draw(bucketImage, bucket.x, bucket.y);
        score.draw(batch, 12);
        batch.end();

        for (Rectangle drop : drops) {

            if (drop == normalDrop && normalDrop !=null) {
                center(drop);
            }
            else {
                switch (direction) {
                    case center:
                        center(drop);
                        break;
                    case left:
                        left(drop);
                        break;
                    case right:
                        right(drop);
                        break;
                }
            }

            if (abnormal != null && TimeUtils.nanoTime() - lastShadow > 1000) {
                if (flag) {
                    abnormal.x += 200;
                }
                else {
                    abnormal.x -= 200;
                }
                flag = !flag;
                lastShadow = TimeUtils.nanoTime();
                if (abnormal.y <100) {
                    abnormal = null;
                }

            }


            if (drop.y < 0) {
                drops.removeValue(drop, false);
            }

            if ((drop.x < 0)) {
                direction = right;
            }

            if (drop.x > 800 - 64) {
                direction = left;
            }

            if (drop.overlaps(bucket)) {
                drops.removeValue(drop, false);
                dropSound.play();
            }

            //每一秒生成一个雨滴
            if (TimeUtils.nanoTime() - lastDropTime > 1000000000) {
                spawnDrop();
            }

        }
	}

	private void left(Rectangle drop) {
        drop.y -= Gdx.graphics.getDeltaTime() * 200;
        drop.x -= Gdx.graphics.getDeltaTime() * 200;
    }

    private void right(Rectangle drop) {
        drop.y -= Gdx.graphics.getDeltaTime() * 200;
        drop.x += Gdx.graphics.getDeltaTime() * 200;
    }

    private void center(Rectangle drop) {
        drop.y -= Gdx.graphics.getDeltaTime() * 200;
    }


    private void spawnDrop() {
        Rectangle drop = new Rectangle();
        drop.setY(480);

        int x = MathUtils.random(0,800-64);
        Boolean normal = false;
        Boolean random = false;

        drop.setX(x);

        drop.width = 64;
        drop.height = 64;
        drops.add(drop);
        lastDropTime = TimeUtils.nanoTime();

        if (MathUtils.random(0,4)>3) {
            normal = true;
        }
        else if (MathUtils.random(0,4)<1) {
            random = true;
        }

        if (normal) {
            normalDrop = drop;
        }

        if (random) {
            abnormal = drop;
            lastShadow = lastDropTime;
        }
    }

    @Override
    public void show() {

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
	public void dispose () {
		batch.dispose();
        dropSound.dispose();
        rainMusic.dispose();
        bucketImage.dispose();
        rainImage.dispose();
	}

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touch.set(screenX, screenY, 0);
        camera.unproject(touch);

//        Gdx.app.log("test", String.valueOf(touch.x) + "--" + String.valueOf(touch.y));

        bucket.setX(touch.x-32);

        if (touch.x < 400) {
            direction = right;
        }
        else {
            direction = left;

        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        touch.set(screenX, screenY, 0);
        camera.unproject(touch);

//        Gdx.app.log("test", String.valueOf(touch.x) + "--" + String.valueOf(touch.y));

        bucket.setX(touch.x-32);

        if (touch.x < 400) {
            direction = right;
        }
        else {
            direction = left;

        }

        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
