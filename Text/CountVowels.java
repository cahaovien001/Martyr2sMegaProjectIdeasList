import java.util.*;

public class CountVowels {
    public static void main(String... args) {
	HashMap<Character, Integer> dict =
	    new HashMap<Character, Integer>();
	dict.put('a', 0);
	dict.put('e', 1);
	dict.put('i', 2);
	dict.put('o', 3);
	dict.put('u', 4);
	dict.put('A', 0);
	dict.put('E', 1);
	dict.put('I', 2);
	dict.put('O', 3);
	dict.put('U', 4);

	HashMap<Integer, Character> reversedict =
	    new HashMap<Integer, Character>();
	reversedict.put(0, 'a');
	reversedict.put(1, 'e');
	reversedict.put(2, 'i');
	reversedict.put(3, 'o');
	reversedict.put(4, 'u');
	
	int [] counts = new int[5];

	for(int i=0; i<counts.length; i++) {
	    counts[i] = 0;
	}
		
	if (args.length > 0)
	    for(String s : args) {
		for(int i=0; i<s.length(); i++) {
		    char c = s.charAt(i);
		    if(dict.containsKey(c)) {
			counts[dict.get(c)]++;
		    }
		}
	    }
	else {
	    Scanner scan = new Scanner(System.in);
	    String scanned;
	    while (scan.hasNext()) {
		scanned = scan.nextLine();

		for (int i=0; i<scanned.length(); i++) {
		    char c = scanned.charAt(i);
		    if(dict.containsKey(c)) {
			counts[dict.get(c)]++;
		    }
		}
	    }
	}

	System.out.printf("Vowels counted:\n");
	for(int i=0; i<counts.length; i++) {
	    System.out.printf("%c: %d\n", reversedict.get(i),
			      counts[i]);
	}

    }
}
   
