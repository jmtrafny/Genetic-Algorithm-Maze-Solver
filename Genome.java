package cs455;


import java.util.Random;

public class Genome {
	Random xd = new Random();

	public static final char[] genes = { 'N', 'E', 'S', 'W', 'N', 'E', 'S', 'W', 'N', 'E', 'S', 'W', 'X'};
	public static final double penalty_per_step = 1;
	public static final double penalty_multiplier = 3;
	public static final double dist_score_multiplier = 2;

	public char[] genome;// = new char[GENOME_LENGTH];
	public double fitness;
	public float mutationRate;

	/*
	 *  Constructor for Genome Object
	 */
	public Genome(int numOfGenes) {
		genome = new char[numOfGenes];
		for (int i = 0; i < numOfGenes; i++) {
			genome[i] = genes[xd.nextInt(genes.length)];
		}
		mutationRate = (float) 0.01;
		
	}

	// Function to set the fitness of a genome.
	public double evaluateFitness(double bonus) {
		
		int start_row = 1;
		int start_col = 1;
		int end_row = 12;
		int end_col = 12;

		MazePosition pos = new MazePosition(start_row, start_col);

		double penalty = 0;
		for (char gene : this.geneString().toCharArray()) {
			int n, e, s, w;
			n = pos.getNeighborValueNeg1IfEdge(Direction.N);
			e = pos.getNeighborValueNeg1IfEdge(Direction.E);
			s = pos.getNeighborValueNeg1IfEdge(Direction.S);
			w = pos.getNeighborValueNeg1IfEdge(Direction.W);
			
//			// Verbose debugging.
//			System.out.printf("pos:[%d,%d] val:%d next:%s n:%d  s:%d  e:%d  w:%d \n", 
//							  pos.getRowIdx(), pos.getColumnIdx(), pos.getValue(), gene, n, s, e, w);
			
			if (gene == 'N') {
				if (n == 0 || n == 2 || n == 3) {
					pos = pos.getNeighbor(Direction.N);
					penalty += penalty_per_step;
				} else {
					penalty += penalty_per_step * penalty_multiplier;
				}
			} else if (gene == 'E') {
				if (e == 0 || e == 2 || e == 3) {
					pos = pos.getNeighbor(Direction.E);
					penalty += penalty_per_step;
				} else {
					penalty += penalty_per_step * penalty_multiplier;
				}
			} else if (gene == 'S') {
				if (s == 0 || s == 2 || s == 3) {
					pos = pos.getNeighbor(Direction.S);
					penalty += penalty_per_step;
				} else {
					penalty += penalty_per_step * penalty_multiplier;
				}
			} else if (gene == 'W') {
				if (w == 0 || w == 2 || w == 3) {
					pos = pos.getNeighbor(Direction.W);
					penalty += penalty_per_step;
				} else {
					penalty += penalty_per_step * penalty_multiplier;
				}
			} 
		}

		double x1 = pos.getRowIdx();
		double y1 = pos.getColumnIdx();

		double distFromStart = Math.sqrt(Math.pow(start_row - x1, 2) + Math.pow(start_col - y1, 2));
		double distFromGoal = Math.sqrt(Math.pow(end_row - x1, 2) + Math.pow(end_col - y1, 2));

		double distScore = dist_score_multiplier * (distFromStart - distFromGoal);
		double mazeScore = distScore + bonus - penalty;
		if (mazeScore <= distScore)
			mazeScore = distScore;
		if (mazeScore <= 0)
			mazeScore = 1;

		fitness = mazeScore;
		return mazeScore;
	}

	// Single-Point Crossover function
	public Genome randomCrossover(Genome partner) {
		Genome child = new Genome(partner.genome.length);
		int crosspoint = xd.nextInt(genome.length);
		for (int i = 0; i < genome.length; i++) {
			if (i < crosspoint)
				child.genome[i] = genome[i];
			else
				child.genome[i] = partner.genome[i];
		}
		return child;
	}
	
	// Two-Point Crossover function
	public Genome twoPtCrossover(Genome partner) {
		Genome child = new Genome(partner.genome.length);
		
		int crosspoint1 = xd.nextInt(genome.length);
		int crosspoint2 = xd.nextInt(genome.length);
		
		// Ensure crosspoints are different...
		if (crosspoint1 == crosspoint2){
			if(crosspoint1 == 0){
				crosspoint2++;
			} else {
				crosspoint1--;
			}
		}
		// .. and crosspoint1 is lower than crosspoint2
		if (crosspoint2 < crosspoint1) {
			int temp = crosspoint1;
			crosspoint1 = crosspoint2;
			crosspoint2 = temp;
		}
		
		for (int i = 0; i < genome.length; i++) {
			if (i < crosspoint1 || i > crosspoint2)
				child.genome[i] = genome[i];
			else
				child.genome[i] = partner.genome[i];
		}
		return child;
	}

	// Mutate function
	public void mutate() {
		for (int i = 0; i < genome.length; i++) {
			if (xd.nextFloat() < mutationRate) {
				genome[i] = genes[xd.nextInt(genes.length)];
			}
		}
	}

	// To string... basically
	String geneString() {
		return new String(genome);
	}
	
}
