/**
 * @author Jerry Binder jmbind1@ilstu.edu
 * 
 * LR(1) parser using a given grammar and parsing table.
 * All code is property of Jerry Binder.
 * Grammar and parsing table provided by Dr Chung-Chih Li.
 */

import java.util.Stack;

/*
 * Grammar:
 * E -> E + T | E - T | T
 * T -> T * F | T / F | F
 * F -> (E)   | n		(where n is any positive integer)
 */
public class LR1
{
	/*
	 * Declarations
	 */
	private static String input;		// String containing user input.
	private static String token;		// Current token.
	private static int curr;			// Current index in input.
	private static int state;			// Current state of the parser.
	private static boolean complete;	// If true, parsing is complete.
	private static Stack<String> stack;	// Parsing stack.
	
	public static void main(String[] args)
	{
        	stack = new Stack<String>();
        	input = token = "";
        	curr = 0;
        	state = -1;
        	complete = false;

		/*
		 * Ends program if there are no args. 
		 * If there are, it adds an end symbol ($) 
		 * and creates a char[] array from args[0].
		 */		
		if(args.length == 0)
			printErrorAndExit(1);
		
		input = args[0];
        	input = input.replaceAll("\"", "");		// removes quotation marks
        	input = input + "$";					// adds end symbol
		push("0");
		setToken();
		
		while(!complete)
			parse();
		
		// TODO print answer
		// TODO add math logic
		// TODO make Nonterminal object hold values of nonterminals (see assignment pdf)
				// should I make the stack a stack of nonterminals with values, then print the value of E at the end?
		/*
		 * Whenever the parser obtains a nonterminal symbol from some reduction, 
		 * its value should be computed. Thus you may design a certain data structure 
		 * for every nonterminal symbol to store its value.
		 * E.g. the E in the stack has value 317 at the end.
		 */
	}
	
	class Nonterminal{
		public char symbol;
		public int value;
		
		Nonterminal(char symbol, int value){
			this.symbol = symbol;
			this.value = value;
		}
	};
	
	private static String pop(){
		String result =	stack.pop();
		System.out.println("Popped: " + result + 
				"\nStack:" + stack.toString() + 
				"\nToken:" + token + 
				"\nInput String: " + input.substring(curr) + "\n\n");
		return result;
	}
	
	private static void push(String s){
		stack.push(s);
		System.out.println("Pushed: " + s +
				"\nStack:" + stack.toString() + 
				"\nToken: " + token + 
				"\nInput String: " + input.substring(curr) + "\n\n");
	}
	
	private static boolean isNonterminal(char c){
		switch(c){
			case '+':
			case '-':
			case '*':
			case '/':
			case '(':
			case ')':
			case '$':
				return true;
			default:
				return false;
		}
	}
	
	private static void setToken(){
		if(isNonterminal(input.charAt(curr)))
		{
			token = "" + input.charAt(curr);
			curr++;
		}
		else if(Character.isDigit(input.charAt(curr)))
		{
			token = "";
			while(Character.isDigit(input.charAt(curr)))
			{
				token += input.charAt(curr);
				curr++;
			}
		}
		else
		{
			printErrorAndExit();
		}
	}
	
	private static boolean tokenIs(char c){
		if(token.charAt(0)==c)
			return true;
		return false;
	}
	
	private static void parse(){
		String temp = stack.peek();
		
		if(Character.isDigit(temp.charAt(0)))
			state = Integer.parseInt(temp);
		else
			state = -1;
		
		switch(state)
		{
			case 0:
				state0();
				break;
			case 1:
				state1();
				break;
			case 2:
				state2();
				break;
			case 3:
				state3();
				break;
			case 4:
				state4();
				break;
			case 5:
				state5();
				break;
			case 6:
				state6();
				break;
			case 7:
				state7();
				break;
			case 8:
				state8();
				break;
			case 9:
				state9();
				break;
			case 10:
				state10();
				break;
			case 11:
				state11();
				break;
			default:
				printErrorAndExit();
		}
	}
	
	private static void pushE(){
        // E -> E + T | E - T | T
		String temp = stack.peek();

		push("E");
		
		if(temp.equals("0"))
			push("1");
		else if(temp.equals("4"))
			push("8");
		else
			printErrorAndExit();
	}
	
	private static void pushT(){
        // T -> T * F | T / F | F
		String temp = stack.peek();

		push("T");
		
		if(temp.equals("0") || temp.equals("4"))
			push("2");
		else if(temp.equals("6"))
			push("9");
		else
			printErrorAndExit();
	}
	
	private static void pushF(){
        // F -> (E)   | n		(where n is any positive integer)
		String temp = stack.peek();

		push("F");
		
		if(temp.equals("0") || temp.equals("4") || temp.equals("6"))
			push("3");
		else if(temp.equals("7"))
			push("10");
		else
			printErrorAndExit();
	}
	
	private static void state0(){
		char temp = token.charAt(0);
		
		if(Character.isDigit(temp)){
			//shift 5
			push(token);
			push("5");
			setToken();
		}else if(temp == '('){
			//shift 4
			push(token);
			push("4");
			setToken();
		}else{
			printErrorAndExit();
		}
	}
	
	private static void state1(){
		char temp = token.charAt(0);
		
		switch(temp){
			case '+':
				//shift 6
				push(token);
				push("6");
				setToken();
				break;
			case '-':
				//shift 6
				push(token);
				push("6");
				setToken();
				break;
			case '$':
				complete = true;
				break;
			default:
				printErrorAndExit();
		}
	}
	
