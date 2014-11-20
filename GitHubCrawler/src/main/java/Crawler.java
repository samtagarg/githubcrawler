import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Crawler {
	private static void printHits(WebDriver driver) throws IOException,
			InterruptedException {
		String xpath = "//h3[@class='repo-list-name']/*";
		List<WebElement> hits = driver.findElements(By.xpath(xpath));
		Iterator<WebElement> iter = hits.iterator();
		while (iter.hasNext()) {
			String url = "https://github.com/" + iter.next().getText() + ".git";

			int code = getCode(url);
			System.out.println(url + " " + code);
			if (code == 200)
				executeCommand("cd \"C:\\Users\\TARUN JANDRA\\Desktop\\ProjectsGitHub\" && git clone "
						+ url);

			// break;
		}
	}

	private static void fetchURL(WebDriver driver, String url) {
		driver.get(url);
		List<WebElement> hls = driver.findElements(By.xpath("//h1"));
		Iterator<WebElement> iter = hls.iterator();
		while (iter.hasNext()) {
			System.out.println(iter.next().getText());
		}
	}

	public static void executeCommand(String command) throws IOException,
			InterruptedException {
		ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);
		builder.redirectErrorStream(true);
		System.out.println(command);
		Process p = builder.start();
		// p.waitFor();
		BufferedReader r = new BufferedReader(new InputStreamReader(
				p.getInputStream()));
		String line;
		while (true) {
			line = r.readLine();
			if (line == null) {
				break;
			}
			System.out.println(line);
		}
	}

	public static int getCode(String url) {
		int code = 404;
		try {
			String url1 = url;
			URL url2 = new URL(url1);
			HttpURLConnection connection = (HttpURLConnection) url2
					.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			code = connection.getResponseCode();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return code;
	}

	public static void main(String[] args) throws InterruptedException,
			IOException {
		File file = new File("C:\\Users\\TARUN JANDRA\\Desktop\\ProjectsGitHub");
		if (!file.exists())
			file.mkdir();
		process();

	}

	private static void process() throws InterruptedException, IOException {
		// int minSize = 1000;
		// int maxSize = 1050;
		long delay = 5000;
		WebDriver driver = new FirefoxDriver();
		String url = "https://github.com/search?l=Java&q=language%3AJava&ref=advsearch&type=Repositories&utf8=✓";

		// url += "+size:" + minSize + ".." + maxSize;
		for (int i = 1; i <= 100; i++) {
			fetchURL(driver, url + "&p=" + i);
			Thread.sleep(delay);
			printHits(driver);
		}
		driver.close();
	}
}
