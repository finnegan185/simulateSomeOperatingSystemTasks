/* File:	BankerPlus.java
 * Author:	Zachary Finnegan
 * Date:	2/8/2020
 * Purpose:	Practice preventing deadlocks through the banker algorithm.
 */

package operatingSystems;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Scanner;

public class BankerPlus {
	public static int P, R;
	public static int[] total;
	public static int[][] max;
	public static int[][] allocation;
	public static int[] available;
	public static int[] processes;
	public static int numSafe;
	
	public static void main(String[] args) {
		/*
		 * Runs the program and calls necessary methods.
		 */
		parse();
		processes = new int[P];
		for(int i = 0; i < P; i++) {
			processes[i] = i;
		}
		calculateAvailable();
		Deque<Integer> sequence = new LinkedList<Integer>();
		int[][] need = new int[P][R];
		calculateNeed(need, max, allocation);
		System.out.println("Need " + Arrays.deepToString(need));	
		boolean[] finish = new boolean[P];
		int[] work = new int[R];
		for(int i = 0; i < R; i++) {
			work[i] = available[i];
		}
		
		numSafe = 0;
		System.out.println("The safe sequences are: ");
		findSafe(processes, need, finish, work, sequence);
		if(numSafe == 0) {
			System.out.println("There are no safe sequences.");
		}
	}
	
	public static void calculateAvailable() {
		/*
		 * Calculates how many resource are availabe in the system.
		 */
		int[] alreadyAllocated = new int[R];
		for(int row = 0; row < P; row++) {
			for(int col = 0; col < R; col++) {
				alreadyAllocated[col] += allocation[row][col];
			}
		}
		available = new int[R];
		for(int i = 0; i < R; i++) {
			available[i] = total[i] - alreadyAllocated[i];
		}
		System.out.println("Available " + Arrays.toString(available));
	}
	
	public static void findSafe(int[] processes, int[][] need, boolean[] finish, int[] work, Deque<Integer> sequence) {
		/*
		 * Recursive function for finding all of the possible safe sequences of executing the processes.
		 */
		if(sequence.size() == P) {
			System.out.println(sequence.toString());
			numSafe++;
			return;
		}
		for(int i = 0; i < P; i++) {
			if(!finish[i]) {
				int q;
				for(q = 0; q < R; q++) {
					if(need[i][q] > work[q]) {
						break;
					}
				}
				if(q == R) {
					for(int r = 0; r < R; r++) {
						work[r] += allocation[i][r];
					}
					finish[i] = true;
					sequence.add(i);
					findSafe(processes, need, finish, work, sequence);
					sequence.removeLast();
					finish[i] = false;
					for(int r = 0; r < R; r++) {
						work[r] -= allocation[i][r];
					}
				}
			}
		}
	}
	
	static void calculateNeed(int[][] need, int[][] max, int[][] allocation) {
		/*
		 * Calculates the number of resources each process requires.
		 */
		for(int i = 0; i < P; i++) {
			for(int j = 0; j < R; j++) {
				need[i][j] = max[i][j] - allocation[i][j];
			}
		}
	}
	
	public static void parse() {
		/*
		 * Reads the input file and turns the data into usable data structures.
		 */
		Scanner input = new Scanner(System.in);
		boolean fileOpened = false;
		while(fileOpened == false) {
			System.out.println("Pease enter file name with '.txt' ending: ");
			try(Scanner sc = new Scanner(new File(input.next()))){
				fileOpened = true;
				Scanner line = new Scanner(sc.nextLine());
				if(line.next().equalsIgnoreCase("processes")) {
					P = line.nextInt();
					line.close();
				}
				line = new Scanner(sc.nextLine());
				if(line.next().equalsIgnoreCase("resources")) {
					R = line.nextInt();
					line.close();
				}
				//Array instantiation
				total = new int[R];
				max = new int[P][R];
				allocation = new int[P][R];
				
				if(sc.nextLine().equalsIgnoreCase("total")) {
					int totCnt = 0;
					line = new Scanner(sc.nextLine());
					while(line.hasNextInt()) {
						int tempInt = line.nextInt();
						total[totCnt] = (tempInt);
						totCnt++;
					}
					line.close();
				}
				if(sc.nextLine().equalsIgnoreCase("max")) {
					int maxRow = 0;
					while(sc.hasNextInt()) {
						line = new Scanner(sc.nextLine());
						int maxCol = 0;
						while(line.hasNextInt()) {
							int tempInt = line.nextInt();
							max[maxRow][maxCol] = (tempInt);
							maxCol++;
						}
						maxRow++;
					}
				}
				if(sc.nextLine().equalsIgnoreCase("allocation")) {
					int alloRow = 0;
					while(sc.hasNextInt()) {
						line = new Scanner(sc.nextLine());
						int alloCol = 0;
						while(line.hasNextInt()) {
							int tempInt = line.nextInt();
							allocation[alloRow][alloCol] = (tempInt);
							alloCol++;
						}
						alloRow++;
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			System.out.println("Success");
			System.out.println("Max " + Arrays.deepToString(max));
			System.out.println("Allocation " + Arrays.deepToString(allocation));
			System.out.println("Total " + Arrays.toString(total));
			input.close();
		}
	}
	
}

