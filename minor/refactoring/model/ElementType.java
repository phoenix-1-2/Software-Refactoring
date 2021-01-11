package minor.refactoring.model;

public enum ElementType
{
	CLASS ("class"),
	ENUM ("enum"),
	INTERFACE ("interface");

	private final String identifier;

	ElementType(String id)
	{
		this.identifier = id;
	}

	public String getIdentifier()
	{
		return identifier;
	}

	public static ElementType fromIdentifier(String id)
	{
		for (ElementType type: ElementType.values())
			if (type.getIdentifier().compareToIgnoreCase(id) == 0)
				return type;
		
		return null;
	}
}