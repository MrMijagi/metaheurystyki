package main;

public class ParametersTuning {
	public static void main(String[] args) {
		switch(args[3]) {
		case "ga":
			Main.getGeneticAlgorithmSolverStatistics(args[0], args[1], args[2], 10);
			break;
		case "ts":
			Main.getTabuSearchSolverStatistics(args[0], args[1], args[2], 10);
			break;
		case "sa":
			Main.getSimulatedAnnealingStatistics(args[0], args[1], args[2], 10);
			break;
		case "h1":
			Main.getHybridOneSolverStatistics(args[0], args[1], args[2], 10);
			break;
		case "h2":
			Main.getHybridTwoSolverStatistics(args[0], args[1], args[2], 10);
			break;
		}
	}
}
