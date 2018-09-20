/**
 * @author Jerry Binder jmbind1@ilstu.edu
 * 
 * Recursive-descent parser using a given LL(1) parsing table
 * All code is property of Jerry Binder.
 * Grammar provided by Dr Chung-Chih Li
 */

import java.util.Stack;

public class LL1 {
	private static char[] tokens;
	private static int curr;
	private static Stack<Character> operators;
	private static Stack<Integer> numbers;
	
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
		numbers = new Stack<Integer>();
		operators = new Stack<Character>();
		curr = 0;
		
		// Ends program if there are no args.		
		if(args.length == 0) {
			printErrorAndExit(1);
		}else {
			tokens = args[0].toCharArray();
		}
		
		parseE();
		
		if(tokens[curr] == '$') {
			System.out.println("Success! Input is valid. Answer: ");
		}else {
			System.out.println("Failure! Input is invalid.");
		}
		
		// If all goes well in parsing, you should have a stack for numbers and operators here.
		// TODO: build parsing tree, solve problem, print answer
	}
	
	/**
	 * E -> TE'
	 */
	private static void parseE() {
		parseT();
		parseEPrime();
	}
	
	/**
	 * E' -> +TE' | -TE' | lambda
	 */
	private static void parseEPrime() {
		char temp = tokens[curr];
		if(temp == '+') {
			operators.push(temp);
			curr++;
			parseT();
			parseEPrime();
		}else if(temp == '-') {
			parseT();
			parseEPrime();
		}else if(temp == '$' || temp == ')'){
			// TODO: should anything go here? probably not.
		}else {
			printErrorAndExit(0);
		}
	}
	
	/**
	 * T -> FT'
	 */
	private static void parseT() {
		parseF();
		parseTPrime();
	}
	
	/**
	 * T' -> *FT' | /FT' | lambda
	 */
	private static void parseTPrime() {
		char temp = tokens[curr];
		if(temp == '*' || temp == '/') {
			operators.push(temp);
			curr++;
			parseT();
			parseEPrime();
		}else {
			// TODO: what do I do for lambda?
		}
	}
	
	/**
	 * F -> (E) | n
	 */
	private static void parseF() {
		char temp = tokens[curr];
		if(temp == '(') {
			operators.push(temp);
			curr++;
			parseE();
			if(tokens[curr] == ')') {
				curr++;
			}else {
				printErrorAndExit(0);
			}
		}else if(Character.isDigit(temp)) {
			/* The following code checks concurrent symbols to make sure the full number is parsed
			 * rather than only its leftmost digit.
			 */
			bool numberIsOver = false;
			String fullNumber = "" + temp;
			while(!numberIsOver) {
				int n = 1;
				if(Character.isDigit(tokens[curr+n]){
					fullNumber += tokens[curr+n]);
				}else {
					numberIsOver = true;
				}
			}
			numbers.push(Integer.parseInt(fullNumber));
			curr++;
		}
	}
	
	/**
	 * Prints an error to the terminal and exits the program.
	 * Call this when the input is empty or is proven invalid during parsing.
	 * errorNumber == 1 will also print a message about valid inputs.
	 * @param errorNumber
	 */
	private static void printErrorAndExit(int errorNumber) {
		System.out.println("Error!");
		switch(errorNumber) {
		case 1:
			System.out.println("You must pass an argument into this program.\nExample: java LL1 100-((2*(5-3))-2)+3");
		case 2: 
			System.out.println("Invalid input. Problem cannot be parsed.")
		}
		System.out.println("Exiting.");
		System.exit(-1);
	}
}
