import java.io.*;
import java.util.*;
public class ManualSearch {
  static double [] winDistribution;
  static String s = "nnnnn";
  static double weight [];
  static double max = 0.0;
  static int[][] freq = new int[5][26];
  static ArrayList<Character> greenLet = new ArrayList<Character>();
  static ArrayList<String> wordsThatNoWork = new ArrayList<String>();
	static String[] counts = new String[26];
  static String officalAns = "";
  //if a letter is yellow at a given position it cannot be in that position, this array accounts for that
  static int[][] removeYellow;
  static ArrayList<Character> noUse = new 
  ArrayList<Character>();
  static int firstGuess = 0;
  static ArrayList<Character> yellow = new ArrayList<Character>();
	static char[] green = new char[5];
  static int noFinish = 0;
	public static final String GREEN = "\u001B[32m";
	public static final String RED = "\u001B[31m";
	public static final String BLACK = "\u001B[30m";
  public static final String BLACK_B = "\u001B[40m";
	public static final String RED_B = "\u001B[41m"; 
	public static final String GREEN_B = "\u001B[42m";
	public static final String RESET = "\u001B[0m";
	public static final String YELLOW_B = "\u001B[43m";
	
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    //for auto solve one word at a time
    //officalAns = in.next();
		BufferedReader br = null;
		BufferedReader br2 = null;
    try {	
    	br = new BufferedReader(new FileReader("words.txt"));
			ArrayList<String> words = new ArrayList<String>();
      //store original word list
      ArrayList<String> cpy = new ArrayList<String>();
      ArrayList<String> answers = new ArrayList<String>();
			String word = br.readLine();
			while (word != null) {
				words.add(word);
				word = br.readLine();
			}
    
			br2 = new BufferedReader(new FileReader("answers.txt"));
		  
			String ans = br2.readLine();
			while (ans != null) {
				answers.add(ans);
				ans = br2.readLine();
			}
      Collections.sort(words);
      //declaring win distribution array

      winDistribution = new double [6];

      Collections.sort(answers);

      //to restore answers array after it is altered
      cpy.addAll(answers);

      //adding all words we want to test in auto solve
     
      
      //for loop testing the entire data set and creating a score distribution
      
      //resetting all arrays for new word
      
      removeYellow = new int[5][26];
     
      Arrays.fill(green, '0');
      
      //printing the guess and creating it
      calculateFreq(answers);
     String first = "";
      
      first = "salet";

      System.out.println(first);
	  	for (int i = 0; i < 6; i++){

	      //for manual solve
        String s = in.next();

        int cnt = 0;
        //creating a string that tells us whether a letter cannot be used, is in the right position, or exists in the word but is in the wrong position
	      for (int j = 0; j < 5; j++){
          if (s.charAt(j) == 'n'){
	          noUse.add(first.charAt(j));
          }else if (s.charAt(j) == 'y') {
						yellow.add(first.charAt(j));
            int c = (int)first.charAt(j);
            removeYellow[j][c-97] = 1;
					 }else {
						green[j] = first.charAt(j);
            greenLet.add(first.charAt(j));
            cnt++;
					}	
	  		}
        //if all green letters reset the setup
        if (cnt == 5 || i == 5){
          if (officalAns.equals(first)){
          winDistribution[i]++;
          }else {
          noFinish++;
          wordsThatNoWork.add(officalAns);
          }
          //System.out.println(first+" is this right");
          answers.clear();
          answers.addAll(cpy);
          
          break;
        }

        //changing the word list to reflect what letters can and cannot be used etc
        ArrayList<String> temp = new ArrayList<String>();
        temp.addAll(calculateWord(answers));
        //copying new calculated words into answers
        answers.clear();
        answers.addAll(temp);
        calculateFreq(answers);
        //creating and printing the guess.
        answers.remove(first);
        first = guess(answers);

        
      }
      
			}catch (IOException e) {
				e.printStackTrace();
		} 	
  
}
  //doesn't allow copies of letters on the first guess only
	public static boolean uniqueCharacters(String str) {
    //if its not the first guess we want to be able to see duplicate letters
    if (firstGuess == 0){
			firstGuess++;
      return true;
    }
		for (int i = 0; i < str.length(); i++)
			for (int j = i + 1; j < str.length(); j++)
				if (str.charAt(i) == str.charAt(j))
					return false;
		return true;
	}

//formulates a guess and the percent chance of that being the right word
	static boolean takeOutGreen = false;
static String t = "";
static String temp = "";
static int tempInd = 0;
static double tempMax = 0;
	public static String guess(ArrayList<String> word) {
   
		weight = new double [word.size()];
		t = "";
		max = 0.0;
		double size = word.size();
		int ind = 0;
    
		for (int i = 0; i < word.size(); i++){

      
		//	if (takeOutGreen) {
     // if (!skipWordIfGreen(word.get(i)))
     // continue;
		//	}
      
			double sum = 0.0;
			for (int j = 0; j < 5; j++) {
				int c = (int) word.get(i).charAt(j);
				if (j == 0)
					sum += ((double) freq[j][c - 97] / size);
				else
					sum *= ((double) freq[j][c - 97] / size);
			}
			weight[i] = sum;
     
      //choosing the word with the probability of success
			if (sum > max){
				t = word.get(i);
				max = sum;
				ind = i;
			}
		//	if (takeOutGreen)
     // System.out.println(t);
		}

//pretty sure this is useless but keeping it just in case I think of something
    /*if (!takeOutGreen){
    temp = t;
    tempInd = ind;
    tempMax = max;
    }

    if (max < 25.0 && !takeOutGreen){
      takeOutGreen = true;
      guess(word);
    }
//saving previous variables in case new word doesn't exist
  if (takeOutGreen && t.equals("")){
    t = temp;
    ind = tempInd;
    max = tempMax;
    }  
    
    takeOutGreen = false;
  */
    System.out.printf(t+" %.8f%%\n", max * 100);
		return t;
	}
