package com.jiayuqicz.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class DropGameScreen implements Screen {

    private DropGame game;
	private Texture bucketImage = null;
	private Texture rainImage = null;
	private Sound dropSound = null;
	private Music rainMusic = null;


    private final int left = 0;
    private final int center = 1;
    private final int right = 2;

    private int direction = center;

    private OrthographicCamera camera = null;
    private SpriteBatch batch = null;
    private Rectangle bucket = null;

    private Vector3 touch = null;

    private Array<Rectangle> drops = null;
    private long lastDropTime;

    public DropGameScreen (DropGame game) {

        this.game = game;

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
        batch.end();

        if (Gdx.input.isTouched()) {
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);
            bucket.x = touch.x - 32;

            if (touch.x < 400) {
                direction = right;
            }
            else {
                direction = left;
            }

        }

        for (Rectangle drop : drops) {

            switch (direction) {
                case center :
                    center(drop);
                    break;
                case left :
                    left(drop);
                    break;
                case right:
                    right(drop);
                    break;
            }


            if (drop.y < 0) {
                drops.removeValue(drop, false);
            }

            if ((drop.x < 0)) {
                direction = right;
            }
            if (drop.x > 800-64) {
                direction = left;
            }

            if (drop.overlaps(bucket)) {
                drops.removeValue(drop, false);
                dropSound.play();
            }
        }

        if (TimeUtils.nanoTime() - lastDropTime > 1000000000) {
            spawnDrop();
            Gdx.app.log("test", String.valueOf(touch.x));
            Gdx.app.log("test", String.valueOf(direction));
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
        drop.setX(MathUtils.random(0,800-64));
        drop.width = 64;
        drop.height = 64;
        drops.add(drop);
        lastDropTime = TimeUtils.nanoTime();
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
}
