package PageClasses;

import java.util.Hashtable;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.FindBy;
import com.aventstack.extentreports.ExtentTest;
import BaseClasses.PageBaseClass;
import Utilities.ExcelUtils;

public class CourseraForCampusPage extends PageBaseClass {

	public String outputFilePath;
	public ExcelUtils writer;
	public int errorMsgIndex;

	// Initialising the driver, logger, and errorMsgIndex
	public CourseraForCampusPage(WebDriver driver, ExtentTest logger, int errorMsgIndex) {
		super(driver, logger);
		this.errorMsgIndex = errorMsgIndex;
		reportPass("Successfully Entered inside the data-driven form testing method!");
	}

	// These @FindBy annotations are used to locate specific WebElements of specific webpages
	@FindBy(id = "FirstName")
	public WebElement firstName;

	@FindBy(id = "LastName")
	public WebElement lastName;

	@FindBy(id = "Email")
	public WebElement workEmail;

	@FindBy(id = "Title")
	public WebElement jobTitle;

	@FindBy(id = "Phone")
	public WebElement phoneNo;

	@FindBy(id = "Company")
	public WebElement institutionName;

	@FindBy(id = "Institution_Type__c")
	public WebElement institutionTypeOptions;

	@FindBy(id = "Employee_Range__c")
	public WebElement institutionStrengthOptions;

	@FindBy(id = "Self_reported_employees_to_buy_for__c")
	public WebElement courseraUsersCountOptions;

	@FindBy(id = "Country")
	public WebElement countryOptions;

	@FindBy(id = "State")
	public WebElement stateOptions;

	@FindBy(css = "span > .mktoButton")
	public WebElement submitButton;

	@FindBy(css = ".mktoError > div:nth-child(2)")
	public WebElement errorMessage;

	public void openFormMsgOutputFile() {
		// Initialising the outputFilePath
		outputFilePath = userDir + prop.getProperty("errorMsgFileRelPath");
		
		// Initialising the writer object by selecting a browser specific sheet
		if (driver instanceof ChromeDriver) {
			writer = new ExcelUtils(outputFilePath, "ChromeOutput");
		} else if (driver instanceof FirefoxDriver) {
			writer = new ExcelUtils(outputFilePath, "FirefoxOutput");
		} else {
			writer = new ExcelUtils(outputFilePath, "OperaOutput");
		}

	}

	public void fillAndSubmitForm(Hashtable<String, String> dataTable) {
		try {
			reportInfo("Filling 'Ready to transform' form fields...");
			scrollDown(44); // Scolling down to view the form
			
			// Entering specific dataTable values into specific fields
			enterText(firstName, "First Name", dataTable.get("FirstName"));
			enterText(lastName, "Last Name", dataTable.get("LastName"));
			enterText(workEmail, "Work Email ID", dataTable.get("Email"));
			enterText(jobTitle, "Job Title", dataTable.get("JobTitle"));
			enterText(phoneNo, "Phone Number", dataTable.get("PhoneNo"));
			enterText(institutionName, "Institution Name", dataTable.get("InstitutionName"));

			// Selecting specific dataTable values from specific fields
			selectDropDownValue(institutionTypeOptions, "Institution Type", dataTable.get("InstitutionType"));
			selectDropDownValue(institutionStrengthOptions, "Institution Strength",
					dataTable.get("InstitutionStrength"));
			selectDropDownValue(courseraUsersCountOptions, "Coursera Users Count", dataTable.get("CourseraUsersCount"));
			selectDropDownValue(countryOptions, "Country", dataTable.get("Country"));

			if (isFresh(stateOptions)) {
				// Selecting the specific state option, if the 'state' select box is displayed
				selectDropDownValue(stateOptions, "State", dataTable.get("State"));
			}
			
			elementClick(submitButton, "Submit button"); // Clicking the submit button
			waitFor(1); // Waiting to view the error message of a specific field

			if (isFresh(errorMessage)) {
				// Writing the error message details, if the error message is displayed
				writer.setCellData(0, "Sl no.", errorMsgIndex, Integer.toString(errorMsgIndex));
				writer.setCellData(0, "Test Case Name", errorMsgIndex, dataTable.get("Comment"));
				writer.setCellData(0, "Error Message Presence (YES/NO)", errorMsgIndex, "YES");

				// Logging the successful display of the error message
				reportPass("Error message is displayed, which is what we expected!");
				return;
			}

			// Writing the error message details if error message is NOT displayed
			writer.setCellData(0, "Sl no.", errorMsgIndex, Integer.toString(errorMsgIndex));
			writer.setCellData(0, "Test Case Name", errorMsgIndex, dataTable.get("Comment"));
			writer.setCellData(0, "Error Message Presence (YES/NO)", errorMsgIndex, "NO");
			writer.setCellData(0, "Invalid Field", errorMsgIndex, "_");
			writer.setCellData(0, "Error Message", errorMsgIndex, "_");
			writer.setCellData(0, "Pass/Fail", errorMsgIndex, "FAIL");

			goBack(); // Returning back to the formPage from 'Thank you' page
			
			// Failing the test case due to absense of error message
			reportFail("Error message NOT displayed, which is a failure!");

		} catch (Exception e) {
			reportFail(e.getMessage());
		}
	}

	public void displayErrorMessage() {
		try {
			waitForElement(errorMessage); // Waiting for error message element
			String errormessage = errorMessage.getText(); // Retrieving the error message
			
			// Replacing the unwanted characters in the 'id' attribute of error message
			String invalidField = errorMessage.getAttribute("id").substring(8).replaceAll("_", " ").replace("c", "")
					.trim();

			reportInfo("Storing form error message in Excel file...");

			// Writing the error message details in the excel file
			writer.setCellData(0, "Invalid Field", errorMsgIndex, invalidField);
			writer.setCellData(0, "Error Message", errorMsgIndex, errormessage);
			writer.setCellData(0, "Pass/Fail", errorMsgIndex, "Pass");

			// Logging the successful writing of error message details
			reportPass("Form error message stored in Excel file successfully!");
			refreshPage();

		} catch (Exception e) {
			reportFail(e.getMessage());
		}
	}
}
