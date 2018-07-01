package webtables;

import java.util.*;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.*;
import org.testng.annotations.*;
import io.github.bonigarcia.wdm.WebDriverManager;

public class WebTablesHashMap {

	WebDriver driver;
	Map<Integer, String> applicants = new HashMap<>();

	@BeforeClass
	public void setUp() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().window().fullscreen();
		driver.get("https://forms.zohopublic.com/murodil/report/Applicants/reportperma/DibkrcDh27GWoPQ9krhiTdlSN4_34rKc8ngubKgIMy8");
	}
	
	@Test
	public void test() throws InterruptedException {
		driver.findElement(By.xpath("//select/option[.='100']")).click();
		Thread.sleep(2000);
		
		int to = Integer.parseInt(driver.findElement(By.xpath("//span[@id='to']")).getText());
		int total = Integer.parseInt(driver.findElement(By.xpath("//span[@id='total']")).getText());

		while (to <= total) {
			List<WebElement> lst = driver.findElements(By.xpath("//table/tbody/tr"));
			for (WebElement we : lst) {
			String str = we.getText().substring(we.getText().indexOf(" ")+1);
			applicants.put(Integer.parseInt(we.getText().substring(0, we.getText().indexOf(" "))), str.replace(" ", "-"));
			}
			Thread.sleep(1000);
			if(to == total) break;
			driver.findElement(By.xpath("//a[@class='nxtArrow']")).click();
			Thread.sleep(1000);
			to = Integer.parseInt(driver.findElement(By.xpath("//span[@id='to']")).getText());
		}	
		Assert.assertEquals(total, applicants.size());
	}
}