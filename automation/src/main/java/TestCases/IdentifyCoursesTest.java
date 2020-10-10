package TestCases;

import java.util.Hashtable;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import BaseClasses.BaseTestClass;
import BaseClasses.PageBaseClass;
import PageClasses.CourseDetailsPage;
import PageClasses.CourseraForBusinessPage;
import PageClasses.CourseraForCampusPage;
import PageClasses.HomePage;
import PageClasses.LanguageLearningPage;
import PageClasses.ResultsPage;
import Utilities.TestDataProvider;

public class IdentifyCoursesTest extends BaseTestClass {

	String currentBrowser;
	int errorMsgIndex;
	HomePage homePage;
	ResultsPage resultsPage;
	CourseDetailsPage courseDetailsPage;
	LanguageLearningPage languageLearningPage;
	CourseraForBusinessPage courseraForBusinessPage;
	CourseraForCampusPage courseraForCampusPage;

	@Parameters("browser") // Retrieving the specific browser value from the suite file
	@Test(groups = { "smoke", "regression" }) // Specifying the groups to which this test method belongs
	public void searchTest(String browser) {
		this.currentBrowser = browser; // Initialising the browser variable
		// Creating a new test in the report
		logger = report.createTest(browser + " Results : Search Courses");
		invokeBrowser(browser); // Invoking the specific browser

		// Passing the driver and logger to the object
		PageBaseClass pageBase = new PageBaseClass(driver, logger);
		PageFactory.initElements(driver, pageBase);
		homePage = pageBase.openApplication(); // Opening the homePage

		resultsPage = homePage.searchAndDisplayCourses(); // Searching and displaying courses in ResultsPage
		resultsPage.selectEnglish(); // Selecting English checkbox
		resultsPage.selectBeginner(); // Selecting the Beginner checkbox

		// Navigating to the first 2 courses
		for (int courseNo = 1; courseNo <= 2; courseNo++) {
			// Opening the specific course page
			courseDetailsPage = resultsPage.openCourseDetailsPage(courseNo);
			courseDetailsPage.storeCourseDetails(); // Storing its details in excel file
		}
		// Opening language learning page
		languageLearningPage = resultsPage.openLanguageLearningPage();
		languageLearningPage.openOutputFileSheet(); // Initialising the outputFile and browser specific sheet
		languageLearningPage.storeLanguagesWithCount(); // Storing Language details
		languageLearningPage.storeLevelsWithCount(); // Storing Level details
		languageLearningPage.backToHomePage(); // Navigating back to home page
		courseraForBusinessPage = homePage.clickForEnterprise(); // Clicking the particular link
		courseraForBusinessPage.openCourseraForCampus(); // Opening the formPage 
	}

	@Test(groups = "regression", dataProvider = "getData", priority = 1)
	public void formTest(Hashtable<String, String> dataTable) {
		// Creating a new test in html report
		logger = report.createTest(currentBrowser + " Results : " + dataTable.get("Comment"));

		// Passing driver, logger, and incremented errorMsgIndex
		courseraForCampusPage = new CourseraForCampusPage(driver, logger, ++errorMsgIndex);
		PageFactory.initElements(driver, courseraForCampusPage);

		// Initialising the outputFileWriter and sheet
		courseraForCampusPage.openFormMsgOutputFile();
		courseraForCampusPage.fillAndSubmitForm(dataTable); // Filling the form fields
		courseraForCampusPage.displayErrorMessage(); // Displaying the error message, if present
	}

	@DataProvider
	public Object[][] getData() {
		// Returning the 2-D object array containing hashtables for each testcase data
		return TestDataProvider.getTestData("IdentifyCoursesTestData.xlsx", "FormInputTestCases", "InvalidInputs");
	}

}
