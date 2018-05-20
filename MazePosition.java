package cs455;

//Needed only by MazePosition
import java.util.Arrays;
import java.util.Objects;
@SuppressWarnings("unused")

enum Direction {
	N(-1, 0), S(1, 0), W(0, -1), E(0, 1);
	// config
	private final int rowSteps;
	private final int colSteps;

	private Direction(int rowSteps, int colSteps) {
		this.rowSteps = rowSteps;
		this.colSteps = colSteps;
	}

	public int getNewRowIdx(int currentRowIdx) {
		return (currentRowIdx + getRowSteps());
	}

	public int getNewColIdx(int currentColIdx) {
		return (currentColIdx + getColSteps());
	}

	public int getRowSteps() {
		return rowSteps;
	}

	public int getColSteps() {
		return colSteps;
	}
};

class MazePosition {
	// config
	private static int[][] MAZE_GRID;
	private final int rowIdx;
	private final int colIdx;
	// internal
	private final int rowIdxMinus1;
	private final int colIdxMinus1;

	public MazePosition(int[][] MAZE_GRID) {
		if (MazePosition.MAZE_GRID != null) {
			throw new IllegalStateException("Maze double-array already set. Use x/y constructor.");
		}
		MazePosition.MAZE_GRID = MAZE_GRID;

		// TODO: Crash if null or empty, or sub-arrays null or empty, or unequal
		// lengths, or contain anything but 0 or -1.

		rowIdx = -1;
		colIdx = -1;
		rowIdxMinus1 = -1;
		colIdxMinus1 = -1;
	}

	public MazePosition(int rowIdx, int colIdx) {
		if (MazePosition.MAZE_GRID == null) {
			throw new IllegalStateException("Must set maze double-array with: new MazePosition(int[][]).");
		}
		if (rowIdx < 0 || rowIdx > MazePosition.getRowCount()) {
			throw new IllegalArgumentException("rowIdx (" + rowIdx + ") is invalid.");
		}
		if (colIdx < 0 || colIdx > MazePosition.getColumnCount()) {
			throw new IllegalArgumentException("colIdx (" + colIdx + ") is invalid.");
		}

		this.rowIdx = rowIdx;
		this.colIdx = colIdx;
		rowIdxMinus1 = (rowIdx - 1);
		colIdxMinus1 = (colIdx - 1);
	}

	public boolean isPath() {
		return (getValue() == 0);
	}

	public int getValue() {
		return MazePosition.MAZE_GRID[getRowIdx()][getColumnIdx()];
	}

	public int getRowIdx() {
		return rowIdx;
	}

	public int getColumnIdx() {
		return colIdx;
	}

	public MazePosition getNeighbor(Direction dir) {
		Objects.requireNonNull(dir, "dir");
		return (new MazePosition(dir.getNewRowIdx(getRowIdx()), dir.getNewColIdx(getColumnIdx())));
	}

	public MazePosition getNeighborNullIfEdge(Direction dir) {
		if (isEdgeForDirection(dir)) {
			return null;
		}
		return getNeighbor(dir);
	}

	public int getNeighborValueNeg1IfEdge(Direction dir) {
		MazePosition pos = getNeighborNullIfEdge(dir);
		//System.out.println(dir + ":" + pos);
		return ((pos == null) ? -1 : pos.getValue());
	}

	public static final int getRowCount() {
		return MAZE_GRID.length;
	}

	public static final int getColumnCount() {
		return MAZE_GRID[0].length;
	}

	public boolean isEdgeForDirection(Direction dir) {
		Objects.requireNonNull(dir);
		switch (dir) {
		case N:
			return isTopEdge();
		case S:
			return isBottomEdge();
		case W:
			return isLeftEdge();
		case E:
			return isRightEdge();
		}
		throw new IllegalStateException(toString() + ", dir=" + dir);
	}

	public boolean isLeftEdge() {
		return (getColumnIdx() == 0);
	}

	public boolean isTopEdge() {
		return (getRowIdx() == 0);
	}

	public boolean isBottomEdge() {
		//return (getRowIdx() == rowIdxMinus1);
		return (getRowIdx() == MAZE_GRID.length-1);
	}

	public boolean isRightEdge() {
		//return (getColumnIdx() == colIdxMinus1);
		return (getColumnIdx() == MAZE_GRID[0].length-1);
	}

	public String toString() {
		return "[" + getRowIdx() + "," + getColumnIdx() + "]=" + getValue();
	}
}