//here we recalculate the frequency of each letter at each position
	public static void calculateFreq(ArrayList<String> word){
    for (int i = 0; i < 5; i++)
    Arrays.fill(freq[i], 0);
		String[] alpha = "abcdefghijklmnopqrstuvwxyz".split("");
		for (int i = 0; i < 26; i++) {
			for (int j = 0; j < word.size(); j++) {
				for (int k = 0; k < 5; k++) {
					if (word.get(j).substring(k,k+1).equals(alpha[i])) {
						freq[k][i]++;
					}
				}
			}
    }
    
		output(word);
  }
// remake the list of words based on what shows up on the guess
	public static ArrayList<String> calculateWord(ArrayList<String> word){
		ArrayList<String> newWord = new ArrayList<String>();
    
	  for (int i = 0; i < word.size(); i++) {
      boolean work = true;
      // if a letter is grey, any word containing it is thrown out
			for (int j = 0; j < noUse.size(); j++){
        if (word.get(i).indexOf(noUse.get(j)) != -1){
          //for edge case 
          if (green[word.get(i).indexOf(noUse.get(j))] != noUse.get(j)){
            //edge case where a letter is in word but only once
          if (!greenLet.contains(noUse.get(j)) && !yellow.contains(noUse.get(j))){
          work = false;
          
         
          break;
          }
          }
        }
        //if more copies of letter in a word than needed throws it out
        if (!counter(noUse.get(j), word.get(i))){
          System.out.println(1);
          work = false;
          break;
        }
      }

          

      //if a letter is green, word is thrown out if word doesn not have that letter at the specific index.
			for (int j = 0; j < green.length; j++) {
				if (word.get(i).charAt(j) != green[j] && green[j] != '0') {
					work = false;
          break;
				}
			}
      //if the letter was yellow, word is taken out if it doesn't include that letter
      for (int j = 0; j < yellow.size(); j++){
        if (word.get(i).indexOf(yellow.get(j)) == -1){
          work = false;
          break;
        }
      }
      //if a letter is yellow this makes sure no word with the that letter at the same index is chosen
    for (int j = 0; j < 5; j++){
      int c = (int)word.get(i).charAt(j);
      if (removeYellow[j][c-97] == 1){
        work = false;
        break;
      }
    }
      if (work)
      	newWord.add(word.get(i));
		}
  	return newWord;
	}
  //takes care of printing out the percentages
	public static void output(ArrayList<String> word) {
		int sum = word.size();
		System.out.println("\tFirst\tSecond\tThird\tFourth\tFifth");
		for (int i = 0; i < 26; i++) {
			double[] percents = new double[5];
			for (int j = 0; j < 5; j++) {
				percents[j] = ((double) freq[j][i] / sum) * 100;
			}
			String[] alpha = "abcdefghijklmnopqrstuvwxyz".split("");
			String base = alpha[i].toUpperCase();
			System.out.printf(base);
			for (int j = 0; j < 5; j++) {
				if (percents[j] >= 10) {
					System.out.printf(GREEN_B + BLACK + "\t%.3f%%" + RESET, percents[j]);
				} else if (percents[j] >= 5) {
					System.out.printf(GREEN + "\t%.3f%%" + RESET, percents[j]);
				} else if (percents[j] <= 1) {
					System.out.printf(RED_B + BLACK + "\t%.3f%%" + RESET, percents[j]);	
				} else if (percents[j] <= 2.5) {
					System.out.printf(RED + "\t%.3f%%" + RESET, percents[j]);	
				} else {
					System.out.printf("\t%.3f%%", percents[j]);
				}
			}
			System.out.println();
		}
	}
  
  //print guesses with nice colors
  public static void printGuess(String guess){
    for (int i = 0; i < 5; i++){
      if (s.charAt(i) == 'y')
      System.out.print(YELLOW_B + guess.charAt(i) + RESET);
      else if (s.charAt(i) == 'g')
      System.out.print(GREEN_B + guess.charAt(i) + RESET);
      else
      System.out.print(RED_B + guess.charAt(i) + RESET);
    }
    System.out.println();

  }

  public static boolean skipWordIfGreen (String curW) {
      
      for (int i = 0; i < 5; i++){
        if (green[i] == curW.charAt(i))
        return false;
      }

    return true;
    }

	public static boolean counter(char c, String guess) {
		
			int g = Collections.frequency(greenLet, c);
			int y = Collections.frequency(yellow, c);
      int n = 0;
      for (int j = 0; j < 5; j++) {
        if (guess.charAt(j) == c)
        n++;
      }
			if (n > g + y) {
				return false;
			}
		
    return true;
	}
}