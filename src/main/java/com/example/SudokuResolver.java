package com.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SudokuResolver {

	// private static final Logger LOGGER =
	// Logger.getLogger(SudokuResolver.class.getName());
	// private static final ConsoleHandler handler = new ConsoleHandler();

	private static int length = 9;
	private static int blocLength = 3;
	private static List<Integer> numbers = new ArrayList<Integer>(
			Arrays.asList(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 }));
	private static final Integer[][] matrix = new Integer[length][length];
	private static final Integer[][] initMatrix = new Integer[length][length];
	private static LessMissedPosition lessMissedPosition;

	public static void main(String[] args) {
		SudokuResolver sudokuResolver = new SudokuResolver();
		sudokuResolver.resolveBoard();
	}

	private void resolveBoard() {
		long start = System.currentTimeMillis();
		readSudoku();
		// sudokuResolver.readSudokuPostScript();
		System.out.println("display init matrix");
		displyMatrix(initMatrix);
		System.out.println("display clone matrix");
		displyMatrix(matrix);
		resolve();
		long end = System.currentTimeMillis();
		System.out.println("resolved in " + (end - start));
	}

	void displyMatrix(Integer[][] array) {
		System.out.println("##################");
		String display = "";
		for (Integer[] integers : array) {
			for (Integer integer : integers) {
				if (integer != null) {
					display += integer + " ";
				} else {
					display += ". ";
				}
			}
			display += "\n";
		}
		System.out.println(display);
		System.out.println("##################");
	}

	void readSudokuPostScript() {
		BufferedReader br = null;
		String fileName = "C:\\cours\\sudoku.ps";
		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader(fileName));
			int i = 0;
			Pattern regex = Pattern.compile("\\((.*?)\\)");
			while ((sCurrentLine = br.readLine()) != null) {
				Matcher regexMatcher = regex.matcher(sCurrentLine);
				int j = 0;
				while (regexMatcher.find()) {// Finds Matching Pattern in String
					try {
						initMatrix[i][j] = Integer.valueOf(regexMatcher.group(1));
					} catch (NumberFormatException e) {

					}
					j++;
				}
				i++;
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		cloneMatrix(initMatrix, matrix);
	}

	void readSudoku() {
		BufferedReader br = null;
		String fileName = "C:\\cours\\sudo.txt";
		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader(fileName));
			int i = 0;
			while ((sCurrentLine = br.readLine()) != null) {
				if (sCurrentLine.contains("|") && !sCurrentLine.contains("---")) {
					// String line =
					// sCurrentLine.substring(sCurrentLine.indexOf("|") + 1,
					// sCurrentLine.lastIndexOf("|"))
					// .replace("| ", "").trim();
					String line = sCurrentLine.replace("| ", "").trim();
					String[] split = line.split(" ");
					for (int j = 0; j < split.length; j++) {
						try {
							initMatrix[i][j] = Integer.valueOf(split[j]);
						} catch (NumberFormatException e) {

						}
					}
					System.out.println(line);
					i++;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		cloneMatrix(initMatrix, matrix);
	}

	void cloneMatrix(Integer[][] source, Integer[][] destination) {
		for (int i = 0; i < source.length; i++) {
			for (int j = 0; j < source[i].length; j++) {
				destination[i][j] = source[i][j];
			}
		}
	}

	List<Integer> getLine(int line) {
		return new ArrayList<Integer>(Arrays.asList(matrix[line]));
	}

	List<Integer> getColumn(int column) {
		List<Integer> result = new ArrayList<Integer>();
		for (Integer[] line : matrix) {
			result.add(line[column]);
		}
		return result;
	}

	Integer[][] getBloc(int startLine, int startColumn) {
		startLine = (startLine / blocLength) * blocLength;
		startColumn = (startColumn / blocLength) * blocLength;

		Integer[][] result = new Integer[blocLength][blocLength];
		for (int i = startLine; i < startLine + blocLength; i++) {
			for (int j = startColumn; j < startColumn + blocLength; j++) {
				result[i - startLine][j - startColumn] = matrix[i][j];
			}
		}

		return result;
	}

	List<Integer> getMissedNumbers(int posX, int posY) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		if (matrix[posX][posY] == null) {
			for (Integer number : numbers) {
				if (getLine(posX).contains(number)) {
					continue;
				}
				if (getColumn(posY).contains(number)) {
					continue;
				}
				if (contains(getBloc(posX, posY), number)) {
					continue;
				}

				result.add(number);
			}
		}

		return result;
	}

	void resolve() {
		resolveWithPutting();

		boolean resolved = isResolved();
		System.out.println("is resolved : " + resolved);
		if (!resolved) {
			Path path = new Path(null, matrix, lessMissedPosition);
			boolean isResolved = path.resolveWithTryPath();
			System.out.println("is resolved with try path : " + isResolved);

		}
	}

	private void resolveWithPutting() {
		boolean retry;
		do {
			displyMatrix(matrix);
			retry = checkAndPut();
		} while (retry);
	}

	boolean isResolved() {
		for (int i = 0; i < matrix.length; i++) {
			if (!Arrays.asList(matrix[i]).containsAll(numbers)) {
				return false;
			}
		}

		for (int i = 0; i < matrix.length; i++) {
			if (!getColumn(i).containsAll(numbers)) {
				return false;
			}
		}

		for (int i = 0; i < length / blocLength; i++) {
			for (int j = 0; j < length / blocLength; j++) {
				if (!blocAsList(getBloc(i * blocLength, j * blocLength)).containsAll(numbers)) {
					return false;
				}
			}
		}

		return true;
	}

	private List<Integer> blocAsList(Integer[][] bloc) {
		List<Integer> result = new ArrayList<Integer>();
		for (Integer[] integers : bloc) {
			for (Integer integer : integers) {
				result.add(integer);
			}
		}
		return result;
	}

	boolean isFilled() {
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				if (matrix[i][j] == null) {
					return false;
				}
			}
		}

		return true;
	}

	private boolean checkAndPut() {
		Boolean retry = false;
		for (int posX = 0; posX < length; posX++) {
			for (int posY = 0; posY < length; posY++) {
				if (matrix[posX][posY] == null) {
					List<Integer> missedNumbers = getMissedNumbers(posX, posY);
					if (missedNumbers.size() == 0) {
						System.out.println("Cannot fill any value in " + posX + " , " + posY);
						lessMissedPosition = null;
						return false;
					} else if (missedNumbers.size() == 1) {
						matrix[posX][posY] = missedNumbers.get(0);
						System.out.println("####" + missedNumbers.get(0) + "### in " + posX + "," + posY);
						retry = true;
					} else if (lessMissedPosition == null || (missedNumbers.size() < lessMissedPosition
							.getMissedNumbers().size()
							&& (lessMissedPosition.getPosX() != posX || lessMissedPosition.getPosY() != posY))) {
						lessMissedPosition = new LessMissedPosition(posX, posY, missedNumbers);
					}
				}
			}
		}

		return retry;
	}

	boolean contains(Integer[][] bloc, int number) {
		for (Integer[] subLine : bloc) {
			for (Integer value : subLine) {
				if (value != null && value == number) {
					return true;
				}
			}
		}

		return false;
	}

	class Path {
		Path previewsPath;
		Integer[][] stableMatrix = new Integer[length][length];
		LessMissedPosition triedLessMissedPosition;
		int triedNumber;

		public Path(Path previewsPath, Integer[][] stableMatrix, LessMissedPosition lessMissedPosition) {
			super();
			System.out.println("Blocked situation. Try in postion " + lessMissedPosition.getPosX() + " , "
					+ lessMissedPosition.getPosY() + " with possible values : "
					+ lessMissedPosition.getMissedNumbers());
			displyMatrix(stableMatrix);
			this.previewsPath = previewsPath;
			System.out.println("We are in the " + getLevel() + " level");
			cloneMatrix(stableMatrix, this.stableMatrix);
			try {
				this.triedLessMissedPosition = lessMissedPosition.clone();
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			SudokuResolver.lessMissedPosition = null;

		}

		int getLevel() {
			if (previewsPath == null) {
				return 1;
			} else {
				return previewsPath.getLevel() + 1;
			}
		}

		boolean tryAndPut() {
			System.out.println("Blocked situation. Try value " + triedNumber + " in postion "
					+ triedLessMissedPosition.getPosX() + " , " + triedLessMissedPosition.getPosY()
					+ " with possible values : " + triedLessMissedPosition.getMissedNumbers() + "");
			cloneMatrix(stableMatrix, matrix);
			System.out.println("The matrix is : ####### Level " + getLevel() + " " + triedLessMissedPosition.getPosX()
					+ " " + triedLessMissedPosition.getPosY());
			displyMatrix(stableMatrix);
			matrix[triedLessMissedPosition.getPosX()][triedLessMissedPosition.getPosY()] = this.triedNumber;
			resolveWithPutting();
			if (isResolved()) {
				return true;
			} else if (isFilled()) {
				// TODO normalement on doit pas entrer dans ce bloque
				return false;
			} else {
				// return new Path(this, matrix,
				// lessMissedPosition).tryAndPut(triedNumber);
			}

			return false;
		}

		boolean resolveWithTryPath() {
			if (triedNumber == 0) {
				triedNumber = triedLessMissedPosition.getMissedNumbers().get(0);
			} else {
				if (triedLessMissedPosition.getMissedNumbers()
						.indexOf(triedNumber) < triedLessMissedPosition.getMissedNumbers().size() - 1) {
					triedNumber = triedLessMissedPosition.getMissedNumbers()
							.get(triedLessMissedPosition.getMissedNumbers().indexOf(triedNumber) + 1);
				} else {
					if (previewsPath == null) {
						System.out.println("Failed to resolve !!!!!!!!!!");
						return false;
					} else {
						System.out.println("all numbers are tested. go to previews level " + previewsPath.getLevel());
						return previewsPath.resolveWithTryPath();
					}
				}
			}
			boolean tryAndPut = tryAndPut();
			if (tryAndPut) {
				return true;
			} else {
				if (lessMissedPosition == null) {
					System.out
							.println("tried number " + triedNumber + "in position " + triedLessMissedPosition.getPosX()
									+ " ," + triedLessMissedPosition.getPosY() + " is rong ");
					System.out.println("Check if we back to the new trying number or if we back to the previews path");
					if (triedLessMissedPosition.getMissedNumbers()
							.indexOf(triedNumber) < triedLessMissedPosition.getMissedNumbers().size() - 1) {
						System.out.println("We try the next number");
						resolveWithTryPath();
					} else {
						System.out.println("We back to the previews path.currentLevl : " + getLevel() + " batck to : "
								+ previewsPath.getLevel() + " level");
						return false;
					}
				} else {
					System.out.println("Next level path");
					Path path = new Path(this, matrix, lessMissedPosition);
					boolean resolveWithTryPath = path.resolveWithTryPath();
					if (!resolveWithTryPath) {
						if (previewsPath == null) {
							System.out.println("The tried value " + triedNumber + " on top level is rong");
						} else {
							System.out.println(
									"The tried value " + triedNumber + " in " + triedLessMissedPosition.getPosX()
											+ " , " + triedLessMissedPosition.getPosY() + " is rong");
						}
						return resolveWithTryPath();
					}
				}
			}
			return isResolved();
		}

	}

	class LessMissedPosition {
		int posX;
		int posY;
		List<Integer> missedNumbers;

		public LessMissedPosition(int posX, int posY, List<Integer> missedNumbers) {
			super();
			this.posX = posX;
			this.posY = posY;
			this.missedNumbers = missedNumbers;
		}

		public int getPosX() {
			return posX;
		}

		public void setPosX(int posX) {
			this.posX = posX;
		}

		public int getPosY() {
			return posY;
		}

		public void setPosY(int posY) {
			this.posY = posY;
		}

		public List<Integer> getMissedNumbers() {
			return missedNumbers;
		}

		public void setMissedNumbers(List<Integer> missedNumbers) {
			this.missedNumbers = missedNumbers;
		}

		@Override
		protected LessMissedPosition clone() throws CloneNotSupportedException {
			LessMissedPosition clonnedObject = new LessMissedPosition(posX, posY, new ArrayList<Integer>());
			clonnedObject.getMissedNumbers().addAll(missedNumbers);
			return clonnedObject;
		}

	}
}
