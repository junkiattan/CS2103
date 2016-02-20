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

// 4. In the implementation, separate the printing from the functionalities of the methods, dont combine them together.
//Save the printing for the higher levels, e.g. modifyInput or in main instead of in each sub-method themselves such as add, etc.
// 5. If the scanner needs to be used throughout the program, instead of declaring it as global, can use the Singleton pattern for Scanner
// 6. Unlike for Scanner in which one instance can be reused, multiple instances of PrintWriter must be created, hence dont need to create
//a separate method for creating the printWriter based on the fileName

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
}


public class TextBuddy {
	
	// All strings to have newline characters to standardise for use in mainMessagePrinter method
	private static final String MESSAGE_WELCOME = "Welcome to TextBuddy. %1$s is ready for use\n";
	private static final String MESSAGE_DISPLAY = "%1$s. %2$s\n";
	private static final String MESSAGE_DELETE = "deleted from %1$s: \"%2$s\"\n";
	private static final String MESSAGE_EMPTY = "%1$s is empty\n";
	private static final String MESSAGE_ADD = "added to %1$s: \"%2$s\"\n";
	private static final String MESSAGE_CLEAR = "all content deleted from %1$s\n";
	private static final String MESSAGE_SORT = "List is now sorted\n";
	private static final String MESSAGE_INVALID = "Invalid command\n";
	private static final String MESSAGE_INVALID_DELETE = "Item does not exist or has already been deleted\n";
	private static final String MESSAGE_INVALID_SORT_EMPTY = "List cannot be sorted as it is empty\n";
	private static final String MESSAGE_INVALID_SEARCH_EMPTY = "List cannot be searched as it is empty\n";
	private static final String MESSAGE_SEARCH_EMPTY = "No line with such a word exists\n";
	
	private static ArrayList<String> stringStorage = new ArrayList<String>();
	
	public static void main(String args[]) throws FileNotFoundException{ // args[0] is the filename
		
		mainMessagePrinter(welcomeMessage(args[0]));
		while (true){
			executeCommand(args[0]);
		}
	}
	
	// overload the method to print for display
	public static void mainMessagePrinter(String sentence){
		System.out.print(sentence);
	}
	public static void mainMessagePrinter(ArrayList<String> storage){
		for (String s: storage){
			System.out.print(s);
		}
	}

	public static String welcomeMessage(String fileName){
		String printSentence = String.format(MESSAGE_WELCOME, fileName);
		return printSentence;
	}
	
	// Following 2 methods to get file and create a print writer based on the fileName
	public static File getFile(String fileName){
		File file = null;
		try {
			file = new File(fileName);
		} catch (Exception e){
		}
		return file;
	}
	
