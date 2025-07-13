package selenium.standAlone;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class StandAloneTC2 {

    public static void main(String[] args) throws InterruptedException {
    	
    	/* ───────────────────────── GET DATA ───────────────────────── */
    	
//    	String fromStn = sh.
//    	String toStn
//    	String mail
//    	Stirng phoneNo
//    	String name
//    	String age
//    	

        /* ───────────────────────── DRIVER SET  UP ───────────────────────── */
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        /* ─────────────────────── SEARCH PAGE FLOW ──────────────────────── */
        driver.get("https://www.redbus.in/");

        driver.findElement(By.xpath("//div[contains(text(),'From')]")).click();
        Thread.sleep(800);
        driver.switchTo().activeElement().sendKeys("Mumbai");
        Thread.sleep(800);
        driver.findElement(By.xpath("//div[@class='listHeader___40b031']")).click();

        Thread.sleep(800);
        driver.switchTo().activeElement().sendKeys("Pune");
        Thread.sleep(800);
        driver.findElement(By.className("listHeader___40b031")).click();

        /* ────────── pick next Saturday (or same week Saturday) ─────────── */
        driver.findElement(By.xpath("//span[@class='doj___21b4b7']")).click();
        driver.switchTo().activeElement();          // now inside calendar
        String todayStr = driver.findElement(
                By.xpath("//div[contains(@class,'selected___7bc605')]//span")).getText();

        int offset = daysToCurrentSat(Integer.parseInt(todayStr));
        if (offset > 0) {
            driver.findElement(By.xpath(
                    "(//div[contains(@class,'selected___7bc605')]//following::li)[" + offset + ']'))
                  .click();
        }

        /* ───────────────────────── BUS SEARCH ─────────────────────────── */
        driver.findElement(By.xpath("//button[contains(text(),'Search buses')]")).click();
        Thread.sleep(2000);

        // filters
        driver.findElement(By.xpath("//div[contains(text(),'Primo Bus')]")).click();
        driver.findElement(By.xpath("//div[contains(text(),'Departure time from source')]")).click();
        driver.findElement(By.xpath("//div[contains(text(),'Evening')]")).click();
        driver.findElement(By.xpath("//div[contains(text(),'Bus features')]")).click();
        driver.findElement(By.xpath("(//div[contains(text(),'Live Tracking')])[2]")).click();

        /* ────────────────────── FIND CHEAPEST BUS ─────────────────────── */
        String totalText = driver.findElement(
                By.xpath("//div[@class='busesFoundText__ind-search-styles-module-scss-PHVGD']"))
                .getText();                     // e.g., “15 buses found”
        int busCount = Integer.parseInt(totalText.split(" ")[0]);

        int cheapestFare = Integer.MAX_VALUE;
        WebElement cheapestBusCard = null;
        JavascriptExecutor js = (JavascriptExecutor) driver;

        for (int i = 1; i <= busCount; i++) {
            WebElement card = driver.findElement(
                    By.xpath("(//li[contains(@class,'tupleWrapper___a09825')])[" + i + ']'));

            js.executeScript("arguments[0].scrollIntoView(true);", card); // view the card

            // get price text from the same card
            String priceTxt = card.findElement(
                    By.xpath(".//p[@class='finalFare___898bb7']")).getText(); // “₹1,199”

            int price = Integer.parseInt(priceTxt.replaceAll("\\D", ""));   // strip non digits

            if (price < cheapestFare) {
                cheapestFare = price;
                cheapestBusCard = card;      // reuse; no extra DOM lookup
            }
        }

        System.out.println("Cheapest fare: ₹" + cheapestFare);

        /* ─────────── select that bus, seat, and fill passenger form ────── */
        js.executeScript("arguments[0].scrollIntoView(true);", cheapestBusCard);
        cheapestBusCard.findElement(
                By.xpath(".//button[contains(@class,'viewSeatsBtn___f9f605')]")).click();

        Thread.sleep(1200);  // give seat layout time to render

        // try sleeper seat first, else regular seat
        try {
//            driver.findElement(By.xpath("//span[contains(@class,'sleeper__ind-seat')]")).click();
            
            driver.findElement(By.xpath("(//span[contains(@class,'sleeper__ind-seat')])[2]")).click();
            
            
        } catch (Exception ignore) {
            driver.findElement(By.xpath("//span[contains(@class,'seat__ind-seat')]")).click();
        }

        driver.findElement(By.xpath("//button[contains(text(),'Select boarding & dropping points')]")).click();
        driver.findElement(By.xpath("//div[contains(@class,'bpdpSelection___69f41b')]")).click();
        driver.findElement(By.xpath("(//div[contains(@class,'bpdpSelection___69f41b')][@data-id='0'])[2]")).click();

        driver.findElement(By.xpath("//input[@placeholder='Email']")).sendKeys("roushankumar22145@gmail.com");
        driver.findElement(By.xpath("//input[@placeholder='Phone']")).sendKeys("7970835758");

        /* state of residence pop up */
        driver.findElement(By.id("0_201")).click();
        Thread.sleep(800);
        driver.findElement(By.cssSelector(".listHeader___d239ee.undefined")).click();

        /* scroll a bit so the passenger form is not hidden */
        js.executeScript("window.scrollBy(0, 120);");

        driver.findElement(By.xpath("//input[@placeholder='Enter your Name']")).sendKeys("Roushan Verma");
        driver.findElement(By.xpath("//input[@placeholder='Enter Age']")).sendKeys("24");
        driver.findElement(By.xpath("//label[text()='Male']")).click();

        driver.findElement(By.id("fcRejectText")).click();    // decline cancellation fee protection
        driver.findElement(By.id("insuranceConfirmText")).click(); // opt in insurance
        driver.findElement(By.xpath("//button[contains(text(),'Continue booking')]")).click();

        /* ───────────────────────── PAYMENT PAGE ───────────────────────── */
        driver.findElement(By.xpath("//div[contains(text(),'Pay through UPI ID')]")).click();
        driver.findElement(By.xpath("//input[contains(@class,'inputField')]"))
              .sendKeys("rroushan40@okaxis");
        driver.findElement(By.xpath("//button[@class='primaryButton___f9943b ']")).click();

        Thread.sleep(5000);   // pause to observe result
        driver.quit();
    }

    /** Returns how many days to the nearest Saturday within the same week. */
    private static int daysToCurrentSat(int today) {
        LocalDate now = LocalDate.now();
        LocalDate sat = now.with(DayOfWeek.SATURDAY);
        int delta = sat.getDayOfMonth() - today;
        return delta < 0 ? 7 + delta : delta;
    }
}
