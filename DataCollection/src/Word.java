class HebLetter {
	private char letter;
	private char[] nikkud;
}

public class Word implements java.io.Serializable {
	
	/**
	 * Must declare this to be serialized
	 */
	private static final long serialVersionUID = -4870701290724888920L;
	
	private String heb, hebNoVowels, def, translit;
	
	public Word(String hebrew, String definition, String transliteration) {
		heb = hebrew;
		hebNoVowels = strip(heb);
		def = definition;
		translit = transliteration;
	}
	
	public String getHeb() {
		return heb;
	}
	
	public String getHebNoVowels() {
		return hebNoVowels;
	}
	
	/**
	 * Returns the transliteration with the accented syllable currounded with <>
	 * @return
	 */
	public String getTranslit() {
		return translit;
	}
	
	/**
	 * Returns the HTML formatted transliteration with the accented syllable in bold
	 */
	public String getTranslitHTML() {
		int start = translit.indexOf('<');
		if (start < 0) {
			return translit;
		} else {
			int end = translit.indexOf('>');
			return "<html>"
					+ translit.substring(0, start)
					+ "<strong>"
					+ translit.substring(start + 1, end)
					+ "</strong>"
					+ translit.substring(end + 1)
					+ " </html>";
		}
	}
	
	public String getDef() {
		return def;
	}
	
	/**
	 * Checks if the english input matches the word's english translation.
	 * Not case-sensitive.
	 * @param input the user input (in english)
	 * @return true if the input equals this word's english translation, false otherwise
	 */
	public boolean checkDef(String input) {
		return input.compareToIgnoreCase(def) == 0;
	}
	
	/**
	 * Checks if the hebrew input matches the word's hebrew translation.
	 * Precondition: hebInput doesn't have any hebrew vowels or diacritics
	 * Will return correct if the base letters match up
	 * @param hebInput the user input (in hebrew)
	 * @return true if the input equals this word's hebrew translation, false otherwise
	 */
	public boolean checkHeb(String hebInput) {
		return strip(hebInput).equals(hebNoVowels);
	}
	
	/**
	 * Strips the input hebrew text of anything that isn't a hebrew letter
	 * i. e. it strips the text of all hebrew vowels
	 * Leaves the geresh and other yod/vav punctuation marks
	 * @param hebrew Hebrew text (with vowels)
	 * @return the hebrew text, stripped of all vowels and diacritics
	 */
	public static String strip(String hebrew) {
		//all hebrew letters fall between these //unicode characters
		final char LOW_BOUND = '\u05D0', HIGH_BOUND = '\u05F4';
		
		char[] chars = hebrew.toCharArray();
		String noVowels = "";
		
		for (char c : chars) {
			if (c >= LOW_BOUND && c <= HIGH_BOUND) {
				noVowels += c;
			}
		}
		
		return noVowels;
	}
	
	public static Word toWord(String str) {
		String eng, heb, translit;
		int marker = str.indexOf('|');
		heb = str.substring(0, marker);
		translit = str.substring(marker + 1, marker = str.indexOf('|', marker + 1));
		eng = str.substring(marker + 1);
		return new Word(heb, eng, translit);
	}
	
	public boolean checkEng(String response) {
		response = response.trim();
		if (response.length() > 2 && response.substring(0, 3).compareToIgnoreCase("to ") == 0) {
			response = response.substring(3);
		}
		response = response.replaceAll(" to ", "");
		response = response.replaceAll(",", ";");
		response = response.replaceAll(";{2,}", ";");
		response = response.replaceAll("[^a-zA-Z;]", "");
		String[] responses = def.split("\\;");
		
		String answer = def;
		int start = def.indexOf('(');
		int end = def.indexOf(')');
		while (start >= 0) {
			if (end > 0) {
				def = def.substring(0, start) + def.substring(end + 1);
			} else {
				break;
			}
			start = def.indexOf('(');
			end = def.indexOf(')');
		}
		
		if (def.length() > 2 && def.substring(0, 3).compareToIgnoreCase("to ") == 0) {
			def = def.substring(3);
		}
		def = def.replaceAll(" to ", "");
		def = def.replaceAll(",", ";");
		def = def.replaceAll(";{2,}", ";");
		def = def.replaceAll("[^a-zA-Z;]", "");
		String[] answers = def.split("\\;");
		
		for (String r : responses) {
			for (String a : answers) {
				if (r.compareToIgnoreCase(a) == 0) {
					
				}
			}
		}
		
		for (String str : answers) {
			if (response.compareToIgnoreCase(str) == 0) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return heb + '|' + hebNoVowels + '|' + translit + '|' + def;
	}
}