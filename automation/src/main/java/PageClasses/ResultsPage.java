package PageClasses;

import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.aventstack.extentreports.ExtentTest;
import BaseClasses.PageBaseClass;

public class ResultsPage extends PageBaseClass {

	// Initialising the driver and logger by passing them to the parent constructor
	public ResultsPage(WebDriver driver, ExtentTest logger) {
		super(driver, logger);
	}

	public String dropDownButtonName;

	// These @FindBy annotations are used to locate specific WebElements of specific webpages
	@FindBy(className = "cif-chevron-down")
	public WebElement exploreElement;

	@FindBy(xpath = "//ul[@class='mega-menu-items']//li//span")
	public List<WebElement> exploreList;

	@FindBy(xpath = "(//span[@class='Select-arrow-zone']/*)[1]")
	public WebElement languageDropDownButton;

	@FindBy(xpath = "//input[@type='checkbox' and @value='English']")
	public WebElement selectEnglishCheckbox;

	@FindBy(xpath = "(//span[@class='Select-arrow-zone']/*)[2]")
	public WebElement levelDropDownButton;

	@FindBy(xpath = "//input[@type='checkbox' and @value='Beginner']")
	public WebElement selectBeginnerCheckbox;

	@FindBy(css = ".vertical-box > div:nth-child(1) > h2")
	public List<WebElement> courseList;

	public void selectEnglish() {
		try {
			dropDownButtonName = "Language Filter Dropdown Button";
			// Clicking the specified elements
			elementClick(languageDropDownButton, dropDownButtonName); 
			elementClick(selectEnglishCheckbox, "English Language Checkbox"); 
			elementClick(languageDropDownButton, dropDownButtonName);
		} catch (Exception e) {
			reportFail(e.getMessage());
		}
	}

	public void selectBeginner() {
		try {
			dropDownButtonName = "Level Filter Dropdown Button";
			elementClick(levelDropDownButton, dropDownButtonName);
			elementClick(selectBeginnerCheckbox, "Beginner Level Checkbox");
			elementClick(levelDropDownButton, dropDownButtonName);
		} catch (Exception e) {
			reportFail(e.getMessage());
		}
	}

	public CourseDetailsPage openCourseDetailsPage(int index) {
		try {
			String resultsPage = driver.getWindowHandle(); // Initialising the current window handle
			String coursePage = null;

			waitForElements(courseList); // Wait for 'courseList' elements
			reportInfo("Opening course No." + index + " ..."); // Logging the specific course no.
			elementClick(courseList.get(index - 1), "Course No." + index); // Clicking the specific course

			for (String handle : driver.getWindowHandles()) {
				if (!handle.equals(resultsPage)) {
					// Initialising the course page window handle 
					coursePage = handle;
					break;
				}
			}
			switchToWindow(coursePage); // Switching to course page window handle

			// Passing the driver, logger, course index, and results page window handle to the object
			CourseDetailsPage courseDetailsPage = new CourseDetailsPage(driver, logger, index, resultsPage);
			PageFactory.initElements(driver, courseDetailsPage);
			return courseDetailsPage;
		} catch (Exception e) {
			reportFail(e.getMessage());
		}
		return null;
	}

	public LanguageLearningPage openLanguageLearningPage() {
		try {
			elementClick(exploreElement, "Explore drop down"); 
			// Selecting the specified element
			selectElementInList(exploreList, prop.getProperty("exploreListItem"));

			// Verifying the page title
			verifyPageTitle(prop.getProperty("languageLearningPageTitle"));
			// Logging the successful page title verification
			reportPass("Successfully opened 'Language Learning' page!");

			LanguageLearningPage languageLearningPage = new LanguageLearningPage(driver, logger);
			PageFactory.initElements(driver, languageLearningPage);
			return languageLearningPage;
		} catch (Exception e) {
			reportFail(e.getMessage());
		}
		return null;
	}

}
