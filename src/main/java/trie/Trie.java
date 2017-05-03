package trie;

import java.util.*;

class TrieNode {

	Character val;
	Map<Character, TrieNode> map;
	// boolean isWord;
	// List<Integer> pages;
	// word store in the map<word, rank> to get the most priority word one by
	// one
	//List<Map<Integer, Float>> pages;
	Map<Integer, Float> pages;

	TrieNode() {
		val = null;
		map = new HashMap<Character, TrieNode>();
		// isWord = false;
		pages = new HashMap<>();
	}

	TrieNode(char ch) {
		val = ch;
		map = new HashMap<Character, TrieNode>();
		// isWord = false;
		pages = new HashMap<>();
	}

	public String toString() {
		return val + pages.toString();
	}
}

public class Trie {

	private TrieNode root;

	public Trie() {

		root = new TrieNode();
	}

	// insert a word into trie
	public void insert(String word, int page, float rank) {

		TrieNode cur = root;
		for (int i = 0; i < word.length(); i++) {
			char ch = word.charAt(i);
			if (!cur.map.containsKey(ch)) {
				cur.map.put(ch, new TrieNode(ch));
			}

			cur = cur.map.get(ch); // link to next trie node
			// if (i == word.length() - 1) // set word flag trues
			// cur.isWord = true;
		}
		// cur.isWord = true;

		// initialize the word map, not in the trieNode constructor, because... 
		//now i get it, just use the map to store the map<page, rank>
		//cur.wordRank = new HashMap<>();
		//cur.wordRank.put(page, rank);
		//cur.pages.add(cur.wordRank);
	
		cur.pages.put(page, rank);
	}

	private TrieNode search(String word, TrieNode cur) {

		for (int i = 0; i < word.length(); i++) {
			char ch = word.charAt(i);
			if (!cur.map.containsKey(ch))
				return null;
			cur = cur.map.get(ch);
		}

		// could not return true directly, because word match the prefix of
		// trie, but it's not inserted in trie before.
		// return true;
		// return !cur.pages.isEmpty();
		return cur;
	}

	public Map<Integer, Float> getPages(String word) {


		TrieNode cur = search(word, root);
		if(cur == null)
			return null;

		return cur.pages;
	}

	public boolean startsWith(String prefix) {

		TrieNode cur = root;
		for (int i = 0; i < prefix.length(); i++) {
			char ch = prefix.charAt(i);
			if (!cur.map.containsKey(ch))
				return false;
			cur = cur.map.get(ch);
		}
		return true; // string itself is prefix
	}

	public static void main(String[] args) {
		Trie trie = new Trie();
		trie.insert("hello", 1, 2.2f);
		trie.insert("gavin", 2, 3.4f);
		trie.insert("hello", 3, 5.5f);
		// trie.insert("how", 3);
		System.out.println(trie.getPages("hello"));
		// System.out.println(trie.getPages("gav"));

	}
}
