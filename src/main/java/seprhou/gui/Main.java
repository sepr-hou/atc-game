package seprhou.gui;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/**
 * The initial class which starts libGDX using the lwjgl backend
 */
public class Main 
{
	public static void main(String[] args)
	{
		boolean windowed = false;

		// Check arguments
		if (args.length >= 1)
		{
			if (args[0].equals("-windowed") || args[0].equals("-w"))
			{
				windowed = true;
			}
			else
			{
				System.err.println("Pass -w or -windowed to run the game in a window");
				System.err.println();
				return;
			}
		}

		// Setup game config
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Airspace Frenzy 7";

		if (windowed)
		{
			cfg.width = 800;
			cfg.height = 500;
			cfg.fullscreen = false;
		}
		else
		{
			// Use whatever display mode we're currently on (hopefully it'll work)
			cfg.setFromDisplayMode(LwjglApplicationConfiguration.getDesktopDisplayMode());
			cfg.resizable = false;
		}

		// Run the game
		new LwjglApplication(new AtcGame(), cfg);
	}
}
