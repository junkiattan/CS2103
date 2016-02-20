package textBuddy;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
// Assumptions: Starting file is empty and that the file refreshes back to empty after it is terminated.
// Learning points:
// 1. To add strings, use println so strings will be added line-by-line
// 2. To delete, create a new instance of PrintWriter to bring cursor back up. Using print instead of println will do
// 3. Each time print or println is used, you must flush it immediately afterwards

/**
 * 
 * @author Tan Jun Kiat
 *
 */
// below class is to separate the handling of user input/output in order to avoid global declaration of Scanner
class IO{
	private static Scanner sc = null;
	private IO(){
	};
	public static Scanner getSystemScanner(){
		if (sc == null){
			sc = new Scanner(System.in);
		}
		return sc;
	}
	public static String getNextLine(){
		return getSystemScanner().nextLine();
	}
	
	
	//public static PrintWriter get
}

public class TextBuddy {
	
	private static final String MESSAGE_WELCOME = "Welcome to TextBuddy. %1$s is ready for use\n";
	private static final String MESSAGE_DISPLAY = "%1$s. %2$s\n";
	private static final String MESSAGE_DELETE = "deleted from %1$s: \"%2$s\"\n";
	private static final String MESSAGE_EMPTY = "%1$s is empty\n";
	private static final String MESSAGE_ADD = "added to %1$s: \"%2$s\"\n";
	private static final String MESSAGE_CLEAR = "all content deleted from %1$s\n";
	private static final String MESSAGE_INVALID = "Invalid command\n";
	private static final String MESSAGE_INVALID_DELETE = "Item does not exist or has already been deleted\n";
	private static ArrayList<String> stringStorage;
	
	public static void main(String args[]) throws FileNotFoundException{ // args[0] is the filename
		mainMessagePrinter(welcomeMessage(args));
		
		stringStorage = new ArrayList<String>();
		
		File userFile = new File(args[0]);
		PrintWriter printWriter = null;
		try {
			printWriter = new PrintWriter(userFile);
		} catch (FileNotFoundException e) {}
		
		while (true){	
			modifyInput(args, userFile, printWriter);
		}
	}
	
	public static void mainMessagePrinter(String sentence){
		System.out.print(sentence);
	}

	public static String welcomeMessage(String[] args){
		String printSentence = String.format(MESSAGE_WELCOME, args[0]);
		return printSentence;
	}

	private static void modifyInput(String[] args, File userFile,
			PrintWriter printWriter) throws FileNotFoundException {
		String[] input = IO.getNextLine().split(" ");
		switch (input[0])
		{
		case "add":
			addAndPrint(args, stringStorage, printWriter, input);
			break;
		case "display":
			displayAndPrint(args, stringStorage);
			break;
		case "delete":
			deleteAndPrint(args, stringStorage, userFile, input);
			break;
		case "clear":
			clearAndPrint(args, stringStorage, userFile);
			break;
		case "exit":
			System.exit(0);
		default:
			//throw an error if the command is not recognized
			System.out.printf(MESSAGE_INVALID);
		}
	}

	public static String displayAndPrint(String[] args, ArrayList<String> stringStorage) {
		// if there are text to be displayed
		// displayPrintFunction to print out text in order of addition
		if (!stringStorage.isEmpty()){
			for (int i=0; i<stringStorage.size(); i++){
				String printSentence = String.format(MESSAGE_DISPLAY, (i+1), stringStorage.get(i));
				System.out.print(printSentence);
				return printSentence;
				//System.out.printf(MESSAGE_DISPLAY, (i+1), stringStorage.get(i));
			}
		} else {
		// if file is empty, to display print below
		String printSentence = String.format(MESSAGE_EMPTY, args[0]);
		System.out.print(printSentence);
		return printSentence;
		//System.out.printf(MESSAGE_EMPTY, args[0]);
		}
		return null;
	}

	private static void addAndPrint(String[] args, ArrayList<String> stringStorage, PrintWriter printWriter,
			String[] input) {
		// adds the text to the file
		String sentence = convertToString(input);
		stringStorage.add(sentence);
		fileAdder(printWriter, sentence);
		System.out.printf(MESSAGE_ADD, args[0], sentence);
	}

	private static void fileAdder(PrintWriter printWriter, String sentence) {
		printWriter.println(sentence);
		printWriter.flush();
	}
	private static void fileDeleter(PrintWriter printWriter, String sentence) {
		printWriter.print(sentence);
		printWriter.flush();
	}

	private static void clearAndPrint(String[] args, ArrayList<String> stringStorage, File userFile)
			throws FileNotFoundException {
		PrintWriter clearPrintWriter = new PrintWriter(userFile);
		// clear the content from the text file
		// after which add below print statement
		stringStorage.clear();
		fileDeleter(clearPrintWriter, "");
		clearPrintWriter.close();
		System.out.printf(MESSAGE_CLEAR, args[0]);
	}

	private static void deleteAndPrint(String[] args, ArrayList<String> stringStorage, File userFile, String[] input)
			throws FileNotFoundException {
		int numOfRemovable = -1;
		try{
			numOfRemovable = Integer.parseInt(input[1]);
		} catch (Exception e){}
		if (numOfRemovable == 0 || numOfRemovable > stringStorage.size()){
			System.out.println(MESSAGE_INVALID_DELETE);
		} else if (numOfRemovable == -1){
			System.out.println(MESSAGE_INVALID);
		} else {
			String word = stringStorage.remove(numOfRemovable-1);
			// use the numOfRemovable to remove desired text within file
			PrintWriter deleterPrintWriter = new PrintWriter(userFile);
			fileDeleter(deleterPrintWriter, "");
			for (String s: stringStorage){
				fileAdder(deleterPrintWriter, s);
			}
			// after which close and follow up with feedback
			deleterPrintWriter.close();
			System.out.printf(MESSAGE_DELETE, args[0], word);
		}
	}

	public static String convertToString(String[] input){
		String sentence = "";
		for (int i=1; i<input.length; i++){
			if (i==input.length-1){
				sentence += input[i];
			} else {
				sentence += input[i] + " ";
			}
		}
		return sentence;
	}
	
}
