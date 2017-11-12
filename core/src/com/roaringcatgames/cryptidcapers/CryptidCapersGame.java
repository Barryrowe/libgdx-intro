package com.roaringcatgames.cryptidcapers;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class CryptidCapersGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Sprite nessie;

	private final float NESSIE_SPEED = 20f;

	private boolean isGoingLeft = true;

	private void log(String message){
	    Gdx.app.log("APPLICATION_ADAPTER", message);
    }

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		nessie = new Sprite(
            new TextureRegion(
                new Texture("nessie/still.png")
            )
        );
		nessie.setPosition(257, 257);
        log("CREATE FIRED");
	}

	@Override
	public void resume() {
		super.resume();
		log("RESUME FIRED");
	}

	@Override
	public void pause() {
		super.pause();
		log("PAUSE FIRED");
	}

	@Override
	public void render () {

	    update(Gdx.graphics.getDeltaTime());
	    draw();
	}
	
	@Override
	public void dispose () {
        log("DISPOSE FIRED");
		batch.dispose();
		img.dispose();
	}

	private void update(float deltaTime){
	    //Naively prevent a frame from processing above 60fps
	    float throttledDelta = MathUtils.clamp(deltaTime, 0f, 1f/60f);

	    float xAdjustment = throttledDelta * NESSIE_SPEED;

	    if(isGoingLeft){
	        nessie.setX(nessie.getX() - xAdjustment);
	        if(nessie.getX() <= 0f){
	            isGoingLeft = false;
            }
        }else{
	        nessie.setX(nessie.getX() + xAdjustment);
	        if(nessie.getX() >= 600f){
	            isGoingLeft = true;
            }
        }
    }

    private void draw(){
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        batch.draw(img, 0, 0);

        nessie.draw(batch);

        batch.end();
    }
}
