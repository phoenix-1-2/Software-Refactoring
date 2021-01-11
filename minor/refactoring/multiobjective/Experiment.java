package minor.refactoring.multiobjective;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.Vector;

import jmetal.base.Algorithm;
import jmetal.base.Solution;
import jmetal.base.SolutionSet;
import jmetal.base.Variable;
import jmetal.util.JMException;
import minor.refactoring.calculator.CouplingCalculator;
import minor.refactoring.model.Project;


public class Experiment<ProblemClass, InstanceClass>
{
	
	public Experiment()
	{
	}
		
	private boolean identicalObjectives(Solution solution1, Solution solution2, double tolerance)
	{
		if (solution1.numberOfObjectives() != solution2.numberOfObjectives())
			return false;
		
		for (int i = 0; i < solution1.numberOfObjectives(); i++)
		{
			double objective1 = solution1.getObjective(i);
			double objective2 = solution2.getObjective(i);
			
			if (Math.abs(objective1 - objective2) > tolerance)
				return false;
		}
		
		return true;
	}
		
	private boolean checkDuplicateObjectives(Vector<Solution> uniqueSolutions, Solution next, double tolerance)
	{
		for (int i = 0; i < uniqueSolutions.size(); i++)
		{
			Solution current = uniqueSolutions.get(i);
			
			if (identicalObjectives(current, next, tolerance))
				return true;
		}
		
		return false;
	}

	private Vector<Solution> getUniqueObjectives(SolutionSet result, double tolerance)
	{
		Vector<Solution> unique = new Vector<Solution>();

		for (int i = 0; i < result.size(); i++)
		{
			Solution solution = result.get(i);
			
			if (!checkDuplicateObjectives(unique, solution, tolerance))
				unique.add(solution);
		}
		
		return unique;
	}

	private DominationStatus checkDomination (Solution current, Solution newSolution)
	{
		boolean currentAlwaysDominated = true;
		boolean newAlwaysDominated = true;
		
		for (int i = 0; i < current.numberOfObjectives(); i++)
		{
			double currentObjective = current.getObjective(i);
			double newObjective = newSolution.getObjective(i); 
		
			if (currentObjective < newObjective)
				newAlwaysDominated = false;

			if (currentObjective > newObjective)
				currentAlwaysDominated = false;
				
			if (!currentAlwaysDominated && !newAlwaysDominated)
				return DominationStatus.NON_DOMINATING;
		}
				
		if (currentAlwaysDominated)
			return DominationStatus.DOMINATED;
				
		return DominationStatus.DOMINATOR;
	} 

	public void mergeObjectives(Vector<Solution> frontier, Vector<Solution> unique)
	{
		for (int i = 0; i < unique.size(); i++)
		{
			Solution newSolution = unique.get(i);
			DominationStatus newStatus = DominationStatus.NON_DOMINATING;
			
			for (int j = frontier.size()-1; j >= 0; j--)
			{
				Solution current = frontier.get(j);
				DominationStatus status = checkDomination(current, newSolution);
				
				if (status == DominationStatus.DOMINATOR)
					frontier.remove(j);
				
				if (status == DominationStatus.DOMINATED)
					newStatus = DominationStatus.DOMINATED;
			}
				
			if (newStatus != DominationStatus.DOMINATED)
				frontier.add(newSolution);
		}
	}
	
	private double applySolutionToCalculator(Project project, Solution solution) throws Exception
	{
		CouplingCalculator calculator = new CouplingCalculator(project);
		Variable[] sequence = solution.getDecisionVariables();
		calculator.reset();

		for (int i = 0; i < sequence.length; i++)
		{
			int packageIndex = (int) sequence[i].getValue();
			calculator.moveClass(i, packageIndex);
		}
		
		return calculator.calculateModularizationQuality();
	}

	private void printParetoFrontier(Writer bw, InstanceClass instance, Vector<Solution> uniques) throws Exception
	{
		DecimalFormat dc = new DecimalFormat("0.####");
		for (int j = 0; j < uniques.size(); j++)
		{
			Solution solution = uniques.get(j);
			System.out.print(j + " :");
			Variable[] packages = solution.getDecisionVariables();
			for(int i=0; i<packages.length; i++)
			{
				
				System.out.print(" " + packages[i]);

			}
			double mq = applySolutionToCalculator((Project)instance, solution);
			System.out.print("\t MQ = " + dc.format(mq) + "\n\n");

		}
	}

	public void runCycles(Writer bw, ProblemBuilder<ProblemClass, InstanceClass> builder, InstanceClass instance, int cycles) throws Exception
	{
		Vector<Solution> bestFrontier = new Vector<Solution>();
		Algorithm algorithm = builder.createAlgorithm(instance);
		
		for (int i = 0; i < cycles; i++)
		{
			// executes the NSGAII algorithm
			long initTime = System.currentTimeMillis();
			SolutionSet result = algorithm.execute();
			long estimatedTime = System.currentTimeMillis() - initTime;
			
			// find unique point for Pareto-front in the current solution
			Vector<Solution> uniques = getUniqueObjectives(result, 0.001);
			
			// merges the unique point with the best frontier			
			mergeObjectives(bestFrontier, uniques);
			
			// prints summary information 
			System.out.println("Cycle #" + i + " (" + estimatedTime + " ms; " + bestFrontier.size() + " best solutions)");
			printParetoFrontier(bw, instance, uniques);
		}

		System.out.println("Final Pareto Frontier : ");
		printParetoFrontier(bw, instance, bestFrontier);
	}

	public void runCycles(String path, ProblemBuilder<ProblemClass, InstanceClass> builder, Vector<InstanceClass> instances, int cycles) throws Exception
	{
		FileOutputStream fos = new FileOutputStream(path);
		OutputStreamWriter bw = new OutputStreamWriter(fos);

		for (int i = 0; i < instances.size(); i++)
		{
			System.out.println("=============================================================");
			System.out.println("File Number - " + (i+1));
			System.out.println("=============================================================");
			runCycles(bw, builder, instances.get(i), cycles);
		}

		fos.close();
		bw.close();
	}
}

enum DominationStatus
{
	NON_DOMINATING,
	DOMINATED,
	DOMINATOR;
}