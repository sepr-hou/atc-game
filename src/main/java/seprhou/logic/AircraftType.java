package seprhou.logic;

public class AircraftType
{
	private String name;
	private float size, weight, ascentRate, minSpeed, maxSpeed, maxAltitude, maxAcceleration, maxTurnRate;
	private int crew;

	public AircraftType(String name, float size, float weight,
						float ascentRate, float minSpeed, float maxSpeed,
						float maxAltitude, float maxAcceleration, float maxTurnRate,
						int crew)
	{
		this.name = name;
		this.size = size;
		this.weight = weight;
		this.ascentRate = ascentRate;
		this.minSpeed = minSpeed;
		this.maxSpeed = maxSpeed;
		this.maxAltitude = maxAltitude;
		this.maxAcceleration = maxAcceleration;
		this.maxTurnRate = maxTurnRate;
		this.crew = crew;
	}

	public String getName()
	{
		return name;
	}

	public float getSize()
	{
		return size;
	}

	public float getWeight()
	{
		return weight;
	}

	public float getAscentRate()
	{
		return ascentRate;
	}

	public float getMinSpeed()
	{
		return minSpeed;
	}

	public float getMaxSpeed()
	{
		return maxSpeed;
	}

	public float getMaxAltitude()
	{
		return maxAltitude;
	}

	public float getMaxAcceleration()
	{
		return maxAcceleration;
	}

	public float getMaxTurnRate()
	{
		return maxTurnRate;
	}

	public int getCrew()
	{
		return crew;
	}
}
