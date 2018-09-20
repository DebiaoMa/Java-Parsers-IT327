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
	 * E	-> TE'
	 * E'	-> +TE' | -TE' | lambda
	 * T	-> FT'
	 * T'	-> *FT' | /FT' | lambda
	 * F	-> (E) 	| n
	 */

	public static void main(String[] args) {	
		// Declarations
		curr = 0;
		
		/**
		 * Ends program if there are no args. 
		 * If there are, it adds an end token ($) 
		 * and creates a char[] array from args[0].
		 */
		if(args.length == 0) {
			printErrorAndExit(1);
		}else {
            String temp = args[0];
            temp = temp.replaceAll("\"", "");	// removes quotation marks
            temp = temp + "$";					// adds end token
			tokens = temp.toCharArray();		// turns input into char[]
		}
		System.out.println("1");
		int answer = parseE();	// begins recursion
		
		// double-checks to make sure the end token was actually reached.
		if(tokens[curr] == '$') {
			System.out.println("Success! Input is valid. Answer: " + answer);
		}else {
			printErrorAndExit(0);
		}
	}
	
	/**
	 * E -> TE'
	 * Starting token.
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
		int numT = 0;
		
		switch(temp){
		case '+':
			curr++;
			numT = parseT();
			return parseEPrime(n + numT);
		case '-':
			curr++;
			numT = parseT();
			return parseEPrime(n - numT);
		case '$':
		case ')':
			return n;
		default:
			System.out.println("2");
			printErrorAndExit(0);
			return n;
		}
	}
	
	/**
	 * T -> FT'
	 */
	private static int parseT() {
		int n = parseF();
		return parseTPrime(parseF());
	}
	
	/**
	 * T' -> *FT' | /FT' | lambda
	 * Parses multiplication or division.
	 * @param int n - number passed through from ParseF()
	 */
	private static int parseTPrime(int n) {
		
		char temp = tokens[curr];
		int numF = 0;
		
		switch(temp){
		case '*':
			curr++;
			numF = parseF();
			return parseTPrime(n * numF);
		case '/':
			curr++;
			numF = parseF();
			return parseTPrime(n / numF);
		case '+':
		case '-':
		case ')':
		case '$':
			break;
		default:
			System.out.println("3");
			printErrorAndExit(0);
		}
		return n;
	}
	
	/**
	 * F -> (E) | n
	 * Parses tokens within () or parses a number.
	 */
	private static int parseF() {
		
		char temp = tokens[curr];
		int numE;
		
		if(temp == '(') {
			curr++;
			numE = parseE();
			if(tokens[curr] == ')') {
				curr++;
				return numE;
			}else if(tokens[curr] == '('){	// if there are multiple parentheses in a row
				curr++;						// this continues the recursive parsing
				return numE + parseF();
			}else{
				printErrorAndExit(2);
				return numE;
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
			} else{		// occurs if token is neither ( nor an int
				System.out.println("4");
				printErrorAndExit(0);
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
			System.out.println("You must close parentheses.");
			break;
		default: 
			System.out.println("Invalid input. Problem cannot be parsed.");
			break;
		}
		System.out.println("Exiting.");
		System.exit(-1);
	}
}
