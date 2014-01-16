package seprhou.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Class which stores references to game assets
 */
public final class Assets
{
	// Textures
	public static Texture BACKGROUND_TEXTURE = loadTextureFromFile("backgroundimage.png");
	public static Texture WAYPOINT_TEXTURE = loadTextureFromFile("waypoint.png");
	public static Texture AIRCRAFT_TEXTURE = loadTextureFromFile("airplane.png");
	public static Texture MENU_BACKGROUND_TEXTURE = loadTextureFromFile("menubackground.png");
	public static Texture CIRCLE_TEXTURE = loadTextureFromFile("circle.png");
	public static Texture GAMEOVER_TEXTURE = loadTextureFromFile("gameoverbackground.png");
	public static Texture AIRCRAFT_SELECTED = loadTextureFromFile("airplaneselected.png");

	// Game cursor
	public static Pixmap CURSOR_IMAGE = new Pixmap(getDataFile("cursor.png"));

	// Skin (used for menu gui)
	public static Skin SKIN = new Skin(getDataFile("uiskin.json"));
	public static BitmapFont FONT = SKIN.getFont("default-font");

	/**
	 * Loads a texture from a file
	 *
	 * @param name name of the file to access
	 * @return the new texture
	 */
	private static Texture loadTextureFromFile(String name)
	{
		Texture texture = new Texture(getDataFile(name));
		texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		return texture;
	}

	/**
	 * Returns a handle to a data file
	 *
	 * <p>
	 * This will choose the correct path depending on whether the project
	 * was built using Maven or not.
	 *
	 * @param name name of the file to access
	 */
	private static FileHandle getDataFile(String name)
	{
		FileHandle handle;

		// Construct path to file
		String fullName = "data/" + name;

		// Try main path first
		handle = Gdx.files.internal(fullName);
		if (handle.exists())
			return handle;

		// Try non maven path
		handle = Gdx.files.internal("src/main/resources/" + fullName);
		if (handle.exists())
			return handle;

		// Oh noes
		throw new RuntimeException("Data file not found: " + name);
	}

	private Assets()
	{
	}
}
