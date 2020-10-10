package Utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;


public class ExtentReportManager {
	
		public static ExtentReports report;
		
		public static ExtentReports getReportInstance(){
			
			if(report == null){
				String reportName = System.getProperty("user.dir")+"/test-reports/"+DateUtil.getTimeStamp()+".html";
				ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(reportName);
				report =  new ExtentReports();
				report.attachReporter(htmlReporter);
				
				report.setSystemInfo("OS", "Windows 10");
				report.setSystemInfo("Environment", "UAT");
				report.setSystemInfo("Build Number", "10.8.1");
				report.setSystemInfo("Browser", "Chrome/Opera/Firefox");
				
				htmlReporter.config().setDocumentTitle("Coursera Online Search Automation");
				htmlReporter.config().setReportName("Smoke/Regression Testing");
				htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
				htmlReporter.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");
				htmlReporter.config().setTheme(Theme.STANDARD);
			}
			
			return report;
		}

}
