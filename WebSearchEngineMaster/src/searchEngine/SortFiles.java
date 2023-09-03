package searchEngine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author Shreyansh Dalwadi
 *
 */
public class SortFiles {

	
		/**
		 * Method will sort html links based on it's frequency
		 * @param htmlLinks = hashtable of links and its frequency
		 * @param count = number of files in which pattern is found.
		 */
		public static void sort(Hashtable<String, Integer> htmlLinks, int count) {
			// Transfer as List and sort it
			ArrayList<Map.Entry<String, Integer>> arrayList = new ArrayList<Map.Entry<String, Integer>>(htmlLinks.entrySet());

			Collections.sort(arrayList, new Comparator<Map.Entry<String, Integer>>() {

				public int compare(Map.Entry<String, Integer> obj1, Map.Entry<String, Integer> obj2) {
					return obj2.getValue().compareTo(obj1.getValue());
				}
			});

			if (count != 0) {

				System.out.println("\nTop 10 Web pages containing results related to given search \n");
				int webPageCount = 10;
				for(int i=0;i<webPageCount&&arrayList.size()>i;i++) {
					System.out.println(" " + (i+1) + " " + arrayList.get(i).getKey());
				}
			} 

		}
}
