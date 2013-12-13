package seprhou.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

import java.awt.*;

public class Assets
{
	public static Texture BACKGROUND_TEXTURE = new Texture(getDataFile("backgroundimage.png"));

	public static Texture AIRCRAFT_TEXTURE = new Texture(getDataFile("arrow.png"));

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
