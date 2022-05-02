import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class WebCopier {
	
	private static final String BASE = "https://www.pealim.com/dict/?page=";
	
	private static String getSource(String url) throws IOException {
		URL urlObj = new URL(url);
		URLConnection con = urlObj.openConnection();
		con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
		
		/*
		 * Try-with-resources statement: automatically closes all resources declared in parentheses
		 * Any object that implements java.lang.AutoCloseable can be a resource
		 */
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
			String inputLine;
			StringBuilder builder = new StringBuilder();
			while ((inputLine = reader.readLine()) != null) {
				builder.append(inputLine);
			}
			return builder.toString();
		}
	}

	public static String getPageSource(int page) throws IOException {
		return getSource(BASE + page);
	}
	
	private static String getHebrew(String row) {
		final String HEB_START = "\"menukad\">";
		int start = row.indexOf(HEB_START) + HEB_START.length();
		return row.substring(start, row.indexOf("<", start));
	}
	
	/** Gets hebrew transliteration */
	private static String getTranslit(String row) {
		final String START = "\"dict-transcription\">";
		final String END = "</span>";
		int start = row.indexOf(START) + START.length();
		String transliteration = row.substring(start, row.indexOf(END, start));
		//return transliteration.replace("<b>", "<").replace("</b>", ">");
		return transliteration.replace("<b>", "").replace("</b>", "");
	}
	
	private static String getRoot(String row) {
		//the root is actually the second column of the table, so it follows the second occurrence
		//of <td> in the row
		int start = row.indexOf("<td>", row.indexOf("<td>") + 1);
		start = start + "<td>".length();
		return row.substring(start, row.indexOf("</td>"));
	}
	
	private static String getDef(String row) {
		final String START = "\"dict-meaning\">";
		int start = row.indexOf(START) + START.length();
		return row.substring(start, row.indexOf("</td>", start));
	}
	
	public static ArrayList<String> getRows(String source) {
		final String ROW_START = "<tr", ROW_END = "</tr>";
		int tableStartIdx = source.indexOf("<tbody>"); //html key for the start of the table body
		source = source.substring(tableStartIdx);
		ArrayList<String> rows = new ArrayList<String>();
		
		int nextRowIdx;
		while ((nextRowIdx = source.indexOf(ROW_START)) != -1) {
			int rowEndIdx = source.indexOf(ROW_END) + ROW_END.length();
			rows.add(source.substring(nextRowIdx, rowEndIdx));
			source = source.substring(rowEndIdx);
		}
		
		return rows;
	}

	public static final boolean isEmpty(String source) {
		return source.indexOf("<tbody></tbody>") > 0;
	}

	public static String getWordString(String row) {
		return getWord(row).toString();
	}

	public static Word getWord(String row) {
		return new Word(getHebrew(row), getDef(row), getTranslit(row));
	}
	
	public static ArrayList<Word> getWords() {
		ArrayList<Word> words = new ArrayList<Word>();

		int page = 1;
		try {
			String source;
			do {
				source = getPageSource(page);

				ArrayList<String> rows = getRows(source);
				for (String row : rows) {
					words.add(getWord(row));
				}

				page++;
			} while (!isEmpty(source));
		} catch (IOException e) {
			System.out.println("Error reading page " + page);
			System.out.println(e.getMessage());
		}

		return words;
	}
}
