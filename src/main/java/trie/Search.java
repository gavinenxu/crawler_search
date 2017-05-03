package trie;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Collections;;

public class Search {

	private Trie trie;
	private float rank = 0.00000f;

	public Search() {
		trie = new Trie();
	}

	/*
	 * read file and store it in the trie readfile:
	 * http://stackoverflow.com/questions/4716503/reading-a-plain-text-file-in-
	 * java
	 */
	public void readFile() throws IOException {

		/*
		 * read file by the NIO.Files
		 */
		/*
		 * try { String content = new
		 * String(Files.readAllBytes(Paths.get("stackoverflow.txt")));
		 * System.out.println(content); } catch (IOException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */

		/*
		 * read file using the linear reader (parallel not implemented), Use a
		 * BufferedReader over a FileReader, read line by line, split lines by
		 * calling String.split().
		 */

		String line;
		int page = 0;
		File file = new File("/Users/gavin/java_workspace/crawler/stackoverflow.txt");
		FileReader fd = new FileReader(file);
		BufferedReader bd = new BufferedReader(fd);

		while ((line = bd.readLine()) != null) {
			doSomethingWithLine(line, ++page);
		}

	}

	public void doSomethingWithLine(String line, int page) {

		int remainder = page % 3;

		// rank the word of the page, status format: , Votes:0 Answered:0
		// Views:1
		if (remainder == 1) {
			int i = 0;
			while (i < line.length()) {
				if (Character.isLetterOrDigit(line.charAt(i)))
					break;
				i++;
			}
			line = line.substring(i);
			String[] s1 = line.toLowerCase().split(" ");
			int[] grade = new int[3];
			int m = 0;
			for (String status : s1) {
				String[] statusScore = status.split(":");
				String score = statusScore[1];
				if (score.charAt(score.length() - 1) == 'k') {
					if (score.indexOf('.') != -1) {

						grade[m++] = (int) (Float.parseFloat(score.substring(0, score.length() - 1)) * 1000);
						continue;
					}
					grade[m++] = Integer.parseInt(score.substring(0, score.length() - 1)) * 1000;
					continue;
				}
				grade[m++] = Integer.parseInt(score);
			}
			// calculate the word's rank, using 0.3*votes + 0.6*answered +
			// 0.1*views
			rank = (float) (0.3 * grade[0] + 0.6 * grade[1] + 0.1 * grade[2]);

		} else if (remainder == 2) {
			// theme format: How to use protobuf payload with UDP protocol in
			// Netty? Could you give me a example for this with custom protobuf
			// protocol? $::

			String[] theme = line.split(" ");
			for (String word : theme) {

				int length = word.length() - 1;
				int i = 0, j = length;
				while (i < length) {
					if (Character.isLetterOrDigit(word.charAt(i)))
						break;
					i++;
				}
				while (j > 0 && j >= i) {
					if (Character.isLetterOrDigit(word.charAt(j)))
						break;
					j--;
				}

				word = word.substring(i, j + 1).trim();

				trie.insert(word, page, rank); // add the theme word into trie
			}

		} else {
			// tags format: [linux, bash, shell, tcl, expect]
			line = line.substring(1, line.length() - 1);
			String[] tags = line.split(",");
			for (String tag : tags) {
				trie.insert(tag.trim(), page, rank); // add the tag word into
														// trie
			}
		}
	}

	/*
	 * use the rank to sort the word show list
	 */
	public void showWordRankPage(String word) {

		Map<Integer, Float> map = trie.getPages(word);
		// System.out.println("entry: "+map.entrySet());
		if (map == null) {
			System.out.println("there is no such word in the file for the word: " + word);
			return;
		}
		System.out.println(word + ": occured " + map.size() + " times!");
		System.out.println("And it occured in the original txt in following rows: ");

		Set<Map.Entry<Integer, Float>> set = map.entrySet();
		for (Map.Entry<Integer, Float> entry : set)
			System.out.print(entry.getKey() + " ");

		System.out.println("\nafter sort by the ranking value: ");
		map = java8_sortByValue(map);
		System.out.println(map + "\n");
	}

	/*
	 * sort the map by value
	 */
	private static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {

		List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
		System.out.println("list: " + list);
		Map<K, V> result = new LinkedHashMap<>();

		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {

			public int compare(Entry<K, V> o1, Entry<K, V> o2) {

				// from bigger to smaller
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	/*
	 * java 8 functional programming sortByValue
	 */

	private static <K, V extends Comparable<? super V>> Map<K, V> java8_sortByValue(Map<K, V> map) {

		return map.entrySet().stream().sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}

	public static void main(String[] args) throws IOException {

		Search s = new Search();
		s.readFile();
		s.showWordRankPage("list");
		s.showWordRankPage("biubibi");

		// System.out.println("$::".substring(2, 2));
		// System.out.println("".length());
	}
}
