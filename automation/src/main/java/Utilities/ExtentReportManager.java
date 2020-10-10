package Utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;


public class ExtentReportManager {
	
		public static ExtentReports report;
		
		public static ExtentReports getReportInstance(){
			
			if(report == null){
				// Initialising the report name
				String reportName = System.getProperty("user.dir")+"/test-reports/"+DateUtil.getTimeStamp()+".html";
				// Creating an ExtentHtmlReporter object
				ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(reportName);
				report =  new ExtentReports(); // Creating a new ExtentReport object
				report.attachReporter(htmlReporter); // Attaching the html reporter to extent report object
				
				// Initialising the various system related specifications in the report
				report.setSystemInfo("OS", "Windows 10");
				report.setSystemInfo("Environment", "UAT");
				report.setSystemInfo("Build Number", "10.8.1");
				report.setSystemInfo("Browser", "Chrome/Opera/Firefox");
				
				// Initialising the configuration details of the html reporter
				htmlReporter.config().setDocumentTitle("Coursera Online Search Automation");
				htmlReporter.config().setReportName("Smoke/Regression Testing");
				htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
				htmlReporter.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");
				htmlReporter.config().setTheme(Theme.STANDARD);
			}
			
			return report;
		}

}
