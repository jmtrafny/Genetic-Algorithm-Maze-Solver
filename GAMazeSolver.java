package cs455;

//import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GAMazeSolver {

	public static final int[][] MAZE_GRID = new int[][] { 
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
			{ 1, 2, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1 }, 
			{ 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1 }, 
			{ 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1 },
			{ 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 1 }, 
			{ 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1 }, 
			{ 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1 },
			{ 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1 }, 
			{ 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1 },
			{ 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1 },
			{ 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1 },
			{ 1, 0, 1, 0, 0, 0, 1, 0, 1, 1, 0, 1, 1, 1 },
			{ 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 3, 1 },
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 } };
			
//			public static final int[][] MAZE_GRID = new int[][] { 
//				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
//				{ 1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, 
//				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1 }, 
//				{ 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1 },
//				{ 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1 }, 
//				{ 1, 0, 1, 1, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1 }, 
//				{ 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1 },
//				{ 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 1, 0, 1 }, 
//				{ 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1 },
//				{ 1, 1, 0, 1, 1, 1, 0, 1, 1, 0, 0, 1, 0, 1 },
//				{ 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1 },
//				{ 1, 0, 1, 0, 0, 0, 1, 0, 1, 1, 0, 1, 0, 1 },
//				{ 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 3, 1 },
//				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 } };
	
	// Longest possible path ~= r-1(c/2)
	public static final int GENOME_LENGTH = (int) Math.floor((MAZE_GRID.length - 1) * (MAZE_GRID[0].length / 2));
	
	@SuppressWarnings("unused")
	private static final MazePosition MAZE_HOLDER = new MazePosition(MAZE_GRID);

	public static void main(String[] args) {
		Random xd = new Random();

		// Initialize Population
		Genome[] generation = new Genome[100];
		for (int i = 0; i < generation.length; i++) {
			generation[i] = new Genome(GENOME_LENGTH);
		}

		// Prepare Simulation
		int gen = 0;
		Genome bestGenome = new Genome(GENOME_LENGTH);
		double bestFitness = 0;
		boolean finished = false;

		// Simulate untill 100% or 25000 generations
		while (!finished) {

			// Calculate fitness of each genome and assign fitness
			for (int i = 0; i < generation.length; i++) {
				double mazeScore = generation[i].evaluateFitness(MAZE_GRID.length * MAZE_GRID[0].length);
				if (mazeScore > bestFitness) {
					bestFitness = mazeScore;
					bestGenome = generation[i];
					// New fitness record (all generations)
					System.out.printf("Generation %d: %2.2f >%s\n", gen, bestFitness, bestGenome.geneString());
				}
			}

			// Best of each generation.
			// System.out.println("Generation " + gen + ": " + bestFitness + "
			// >" + bestGenome.geneString());

			// Build mating pool weighted on fitness
			ArrayList<Genome> matingPool = new ArrayList<Genome>();
			for (int i = 0; i < generation.length; i++) {
				int n = (int) Math.ceil(generation[i].fitness);
				for (int j = 0; j < n; j++) {
					matingPool.add(generation[i]);
				}
			}

			// Reproduction
			for (int i = 0; i < generation.length; i++) {
				int a = xd.nextInt(matingPool.size());
				int b = xd.nextInt(matingPool.size());

				Genome parentA = matingPool.get(a);
				Genome parentB = matingPool.get(b);
				//Genome child = parentA.randomCrossover(parentB);
				Genome child = parentA.twoPtCrossover(parentB);
				child.mutate();

				generation[i] = child;
			}
			gen++;

			if (gen >= 1000)
				finished = !finished;
		}

		/*
		 *  Print best solved maze... this is silly...
		 */
		int[][] solved_map = makeSolvedMap(bestGenome);
		
		System.out.println("fin: " + bestGenome.geneString());
		for (int[] x : solved_map) {
			for (int y : x) {
				if (y == 0) System.out.print("   ");
				if (y == 1) System.out.print(" X ");
				if (y == 2) System.out.print(" S ");
				if (y == 3) System.out.print(" G ");
				if (y == 4) System.out.print(" * ");
			}
			System.out.println();
		}
	}

	private static int[][] makeSolvedMap(Genome bestGenome) {
		
		final int[][] SOLVED_GRID = MAZE_GRID;
		
		int start_row = 1;
		int start_col = 1;

		MazePosition pos = new MazePosition(start_row, start_col);

		for (char gene : bestGenome.geneString().toCharArray()) {
			int n, e, s, w;
			n = pos.getNeighborValueNeg1IfEdge(Direction.N);
			e = pos.getNeighborValueNeg1IfEdge(Direction.E);
			s = pos.getNeighborValueNeg1IfEdge(Direction.S);
			w = pos.getNeighborValueNeg1IfEdge(Direction.W);
			
			if (gene == 'N') {
				if (n == 0 || n == 2 || n == 3 || n == 4) {
					SOLVED_GRID[pos.getRowIdx()][pos.getColumnIdx()] = 4;
					pos = pos.getNeighbor(Direction.N);
				}
			} else if (gene == 'E') {
				if (e == 0 || e == 2 || e == 3 || e == 4) {
					SOLVED_GRID[pos.getRowIdx()][pos.getColumnIdx()] = 4;
					pos = pos.getNeighbor(Direction.E);
				}
			} else if (gene == 'S') {
				if (s == 0 || s == 2 || s == 3 || s == 4) {
					SOLVED_GRID[pos.getRowIdx()][pos.getColumnIdx()] = 4;
					pos = pos.getNeighbor(Direction.S);
				}
			} else if (gene == 'W') {
				if (w == 0 || w == 2 || w == 3 || w == 4) {
					SOLVED_GRID[pos.getRowIdx()][pos.getColumnIdx()] = 4;
					pos = pos.getNeighbor(Direction.W);
				}
			} 
//			System.out.printf("pos:[%d,%d] val:%d next:%s n:%d  s:%d  e:%d  w:%d \n", 
//			  pos.getRowIdx(), pos.getColumnIdx(), pos.getValue(), gene, n, s, e, w);

		}
		return SOLVED_GRID;
	}
}