	private static void state2(){
		char temp = token.charAt(0);
		
		switch(temp){
			case '+':
				//E->T
				pop();
				if(pop().charAt(0)!='T'){
					printErrorAndExit();
				}
				pushE();
				break;
			case '-':
				//E->T
				pop();
				if(pop().charAt(0)!='T'){
					printErrorAndExit();
				}
				pushE();
				break;
			case '*':
				//shift 7
				push(token);
				push("7");
				setToken();
				break;
			case '/':
				//shift 7
				push(token);
				push("7");
				setToken();
				break;
			case ')':
			case '$':
				//E->T
				pop();
				if(pop().charAt(0)!='T'){
					printErrorAndExit();
				}
				pushE();
				break;
			default:
				printErrorAndExit();
		}
	}
	
	private static void state3(){
		char temp = token.charAt(0);
		
		switch(temp){
			case '+':
				//T->F
				pop();
				if(pop().charAt(0)!='F')
					printErrorAndExit();
				pushT();
				break;
			case '-':
				//T->F
				pop();
				if(pop().charAt(0)!='F')
					printErrorAndExit();
				pushT();
				break;
			case '*':
				//T->F
				pop();
				if(pop().charAt(0)!='F')
					printErrorAndExit();
				pushT();
				break;
			case '/':
				//T->F
				pop();
				if(pop().charAt(0)!='F')
					printErrorAndExit();
				pushT();
				break;
			case ')':
			case '$':
				//T->F
				pop();
				if(pop().charAt(0)!='F')
					printErrorAndExit();
				pushT();
				break;
			default:
				printErrorAndExit();
		}
	}
	
	private static void state4(){
		char temp = token.charAt(0);
		
		if(Character.isDigit(temp)){
			//shift 5
			push(token);
			push("5");
			setToken();
		}else if(temp == '('){
			//shift 4
			push(token);
			push("4");
			setToken();
		}else
			printErrorAndExit();
	}
	
	private static void state5(){
		char temp = token.charAt(0);
		
		switch(temp){
			case '+':
				//F->n
				pop();
				if(!Character.isDigit(pop().charAt(0)))
				{
					printErrorAndExit();
				}
				pushF();
				break;
			case '-':
				//F->n
				pop();
				if(!Character.isDigit(pop().charAt(0)))
				{
					printErrorAndExit();
				}
				pushF();
				break;
			case '*':
				//F->n
				pop();
				if(!Character.isDigit(pop().charAt(0)))
				{
					printErrorAndExit();
				}
				pushF();
				break;
			case '/':
				//F->n
				pop();
				if(!Character.isDigit(pop().charAt(0)))
				{
					printErrorAndExit();
				}
				pushF();
				break;
			case ')':
			case '$':
				//F->n
				pop();
				if(!Character.isDigit(pop().charAt(0)))
				{
					printErrorAndExit();
				}
				pushF();
				break;
			default:
				printErrorAndExit();
		}
	}
	
	private static void state6(){
		char temp = token.charAt(0);
		
		if(Character.isDigit(temp)){
			//shift 5
			push(token);
			push("5");
			setToken();
		}else if(temp == '('){
			//shift 4
			push(token);
			push("4");
			setToken();
		}else{
			printErrorAndExit();
		}
	}
	
	private static void state7(){
		char temp = token.charAt(0);
		
		if(Character.isDigit(temp)){
			//shift 5
			push(token);
			push("5");
			setToken();
		}else if(temp == '('){
			//shift 4
			push(token);
			push("4");
			setToken();
		}else{
			printErrorAndExit();
		}
	}
	
	private static void state8(){
		char temp = token.charAt(0);
		
		switch(temp){
			case '+':
				//shift 6
				push(token);
				push("6");
				setToken();
				break;
			case '-':
				//shift 6
				push(token);
				push("6");
				setToken();
				break;
			case ')':
				//shift 11
				push(token);
				push("11");
				setToken();
				break;
			default:
				printErrorAndExit();
		}
	}
	
	// TODO
	private static void state9(){
		if(tokenIs('+') || tokenIs(')') || tokenIs('$'))
		{
			//E->E+T
			pop();
			if(pop().charAt(0)!='T')
			{
				printErrorAndExit();
			}
			pop();
			if(pop().charAt(0)!='+')
			{
				printErrorAndExit();
			}
			pop();
			if(pop().charAt(0)!='E')
			{
				printErrorAndExit();
			}
			pushE();
		}
		else if(tokenIs('*'))
		{
			//shift 7
			push(token);
			push("7");
			setToken();
		}
		else
			printErrorAndExit();
	}
	
	// TODO
	private static void state10(){
		if(tokenIs('+') || tokenIs('*') || tokenIs(')') || tokenIs('$'))
		{
			//T->T*F
			pop();
			if(pop().charAt(0)!='F')
			{
				printErrorAndExit();
			}
			pop();
			if(pop().charAt(0)!='*')
			{
				printErrorAndExit();
			}
			pop();
			if(pop().charAt(0)!='T')
			{
				printErrorAndExit();
			}
			pushT();
		}
		else
			printErrorAndExit();
	}
	
	private static void state11(){
		char temp = token.charAt(0);
		
		switch(temp){
			case '+':
			case '-':
			case '*':
			case '/':
			case ')':
			case '$':
				//F->(E)
				pop();
				if(pop().charAt(0)!=')')
				{
					printErrorAndExit();
				}
				pop();
				if(pop().charAt(0)!='E')
				{
					printErrorAndExit();
				}
				pop();
				if(pop().charAt(0)!='(')
				{
					printErrorAndExit();
				}
				pushF();
				break;
			default:
				printErrorAndExit();
		}
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
	
	/**
	 * No-args version of printErrorAndExit.
	 * Prints a default error message and exits the program.
	 */
	private static void printErrorAndExit(){
		printErrorAndExit(0);
	}
}
