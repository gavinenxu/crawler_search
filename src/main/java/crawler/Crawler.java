package crawler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.*;

public class Crawler {

	/**
	 * 传入url，将要访问的页面转换成String字符串
	 * 
	 * @param url
	 * @return htmlString
	 * @throws IOException
	 */
	public static String getHtmlString(String url) throws IOException {

		String htmlString = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		// http get method
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse response = httpclient.execute(httpGet);
			int resStatus = response.getStatusLine().getStatusCode();
			if (resStatus == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				if (entity != null)
					htmlString = EntityUtils.toString(entity);
			}

		} catch (Exception e) {
			System.out.println("get url exception");
			e.printStackTrace();
		} finally {
			httpclient.close();
		}

		return htmlString;
	}

	/**
	 * 将访问的html页面保存为文件
	 * 
	 * @param htmlString
	 * @throws ParseException、IOException
	 * 			@throws
	 */

	public static void jsoupToFile(String htmlString) throws ParseException, IOException {

		/*
		 * String dirPath = "/Users/gavin/java_workspace/crawler/"; File file =
		 * new File(dirPath); if (file == null || !file.exists()) {
		 * file.mkdirs(); }
		 */

		/*
		 * String filePath =
		 * "/Users/gavin/java_workspace/crawler/stackoverflow.txt"; File
		 * htmlFile = new File(filePath); if (htmlFile == null ||
		 * !htmlFile.exists()) { htmlFile.createNewFile(); }
		 * 
		 * 
		 * FileOutputStream out = new FileOutputStream(htmlFile); // write the
		 * html into file with utf-8 out.write(htmlString.getBytes("UTF-8"));
		 * 
		 * if (out != null) { out.close(); }
		 */
		Path filePath = Paths.get("stackoverflow.txt");
		if (!Files.exists(filePath)) {
			Files.createFile(filePath);
		}
		Files.write(filePath, htmlString.getBytes("UTF-8"), StandardOpenOption.APPEND);

	}

	/**
	 * 处理response，Jsoup解析页面，获取所 需数据
	 * 
	 * @param url
	 * @throws IOException
	 */
	public static void getQustionList(String url) throws IOException {

		// write question into file by using jsoup
		Question question = new Question();
		Status status = new Status();

		List<String> qusToFile = new ArrayList<String>();

		String content = getHtmlString(url);
		Document doc = Jsoup.parse(content);
		Elements links = doc.getElementsByClass("question-summary narrow");
		for (Element e : links) {
			// write status
			Elements votes = e.getElementsByClass("votes").select("span");
			Elements answered = e.getElementsByClass("answered").select("span");
			Elements views = e.getElementsByClass("views").select("span");
			// System.out.println(votes.text().toString() +" "
			// +answered.text().toString() +" "+ views.text().toString());

			status.setVotes(votes.text());
			if (answered.text().trim().equals(""))
				status.setAnswered("0");
			else {
				status.setAnswered(answered.text());
			}
			status.setViews(views.text());

			// System.out.println(status.toString());

			// write question
			List<String> categories = new ArrayList<String>();
			Elements theme = e.getElementsByClass("question-hyperlink");
			Elements tags = e.select("a[rel=tag]");
			for (Element tag : tags)
				categories.add(tag.text());

			question.setCategories(categories);
			question.setStatus(status);
			question.setTheme(theme.text());
			// System.out.println(question.toString());

			// System.out.println("-----------------------------------------");

			qusToFile.add(question.toString());
			/*
			 * if create List<Question> qusToFile = new ArrayList<Question>();
			 * qusToFile.add(question),file会被覆盖
			 */
		}
		// System.out.println(qusToFile);

		String s = qusToFile.toString();
		jsoupToFile(s.substring(0, s.length() - 1));
	}

	public static void main(String[] args) throws IOException {
		String url = "http://stackoverflow.com/";
		getQustionList(url);
		System.out.println("clawer succeed from: " + url);
	}

}
