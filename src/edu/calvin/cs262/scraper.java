import java.util.Scanner;
import java.net.URL;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.safety.Whitelist;

//javac -cp "./jsoup.jar" scraper.java
//java -classpath ./jsoup.jar:. scraper

//public static String get(String url) throws Exception {
 //  StringBuilder sb = new StringBuilder();
 //  for(Scanner sc = new Scanner(new URL(url).openStream()); sc.hasNext(); )
 //     sb.append(sc.nextLine()).append('\n');
 //  return sb.toString();
//}

public class scraper {
public static String get(String url) throws Exception {
   StringBuilder sb = new StringBuilder();
   for(Scanner sc = new Scanner(new URL(url).openStream()); sc.hasNext(); )
      sb.append(sc.nextLine()).append('\n');
   return sb.toString();
}
public static void main(String[] args) throws Exception {
   //System.out.println(get("http://www.calvin.edu/archive/student-news/201612/0005.html"));
	process();
}

public static void process() throws Exception {
	ArrayList<String> queryList = new ArrayList<String>();
	ArrayList<String> senderEmailList = new ArrayList<String>();
	ArrayList<String> senderNameList = new ArrayList<String>();
	ArrayList<String> squeryList = new ArrayList<String>();
	FileWriter aqueries = new FileWriter("aqueries.txt");
	FileWriter squeries = new FileWriter("squeries.txt");
	int counter = 1;
	int aCounter = 1;
	for (int i = 1; i < 10; i++) {
	String muhURL = get("http://www.calvin.edu/archive/student-news/201612/000" + String.valueOf(i) + ".html");
	Document doc = Jsoup.parse(muhURL);
	doc.outputSettings(new Document.OutputSettings().prettyPrint(false));
	//doc.select("br").append("\\n");
	doc.select("p").prepend("\\n");
	String s = doc.html().replaceAll("\\\\n", "\n"); //\n in quotes
	muhURL = Jsoup.clean(s, "",  Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
	//System.out.println(muhURL);
	
	int startIndex = muhURL.indexOf("----------------------------------------------------------------------");
	while (true) {
		//Article tempArticle = new Article();
		int senderPos = muhURL.indexOf("From: ", startIndex);
		if (senderPos == -1) {
			break;
		}
		//System.out.println(startIndex);
		//System.out.println(senderPos);
		int senderStartPos = muhURL.indexOf("&lt;", senderPos);
		String senderName = muhURL.substring(senderPos + 6, senderStartPos - 1);
		int senderEndPos = muhURL.indexOf("&gt", senderStartPos);
		String senderEmail = muhURL.substring(senderStartPos + 4, senderEndPos);
		System.out.println("-" + senderEmail + "-");
		System.out.println("-" + senderName + "-");
		int subjectPos = muhURL.indexOf("Subject: ", senderEndPos);
		int subjectEndPos = muhURL.indexOf("\n", subjectPos);
		String subject = muhURL.substring(subjectPos + 9, subjectEndPos);
		System.out.println("-" + subject + "-");
		subjectEndPos = subjectEndPos + 3;
		int bodyEndPos = muhURL.indexOf("------------------------------", subjectEndPos);
		String body = muhURL.substring(subjectEndPos, bodyEndPos - 3);
		body = body.replaceAll("\\n", " ");
		body = body.replaceAll("'", "''");
		subject = subject.replaceAll("'", "''");
		startIndex = bodyEndPos;
		System.out.println(body);
		if (!senderEmailList.contains(senderEmail)) {
			senderEmailList.add(senderEmail);
			senderNameList.add(senderName);
			String aQuery = "INSERT INTO Author VALUES (" + String.valueOf(aCounter) + ", '" + senderEmail + "', '" + senderName + "');";
			aqueries.write(aQuery + "\n");
			aCounter = aCounter + 1;
		}
		int authorIndex = senderEmailList.indexOf(senderEmail);
		authorIndex = authorIndex + 1;
		
		String query = "INSERT INTO Article VALUES (" + String.valueOf(counter) + ", '" + subject + "', '" + body + "', " + String.valueOf(authorIndex) + ");";
		counter = counter + 1;
		aqueries.write(query + "\n");
		//break;
	}
}
}
}