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
	

	@Parameters("browser")
	@Test(groups = { "smoke", "regression" })
	public void searchTest(String browser) {
		this.currentBrowser = browser;
		logger = report.createTest(browser + " Results : Search Courses");
		invokeBrowser(browser);
		PageBaseClass pageBase = new PageBaseClass(driver, logger);
		PageFactory.initElements(driver, pageBase);
		homePage = pageBase.openApplication();
		
		  resultsPage = homePage.searchAndDisplayCourses();
		  resultsPage.selectEnglish(); 
		  resultsPage.selectBeginner(); 
		  for (int courseNo = 1; courseNo <= 2; courseNo++) { 
		  courseDetailsPage =  resultsPage.openCourseDetailsPage(courseNo);
		  courseDetailsPage.storeCourseDetails(); 
		  } 
		  languageLearningPage = resultsPage.openLanguageLearningPage();
		  languageLearningPage.openOutputFileSheet();
		  languageLearningPage.storeLanguagesWithCount();
		  languageLearningPage.storeLevelsWithCount(); 
		  languageLearningPage.backToHomePage();
		  courseraForBusinessPage = homePage.clickForEnterprise();
       		  courseraForBusinessPage.openCourseraForCampus();
		  errorMsgIndex = 1;
	}

	@Test(groups = "regression", dataProvider = "getData", priority = 1)
	public void formTest(Hashtable<String, String> dataTable) {
		logger = report.createTest(currentBrowser + " Results : " + dataTable.get("Comment"));

		courseraForCampusPage = new CourseraForCampusPage(driver, logger, ++errorMsgIndex);
		PageFactory.initElements(driver, courseraForCampusPage);

		courseraForCampusPage.openFormMsgOutputFile();
		courseraForCampusPage.fillAndSubmitForm(dataTable);
		courseraForCampusPage.displayErrorMessage();
	}

	@DataProvider
	public Object[][] getData() {
		return TestDataProvider.getTestData("IdentifyCoursesTestData.xlsx", "FormInputTestCases", "InvalidInputs");
	}
	
}
