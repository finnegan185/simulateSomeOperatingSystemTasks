package operatingSystems;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Banker {
	public static int P, R;
	public static int[] total;
	public static int[][] max;
	public static int[][] allocation;
	public static int[] available;
	public static int[] processes;
	
	public static void main(String[] args) {
		parse();
		processes = new int[P];
		for(int i = 0; i < P; i++) {
			processes[i] = i;
		}
		calculateAvailable();
		
	}
	
	public static void calculateAvailable() {
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
		System.out.println("Available = " + Arrays.toString(available));
		isSafe(processes);
	}
	
	public static boolean isSafe(int[] processes) {
		int[][] need = new int[P][R];
		calculateNeed(need, max, allocation);
		System.out.println("Need " + Arrays.deepToString(need));	
		boolean[] finish = new boolean[P];
		int[] sequence = new int[P];
		int[] work = new int[R];
		for(int i = 0; i < R; i++) {
			work[i] = available[i];
		}
		
		int count = 0;
		while(count < P) {
			boolean found = false;
			for(int p = 0; p < P; p++) {
				if(finish[p] == false) {
					int q;
					for(q = 0; q < R; q++) {
						if(need[p][q] > work[q]) {
							break;
						}
					}
					if(q == R) {
						for(int r = 0; r < R; r++) {
							work[r] += allocation[p][r];
						}
						sequence[count++] = p;
						
						System.out.println("Sequence " + Arrays.toString(sequence));
						finish[p] = true;
						found = true;
					}
				}
			}
			
			if(found == false) {
				System.out.println("System is not in a safe state");
				return false;
			}
			
		}
		System.out.println("System is in a safe state. The safe sequence is: ");
		for(int i = 0; i < P; i++) {
			System.out.print(sequence[i] + " ");
		}
		return true;
	}
	
	static void calculateNeed(int[][] need, int[][] max, int[][] allocation) {
		for(int i = 0; i < P; i++) {
			for(int j = 0; j < R; j++) {
				need[i][j] = max[i][j] - allocation[i][j];
			}
		}
	}
	
	public static void parse() {
		Scanner input = new Scanner(System.in);
		boolean fileOpened = false;
		while(fileOpened == false) {
			System.out.println("Pease enter file name with '.txt' ending: ");
			try(Scanner sc = new Scanner(new File(input.next()))){
				fileOpened = true;
				Scanner line = new Scanner(sc.nextLine());
//				System.out.println(line.toString());
				if(line.next().equalsIgnoreCase("processes")) {
					P = line.nextInt();
					line.close();
				} else {
					System.out.println("Fuck after processes");
				}
				line = new Scanner(sc.nextLine());
				if(line.next().equalsIgnoreCase("resources")) {
					R = line.nextInt();
					line.close();
				} else {
					System.out.println("Fuck after resources");
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
//						System.out.println("total " + tempInt);
						total[totCnt] = (tempInt);
						totCnt++;
					}
					line.close();
				} else {
					System.out.println("Fuck after total");
				}
				if(sc.nextLine().equalsIgnoreCase("max")) {
					int maxRow = 0;
					while(sc.hasNextInt()) {
						line = new Scanner(sc.nextLine());
						int maxCol = 0;
						while(line.hasNextInt()) {
							int tempInt = line.nextInt();
//							System.out.println("MaxRow " + maxRow + " maxCol " + maxCol + " equals " + tempInt);
							max[maxRow][maxCol] = (tempInt);
							maxCol++;
						}
						maxRow++;
					}
				} else {
					System.out.println("Fuck after max");
				}
				if(sc.nextLine().equalsIgnoreCase("allocation")) {
					int alloRow = 0;
					while(sc.hasNextInt()) {
						line = new Scanner(sc.nextLine());
						int alloCol = 0;
						while(line.hasNextInt()) {
							int tempInt = line.nextInt();
//							System.out.println(tempInt);
							allocation[alloRow][alloCol] = (tempInt);
							alloCol++;
						}
						alloRow++;
					}
				} else {
					System.out.println("Fuck after allocation");
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			System.out.println("Success");
			System.out.println("Max " + Arrays.deepToString(max));
			System.out.println("Allocation " + Arrays.deepToString(allocation));
			System.out.println("R " + R + " P " + P);
			System.out.println("Total " + Arrays.toString(total));
			input.close();
		}
	}
	
}
