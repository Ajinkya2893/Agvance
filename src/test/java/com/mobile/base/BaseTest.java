package com.mobile.base;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.zeroturnaround.zip.ZipUtil;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Protocol;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.reporter.configuration.ViewName;

import excelHelper.DataUtils;
import excelHelper.ExcelReader;
import listeners.SendEmail;
import utility.CommonHelper;
import utility.Constants;

public class BaseTest{

	protected ExtentTest test;
	protected ExtentTest node;
	protected static ExtentReports report;
	protected static ExtentSparkReporter spark;
	protected static ExtentSparkReporter sparkFail;
	//protected ExtentEmailReporter email;

	protected ExcelReader xls;
	protected String sheetName;
	protected String suiteName;
	protected boolean isskip;
	protected String reportName;
	protected String testName;

	public BaseTest() {
		suiteName = "Agvance Test Suite";
		reportName = "Mobile Demo";
		sheetName = "Mobile";
		xls = new ExcelReader(Constants.DATAFILE.replace("${var}", "Agvance"));
	}

	@BeforeTest(alwaysRun = true)
	public void setUpReport() throws IOException {
		if (spark == null && report == null)
			initiateReport(suiteName, reportName);
	}

	@BeforeMethod(alwaysRun = true)
	public void verifyTestExecution(Method m) throws Exception {
		testName = m.getName();
		if (new DataUtils().isSkip(xls, m.getName())) {
			ExtentTest test = setTestReporter(m.getName());
			test.skip("Skipping the test as runmode is NO in the Excel Sheet");
			Reporter.getCurrentTestResult().setAttribute("TestInstance", test);
			isskip = true;
		}else if (test==null){
			test = setTestReporter(m.getName());
		}
	}

	protected Boolean isTestSkipped(String testName) {
		if (isskip) {
			isskip = false;
			throw new SkipException("Skipping test case " + testName + " as runmode set to NO in excel");
		}
		return true;
	}

	@AfterMethod(alwaysRun = true)
	public void getResult(ITestResult result) throws Exception {
		if(result.getStatus() == ITestResult.FAILURE) {
			if((CommonHelper) result.getAttribute("PageInstance") != null) {
				updateFailWebTest(result);
			}else if (!(test == null)) {
				test.log(Status.FAIL, MarkupHelper.createLabel(result.getName() + " - Test Case Failed", ExtentColor.RED));
				test.log(Status.FAIL, MarkupHelper.createCodeBlock(result.getThrowable().getMessage().toString() + " - Test Case Failed"));
			}
		} else if (result.getStatus() == ITestResult.SKIP && node != null) {
			node.log(Status.SKIP, MarkupHelper.createLabel(result.getName() + " - Test Case Skipped", ExtentColor.ORANGE));
		} else if (result.getStatus() == ITestResult.SUCCESS && node != null) {
			node.log(Status.PASS, MarkupHelper.createLabel(result.getName() + " Test Case PASSED", ExtentColor.GREEN));
		}
	}

	public void updateFailWebTest(ITestResult result) {
		CommonHelper page = (CommonHelper) result.getAttribute("PageInstance");
		if (result.getStatus() == ITestResult.FAILURE && !(page.getDriverInstance() == null) && !(test == null)) {
			node.log(Status.FAIL, MarkupHelper.createLabel(result.getName() + " - Test Case Failed", ExtentColor.RED));
			node.log(Status.FAIL, MarkupHelper.createCodeBlock(result.getThrowable().getMessage().toString() + " - Test Case Failed"));
			try {
				String screenshotPath = page.getScreenShot(page.getDriverInstance(), result.getName());
				if (screenshotPath != null) {
					node.fail("Test Case Failed Snapshot is below " + node.addScreenCaptureFromPath(screenshotPath));
				} else {
					node.fail("Failed to capture screenshot.");
				}
			} catch (Exception e) {
				node.fail("Failed to capture screenshot: " + e.getMessage());
			} 
		}
	}

	@AfterTest
	public void teardown() {
		report.flush();
	}

	@AfterSuite(alwaysRun = true)
	public void closeReport() {
		report.flush();
		if (Constants.isEmailReport) {
			String path = System.getProperty("user.dir") + "/ExtentReport/" + getDate();
			SendEmail send = new SendEmail();
			ZipUtil.pack(new File(path), new File(path + ".zip"));
			send.attach(path + ".zip");
		}
	}

	@DataProvider()
	public Object[][] getData(Method m) {
		return new DataUtils().getData(xls, m.getName(), sheetName);
	}

	@DataProvider()
	public Object[][] getApi(Method m) {
		return new DataUtils().getData(xls, m.getName(), "API");
	}

	private String getDate() {
		return DateTimeFormatter.ofPattern("yyyy_MM_dd HH_mm_ss").format(LocalDateTime.now()).toString();
	}

	private void initiateReport(@Optional String suiteName, @Optional String reportName) throws IOException {
		report = new ExtentReports();
		if (reportName == null && suiteName == null) {
			Constants.filePath = "." + setFilePaths(sheetName);
			reportName = sheetName;
		} else {
			this.reportName = reportName;
			Constants.filePath = System.getProperty("user.dir") + setFilePaths(suiteName);
		}
		// will only contain failures
		sparkFail = new ExtentSparkReporter(Constants.filePath + " Fail.html").filter().statusFilter()
				.as(new Status[] { Status.FAIL }).apply();

		spark = new ExtentSparkReporter(Constants.filePath)
				.viewConfigurer()
				.viewOrder()
				.as(new ViewName[] { 
						ViewName.DASHBOARD, 
						ViewName.TEST, 
						ViewName.AUTHOR, 
						ViewName.DEVICE, 
						ViewName.EXCEPTION, 
						ViewName.LOG 
				})
				.apply();

		spark.config().enableOfflineMode(true);
		spark.config().setOfflineMode(true);
		spark.config().setTheme(Theme.STANDARD);
		spark.config().setTimelineEnabled(true);
		spark.config().setProtocol(Protocol.HTTPS);
		spark.config().setDocumentTitle("Badger Regression");
		spark.config().setReportName(reportName);
		//spark.config()

		report.setAnalysisStrategy(AnalysisStrategy.TEST);
		report.attachReporter(sparkFail, spark);
	}

	protected ExtentTest setNodeReporter(HashMap<String, String> data) throws Exception {
		if(test==null) {
			return setTestReporter(data);
		}else {
			return node = test.createNode(data.get("DeviceDetails"));
		}
	}

	protected ExtentTest setTestReporter(HashMap<String, String> data) throws Exception {
		return report.createTest(data.get("TestName")).assignAuthor(System.getProperty("user.name"))
				.assignDevice(data.get("DeviceDetails"));
	}

	protected ExtentTest setTestReporter(String TestName) throws Exception {
		if(test==null)
			return report.createTest(TestName).assignAuthor(System.getProperty("user.name"));
		else
			return test;
	}

	private String setFilePaths(String folderName) {
		return File.separator + "ExtentReport" + File.separator + folderName + File.separator + getDate()
		+ File.separator;
	}
}