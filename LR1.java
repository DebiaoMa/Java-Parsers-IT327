/**
 * @author Jerry Binder jmbind1@ilstu.edu
 * 
 * LR(1) parser using a given grammar and parsing table.
 * All code is property of Jerry Binder.
 * Grammar and parsing table provided by Dr Chung-Chih Li.
 */

import java.util.Stack;

/**
 * Data structure that contains a symbol,
 * the state it will take the parser to,
 * and its value (if applicable).
 */
class ParsingItem{
	public String symbol;
	public int state;
	public int value;
	public boolean hasValue;	// Prevents default value from being used in math.
	
	public ParsingItem(String symbol, int state, int value, boolean hasValue){
		this.symbol = symbol;
		this.state = state;
		this.value = value;
		this.hasValue = hasValue;
	}
	
	@Override
	public String toString() {
		if(hasValue)
			return "[" + this.symbol + ":" + this.state + "]" + "=" + this.value;
		else
			return "[" + this.symbol + ":" + this.state + "]";
	}
};

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
	private static String token;		// Current token from the input.
	private static int curr;			// Current index in input.
	// private static int state;			// Current state of the parser.
	private static boolean complete;	// If true, parsing is complete.
	private static Stack<ParsingItem> stack;	// Parsing stack.
	
	public static void main(String[] args)
	{
    	stack = new Stack<ParsingItem>();
    	input = token = "";
    	curr = 0;
    	// state = 0;
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
        
		
        push(new ParsingItem("-", 0, 0, false));
		setToken();
		
		while(!complete)
			parse();
		
		System.out.println("Success! Answer = " + stack.peek().value);
		
		/*
		 * Whenever the parser obtains a nonterminal symbol from some reduction, 
		 * its value should be computed. Thus you may design a certain data structure 
		 * for every nonterminal symbol to store its value.
		 * E.g. the E in the stack has value 317 at the end.
		 */
	}
	
	/**
	 * Convenience method to get current state.
	 */
	private static int getState(){
		return stack.peek().state;
	}
	
	/**
	* Pops ParsingItem from the stack and prints the status of the parser.
	*/
	private static ParsingItem pop(){
		ParsingItem result = stack.pop();
		System.out.println("Popped:" + result.toString() + 
				"\nStack:" + stack.toString() + 
				"\nToken:" + token + 
				"\nInput String:" + input.substring(curr) + 
				"\nState:" + getState() + "\n");
		return result;
	}
	
	/**
	* Pushes ParsingItem to the stack and prints the status of the parser.
	*/
	private static void push(ParsingItem s){
		stack.push(s);
		System.out.println("Pushed:" + s.toString() +
				"\nStack:" + stack.toString() + 
				"\nToken:" + token + 
				"\nInput String:" + input.substring(curr) + 
				"\nState:" + getState() + "\n");
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
	
	/**
	 * Retrieves the next token from the input String and assigns
	 * variable "token" to it. In the case of an integer, it will
	 * iterate through the string until it has the full number.
	 */
	private static void setToken(){
		try{
			if(isNonterminal(input.charAt(curr))){
				token = "" + input.charAt(curr);
				curr++;
			}else if(Character.isDigit(input.charAt(curr))){
				boolean numberIsOver = false;
				String fullNumber = "";
				int i = curr;
				while(!numberIsOver && input.length() > i){
					if(Character.isDigit(input.charAt(i))){
						fullNumber += input.charAt(i);
						curr++;
					}else{
						numberIsOver = true;
					}
					i++;
				}
				token = fullNumber;
			}else if(input.charAt(curr) == '$'){
				
			}else{
				printErrorAndExit();
			}
		}catch(StringIndexOutOfBoundsException e){
			
		}
		
		token.replaceAll(" ", "");
		System.out.println("Set token to=" + token + " at " + getState());
	}
	
	/**
	 * Reads the state of the next item on the stack and brings the parser
	 * to the designated state number.
	 */
	private static void parse(){
		switch(getState())
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
	
	/**
	 * E -> E + T | E - T | T
	 */
	private static void pushE(int itemValue, boolean hasValue){
		String symbol = "E";
		int itemState = -1;
		int currState = stack.peek().state;
		if(currState == 0)
			itemState = 1;
		else if(currState == 4)
			itemState = 8;
		else
			printErrorAndExit();
		
		push(new ParsingItem(symbol, itemState, itemValue, hasValue));
	}
	
	/**
	 * T -> T * F | T / F | F
	 */
	private static void pushT(int itemValue, boolean hasValue){
		String symbol = "T";
		int itemState = -1;
		int currState = stack.peek().state;
		
		if(currState == 0 || currState == 4)
			itemState = 2;
		else if(currState == 6)
			itemState = 9;
		else
			printErrorAndExit();
		
		push(new ParsingItem(symbol, itemState, itemValue, hasValue));
	}
	
	/**
	 * F -> (E)   | n		(where n is any positive integer)
	 */
	private static void pushF(int itemValue, boolean hasValue){
		String symbol = "F";
		int itemState = -1;
		int currState = stack.peek().state;
		
		if(currState == 0 || currState == 4 || currState == 6)
			itemState = 3;
		else if(currState == 7)
			itemState = 10;
		else
			printErrorAndExit();
		
		push(new ParsingItem(symbol, itemState, itemValue, hasValue));
	}
	
	/**
	 * If token == n then shift 5.
	 * If token == ( then shift 4.
	 */
	private static void state0(){
		char temp = token.charAt(0);
		
		if(Character.isDigit(temp)){
			push(new ParsingItem("n", 5, Integer.parseInt(token), true));
		}else if(temp == '('){
			push(new ParsingItem("(", 4, 0, false));
		}else{
			printErrorAndExit();
		}
		
		setToken();
	}
	
	/**
	 * If token == + or - then shift 6.
	 * If token == $ then complete = true.
	 */
	private static void state1(){
		char temp = token.charAt(0);
		
		switch(temp){
			case '+':
			case '-':
				push(new ParsingItem(token, 6, 0, false));
				break;
			case '$':
				complete = true;
				break;
			default:
				printErrorAndExit();
		}
		
		if(temp != '$')
			setToken();
	}
	
	/**
	 * If token == + or - or ) or $ then E->T.
	 * If token == * or / then shift 7.
	 */
	private static void state2(){
		char temp = token.charAt(0);
		
		switch(temp){
			case '+':
			case '-':
			case ')':
			case '$':
				ParsingItem T = pop();
				pushE(T.value, T.hasValue);
				break;
			case '*':
			case '/':
				push(new ParsingItem(token, 7, 0, false));
				setToken();
				break;
			default:
				printErrorAndExit();
		}
	}
	
	/**
	 * If token == + or - or * or / or ) or $ then T->F.
	 */
	private static void state3(){
		char temp = token.charAt(0);
		
		switch(temp){
			case '+':
			case '-':
			case '*':
			case '/':
			case ')':
			case '$':
				ParsingItem F = pop();
				pushT(F.value, F.hasValue);
				break;
			default:
				printErrorAndExit();
		}
		
		// setToken();
	}
	
	/**
	 * If token == n then shift 5.
	 * If token == ( then shift 4.
	 */
	private static void state4(){
		char temp = token.charAt(0);
		
		if(Character.isDigit(temp)){
			push(new ParsingItem("n", 5, Integer.parseInt(token), true));
		}else if(temp == '('){
			push(new ParsingItem("(", 4, 0, false));
		}else{
			printErrorAndExit();
		}
		
		// setToken(); //TODO
	}
	
	/**
	 * If token == + or - or * or / or ) or $ then calculate value of F and push it.
	 */
	private static void state5(){
		char temp = token.charAt(0);
		if(Character.isDigit(temp)){
			setToken();
			temp = token.charAt(0);
		}
		
		switch(temp){
			case '+':
			case '-':
			case '*':
			case '/':
			case ')':
			case '$':
				ParsingItem n = pop();
				pushF(n.value, n.hasValue);
				break;
			default:
				printErrorAndExit();
		}
	}
	
	/**
	 * If token == n then shift 5.
	 * If token == ( then shift 4.
	 */
	private static void state6(){
		char temp = token.charAt(0);
		if(Character.isDigit(temp)){
			push(new ParsingItem("n", 5, Integer.parseInt(token), true));
		}else if(temp == '('){
			push(new ParsingItem("(", 4, 0, false));
		}else{
			printErrorAndExit();
		}
		
		if(temp != '$')
			setToken();
	}
	
	/**
	 * If token == n then shift 5.
	 * If token == ( then shift 4.
	 */
	private static void state7(){
		state6();		
		// for my implementation these methods are identical
	}
	
	/**
	 * If token == + or - then shift 6.
	 * If token == ) then shift 11.
	 */
	private static void state8(){
		char temp = token.charAt(0);
		switch(temp){
			case '+':
			case '-':
				push(new ParsingItem(token, 6, 0, false));
				setToken();
				break;
			case ')':
				push(new ParsingItem(token, 11, 0, false));
				setToken();
				break;
			default:
				printErrorAndExit();
		}
	}
	
	/**
	 * If token == * or / then shift 7.
	 * If token == + or - or ) or $ 
	 * then perform addition or subtraction
	 * and push resulting E to stack.
	 */
	private static void state9(){
		char temp = token.charAt(0);
		
		int E, T, result = 0;
		char operator;
		
		switch(temp){
			case '*':
			case '/':
				push(new ParsingItem(token, 7, 0, false));
				setToken();
				break;
			case '+':
			case '-':
			case ')':
			case '$':
				T = pop().value;
				operator = pop().symbol.charAt(0);
				E = pop().value;
				
				if(operator == '+')
					result = E + T;
				else if(operator == '-')
					result = E - T;
				
				pushE(result, true);
				break;
			default:
				printErrorAndExit();
		}
		
//		if(temp != '$')
//			setToken();
	}
	
	/**
	 * If token == + or - or * or / or ) or $ 
	 * then perform multiplication or division 
	 * and push resulting T to stack.
	 */
	private static void state10(){
		char temp = token.charAt(0);
		int F = pop().value;
		char operator = pop().symbol.charAt(0);
		int T = pop().value;
		int result = 0;
		
		if(operator == '*'){
			result = F * T;
		}else if(operator == '/'){
			result = F / T;
		}else{
			printErrorAndExit();
		}
		
		pushT(result, true);
		
		if(temp != '$')
			setToken();
	}
	
	/**
	 * If token == + or - or * or / or ) or $
	 * then find value in parentheses and push it as F.
	 */
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
				if(pop().symbol.charAt(0)!=')')
				{
					printErrorAndExit(2);
				}
				ParsingItem E = pop();
				if(E.symbol.charAt(0)!='E')
				{
					printErrorAndExit();
				}
				if(pop().symbol.charAt(0)!='(')
				{
					printErrorAndExit();
				}
				pushF(E.value, true);
				break;
			default:
				printErrorAndExit();
		}
		
		if(temp != '$')
			setToken();
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
