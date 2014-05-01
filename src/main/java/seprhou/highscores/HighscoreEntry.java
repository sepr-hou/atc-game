package seprhou.highscores;

/**
 * A single entry in the highscores list
 */
public final class HighscoreEntry implements Comparable<HighscoreEntry>
{
	private final long date;
	private final int score;

	/**
	 * Private constructor used by kryo when creating instances of this object
	 */
	@SuppressWarnings("unused")
	private HighscoreEntry()
	{
		date = 0;
		score = 0;
	}

	/**
	 * Creates a new highscore entry using the current date
	 *
	 * @param score the final score
	 */
	public HighscoreEntry(int score)
	{
		this(System.currentTimeMillis(), score);
	}

	/**
	 * Creates a new highscore entry
	 *
	 * @param date date this entry occurs on
	 * @param score the final score
	 */
	public HighscoreEntry(long date, int score)
	{
		this.date = date;
		this.score = score;
	}

	/**
	 * Returns the date this entry was created (milliseconds since Jan 1 1970)
	 */
	public long getDate()
	{
		return date;
	}

	/**
	 * Returns the score of this entry
	 */
	public int getScore()
	{
		return score;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		HighscoreEntry that = (HighscoreEntry) o;

		if (date != that.date) return false;
		if (score != that.score) return false;

		return true;
	}

	@Override
	public int hashCode()
	{
		int result = (int) (date ^ (date >>> 32));
		result = 31 * result + score;
		return result;
	}

	@Override
	public int compareTo(HighscoreEntry o)
	{
		return Integer.compare(score, o.score);
	}
}
