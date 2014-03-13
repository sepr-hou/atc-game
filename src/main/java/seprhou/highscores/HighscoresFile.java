package seprhou.highscores;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.minlog.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Class containing information contained in a highscores file
 */
public class HighscoresFile
{
	private static final String DEFAULT_FILE = File.separator + ".atc-game-scores";

	private final String filename;
	private final Kryo kryo;
	private List<HighscoreEntry> scores = new ArrayList<>();

	private HighscoresFile(String filename)
	{
		this.filename = filename;

		// Initialize kryo
		kryo = new Kryo(null);
		kryo.setAutoReset(true);
		kryo.setRegistrationRequired(true);
		kryo.setReferences(false);

		kryo.register(HighscoreEntry.class);
		kryo.register(HighscoreEntry[].class);
	}

	/**
	 * Gets a readonly list containing the scores
	 */
	public List<HighscoreEntry> getScores()
	{
		return Collections.unmodifiableList(scores);
	}

	/**
	 * Adds a new score and writes it to disk
	 * @param entry new entry
	 * @return true if it was written successfully
	 */
	public boolean addScore(HighscoreEntry entry)
	{
		if (entry == null)
			throw new IllegalArgumentException("entry cannot be null");

		scores.add(entry);
		Collections.sort(scores);

		return writeFile();
	}

	/**
	 * Reloads the highscores from the highscore file
	 * @return true if scores were reloaded successfully
	 */
	public boolean reloadFile()
	{
		// Wipe existing scores
		scores.clear();

		try
		{
			try (Input in = new Input(new FileInputStream(filename)))
			{
				// Read list of objects via kryo
				HighscoreEntry[] entries = kryo.readObject(in, HighscoreEntry[].class);

				// Sort list and save into entries arraylist
				Arrays.sort(entries);
				scores.addAll(Arrays.asList(entries));

				Log.debug("Read highscores file");
				return true;
			}
		}
		catch (FileNotFoundException e)
		{
			Log.info("No highscores file found - creating empty file");
			return true;
		}
		catch (KryoException e)
		{
			Log.error("Error reading highscores file", e);
		}

		return false;
	}

	/**
	 * Writes the scores to disk
	 * @return true if scores were written successfully
	 */
	private boolean writeFile()
	{
		try
		{
			try (Output out = new Output(new FileOutputStream(filename)))
			{
				// Write list of scores via kryo
				kryo.writeObject(out, scores.toArray(new HighscoreEntry[scores.size()]));

				Log.debug("Written highscores file");
				return true;
			}
		}
		catch (FileNotFoundException | KryoException e)
		{
			Log.error("Error writing highscores file", e);
		}

		return false;
	}

	/**
	 * Returns the default highscores filename
	 */
	public static String getDefaultFilename()
	{
		// Home directory followed by DEFAULT_FILE
		return System.getProperty("user.home") + DEFAULT_FILE;
	}

	/**
	 * Reads a highscores file from the default highscores file
	 */
	public static HighscoresFile readFromDefaultFile()
	{
		return readFromFile(getDefaultFilename());
	}

	/**
	 * Reads a highscores file
	 * @param file filename
	 */
	public static HighscoresFile readFromFile(String file)
	{
		// Create + reload highscores object
		HighscoresFile highscoresFile = new HighscoresFile(file);
		highscoresFile.reloadFile();
		return highscoresFile;
	}

	/**
	 * Creates a new (empty) highscores file which saves to the given file
	 * @param file filename
	 */
	public static HighscoresFile createNewFile(String file)
	{
		return new HighscoresFile(file);
	}
}
