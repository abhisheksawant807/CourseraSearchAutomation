package PageClasses;

import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.aventstack.extentreports.ExtentTest;
import BaseClasses.PageBaseClass;
import Utilities.ExcelUtils;

public class LanguageLearningPage extends PageBaseClass {

	public String outputFilePath;
	public ExcelUtils writer;

	// Initialising the driver and logger by passing them to the parent constructor
	public LanguageLearningPage(WebDriver driver, ExtentTest logger) {
		super(driver, logger);
	}

	// These @FindBy annotations are used to locate specific WebElements of specific
	// webpages
	@FindBy(css = "button:nth-child(5) > span")
	public WebElement languageDropDownButton;

	@FindBy(xpath = "//label/span[2]")
	public List<WebElement> languageList;

	@FindBy(css = "button:nth-child(4) > span")
	public WebElement levelDropDownButton;

	@FindBy(xpath = "//label/span[2]")
	public List<WebElement> levelList;

	public void openOutputFileSheet() {
		// Initialising the 'outputFilePath'
		outputFilePath = userDir + prop.getProperty("languageLearningFileRelPath");

		// Initialising the writer object by selecting a browser specific sheet
		if (driver instanceof ChromeDriver) {
			writer = new ExcelUtils(outputFilePath, "ChromeOutput");
		} else if (driver instanceof FirefoxDriver) {
			writer = new ExcelUtils(outputFilePath, "FirefoxOutput");
		} else {
			writer = new ExcelUtils(outputFilePath, "OperaOutput");
		}
	}

	public void storeLanguagesWithCount() {
		try {
			// Clicking the specific button
			elementClick(languageDropDownButton, "Language Dropdown Button");
			reportInfo("Storing all the Languages with their respective counts in an Excel file...");

			waitForElements(languageList); // Waiting for 'languageList' elements
			int index = 0; // Initialising 'index' for language entries
			for (WebElement language : languageList) {
				waitForElement(language);
				String languageData = language.getText();
				int rowNum = index + 2; // Updating 'rowNum' value
				// Retrieving the last index of '(' and ')' in the 'languageData' string
				int openingBracketIndex = languageData.lastIndexOf("(");
				int closingBracketIndex = languageData.lastIndexOf(")");

				// Writing the language no., lanuage name, and language count
				writer.setCellData(1, "Language no.", rowNum, Integer.toString(++index));
				writer.setCellData(1, "Language", rowNum, languageData.substring(0, openingBracketIndex).trim());
				writer.setCellData(1, "Language Count", rowNum,
						languageData.substring(openingBracketIndex + 1, closingBracketIndex).trim());
			}
			// Logging the successful writing of the language details in excel file
			reportPass("Successfully stored all the Languages with their respective counts in an Excel file!");
		} catch (Exception e) {
			reportFail(e.getMessage());
		}
	}

	// Similar to the above method (languages replaced by levels)
	public void storeLevelsWithCount() {
		try {
			elementClick(levelDropDownButton, "Level Dropdown Button");
			reportInfo("Storing all the Levels with their respective counts in an Excel file...");

			waitForElements(levelList);
			int index = 0;
			for (WebElement level : levelList) {
				waitForElement(level);
				String levelData = level.getText();
				int rowNum = index + 2;
				int openingBracketIndex = levelData.lastIndexOf("(");
				int closingBracketIndex = levelData.lastIndexOf(")");

				writer.setCellData(1, "Level no.", rowNum, Integer.toString(++index));
				writer.setCellData(1, "Level", rowNum, levelData.substring(0, openingBracketIndex).trim());
				writer.setCellData(1, "Level Count", rowNum,
						levelData.substring(openingBracketIndex + 1, closingBracketIndex).trim());
			}
			reportPass("Successfully stored all the Levels with their respective counts in an Excel file!");
		} catch (Exception e) {
			reportFail(e.getMessage());
		}
	}

	public HomePage backToHomePage() {
		try {
			navigateTo(homePageURL);

			verifyPageTitle(homePageTitle, newHomePageTitle);
			reportPass("Successfully navigated back to Home Page!");

			HomePage homePage = new HomePage(driver, logger);
			PageFactory.initElements(driver, homePage);
			return homePage;
		} catch (Exception e) {
			reportFail(e.getMessage());
		}
		return null;
	}
}
