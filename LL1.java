/**
 * @author Jerry Binder jmbind1@ilstu.edu
 * 
 * Recursive-descent parser using a given LL(1) parsing table
 * All code is property of Jerry Binder.
 * Grammar provided by Dr Chung-Chih Li
 */

public class LL1 {
	private static char[] tokens;	// array containing all tokens
	private static int curr;		// current index in tokens
	
	/* Grammar:
	 * E -> TE'
	 * E' -> +TE' | -TE' | lambda
	 * T -> FT'
	 * T' -> *FT' | /FT' | lambda
	 * F -> (E) | n
	 * 
	 * E = expression
	 * T = term
	 * F = factor
	 * lambda = empty string
	 */

	public static void main(String[] args) {	
		// Declarations
		curr = 0;
		
		// Ends program if there are no args.		
		if(args.length == 0) {
			printErrorAndExit(1);
		}else {
            String temp = args[0];
            temp = temp.replaceAll("\"", "");	// removes quotation marks if present
            temp = temp + "$";
			tokens = temp.toCharArray();
		}

		int answer = parseE();
		
		if(tokens[curr] == '$') {
			System.out.println("Success! Input is valid. Answer: " + answer);
		}else {
			System.out.println("Failure! Input is invalid.");
		}
	}
	
	/**
	 * E -> TE'
	 */
	private static int parseE() {
		int n = parseT();
		return parseEPrime(n);
	}
	
	/**
	 * E' -> +TE' | -TE' | lambda
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
			printErrorAndExit(0);
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
			printErrorAndExit(0);
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
			}else if(tokens[curr] == '('){
				curr++;
				return n + parseF();		// this may be a problem
			}else{
				printErrorAndExit(2);
				return n;
			}
		} else {
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
			} else{
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
			System.out.println("You must pass an argument into this program.\nExample: java LL1 100-((2*(5-3))-2)+3");
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
}
