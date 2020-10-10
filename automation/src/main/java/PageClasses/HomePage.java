package PageClasses;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.aventstack.extentreports.ExtentTest;

import BaseClasses.PageBaseClass;

public class HomePage extends PageBaseClass {

	public HomePage(WebDriver driver, ExtentTest logger) {
		super(driver, logger);
	}

	public String searchText;

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
			elementClick(searchBox, "Search box");
			searchText = prop.getProperty("searchInput");
			enterText(searchBox, "Search Box", searchText);
			selectElementInList(suggestionBox, searchText);
			elementClick(searchButton, "Search Button");
			
			verifyPageTitle(prop.getProperty("resultsPageTitle"));
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
