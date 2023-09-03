package searchEngine;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import external.EditDistance;
import external.TST;

/**
 * @author Harbhajan Singh
 *
 */
public class AutoCorrect {
	public static TST<Integer> st = new TST<Integer>();
	
	/**
	 * Creates as dictionary of English words
	 */
	public void createDictionary() {
		try {
			FileReader fileReader = new FileReader(Constants.dictionaryPath);
			BufferedReader br = new BufferedReader(fileReader);
			String line = null;
			while ((line = br.readLine()) != null) {
				String word = line.toLowerCase();
				if (!line.contains(" ")) {
					st.put(word, (st.get(word)!=null)?st.get(word):0+1);
				} else {
					String[] strs = line.split("\\s");
					for (String str : strs) {
						st.put(str, (st.get(word)!=null)?st.get(word):0+1);
					}
				}
			}
			fileReader.close();
			br.close();
		} catch (FileNotFoundException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		}
	}
	
	/**
	 * @param inputWord
	 * @return
	 */
	public String suggestWords(String inputWord) {
		if(st.contains(inputWord)) {
			return inputWord;
		}
		String str = inputWord.toLowerCase();
		HashMap<String, Integer> editDistanceMap = new HashMap<>();
		String[] similarWords = new String[10];
		String result = null;
        
		if (inputWord.length() == 0 || inputWord == null || inputWord.length()==1) {
			return result;
		}
		for (String word : st.keys()) {
				int distance = EditDistance.editDistance(word, str);
				editDistanceMap.put(word,distance);
			}
		
		Map<String, Integer> sortedMap= sortByValue(editDistanceMap);
		
		int rank = 0;
		
		for (Map.Entry<String, Integer> map : sortedMap.entrySet()) {
			if (map.getValue() != 0) {
				similarWords[rank] = map.getKey();
				rank++;
				if (rank == 10){ break; }
			}
		}
		result=similarWords[0];
//	    System.out.println("results "+result);
		return result;
	}
	
	public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> map)
	{
		// crate list from the map
		List<Map.Entry<String, Integer> > list = new ArrayList<>(map.entrySet());

		// Sort the list
		list.sort(Map.Entry.comparingByValue());

		// put data from sorted list to linked Hash map to preserve the order of insertion
		// as list is sorted.
		HashMap<String, Integer> sortedListMap = new LinkedHashMap<>();
		for (Map.Entry<String, Integer> word : list) {
			sortedListMap.put(word.getKey(), word.getValue());
		}
		return sortedListMap;
	}
	
	public static void main(String[] args) {
		AutoCorrect autoCorrect=new AutoCorrect();
		autoCorrect.createDictionary();
		System.out.println(autoCorrect.suggestWords("compter"));
	}

}
