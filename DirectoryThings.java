/* File:	DirectoryThing.java
 * Author:	Zachary Finnegan
 * Date:	2/15/2020
 * Purpose: DirectoryThing is for practicing directory and file management along with
 * 			encryption.
 */

package operatingSystems;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class DirectoryThings {
	public static Scanner input = new Scanner(System.in);
	public static Path dir;
	public static String inputDirName;
	
	public static void main(String[] args) {
		int selection = -1;
		System.out.println("Welcome to Directory Things\n");
		while(selection != 0) {
			System.out.println("0 - Exit\n1 - Select directory\n2 - List directory content (first level)"
					+ "\n3 - List directory content (all levels)\n4 - Delete file\n5 - Display file(hexadecimal view)"
					+ "\n6 - Encrypt file (XOR with password)\n7 - Decrypt file (XOR with password)\nSelect option: ");
			if(input.hasNextInt()) {
				try {
					selection = input.nextInt();
					switch(selection) {
						case 0: break;
						case 1: selDir();
							break;
						case 2: listDirFirst();
							break;
						case 3: listDirAll();
							break;
						case 4: delete();
							break;
						case 5: displayHex();
							break;
						case 6: encrypt();
							break;
						case 7: decrypt();
							break;
						default: System.out.println("Please enter a number 0-7.");
							break;
					}
				} catch (NoSuchFileException e1) {
					System.out.println(inputDirName + " is not a valid path name.\nPlease enter a new path in menu option 1.\n");
				} catch (NullPointerException e2) {
					System.out.println("File not found.");
				} catch (IOException | DirectoryIteratorException | InvalidPathException e) {
					e.printStackTrace();
				} 
			}else {
				System.out.println("Please enter a number that corresponds to the menu item you wish to use.\n");
				input.nextLine();
			}
		}
		
		System.out.println("Thank you for using Directory Things. Bye.");
	}
	
	public static void selDir() {
		/*
		 * Used to select a new directory.
		 */
		System.out.println("Please enter absolute directory name: ");
		inputDirName = input.next();
		dir = Paths.get(inputDirName);
		System.out.println("Current Directory set to: " + dir.toString());
		System.out.println();
		input.nextLine();
	}
	
	public static void listDirFirst() throws IOException, InvalidPathException, DirectoryIteratorException, NoSuchFileException {
		/*
		 * Used to display the first level of the selected directory.
		 */
		DirectoryStream<Path> stream = Files.newDirectoryStream(dir);
		System.out.println("First Level list of Directory: " + dir.toString());
		for(Path file: stream) {
			System.out.println(" - " + file.getFileName());
		}
		System.out.println();
	}
	
	public static void listDirAll() throws IOException, InvalidPathException, DirectoryIteratorException, NoSuchFileException {
		/*
		 * Used to display all levels of the selected directory.
		 */
		File root = new File(inputDirName);
		if(root.exists() && root.isDirectory()) {
			File[] fileArray = root.listFiles();
			System.out.println("All level list of Directory: " + inputDirName);
			allDirPrint(fileArray, 0, 0);
		} else {
			throw new NoSuchFileException("");
		}
		System.out.println();
	}
	
	public static void allDirPrint(File[] fileArray, int index, int level) {
		/*
		 * Used to actually print all of the directory files and directories.
		 */
		if(index == fileArray.length) {
			return;
		}
		for(int i = 0; i < level; i++) {
			System.out.print("  ");
		}
		if(fileArray[index].isFile()) {
			System.out.println("- " + fileArray[index].getName());
		}else if(fileArray[index].isDirectory()) {
			System.out.println("- " + fileArray[index].getName());
			allDirPrint(fileArray[index].listFiles(), 0, level + 1);
		}
		allDirPrint(fileArray, ++index, level);
	}
	
	public static void delete() {
		/*
		 * Used to delete the requested file.
		 */
		System.out.println("Enter the name of the file to be deleted: ");
		File file = new File(inputDirName + "\\" + input.next());
		System.out.println(file.toString());
		if(file.delete()) {
			System.out.println("The file was deleted successfully./n");
		} else {
			System.out.println("The specified file does not exist in this path of the directory./n");
		}
	}
	
	public static void displayHex() throws IOException {
		/*
		 * Used to display the Hexidecimal equivalent of the file text.
		 */
		System.out.println("Enter the name of the file to be Hexed: ");
		File file = new File(inputDirName + "\\" + input.next());
		if(file.exists()) {
			FileInputStream stream = new FileInputStream(file);
			System.out.println("Below is your hexed file.");
			int i = 0;
			int count = 0;
			int lineCount = 1;
			System.out.println("Offset   Hexadecimal");
			System.out.print("0000000: ");
			while((i = stream.read()) != -1) {
				System.out.printf("%02X ", i);
				count++;
				if(count == 16) {
					System.out.println("");
					System.out.printf("00000%02X", lineCount);
					System.out.print(": ");
					lineCount++;
					count = 0;
				}
			}
			stream.close();
		} else {
			System.out.println(file.toString() + " does not exist.");
		}
		System.out.println("\n");
	}
	
	public static void encrypt() throws IOException, NullPointerException {
		/*
		 * Used to create a password encrypted file.
		 */
		System.out.println("Enter the password to encrypt the file with: ");
		String password = input.next();
		char[] passArray = password.toCharArray();
		System.out.println("Enter the file name you want to encrypt: ");
		String fileName = inputDirName + "\\" + input.next();
		File file = new File(fileName);
		System.out.println("Enter the name of the encrypted file: ");
		String newFile = input.next();
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			if(file.exists()) {
			 	in = new FileInputStream(file);
			 	out = new FileOutputStream(inputDirName + "\\" + newFile);
			} else {
				System.out.println(fileName + " does not exist in this directory./n");
				return;
			}
			int c;
			int i = 0;
			while ((c = in.read()) != -1) {
				int tempByte = (c ^ passArray[i]);
				out.write(tempByte);
				i++;
				if(i == password.length()) {
					i = 0;
				}
			}
			System.out.println("The requested file has been encrypted and saved as: " + newFile + "\n");
		} finally {
			in.close();
			out.close();
		}
	}
	
	public static void decrypt() throws IOException, NullPointerException {
		/*
		 * Used to decrypt an encrypted file with a password.
		 */
		System.out.println("Enter the password you used to encrypt the encrypted file:");
		String password = input.next();
		char[] passArray = password.toCharArray();
		System.out.println("Enter the file name you want to decrypt.");
		String fileName = inputDirName + "\\" + input.next();
		File file = new File(fileName);
		System.out.println("Enter the name of the decrypted file: ");
		String newFile = input.next();
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			if(file.exists()) {
				in = new FileInputStream(file);
				out = new FileOutputStream(inputDirName + "\\" + newFile);
			} else {
				System.out.println(fileName + " does not exist in this directory./n");
				return;
			}
			int c;
			int i = 0;
			while ((c = in.read()) != -1) {
				int tempByte = (c ^ passArray[i]);
				out.write(tempByte);
				i++;
				if(i == password.length()) {
					i = 0;
				}
			}
			System.out.println("The requested file has been decrypted and saved as: " + newFile + "\n");
		} finally {
			in.close();
			out.close();
		}
	}

}