	public static PrintWriter getPrintWriter(String fileName){
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(getFile(fileName));
		} catch (Exception e) {
		}
		return pw;
	}
	
	// main executeCommand functions. Printing for the lower level methods is done here before it goes back up to user
	public static void executeCommand(String fileName){
		String[] input = IO.getNextLine().split(" ");
		switch (input[0])
		{
		case "add":
			mainMessagePrinter(add(fileName, input));
			break;
		case "display":
			mainMessagePrinter(display(fileName));
			break;
		case "delete":
			mainMessagePrinter(delete(fileName, input));
			break;
		case "clear":
			mainMessagePrinter(clear(fileName));
			break;
		case "sort":
			mainMessagePrinter(sort(fileName));
			break;
		case "search":
			mainMessagePrinter(search(fileName, input));
			break;
		case "exit":
			System.exit(0);
		default:
			//throw an error if the command is not recognized
			mainMessagePrinter(MESSAGE_INVALID);
		}
	}
	
	// Pre: String fileName specifies the desired file to be written into while String[] input represents the data
	//to be written
	// Post: data to be written into file, feedback in form of String is returned
	public static String add(String fileName, String[] input) {
		if (input.length == 1){
			// takes care of null input values
			return MESSAGE_INVALID;
		} else {
			// adds the text to the file
			String sentence = convertToString(input);
			stringStorage.add(sentence);
			fileEditor(fileName, sentence);
			return String.format(MESSAGE_ADD, fileName, sentence);
		}
	}

	// Pre: String fileName specifies the file to be read
	// Post: stores each item in the file (stored in stringStorage for easy retrieval) inside an arrayList and returns that arrayList for printing later
	public static ArrayList<String> display(String fileName) {
		ArrayList<String> displayStorage = new ArrayList<String>();
		// if there are text to be displayed
		// displayPrintFunction to print out text in order of addition
		if (!stringStorage.isEmpty()){
			for (int i=0; i<stringStorage.size(); i++){
				displayStorage.add(String.format(MESSAGE_DISPLAY, (i+1), stringStorage.get(i)));
			}
		} else {
		// if file is empty, to display print below
		// single String sentence is added to arrayList for consistency
		displayStorage.add(String.format(MESSAGE_EMPTY, fileName));
		}
		return displayStorage;
	}
	
	// Pre: String fileName of desired file to be deleted from and the desired id of the item to be deleted (stored in input)
	// Post: Returns String feedback depending on whether the operation is successful
	public static String delete(String fileName, String[] input) {
		int numOfRemovable = -1;
		try{
			numOfRemovable = Integer.parseInt(input[1]);
		} catch (Exception e){}
		if (numOfRemovable == 0 || numOfRemovable > stringStorage.size()){
			return MESSAGE_INVALID_DELETE;
		} else if (numOfRemovable == -1){
			// takes care of null input values
			return MESSAGE_INVALID;
		} else {
			String word = stringStorage.remove(numOfRemovable-1);
			// use the numOfRemovable to remove desired text within file
			fileEditor(fileName, "");
			for (String s: stringStorage){
				fileEditor(fileName, s);
			}
			// after which follow up with feedback
			return String.format(MESSAGE_DELETE, fileName, word);
		}
	}

	//Pre: String fileName of the desired file to be cleared
	// Post: Returns String feedback to indicate successful clearing of desired file
	public static String clear(String fileName) {
		// clear the content from the text file
		// after which add below print statement
		stringStorage.clear();
		fileEditor(fileName, "");
		return String.format(MESSAGE_CLEAR, fileName);
	}
	
	// Pre: desired fileName
	// Post: Method to create a separate instance of PrintWriter each time it is called,
	// in addition the sentence is written into specific file
	public static void fileEditor(String fileName, String sentence) {
		PrintWriter pw = getPrintWriter(fileName);
		if (sentence.equals("")){
			// print is meant for the delete method where the printWriter prints and empty string ""
			pw.print(sentence);
		} else {
			// println is meant for the non-delete methods like add where printWriter writes in a specific string sentence
			pw.println(sentence);
		}
		pw.flush();
	}

	//Pre: String array containing words to be concatenated
	//Post: String representing the concatenated words in the array
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
	
	// Pre: String name of the file to be sorted
	// Post: returns a string which is the feedback of the outcome of the operation
	public static String sort(String fileName){
		if (stringStorage.isEmpty()){
			return MESSAGE_INVALID_SORT_EMPTY;
		} else {
			// use Collections.sort(arrayList) to sort the stringStorage
			Collections.sort(stringStorage);
			// then sort the actual file by overriding it, then adding the elements from stringStorage back to the desired file
			fileEditor(fileName, "");
			for (String line: stringStorage){
				fileEditor(fileName, line);
			}
			return MESSAGE_SORT;
		}
	}
	
	// Pre: String name of the file to be searched and string array containing the input
	// Post: returns an arrayList which either contains feedback or the actual words in the list that contains the specific word
	public static ArrayList<String> search(String fileName, String[] input){
		ArrayList<String> resultStorage = new ArrayList<String>();
		if (stringStorage.isEmpty()){
			resultStorage.add(MESSAGE_INVALID_SEARCH_EMPTY);
			return resultStorage;
		} else if (input.length == 1){
			// check for null input values
			resultStorage.add(MESSAGE_INVALID);
			return resultStorage;
		} else {
			int step = 1;
			for (int i=0; i<stringStorage.size(); i++){
				if (stringStorage.get(i).contains(input[1])){
				resultStorage.add(String.format(MESSAGE_DISPLAY, step, stringStorage.get(i)));
				step++;
				}
			}
			if (resultStorage.isEmpty()){
				resultStorage.add(MESSAGE_SEARCH_EMPTY);
			}
			return resultStorage;
		}
	}	
}
