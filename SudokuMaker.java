/**
 *	SudokuMaker - Creates a Sudoku puzzle using recursion and backtracking
 *
 *	@author Anushka Vijay
 *	@version
 *
 */
public class SudokuMaker {

	private int[][] puzzle;
		
	public SudokuMaker() {
		puzzle = new int [9][9];
		for(int i = 0; i < puzzle.length; i++) { //initializing entire board as empty
			for(int j = 0; j < puzzle[i].length; j++)
				puzzle[i][j] = 0;
		}
	}
	
	public static void main(String [] args) {
		SudokuMaker sm = new SudokuMaker();
		sm.run();
	}
	
	public void run() {
		boolean result = false;
		int iteration = 0;
		while(result == false) {
			iteration++;
			result = createPuzzle(0,0);
		}
		printPuzzle();
	}
	
	public boolean createPuzzle(int row, int col) {  
		if(row == 9) return true; //the puzzle is finished
		int [] list = new int [9]; //list of random integers from 1 to 9
		int count = 0;
		//initialize list of numbers 1 to 9 in random order
		while(count < list.length) {
			int random = (int)(Math.random()*9+1); //creates random integer
			if(count == 0) { //if 0th index of array, initialize immediately
				list[count] = random;
				count++;
			}
			else {
				boolean isRepeat = false;
				for(int i = 0; i < count; i++) //check if random integer already in list
					if(list[i] == random)
						isRepeat = true;
				if(isRepeat == false) { //if random integer not in list already, initialize
					list[count] = random;
					count++;
				}
			}
		}
		int listIndex = 0;
		int newRow = 0, newCol = 0;
		boolean stillSearching = true;
		while(stillSearching && listIndex < list.length) {
			int listValue = list[listIndex];
			if(notInRow(row, col, listValue) && notInCol(row, col, listValue)
									&& notInGrid(row, col, listValue)) {
				puzzle[row][col] = listValue;
				stillSearching = false; //exit out of while loop
				if(col == 8)
					newRow = row+1;
				else {
					newRow = row;
					newCol = col+1;
				}
				if(createPuzzle(newRow, newCol))
					return true;
			}
			else
				listIndex++;
		}
		puzzle[row][col] = 0;
		return false;
	}
	
	private boolean notInRow(int row, int col, int val) {
		for(int i = 0; i < puzzle.length; i++) {
			int puzzleValue = puzzle[row][i];
			if(i != col) { //not the location where you want to put value
				if(val == puzzleValue && puzzleValue != 0)
					return false;
			}
		}
		return true;
	}
	
	private boolean notInCol(int row, int col, int val) {
		for(int i = 0; i < puzzle[row].length; i++) {
			int puzzleValue = puzzle[i][col];
			if(i != row) { //not the location where you want to put value
				if(val == puzzleValue)
					return false;
			}
		}
		return true;
	}
	
	private boolean notInGrid(int row, int col, int val) {
		int colStart = (col/3)*3;
		int rowStart = (row/3)*3;
		for(int r = rowStart; r < rowStart+2; r++) {
			for(int c = colStart; c < colStart+2; c++) {
				int puzzleValue = puzzle[r][c];
				if(row != r && col != c) { //not location where want to put value 
					if(val == puzzleValue)
						return false;
				}	
			}
		}
		return true;
	}
	
	/**
	 *	printPuzzle - prints the Sudoku puzzle with borders
	 *	If the value is 0, then print an empty space; otherwise, print the number.
	 */
	public void printPuzzle() {
		System.out.print("  +-----------+-----------+-----------+\n");
		String value = "";
		for (int row = 0; row < puzzle.length; row++) {
			for (int col = 0; col < puzzle[0].length; col++) {
				// if number is 0, print a blank
				if (puzzle[row][col] == 0) value = " ";
				else value = "" + puzzle[row][col];
				if (col % 3 == 0)
					System.out.print("  |  " + value);
				else
					System.out.print("  " + value);
			}
			if ((row + 1) % 3 == 0)
				System.out.print("  |\n  +-----------+-----------+-----------+\n");
			else
				System.out.print("  |\n");
		}
	}
}
