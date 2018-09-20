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
            System.out.println("Temp = " + temp);
			tokens = temp.toCharArray();
			System.out.println("Length = " + tokens.length + "tokens = " + temp);
		}
		
		for(int i = 0; i < tokens.length; i++){
			System.out.println("Token at index " + i + " is " + tokens[i]);
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
		System.out.println("Parsing E");
		int n = parseT();
		return parseEPrime(n);
	}
	
	/**
	 * E' -> +TE' | -TE' | lambda
	 */
	private static int parseEPrime(int n) {
		System.out.println("Parsing E'");
		
		char temp = tokens[curr];
		
		int n2 = 0;
		switch(temp){
		case '+':
			System.out.println("Found + at index " + curr);
			curr++;
			n2 = parseT();
			return parseEPrime(n + n2);
		case '-':
			System.out.println("Found -");
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
		System.out.println("Parsing T");
		int n = parseF();
		return parseTPrime(n);
	}
	
	/**
	 * T' -> *FT' | /FT' | lambda
	 */
	private static int parseTPrime(int n) {
		System.out.println("Parsing T'");
		System.out.println("curr = " + curr + " size = " + tokens.length + " n = " + n);
		
		char temp = tokens[curr];
		
		int n2 = 0;
		switch(temp){
		case '*':
			System.out.println("Found * at index " + curr);
			curr++;
			n2 = parseF();
			return parseTPrime(n * n2);
		case '/':
			System.out.println("Found - at index " + curr);
			curr++;
			n2 = parseF();
			return parseTPrime(n / n2);
		case '+':
		case '-':
		case ')':
		case '$':
			break;
		default:
			System.out.println("Error on ParseTPrime. n = " + n + " and n2 = " + n2);
			printErrorAndExit(0);
		}
		return n;
	}
	
	/**
	 * F -> (E) | n
	 */
	private static int parseF() {
		System.out.println("Parsing F");
		
//		char temp = '?';
//		try{
//			temp = tokens[curr];
//		}catch(Exception e){
//			printResultAndExit(-1);
//		}
		
		char temp = tokens[curr];
		
		int n, n2;
		if(temp == '(') {
			System.out.println("Found ( at index " + curr);
			curr++;
			n = parseE();
			if(tokens[curr] == ')') {
				System.out.println("Found ) at index " + curr);
				curr++;
				return n;
			}else {
				printErrorAndExit(2);
				return n;
			}
		} else {
			if(Character.isDigit(temp)){
				System.out.println("isDigit = true");
				boolean numberIsOver = false;
				String fullNumber = "";
				int i = curr;
				while(!numberIsOver && tokens.length > i){
					if(Character.isDigit(tokens[i])){
							fullNumber += tokens[i];
							System.out.println(fullNumber);
					}else{
						numberIsOver = true;
					}
					i++;
				}
				System.out.println("Found " + fullNumber + " at index " + curr);
				curr++;
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
		default: 
			System.out.println("Invalid input. Problem cannot be parsed.");
			break;
		}
		System.out.println("Exiting.");
		System.exit(-1);
	}
	
	public static void printResultAndExit(int n){
		System.out.println("Success! Answer = " + n);
		System.exit(0);
	}
}
