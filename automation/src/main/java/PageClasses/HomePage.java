package PageClasses;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.aventstack.extentreports.ExtentTest;

import BaseClasses.PageBaseClass;

public class HomePage extends PageBaseClass {

	// Initialising the driver and logger by passing them to the parent constructor
	public HomePage(WebDriver driver, ExtentTest logger) {
		super(driver, logger);
	}

	public String searchText;

	// These @FindBy annotations are used to locate specific WebElements of specific webpages
	@FindBy(css = ".react-autosuggest__container > input")
	public WebElement searchBox;

	@FindBy(xpath = "//div[@class='react-autosuggest__section-container']//li[@role='option']")
	public List<WebElement> suggestionBox;

	@FindBy(xpath = "(//button/div[@class='magnifier-wrapper'])[2]")
	public WebElement searchButton;

	@FindBy(id = "enterprise-link")
	public WebElement forEnterpriseLink;

	public ResultsPage searchAndDisplayCourses() {
		try {
			elementClick(searchBox, "Search box"); // Clicking the search box
			searchText = prop.getProperty("searchInput"); // Retrieving specific text from 'Config.properties' file
			enterText(searchBox, "Search Box", searchText); // Entering specific text in search box
			selectElementInList(suggestionBox, searchText); // Selecting specific option from the ajax suggestion list
			elementClick(searchButton, "Search Button"); // Clicking the search button
			
			// Verifying the ResultsPage title
			verifyPageTitle(prop.getProperty("resultsPageTitle"));
			// Logging the successful verification of the page title
			reportPass("Successfully opened 'Web Dev Results' page!");
			
			// Creating a new ResultsPage object, initialising the driver and logger, and returning the object
			ResultsPage resultsPage = new ResultsPage(driver, logger);
			PageFactory.initElements(driver, resultsPage);
			return resultsPage;
		} catch (Exception e) {
			reportFail(e.getMessage());
		}
		return null;
	}

	public CourseraForBusinessPage clickForEnterprise() {
		try {
			reportInfo("Clicking 'For Enterprise' link...");
			// Clicking the specific link
			elementClick(forEnterpriseLink, "'For Enterprise' link in topmenu");

			verifyPageTitle(prop.getProperty("courseraForBusinessPageTitle"));
			reportPass("Successfully opened 'Coursera For Business' page!");

			CourseraForBusinessPage courseraForBusinessPage = new CourseraForBusinessPage(driver, logger);
			PageFactory.initElements(driver, courseraForBusinessPage);
			return courseraForBusinessPage;
		} catch (Exception e) {
			reportFail(e.getMessage());
		}
		return null;
	}
}
