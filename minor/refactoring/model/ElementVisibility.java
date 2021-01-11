package minor.refactoring.model;

public enum ElementVisibility
{
	PUBLIC ("public"),
	DEFAULT ("default");

	private final String identifier;

	ElementVisibility(String id)
	{
		this.identifier = id;
	}

	public String getIdentifier()
	{
		return identifier;
	}

	public static ElementVisibility fromIdentifier(String id)
	{
		for (ElementVisibility type: ElementVisibility.values())
			if (type.getIdentifier().compareToIgnoreCase(id) == 0)
				return type;
		
		return null;
	}
}