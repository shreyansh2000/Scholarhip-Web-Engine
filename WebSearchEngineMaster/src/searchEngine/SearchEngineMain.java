package searchEngine;

import java.io.File;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.regex.Pattern;

import external.In;

/**
 * @author KARAN VISHAVJIT
 *
 */
public class SearchEngineMain {
	private static Scanner scanner = new Scanner(System.in);
	private static Hashtable<String, Integer> frequencyInFiles = new Hashtable<String, Integer>();

	
	public static void main(String[] args) {
		String input = "n";
		do {
			System.out.println("\n---------------------------------------------------");
			System.out.println("\n "+Constants.welcomeMessage+" \n");
			System.out.println("---------------------------------------------------");
		
			String option = scanner.next();

			switch (option) {
			case "y":
				String[] urls = Constants.urls;
				crawlWebpages(urls);
				input=searchPattern();
				break;
			case "n":
				System.out.println("Exit...");
				input = "n";
				break;
			default:
				System.out.println(Constants.defaultMessage);
				input="y";
			}
		}
		while(input.equalsIgnoreCase("y"));
		System.out.println("\nThank you for using our search engine!\n");
		System.out.println("\n\n!!!!!!!!!!!!!!\tSee you Soon\t!!!!!!!!!!!!!!\n\n");
		HelperMethods.deleteFiles();
	}

	/**
	 * Method will crawl list of all url's
	 * @param urls
	 */
	public static void crawlWebpages(String[] urls) {
		System.out.println("Initializing crawling web pages......");
		for(int i = 0;i< urls.length;i++)
			WebPageCrawler.crawler(urls[i], 0);
		System.out.println("All web pages crawled");
	}
	
	/**
	 * 
	 * @return
	 */
	public static String searchPattern() {
		String choice = "y";
		do {
			System.out.println("---------------------------------------------------");
			System.out.println("\n Enter Field in which you want to Search Scholarships!! \n ");
			String inputPattern = scanner.nextLine();
			while(inputPattern.isEmpty()) {
				inputPattern = scanner.nextLine();
			}
			System.out.println("---------------------------------------------------");
			inputPattern=PatternLookup.validateWord(inputPattern);	//validate if the input is correct 
			int frequencyOfInput = 0;		// how many times given input appeared in file.
			int noOfFiles = 0;				// in how many files pattern in present.
			frequencyInFiles.clear();		// hashtable with html links as key and frequency of pattern as value 
			File files = null;
			try {
				System.out.println("\nSearching...");
				files = new File(Constants.txtDirectoryPath);
				File[] fileArray = files.listFiles(); //Store files in a file array
				long start,end,total=0;
				
				for (int i = 0; i < fileArray.length; i++) { //iterate through each file.
					if(fileArray[i].exists()) {
						In inputData = new In(fileArray[i].getAbsolutePath());
						String str = inputData.readAll();
						inputData.close();
//						Pattern p = Pattern.compile("::");
//						String[] file_name = p.split(str); //get the link of html form the file using regex
						String[] file_name = str.split("<>"); //get the link of html form the file using regex
						//Search the word in the file
						start=System.nanoTime();
						frequencyOfInput = PatternLookup.searchPattern(str, inputPattern.toLowerCase(), file_name[0]); // search for pattern in text files
						end=System.nanoTime();
						total+=end-start; 		// to calculate time in pattern search
						if (frequencyOfInput != 0) {
							frequencyInFiles.put(file_name[0], frequencyOfInput);	//put html link and frequency value in hashtable
							noOfFiles++;
						}
					}
				}	
				System.out.println("Time elaspsed: "+total);
					if(noOfFiles>0) {
					System.out.println("\nTotal Number of Sites having Scholarships related to: " + inputPattern + " is : " + noOfFiles);
					}else {
						System.out.println("\n No Scholarships found for search: "+ inputPattern);
					}

					SortFiles.sort(frequencyInFiles, noOfFiles); //sort files based on frequency of pattern in file

			} 
			catch (Exception e) {
				System.out.println("Exception:" + e);
			}
			System.out.println("\n Want to search in another field(y/n)?");
			choice = scanner.next();
		} while (choice.equals("y"));
		
		return "n";
	}
}
