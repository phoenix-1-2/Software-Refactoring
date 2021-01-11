package minor.refactoring.model;


public class Dependency
{
	private String elementName;
	private DependencyType type;

	public Dependency(String name, DependencyType type)
	{
		this.elementName = name;
		this.type = type;
	}
	
	public String getElementName()
	{
		return elementName;
	}
	
	public DependencyType getType()
	{
		return type;
	}
}