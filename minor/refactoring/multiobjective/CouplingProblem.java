package minor.refactoring.multiobjective;

import jmetal.base.Problem;
import jmetal.base.Solution;
import jmetal.base.Variable;
import jmetal.base.solutionType.IntSolutionType;
import jmetal.util.JMException;
import minor.refactoring.calculator.CouplingCalculator;
import minor.refactoring.model.Project;

public class CouplingProblem extends Problem
{
	private static final long serialVersionUID = -1L;
	private Project project;
	private CouplingCalculator calculator;
	
	public CouplingProblem(Project project) throws Exception
	{
		this.project = project;
		this.calculator = new CouplingCalculator(project);

		numberOfVariables_ = project.getClassCount();
		numberOfObjectives_ = 2;
		numberOfConstraints_ = 0;

		setVariableLimits();
		solutionType_ = new IntSolutionType(this);
		variableType_ = new Class[numberOfVariables_];
		length_ = new int[numberOfVariables_];
		length_[0] = numberOfVariables_;
	}

	private void setVariableLimits()
	{
		upperLimit_ = new double[numberOfVariables_];
		lowerLimit_ = new double[numberOfVariables_];

		for (int i = 0; i < numberOfVariables_; i++)
		{
			lowerLimit_[i] = 0;
			upperLimit_[i] = project.getPackageCount()-1;
		}
	}

	public CouplingCalculator getCalculator()
	{
		return calculator;
	}

	public void applySolutionToCalculator(Solution solution) throws JMException
	{
		Variable[] sequence = solution.getDecisionVariables();
		calculator.reset();

		for (int i = 0; i < sequence.length; i++)
		{
			int packageIndex = (int) sequence[i].getValue();
			calculator.moveClass(i, packageIndex);
		}
	}

	@Override
	public void evaluate(Solution solution) throws JMException
	{
		applySolutionToCalculator(solution);

		solution.setObjective(0, calculator.calculateDifference());
		solution.setObjective(1, -calculator.calculateModularizationQuality());

	}
}