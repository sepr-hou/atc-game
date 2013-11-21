package seprhou.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class MenuScreen extends AbstractScreen
{
	private static final float BUTTON_WIDTH = 300f;
    private static final float BUTTON_HEIGHT = 60f;
    private static final float BUTTON_SPACING = 10f;
    
    Stage defaultStage = new Stage();
    
    @Override
    public void render(
        float delta )
    {
    	super.render(delta);
        // (1) process the game logic

        // update the actors
        defaultStage.act( delta );

        // draw the actors
        defaultStage.draw();

    }

    
    @Override
    public void resize(
        int width,
        int height )
    {
        super.resize(width, height );
        final float buttonX = ( 800 - BUTTON_WIDTH ) / 2;
        float currentY = 280f;
        defaultStage.clear();
        
        Skin defaultSkin = new Skin(Gdx.files.internal("/usr/userfs/j/jk767/cs/atc-game/src/main/resources/uiskin.json"));
       
        
        // label "Game Title"
        Label welcomeLabel = new Label( "Air traffic controller game", defaultSkin );
        welcomeLabel.setX(( ( 800 - welcomeLabel.getWidth() ) / 2 ));
        welcomeLabel.setY(( currentY + 100 ));
        defaultStage.addActor( welcomeLabel );
 
        // button "Start Game"
        TextButton startGameButton = new TextButton( "Start game", defaultSkin);
        startGameButton.setX(buttonX);
        startGameButton.setY(currentY);
        startGameButton.setWidth(BUTTON_WIDTH);
        startGameButton.setHeight(BUTTON_HEIGHT);
        defaultStage.addActor( startGameButton );
 
        // button "Options"
        TextButton optionsButton = new TextButton( "Options", defaultSkin);
        optionsButton.setX(buttonX);
        optionsButton.setY(( currentY -= BUTTON_HEIGHT + BUTTON_SPACING ));
        optionsButton.setWidth(BUTTON_WIDTH);
        optionsButton.setHeight(BUTTON_HEIGHT);
        defaultStage.addActor( optionsButton );
 
        // button "High Scores"
        TextButton hallOfFameButton = new TextButton( "Hall of Fame", defaultSkin);
        hallOfFameButton.setX(buttonX);
        hallOfFameButton.setY(( currentY -= BUTTON_HEIGHT + BUTTON_SPACING ));
        hallOfFameButton.setWidth(BUTTON_WIDTH);
        hallOfFameButton.setHeight(BUTTON_HEIGHT);
        defaultStage.addActor( hallOfFameButton );
    }
}