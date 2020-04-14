import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/**
 *	SudokuSolver - Solves an incomplete Sudoku puzzle using recursion and backtracking
 *
 *	@author	Anushka Vijay
 *	@since	28 January 2018
 *
 */
public class SudokuSolver {

	private int[][] puzzle;		// the Sudoku puzzle 2-D array
	private String PUZZLE_FILE = "puzzle1.txt";	// default puzzle file
	
	/* Constructor */
	public SudokuSolver() {
		puzzle = new int[9][9];
		// fill puzzle with zeros
		for (int row = 0; row < puzzle.length; row++)
			for (int col = 0; col < puzzle[0].length; col++)
				puzzle[row][col] = 0;
	}
	
	public static void main(String[] args) {
		SudokuSolver sm = new SudokuSolver();
		sm.run(args);
	}
	
	/**
	 * calls necessary methods to print, load, and solve puzzle.
	 * @param args
	 */
	public void run(String[] args) {
		// get the name of the puzzle file
		String puzzleFile = PUZZLE_FILE;
		if (args.length > 0) puzzleFile = args[0];
		//print introduction
		System.out.println("\nSudoku Puzzle Solver");
		// load the puzzle
		System.out.println("Loading puzzle file " + puzzleFile);
		loadPuzzle(puzzleFile);
		printPuzzle();
		// solve the puzzle starting in (0,0) spot (upper left)
		boolean result = false;
		//loop through recursion until sudoku is a success
		while(result == false) {
			result = solvePuzzle(0, 0);
		}
		//print completed puzzle
		printPuzzle();
	}
	
	/**	Load the puzzle from a file
	 *	@param filename		name of puzzle file
	 */
	public void loadPuzzle(String filename) {
		//read in text file of puzzle
		Scanner infile = FileUtils.openToRead(filename);
		//load puzzle into array
		for (int row = 0; row < 9; row++)
			for (int col = 0; col < 9; col++)
				puzzle[row][col] = infile.nextInt();
		//closes scanner
		infile.close();
	}
	
	/**	
	 * Solve the Sudoku puzzle using brute-force method, recursively.
	 * 
	 * @param row	row location of cell
	 * @param col 	column location of cell
	 * @return 		false if cell cannot be filled, backtrack method
	 * 				true if puzzle is finished
	 */
	public boolean solvePuzzle(int row, int col) {
		if(row == 9) return true; //the puzzle is finished
		if(puzzle[row][col] == 0) { //if empty space
			int [] list = new int [9]; //list of random integers from 1 to 9
			int count = 0; //incrementing variable of array
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
			boolean stillSearching = true; //false when unique value in list is found
			while(stillSearching && listIndex < list.length) {
				int listValue = list[listIndex];
				//check if value is not already in row, column or grid
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
					if(solvePuzzle(newRow, newCol)) //recursive call of next cell in row-major order
						return true;
				}
				else
					listIndex++;
			}
			puzzle[row][col] = 0; //if solution fails, intialize as empty cell and backtrack
			return false;
		}
		else {
			int newRow = 0, newCol = 0;
			if(col == 8)
				newRow = row+1;
			else {
				newRow = row;
				newCol = col+1;
			}
			if(solvePuzzle(newRow, newCol)) //recursive call of next cell in row-major order
				return true;
		}
		return false; //backtrack
	}
	
	/**
	 * checks if value is already in row
	 * 
	 * @param row	row location of cell
	 * @param col	column location of cell
	 * @param value	value to be checked if unique
	 * @return 		true if value is unique in row
	 * 				false if value already in row
	 */
	private boolean notInRow(int row, int col, int val) {
		for(int i = 0; i < puzzle.length; i++) {
			int puzzleValue = puzzle[row][i];
			if(i != col) { //not the location where you want to put value
				if(val == puzzleValue && puzzleValue != 0)
					return false; //if value is already in row
			}
		}
		return true; //if value is unique in row
	}
	
	/**
	 * checks if value is already in column
	 * 
	 * @param row	row location of cell
	 * @param col	column location of cell
	 * @param value	value to be checked if unique
	 * @return 		true if value is unique in row
	 * 				false if value already in row
	 */
	private boolean notInCol(int row, int col, int val) {
		for(int i = 0; i < puzzle[row].length; i++) {
			int puzzleValue = puzzle[i][col];
			if(i != row) { //not the location where you want to put value
				if(val == puzzleValue)
					return false; //if value is already in column
			}
		}
		return true; //if value is unique in column
	}
	
	/**
	 * checks if value is already in grid
	 * 
	 * @param row	row location of cell
	 * @param col	column location of cell
	 * @param value	value to be checked if unique
	 * @return 		true if value is unique in row
	 * 				false if value already in row
	 */
	private boolean notInGrid(int row, int col, int val) {
		//starting location of the grid
		int colStart = (col/3)*3;
		int rowStart = (row/3)*3;
		//for loop to check if value is unique in grid
		for(int r = rowStart; r < rowStart+2; r++) {
			for(int c = colStart; c < colStart+2; c++) {
				int puzzleValue = puzzle[r][c];
				if(row != r && col != c) { //not location where want to put value 
					if(val == puzzleValue)
						return false; //if value is already in grid
				}	
			}
		}
		return true; //if value is unique in grid
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
