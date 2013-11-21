package seprhou.gui;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class MainWindowLauncher 
{	
	public static void main(String[] args){
		
		//Define window size
		int width = 800, height = 480;
		
		//Define window title
		String title = "Air traffic controller game";
		
		//Create game
		new LwjglApplication(new MainWindow(), title, width, height, true);

	}
	
}
