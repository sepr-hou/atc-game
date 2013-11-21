package seprhou.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public abstract class AbstractScreen implements Screen
{
	Stage defaultStage = new Stage();
    Skin defaultSkin = new Skin(Gdx.files.internal("src/main/resources/uiskin.json"));
	
	MainWindow mainWindow;
    
    public AbstractScreen(final MainWindow mainWindow)
    {
    	this.mainWindow = mainWindow;
    	defaultStage.addListener(new InputListener()
    	{
    		@Override
    		public boolean keyDown(InputEvent event, int keycode){
    			if (keycode == Keys.ESCAPE){
    				mainWindow.setScreen(new MenuScreen(mainWindow));
    				return true;
    			}
    			return false;
    		}
    	});
    	Gdx.input.setInputProcessor(defaultStage);
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
    public void render(
        float delta )
    {
        // the following code clears the screen with the given RGB color (black)
        Gdx.gl.glClearColor( 0f, 0f, 0f, 1f );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );
        
        // update the actors
        defaultStage.act( delta );

        // draw the actors
        defaultStage.draw();
    }
	
	@Override
	public void resize(int arg0, int arg1) {
		// TODO Auto-generated method stub
		
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
