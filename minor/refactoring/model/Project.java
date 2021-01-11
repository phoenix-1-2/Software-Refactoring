package minor.refactoring.model;

import java.util.Vector;

public class Project
{
	private String name;
	private Vector<ProjectPackage> packages;
	private Vector<ProjectClass> classes;

	public Project(String name)
	{
		this.name = name;
		this.packages = new Vector<ProjectPackage>();
		this.classes = new Vector<ProjectClass>();
	}

	public String getName()
	{
		return name;
	}
	
	public int getPackageCount()
	{
		return packages.size();
	}

	public ProjectPackage getPackageIndex(int index)
	{
		return packages.elementAt(index);
	}

	public int getIndexForPackage(ProjectPackage _package)
	{
		return packages.indexOf(_package);
	}

	public ProjectPackage getPackageName(String name)
	{
		for (ProjectPackage p : packages)
			if (p.getName().compareToIgnoreCase(name) == 0)
				return p;
		
		return null;
	}

	public ProjectPackage addPackage(String name)
	{
		ProjectPackage aPackage = new ProjectPackage(name);
		packages.add(aPackage);
		return aPackage;
	}

	public Iterable<ProjectPackage> getPackages()
	{
		return packages;
	}

	public int getClassCount()
	{
		return classes.size();
	}

	public ProjectClass getClassIndex(int index)
	{
		return classes.elementAt(index);
	}

	public ProjectClass getClassName(String name)
	{
		for (ProjectClass c : classes)
			if (c.getName().compareToIgnoreCase(name) == 0)
				return c;
		
		return null;
	}

	public int getClassIndex(String name)
	{
		for (int i = 0; i < classes.size(); i++)
		{
			ProjectClass c = classes.elementAt(i);
			
			if (c.getName().compareToIgnoreCase(name) == 0)
				return i;
		}
		
		return -1;
	}

	public int getIndexForClass(ProjectClass _class)
	{
		return classes.indexOf(_class);
	}

	public void addClass(ProjectClass c)
	{
		classes.add(c);
	}

	public void removeClass(int index)
	{
		classes.remove(index);
	}

	public void addDependency(String sourceClass, String targetClass)
	{
		ProjectClass source = getClassName(sourceClass);
		
		if (source != null)
			source.addDependency(targetClass);
	}

	public int getDependencyCount()
	{
		int count = 0;
		
		for(ProjectClass c: classes)
			count += c.getDependencyCount();
		
		return count;
	}
}