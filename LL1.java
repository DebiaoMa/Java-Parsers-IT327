/**
 * @author Jerry Binder jmbind1@ilstu.edu
 * 
 * Recursive-descent parser using a given LL(1) parsing table.
 * All code is property of Jerry Binder.
 * Grammar provided by Dr Chung-Chih Li.
 */

public class LL1 {
	private static char[] tokens;	// array containing all tokens
	private static int curr;		// current index in tokens
	
	/* Grammar:
	 * E 	-> TE'
	 * E' 	-> +TE' | -TE' | lambda
	 * T 	-> FT'
	 * T' 	-> *FT' | /FT' | lambda
	 * F 	-> (E) 	| n
	 */

	public static void main(String[] args) {	
		// Declarations
		curr = 0;
		
		/**
		 * Ends program if there are no args. 
		 * If there are, it adds an end symbol ($) 
		 * and creates a char[] array from args[0].
		 */		
		if(args.length == 0) {
			printErrorAndExit(1);
		}else {
            String temp = args[0];
            temp = temp.replaceAll("\"", "");	// removes quotation marks
            temp = temp + "$";				// adds end symbol
			tokens = temp.toCharArray();	// turns input into char[]
		}

		int answer = parseE();	// begins recursion
		
		// double-checks to make sure the end symbol was actually reached
		if(tokens[curr] == '$') {
			System.out.println("Success! Input is valid. Answer: " + answer);
		}else {
			System.out.println("Failure! Input is invalid.");
		}
	}
	
	/**
	 * E -> TE'
	 * Starting symbol.
	 */
	private static int parseE() {
		int n = parseT();
		return parseEPrime(n);
	}
	
	/**
	 * E' -> +TE' | -TE' | lambda
	 * Parses addition or subtraction.
	 * @param int n - number passed through from ParseT()
	 */
	private static int parseEPrime(int n) {
		
		char temp = tokens[curr];
		int n2 = 0;
		
		switch(temp){
		case '+':
			curr++;
			n2 = parseT();
			return parseEPrime(n + n2);
		case '-':
			curr++;
			n2 = parseT();
			return parseEPrime(n - n2);
		case '$':
		case ')':
			return n;
		default:
			printErrorAndExit();
			return n;
		}
	}
	
	/**
	 * T -> FT'
	 */
	private static int parseT() {
		int n = parseF();
		return parseTPrime(n);
	}
	
	/**
	 * T' -> *FT' | /FT' | lambda
	 * Parses multiplication or division.
	 * @param int n - number passed through from ParseF()
	 */
	private static int parseTPrime(int n) {
		
		char temp = tokens[curr];
		int n2 = 0;
		
		switch(temp){
		case '*':
			curr++;
			n2 = parseF();
			return parseTPrime(n * n2);
		case '/':
			curr++;
			n2 = parseF();
			return parseTPrime(n / n2);
		case '+':
		case '-':
		case ')':
		case '$':
			break;
		default:
			printErrorAndExit();
		}
		return n;
	}
	
	/**
	 * F -> (E) | n
	 */
	private static int parseF() {
		
		char temp = tokens[curr];
		int n, n2;
		
		if(temp == '(') {
			curr++;
			n = parseE();
			if(tokens[curr] == ')') {
				curr++;
				return n;
			}else if(tokens[curr] == '('){	// if there are multiple parentheses in a row
				curr++;
				return n + parseF();		// this continues the recursive parsing
			}else{
				printErrorAndExit(2);
				return n;
			}
		} else {	// continues parsing the int until interrupted by a non-number
			if(Character.isDigit(temp)){
				boolean numberIsOver = false;
				String fullNumber = "";
				int i = curr;
				while(!numberIsOver && tokens.length > i){
					if(Character.isDigit(tokens[i])){
							fullNumber += tokens[i];
							curr++;
					}else{
						numberIsOver = true;
					}
					i++;
				}
				return Integer.parseInt(fullNumber);
			} else{		// occurs if the symbol is neither ( nor an int
				printErrorAndExit();
			}
		}
		return -1;
	}
	
	/**
	 * Prints an error to the terminal and exits the program.
	 * Call this when the input is empty or is proven invalid during parsing.
	 * 
	 * @param errorNumber
	 * 1: will also print a message about valid inputs.
	 * 2: will also print a message about closing parentheses.
	 * Other: default error message
	 */
	private static void printErrorAndExit(int errorNumber) {
		System.out.println("Error!");
		switch(errorNumber) {
		case 1:
			System.out.println("You must pass an argument into this program." +
					"\nExample: java LL1 100-((2*(5-3))-2)+3");
			break;
		case 2:
			System.out.println("Must close parentheses.");
			break;
		default: 
			System.out.println("Invalid input. Problem cannot be parsed.");
			break;
		}
		System.out.println("Exiting.");
		System.exit(-1);
	}
	
	/*
	 * No-args version of printErrorAndExit.
	 * Prints a default error message and exits the program.
	 */
	private static void printErrorAndExit(){
		printErrorAndExit(0);
	}
}
