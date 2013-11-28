package seprhou.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public abstract class AbstractScreen implements Screen
{	

	Stage defaultStage = new Stage();
    Skin defaultSkin = new Skin(Assets.getDataFile("uiskin.json"));
	public AtcGame game;
	OrthographicCamera camera;
	SpriteBatch batch;
	public static final int SCREEN_WIDTH = 1680;
	public static final int SCREEN_HEIGHT = 1050;
	
    public AbstractScreen(AtcGame game)
    {
    	this.game = game;
    	
    	//Set up camera with chosen screen width and height
    	camera = new OrthographicCamera();
		camera.setToOrtho(true, SCREEN_WIDTH, SCREEN_HEIGHT);
		
		//Create a batch to enable drawing
		batch = new SpriteBatch();
		
		
		
    	Gdx.input.setInputProcessor(defaultStage);
    }
    
    @Override
    public void render(
        float delta )
    {
        // Clears the screen with the given RGB colour (black)
        Gdx.gl.glClearColor( 0f, 0f, 0f, 1f );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );
        
        // update the actors
        defaultStage.act( delta );
        
        // Begin and end drawing
        batch.begin();
        	// draw the actors
        	defaultStage.draw();
        batch.end();
        
        //Refresh camera
        camera.update();
		batch.setProjectionMatrix(camera.combined);
        
		// Return to main menu on ESCAPE press
    	if(Gdx.input.isKeyPressed(Keys.ESCAPE)) game.setScreen(new MenuScreen(game));
    }
	
    @Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void resize(int arg0, int arg1) {
		// Makes the stage the same size as the camera and changes the actors' sizes in proportion
		defaultStage.setViewport(SCREEN_WIDTH, SCREEN_HEIGHT, false);
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}
}
