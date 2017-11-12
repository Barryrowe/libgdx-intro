package com.roaringcatgames.cryptidcapers;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class CryptidCapersGame extends ApplicationAdapter implements InputProcessor {

    private final float NESSIE_BASE_SPEED = 60f;
    
    private SpriteBatch batch;
    private Vector2 nessiePosition = new Vector2(257f, 3f);
    private TextureRegion nessieStill;
    private TextureRegion nessieFrameToRender;
    private Animation<TextureRegion> nessieWalking;
    private Animation<TextureRegion> nessieRunning;
    private float animationTime = 0f;
    
	private boolean isGoingLeft = true;
    private boolean isNessiePaused = false;

	private void log(String message){
	    Gdx.app.log("APPLICATION_ADAPTER", message);
    }

	@Override
	public void create () {
        log("CREATE FIRED");
		batch = new SpriteBatch();

		nessieStill = new TextureRegion(new Texture("nessie/still.png"));
		Array<TextureRegion> walkingTextures = new Array<TextureRegion>();
		walkingTextures.addAll(
            new TextureRegion(new Texture("nessie/walking_1.png")),
            new TextureRegion(new Texture("nessie/walking_2.png")),
            new TextureRegion(new Texture("nessie/walking_3.png")),
            new TextureRegion(new Texture("nessie/walking_4.png")),
            new TextureRegion(new Texture("nessie/walking_5.png"))
        );
		nessieWalking = new Animation<TextureRegion>(
            1f/5f,
            new Array<TextureRegion>(walkingTextures),
            Animation.PlayMode.LOOP);

        Array<TextureRegion> runningTextures = new Array<TextureRegion>();
        runningTextures.addAll(
                new TextureRegion(new Texture("nessie/run_1.png")),
                new TextureRegion(new Texture("nessie/run_2.png")),
                new TextureRegion(new Texture("nessie/run_3.png")),
                new TextureRegion(new Texture("nessie/run_4.png"))
        );
        nessieRunning = new Animation<TextureRegion>(
                1f/8f,
                new Array<TextureRegion>(runningTextures),
                Animation.PlayMode.LOOP);

        Gdx.input.setInputProcessor(this);

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
	}

	private void update(float deltaTime){
	    //Naively prevent a frame from processing above 60fps
	    float throttledDelta = MathUtils.clamp(deltaTime, 0f, 1f/60f);

        if(!isNessiePaused) {

            //Poll For Input from User to see if they have reset the
            //  direction
            float movementSpeed = NESSIE_BASE_SPEED;
            boolean isRunning = false;
            if(Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
                isGoingLeft = true;
                movementSpeed *= 3f;
                isRunning = true;
            }else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
                isGoingLeft = false;
                movementSpeed *= 3f;
                isRunning = true;
            }

            //Update our position, toggling the direction if we go over
            float xAdjustment = throttledDelta * movementSpeed;
            float newX = nessiePosition.x;
            if (isGoingLeft) {
                newX -= xAdjustment;
            } else {
                newX += xAdjustment;
            }

            nessiePosition.x = MathUtils.clamp(newX, 0f, 400f);

            //If the player is pressing the running keys,
            //  we don't need to flip the direction as they are
            //  currently overriding it anyway.
            if(!isRunning) {
                if (nessiePosition.x == 0f && isGoingLeft) {
                    isGoingLeft = false;
                } else if (nessiePosition.x == 400f && !isGoingLeft) {
                    isGoingLeft = true;
                }
            }

            //Update Animation Frame
            animationTime += throttledDelta;
            nessieFrameToRender = isRunning ?
                    nessieRunning.getKeyFrame(animationTime) :
                    nessieWalking.getKeyFrame(animationTime);
            
        }else{
            nessieFrameToRender = nessieStill;
        }

        //Flip the texture if needed for current direction
        if ((isGoingLeft && nessieFrameToRender.isFlipX()) ||
                (!isGoingLeft) && !nessieFrameToRender.isFlipX()) {
            nessieFrameToRender.flip(true, false);
        }
    }

    private void draw() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        batch.draw(nessieFrameToRender, nessiePosition.x, nessiePosition.y);

        batch.end();
    }

    @Override
    public boolean keyDown(int keycode) {
	    log("Key Down: " + keycode);
	    if(keycode == Input.Keys.SPACE){
	        isNessiePaused = !isNessiePaused;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        log("Key Up: " + keycode);
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
	    log("Key Typed: " + character);
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
	    log("Touch Down from Pointer:" + pointer + " @ X: " + screenX + " Y: " + screenY);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        log("Touch Up from Pointer:" + pointer + " @ X: " + screenX + " Y: " + screenY);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        log("Touch Dragged from Pointer:" + pointer + " @ X: " + screenX + " Y: " + screenY);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
	    log("Mouse Moved  @ X: " + screenX + " Y: " + screenY);
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
	    log("Scrolled by: " + amount);
        return false;
    }
}
