package minor.refactoring;

import java.util.Vector;

import minor.refactoring.calculator.CouplingCalculator;
import minor.refactoring.model.Project;
import minor.refactoring.model.ProjectClass;

public class Publicador
{

	private int classCount;
	private int packageCount;
	private int[][] dependencies;
	private int[] originalPackage;
	private int[] newPackage;
	private int[] originalClasses;
	private int[] newClasses;

	public void printProperties (Project project)throws Exception
	{

		this.classCount = project.getClassCount();
		this.packageCount = project.getPackageCount();
		System.out.println("File name - " + project.getName().substring(19) + "\t");
		System.out.println("No of classes - " + project.getClassCount() + "\t");
		System.out.println("No of packages - " + project.getPackageCount() + "\t");
		System.out.println("No of Dependancy - " + project.getDependencyCount() + "\t");

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
				int p=i;
				int q=classIndex;
				p=p+1;
				q=q+1;
				if (classIndex == -1)
					throw new Exception ("Class not registered in project: " + targetName);

				dependencies[i][classIndex]++;
			}
		}
		System.out.print("\n Original package - ");
		for (int i = 0; i < classCount; i++)
		{
			System.out.print(" "+originalPackage[i]);
		}
		System.out.print("\n Original class - ");
		for (int i = 0; i < packageCount; i++)
		{
			System.out.print(" "+originalClasses[i]);
		}
		//System.out.println("\nDependency Mattrix : ");
		
		
//		for (int i = 0; i < classCount; i++)
//		{
//			System.out.print(i + " : ");
//			for (int j= 0; j < classCount; j++)
//			{
//
//				System.out.print(" "+dependencies[i][j]);
//			}
//			System.out.println();
//		}
		System.out.println();

		CouplingCalculator calculator = new CouplingCalculator(project);
		double mq=calculator.calculateModularizationQuality();
		int c1=calculator.calculateCoupling();
		int c2=calculator.calculateCohesion();
		System.out.println("MQ VALU : " + mq);
		System.out.println("Coupling : " + c1);
		System.out.println("cohesion : " + c2);
	}

	public void printProperties (Vector<Project> projects)throws Exception
	{
		for(Project p: projects) {
			printProperties (p);
		
		}
		
	}
}