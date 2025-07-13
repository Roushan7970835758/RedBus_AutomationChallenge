package selenium.standAlone;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class standAloneTeatCase {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		
		driver.get("https://www.redbus.in/");
		driver.findElement(By.xpath("//div[contains(text(),'From')]")).click();
		Thread.sleep(1000);
		driver.switchTo().activeElement().sendKeys("Mumbai");
		Thread.sleep(1000);
		driver.findElement(By.xpath("//div[@class='listHeader___40b031']")).click();
		Thread.sleep(1000);
		driver.switchTo().activeElement().sendKeys("Pune");
		Thread.sleep(1000);
		driver.findElement(By.className("listHeader___40b031")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//span[@class='doj___21b4b7']")).click();
		driver.switchTo().activeElement();  // switched to calande window
		String currentDay = driver.findElement(By.xpath("//div[contains(@class,'selected___7bc605')]//span")).getText();
		
		int daysCount = daysToCurrentSat(Integer.parseInt(currentDay));
		System.out.println("days to next Sunday: "+daysCount);
		
		if(daysCount >0) {
			driver.findElement(By.xpath("(//div[contains(@class,'selected___7bc605')]//following::li)["+daysCount+"]")).click();
		}
		driver.findElement(By.xpath("//button[contains(text(),'Search buses')]")).click();
	
		Thread.sleep(2000);  // wait for search result
		
		//apply filters
		driver.findElement(By.xpath("//div[contains(text(),'Primo Bus')]")).click();
		driver.findElement(By.xpath("//div[contains(text(),'Departure time from source')]")).click();
		driver.findElement(By.xpath("//div[contains(text(),'Evening')]")).click();
		driver.findElement(By.xpath("//div[contains(text(),'Bus features')]")).click();
		driver.findElement(By.xpath("(//div[contains(text(),'Live Tracking')])[2]")).click();
		
		List<WebElement> buses;
		Set<WebElement> totalBuses = new HashSet<>();
		String totalBusesText = driver.findElement(By.xpath("//div[@class='busesFoundText__ind-search-styles-module-scss-PHVGD']")).getText();
		System.out.println(totalBusesText);
		int busCount = Integer.parseInt(totalBusesText.split(" ")[0]);
		
		Integer leastPrice = Integer.MAX_VALUE;
		WebElement leastBusPrice = null;
		
		
		//now get the least price bus
		for(int i=1;i<=busCount;i++) {
			
//			js.executeScript("window.scrollBy(0,100)");
			WebElement ele = driver.findElement(By.xpath("(//li[contains(@class,'tupleWrapper___a09825')])["+i+"]"));
			
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", ele);
			
			
			totalBuses.add(driver.findElement(By.xpath("(//li[contains(@class,'tupleWrapper___a09825')])["+i+"]")));
			
			String curr_price_String = driver.findElement(By.xpath("(//li[contains(@class,'tupleWrapper___a09825')])["+ i +"]//p[@class='finalFare___898bb7']")).getText();
			
			
			StringBuilder sb = new StringBuilder();
//			String curr_price ="";
			
			for(int j=0;j<curr_price_String.length();j++) {
				char ch = curr_price_String.charAt(j);
				if(Character.isDigit(ch)) {
					sb.append(ch);
//					curr_price+=ch;
				}
			}
			
			String realsbString = sb.toString();
			
			int realPrice = Integer.parseInt(realsbString);
			
			if(realPrice < leastPrice) {
				leastPrice = realPrice;
				leastBusPrice = driver.findElement(By.xpath("(//li[contains(@class,'tupleWrapper___a09825')])["+i+"]"));
			}
			
		}
		
		
		System.out.println("least Price is: "+ leastBusPrice.findElement(By.xpath(".//p[@class='finalFare___898bb7']")).getText());
		
		//press the view seat button of least price bus
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", leastBusPrice);
		
		leastBusPrice.findElement(By.xpath(".//button[contains(@class,'viewSeatsBtn___f9f605')]")).click();
		
		Thread.sleep(1000);
		
		try {
			driver.findElement(By.xpath("//span[@class='sleeper__ind-seat-styles-module-scss-Z8-su ']")).click();  //selecting seat
		}catch(Exception e) {
			driver.findElement(By.xpath("//span[@class='seat__ind-seat-styles-module-scss-tUu8R ']")).click();  //selecting seat
		}
		
		driver.findElement(By.xpath("//button[contains(text(),'Select boarding & dropping points')]")).click();
		driver.findElement(By.xpath("//div[contains(@class,'bpdpSelection___69f41b')]")).click();
		driver.findElement(By.xpath("(//div[contains(@class,'bpdpSelection___69f41b ') and @data-id='0'])[2]")).click();
		
		driver.findElement(By.xpath("//input[@placeholder='Email']")).sendKeys("roushankumar22145@gmail.com");
		driver.findElement(By.xpath("//input[@placeholder='Phone']")).sendKeys("7970835758");
		
		driver.findElement(By.id("0_201")).click();   //selecting state of registance  here pop up opens
		Thread.sleep(1000);
//		driver.switchTo().activeElement();
		driver.findElement(By.className("listHeader___d239ee")).click();
		Thread.sleep(1000);
//		driver.switchTo().activeElement();
		
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 100);");

		driver.findElement(By.xpath("//input[@placeholder='Enter your Name']")).sendKeys("Roushan Verma");
		Thread.sleep(1000);
		
		
		driver.findElement(By.xpath("//input[@placeholder='Enter Age']")).sendKeys("24");
		driver.switchTo().activeElement();
		
		
		
		driver.findElement(By.xpath("//label[text()='Male']")).click();
		driver.findElement(By.id("fcRejectText")).click();           //rejecting cancilation fees
		driver.findElement(By.id("insuranceConfirmText")).click();   //opting insurancd
		driver.findElement(By.xpath("//button[contains(text(),'Continue booking')]")).click();
		
		//payment section
		driver.findElement(By.xpath("//div[contains(text(),'Pay through UPI ID')]")).click();
		driver.findElement(By.xpath("//input[@class='inputField___e2a1dd   ']")).sendKeys("rroushan40@okaxis");
		driver.findElement(By.xpath("//button[@class='primaryButton___f9943b ']")).click();
		
		Thread.sleep(5000);
		
		
		
		
		
		
		
		
	}
	
	public static int daysToCurrentSat(int currentDay) {
		LocalDate  today = LocalDate.now();
		LocalDate saturday = today.with(DayOfWeek.SATURDAY);
		int count = saturday.getDayOfMonth()-currentDay;
		if(count<0) {
			return 7 + count;
		}
		return count;
	}

}
