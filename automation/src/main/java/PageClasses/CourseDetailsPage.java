package PageClasses;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.FindBy;
import com.aventstack.extentreports.ExtentTest;
import BaseClasses.PageBaseClass;
import Utilities.ExcelUtils;

public class CourseDetailsPage extends PageBaseClass {

	public int index;
	public String resultsPage;
	public String outputFilePath;
	public ExcelUtils writer;

	// Initialising the driver, logger, index of the course, and resultsPage window
	// handle
	public CourseDetailsPage(WebDriver driver, ExtentTest logger, int index, String resultsPage) {
		super(driver, logger);
		this.index = index;
		this.resultsPage = resultsPage;
	}

	// These @FindBy annotations are used to locate specific WebElements of specific
	// webpages
	@FindBy(tagName = "h1")
	public WebElement courseTitle;

	@FindBy(xpath = "//div[@class='_1srkxe1s XDPRating']//span[@data-test='number-star-rating']")
	public WebElement courseRating;

	@FindBy(xpath = "//div[@class='ProductGlance']//span[contains(text(),'to complete')]")
	public WebElement courseDuration;

	public void storeCourseDetails() {
		try {
			reportInfo("Storing Course No." + index + " Details in Excel file...");

			// Initialising the outputFilePath
			outputFilePath = userDir + prop.getProperty("courseFileRelPath");

			// Initialising the writer object by selecting a browser specific sheet
			if (driver instanceof ChromeDriver) {
				writer = new ExcelUtils(outputFilePath, "ChromeOutput");
			} else if (driver instanceof FirefoxDriver) {
				writer = new ExcelUtils(outputFilePath, "FirefoxOutput");
			} else {
				writer = new ExcelUtils(outputFilePath, "OperaOutput");
			}

			// Writing the 'course index' in excel file
			writer.setCellData(0, "Course No.", index, Integer.toString(index));

			waitForElement(courseTitle); // Waiting for the specified element
			// Writing the 'course title'
			writer.setCellData(0, "Course Title", index, courseTitle.getText());

			waitForElement(courseRating);
			// Writing the 'course rating'
			writer.setCellData(0, "Course Rating", index, courseRating.getText().substring(0, 3));
			scrollDown(8); // Scrolling down to view the 'course duration'

			waitForElement(courseDuration);
			// Writing 'course duration'
			writer.setCellData(0, "Course Duration", index, courseDuration.getText());
			// Logging the successful writing of all the course details
			reportPass("Successfully Stored Course No." + index + " Details in Excel file!");

			closeWindow(); // Closing the course page window
			switchToWindow(resultsPage); // Switching back to the results page window handle

		} catch (Exception e) {
			reportFail(e.getMessage());
		}
	}
}
