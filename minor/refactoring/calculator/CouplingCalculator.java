package minor.refactoring.calculator;

import java.io.IOException;

import java.io.Writer;
import java.text.DecimalFormat;

import minor.refactoring.model.Project;
import minor.refactoring.model.ProjectClass;
import minor.refactoring.model.ProjectPackage;

public class CouplingCalculator
{
	private Project project;
	private int classCount;
	private int packageCount;
	private int[][] dependencies;
	private int[] originalPackage;
	private int[] newPackage;
	private int[] originalClasses;
	private int[] newClasses;

	
	public CouplingCalculator(Project project) throws Exception
	{
		this.project = project;
		this.classCount = project.getClassCount();
		this.packageCount = project.getPackageCount();
		prepareClasses(project);
	}

	
	//Creates Dependency matrix
	public void prepareClasses(Project project) throws Exception
	{
		dependencies = new int[classCount][classCount];
		
		originalPackage = new int[classCount];
		newPackage = new int[classCount];
		
		originalClasses = new int[packageCount];
		newClasses = new int[packageCount];

		for (int i = 0; i < classCount; i++)
		{
			ProjectClass _class = project.getClassIndex(i);
			int sourcePackageIndex = project.getIndexForPackage(_class.getPackage());
			
			originalPackage[i] = newPackage[i] = sourcePackageIndex;
			originalClasses[sourcePackageIndex]++;
			newClasses[sourcePackageIndex]++; 

			for (int j = 0; j < _class.getDependencyCount(); j++)
			{
				String targetName = _class.getDependencyIndex(j).getElementName();
				int classIndex = project.getClassIndex(targetName);
				
				if (classIndex == -1)
					throw new Exception ("Class not registered in project: " + targetName);
				
				dependencies[i][classIndex]++;
			}
		}
	}

	
	public void reset()
	{
		for (int i = 0; i < classCount; i++)
			newPackage[i] = originalPackage[i];

		for (int i = 0; i < packageCount; i++)
			newClasses[i] = originalClasses[i];
	}

	
	public void moveClass(int classIndex, int packageIndex)
	{
		int actualPackage = newPackage[classIndex];
		
		if (actualPackage != packageIndex)
		{
			newClasses[actualPackage]--;
			newPackage[classIndex] = packageIndex;
			newClasses[packageIndex]++;
		}
	}

	
	public int getClassCount(int packageIndex)
	{
		return newClasses[packageIndex];
	}

	
	public String getClasses(int packageIndex)
	{
		String s = "";

		for (int i = 0; i < classCount; i++)
			if (newPackage[i] == packageIndex)
				s += project.getClassIndex(i).getName() + " ";

		return s;
	}

	
	public int getMoveCount(int packageIndex)
	{
		int count = 0;
		
		for (int i = 0; i < classCount; i++)
			if (originalPackage[i] == packageIndex && newPackage[i] != packageIndex)
				count++;

		return count;
	}

	
	public int getMoveCount()
	{
		int count = 0;

		for (int i = 0; i < classCount; i++)
			if (originalPackage[i] != newPackage[i])
				count++;

		return count;
	}

	
	private int getMinimumClassCount()
	{
		int min = Integer.MAX_VALUE;

		for (int i = 0; i < packageCount; i++)
		{
			int count = newClasses[i];

			if (count < min)
				min = count;
		}

		return min;
	}

	
	private int getMaximumClassCount()
	{
		int max = Integer.MIN_VALUE;

		for (int i = 0; i < packageCount; i++)
		{
			int count = newClasses[i];

			if (count > max)
				max = count;
		}

		return max;
	}

	int countOutboundEdges(int packageIndex)
	{
		int edges = 0;

		for (int i = 0; i < classCount; i++)
		{
			int currentPackage = newPackage[i];
			
			if (currentPackage != packageIndex)
				continue;
			
			for (int j = 0; j < classCount; j++)
				if (dependencies[i][j] > 0 && newPackage[j] != currentPackage)
					edges++;
		}

		return edges;
	}

	
	private int countInboundEdges(int packageIndex)
	{
		int edges = 0;

		for (int i = 0; i < classCount; i++)
		{
			int currentPackage = newPackage[i];
			
			if (currentPackage == packageIndex)
				continue;
			
			for (int j = 0; j < classCount; j++)
				if (dependencies[i][j] > 0 && newPackage[j] == packageIndex)
					edges++;
		}

		return edges;
	}

	
	private int countIntraEdges(int packageIndex)
	{
		int cohesion = 0;

		for (int i = 0; i < classCount; i++)
		{
			int currentPackage = newPackage[i];
			
			if (currentPackage != packageIndex)
				continue;
			
			for (int j = 0; j < classCount; j++)
				if (dependencies[i][j] > 0 && newPackage[j] == currentPackage)
					cohesion++;
		}

		return cohesion;
	}

	
	public int calculateDifference()
	{
		int min = getMinimumClassCount();
		int max = getMaximumClassCount();
		return max - min;
	}
	
	public int calculateCoupling()
	{
		int coupling = 0;
		
		// Traverse over dependency matrix to find if the class is not in the same package but has dependency = 1
		for (int i = 0; i < classCount; i++)
		{
			int currentPackage = newPackage[i];
			
			for (int j = 0; j < classCount; j++)
				if (dependencies[i][j] > 0 && newPackage[j] != currentPackage)
					coupling += 2;
		}

		return coupling;
	}

	
	public int calculateCohesion()
	{
		int cohesion = 0;
		
		// Traverse over dependency matrix to find if the class is in the same package but has dependency = 1
		for (int i = 0; i < classCount; i++)
		{
			int currentPackage = newPackage[i];
			
			for (int j = 0; j < classCount; j++)
				if (dependencies[i][j] > 0 && newPackage[j] == currentPackage)
					cohesion++;
		}

		return cohesion;
	}

	
	public double calculateModularizationQuality()
	{
		double mq = 0.0;

		for (int i = 0; i < packageCount; i++)
		{
			int interEdges = countInboundEdges(i) + countOutboundEdges(i);
			int intraEdges = countIntraEdges(i);
			
			if (intraEdges != 0 && interEdges != 0)
			{
				double mf = intraEdges / (intraEdges + 0.5 * interEdges);
				mq += mf;
			}
		}

		return mq;
	}

}