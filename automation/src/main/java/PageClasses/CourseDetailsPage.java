package PageClasses;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.FindBy;

import com.aventstack.extentreports.ExtentTest;

import BaseClasses.PageBaseClass;
import Utilities.WriteTestOutput;

public class CourseDetailsPage extends PageBaseClass {

	public int index;
	public String resultsPage;
	public String outputFilePath;
	public WriteTestOutput writer;

	public CourseDetailsPage(WebDriver driver, ExtentTest logger, int index, String resultsPage) {
		super(driver, logger);
		this.index = index;
		this.resultsPage = resultsPage;
	}

	@FindBy(tagName = "h1")
	public WebElement courseTitle;

	@FindBy(xpath = "//div[@class='_1srkxe1s XDPRating']//span[@data-test='number-star-rating']")
	public WebElement courseRating;

	@FindBy(xpath = "//div[@class='ProductGlance']//span[contains(text(),'to complete')]")
	public WebElement courseDuration;

	public void storeCourseDetails() {
		try {
			reportInfo("Storing Course No." + index + " Details in Excel file...");

			outputFilePath = userDir + prop.getProperty("courseFileRelPath");
			if(driver instanceof ChromeDriver) {
				writer = new WriteTestOutput(outputFilePath, "ChromeOutput");
			} else if(driver instanceof FirefoxDriver) {
				writer = new WriteTestOutput(outputFilePath, "FirefoxOutput");
			} else {
				writer = new WriteTestOutput(outputFilePath, "OperaOutput");
			}
			
			writer.setCellData(0, "Course No.", index, Integer.toString(index));
			
			waitForElement(courseTitle);
			writer.setCellData(0, "Course Title", index, courseTitle.getText());
			
			waitForElement(courseRating);
			writer.setCellData(0, "Course Rating", index, courseRating.getText().substring(0, 3));
			scrollDown(8);
			
			waitForElement(courseDuration);
			writer.setCellData(0, "Course Duration", index, courseDuration.getText());
			reportPass("Successfully Stored Course No." + index + " Details in Excel file!");

			closeWindow();
			switchToWindow(resultsPage);

		} catch (Exception e) {
			reportFail(e.getMessage());
		}
	}
}
