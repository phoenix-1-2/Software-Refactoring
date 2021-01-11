package minor.refactoring.model;

import java.util.Vector;

public class ProjectClass
{
	private String name;
	private ProjectPackage _package;
	private ElementType type;
	private ElementVisibility visibility;
	private boolean isAbstract;
	private Vector<Dependency> dependencies;

	public ProjectClass(String name, ElementType type, ElementVisibility visibility, boolean isAbstract)
	{
		this.name = name;
		this._package = null;
		this.type = type;
		this.visibility = visibility;
		this.isAbstract = isAbstract;
		this.dependencies = new Vector<Dependency>();
	}
	
	public ProjectClass(String name)
	{
		this(name, ElementType.CLASS, ElementVisibility.PUBLIC, false);
	}

	public ProjectClass(String name, ProjectPackage _package)
	{
		this(name);
		setPackage(_package);
	}

	public String getName()
	{
		return name;
	}

	public ProjectPackage getPackage()
	{
		return _package;
	}

	public void setPackage(ProjectPackage _package)
	{
		this._package = _package;
	}

	public ElementType getType()
	{
		return type;
	}

	public ElementVisibility getVisibility()
	{
		return visibility;
	}

	public boolean isAbstract()
	{
		return isAbstract;
	}

	public int getDependencyCount()
	{
		return dependencies.size();
	}

	public Dependency getDependencyIndex(int index)
	{
		return dependencies.elementAt(index);
	}

	public void addDependency(String elementName, DependencyType type)
	{
		Dependency dependency = new Dependency(elementName, type);
		dependencies.add(dependency);
	}

	public void addDependency(String elementName)
	{
		Dependency dependency = new Dependency(elementName, DependencyType.USES);
		dependencies.add(dependency);
	}

	public Iterable<Dependency> getDependencies()
	{
		return dependencies;
	}
}