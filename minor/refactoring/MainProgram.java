package minor.refactoring;

import java.util.Vector;

import javax.management.modelmbean.XMLParseException;

import minor.refactoring.instancegen.FlatPublisherReaderException;
import minor.refactoring.model.Project;
import minor.refactoring.multiobjective.ClusteringProblemBuilder;
import minor.refactoring.multiobjective.CouplingProblem;
import minor.refactoring.multiobjective.Experiment;
import minor.refactoring.reader.CDAFlatReader;
import minor.refactoring.reader.CDAReader;

@SuppressWarnings("unused")
public class MainProgram
{
	private static String[] instanceFilenames6C = {
		"data\\MQ Evaluation\\Cohesion 10C 3P.txt", /*"data\\MQ Evaluation\\Cohesion 50C 7P.txt"*/
	};
	
	private static String[] instanceFilenames50C =
	{
//		"data\\MQ Evaluation\\javacc5.0.txt",
		//"data\\MQ Evaluation\\TEST1.txt",
		"data\\MQ Evaluation\\Cohesion 50C 7P.txt",
		//"data\\MQ Evaluation\\Cohesion 50C 9P.txt",
		//"data\\MQ Evaluation\\Cohesion 50C 10P.txt",
		//"data\\MQ Evaluation\\Cohesion 50C 11P.txt",
		//"data\\MQ Evaluation\\Cohesion 50C 13P.txt"
	};

	private static String[] instanceFilenames100C =
	{
		"data\\MQ Evaluation\\Cohesion 100C 12P.txt",
		"data\\MQ Evaluation\\Cohesion 100C 14P.txt",
		"data\\MQ Evaluation\\Cohesion 100C 16P.txt",
		"data\\MQ Evaluation\\Cohesion 100C 18P.txt",
		"data\\MQ Evaluation\\Cohesion 100C 20P.txt"
	};

	
	public Project readFromXML (String filename) throws XMLParseException
	{
		CDAReader reader = new CDAReader();
		return reader.execute(filename);
	}

	public Project readFromFlat (String filename) throws FlatPublisherReaderException
	{
		CDAFlatReader reader = new CDAFlatReader(filename);
		reader.execute(filename);
		return reader.getProject();
	}

	private Vector<Project> readInstances(String[] filenames) throws FlatPublisherReaderException, XMLParseException
	{
		Vector<Project> instances = new Vector<Project>();

		for (String filename : filenames)
			if (filename.endsWith("txt"))
				instances.add (readFromFlat(filename));
			else
				instances.add (readFromXML(filename));

		return instances;
	}

	public static final void main(String[] args) throws Exception
	{
		MainProgram mp = new MainProgram();
		Vector<Project> instances = new Vector<Project>();
		
		// Reading data set for refactoring
		instances.addAll(mp.readInstances(instanceFilenames50C));
		
		new Publicador().printProperties(instances);
		Experiment<CouplingProblem, Project> experiment = new Experiment<CouplingProblem, Project>();
		experiment.runCycles("jjavac.txt", new ClusteringProblemBuilder(), instances, 20);
	}
}