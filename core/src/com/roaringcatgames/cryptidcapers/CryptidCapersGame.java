package com.roaringcatgames.cryptidcapers;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CryptidCapersGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	                                           
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
        Gdx.app.log("APPLICATION_LISTENER", "CREATE FIRED");
	}

	@Override
	public void resume() {
		super.resume();
		Gdx.app.log("APPLICATION_LISTENER", "RESUME FIRED");
	}

	@Override
	public void pause() {
		super.pause();
		Gdx.app.log("APPLICATION_LISTENER", "PAUSE FIRED");
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
	}
	
	@Override
	public void dispose () {
        Gdx.app.log("APPLICATION_LISTENER", "DISPOSE FIRED");
		batch.dispose();
		img.dispose();
	}
}
