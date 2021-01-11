package minor.refactoring.reader;

import java.util.Hashtable;
import java.util.Vector;

import minor.refactoring.instancegen.FlatPublisherReader;
import minor.refactoring.instancegen.FlatPublisherReaderException;
import minor.refactoring.model.DependencyType;
import minor.refactoring.model.ElementType;
import minor.refactoring.model.ElementVisibility;
import minor.refactoring.model.Project;
import minor.refactoring.model.ProjectClass;
import minor.refactoring.model.ProjectPackage;

public class CDAFlatReader extends FlatPublisherReader
{
	private Project project;
	private Vector<ProjectClass> classes;

	public CDAFlatReader(String projectName)
	{
		this.project = new Project(projectName);
		this.classes = new Vector<ProjectClass>();
	}

	public Project getProject()
	{
		return project;
	}

	private ProjectClass getClassName(String name)
	{
		for (ProjectClass c : classes)
			if (c.getName().compareToIgnoreCase(name) == 0)
				return c;
		
		return null;
	}

	@Override
	protected void registerEntity(String type, String name, Hashtable<String, String> attributes) throws FlatPublisherReaderException
	{
		if (type.compareToIgnoreCase("class") == 0)
		{
			ProjectClass _class = new ProjectClass(name, ElementType.CLASS, ElementVisibility.PUBLIC, false);
			classes.add(_class);
			return;
		}
		
		if (type.compareToIgnoreCase("package") == 0)
		{
			project.addPackage(name);
			return;
		}
		
		generateException("unknown entity type '" + type + "'");
	}

	@Override
	protected void registerRelationship(String type, String source, String target, Hashtable<String, String> attributes) throws FlatPublisherReaderException
	{
		if (type.compareToIgnoreCase("depends-to") == 0)
		{
			ProjectClass sourceClass = getClassName(source);
			
			if (sourceClass == null)
				generateException("unknown source class '" + source + "'");

			ProjectClass targetClass = getClassName(target);
			
			if (targetClass == null)
				generateException("unknown target class '" + target + "'");
			
			sourceClass.addDependency(target, DependencyType.USES);
			return;
		}
		
		if (type.compareToIgnoreCase("pertains-to") == 0)
		{
			ProjectClass sourceClass = getClassName(source);
			
			if (sourceClass == null)
				generateException("unknown source class '" + source + "'");

			ProjectPackage _package = project.getPackageName(target);
			
			if (_package == null)
				generateException("unknown target package '" + target + "'");
			
			sourceClass.setPackage(_package);
			project.addClass(sourceClass);
			classes.remove(sourceClass);
			return;
		}
		
		generateException("unknown entity type '" + type + "'");
	}

	@Override
	public void cleanUp() throws FlatPublisherReaderException
	{
		if (classes.size() > 0)
			generateException(classes.size() + " classes are not referenced by packages");
	}
}