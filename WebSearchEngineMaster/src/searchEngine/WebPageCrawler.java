package searchEngine;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author Manjinder Singh
 *
 */
public class WebPageCrawler {
	
	private static Set<String> crawledPageSet = new HashSet<String>();
	private static int pageDepth = 2; //increasing the depth will increase the time to crawl web pages
	private static String htmlRegex = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)";

	/**
	 * @param pageUrl = initial web link
	 * @param depth = depth to crawl inside each web link
	 */
	public static void crawler(String pageUrl, int depth) {
		Pattern patternObject = Pattern.compile(htmlRegex);
		if (depth <= pageDepth) {
			try {
				Document document = Jsoup.connect(pageUrl).get();
				HtmlParser.writeHtml(document, pageUrl);
				depth++;
				if (depth < pageDepth) {
					Elements links = document.select("a[href]");
					for (Element page : links) {

						if (validateHtmlLink(page.attr("abs:href")) && patternObject.matcher(page.absUrl("href")).find()) {
							
							System.out.println(page.absUrl("href"));
							crawler(page.absUrl("href"), depth);
							crawledPageSet.add(page.absUrl("href"));
						}
					}
				}
			} catch (Exception e) {
				
			}
		}
	}
	
	/**
	 * 
	 * @param pageUrl = url want to validate
	 * @return 	true if url is valid and is not duplicate 
	 * 			false otherwise
	 */
	private static boolean validateHtmlLink(String pageUrl) {
		if (crawledPageSet.contains(pageUrl)) {
			return false;
		}
		if (pageUrl.endsWith(".doc") || pageUrl.endsWith(".docx") || pageUrl.endsWith(".xlx") || 
				pageUrl.endsWith(".xlsx") || pageUrl.endsWith(".ppt") || pageUrl.endsWith(".pptx") || 
				pageUrl.endsWith(".jpeg") || pageUrl.endsWith(".jpg") || pageUrl.endsWith(".png")
				|| pageUrl.endsWith(".pdf") || pageUrl.contains("#") || pageUrl.contains("?")
				|| pageUrl.contains("mailto:") || pageUrl.startsWith("javascript:") || pageUrl.endsWith(".gif")
				||pageUrl.endsWith(".xml")) {
			return false;
		}
		return true;
	}
	
	public static void main(String[] args) {
		String pageUrl="https://www.dal.ca/faculty/computerscience/current/scholarships.html";
		crawler(pageUrl, 0);
	}
}
