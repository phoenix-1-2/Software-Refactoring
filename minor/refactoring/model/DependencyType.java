package minor.refactoring.model;

public enum DependencyType
{
	USES ("uses"),
	IMPLEMENTS ("implements"),
	EXTENDS ("extends");

	private final String identifier;

	DependencyType(String id)
	{
		this.identifier = id;
	}

	public String getIdentifier()
	{
		return identifier;
	}
	
	public static DependencyType fromIdentifier(String id)
	{
		for (DependencyType type: DependencyType.values())
			if (type.getIdentifier().compareToIgnoreCase(id) == 0)
				return type;
		
		return null;
	}
}