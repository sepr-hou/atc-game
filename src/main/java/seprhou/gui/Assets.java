package seprhou.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

import java.awt.*;

public class Assets
{
	public static Texture BACKGROUND_TEXTURE = newLinearTexture(getDataFile("backgroundimage.png"));
	public static Texture WAYPOINT_TEXTURE = new Texture(getDataFile("waypoint.png"));
	public static Texture AIRCRAFT_TEXTURE = new Texture(getDataFile("airplane.png"));

	/**
	 * Creates a new texture using linear filtering from the given file handle
	 *
	 * @param handle input file handle
	 * @return a new texture using the file and has linear filtering
	 */
	private static Texture newLinearTexture(FileHandle handle)
	{
		Texture texture = new Texture(handle);
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
	public static FileHandle getDataFile(String name)
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
}
