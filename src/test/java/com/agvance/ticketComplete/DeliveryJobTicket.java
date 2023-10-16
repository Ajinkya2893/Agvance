package com.agvance.ticketComplete;

import java.util.HashMap;

import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.mobile.base.BaseTest;

import pageObject.JobsPage;

public class DeliveryJobTicket extends BaseTest{

	@Test(dataProvider = "getData")
	public void completeDeliveryJob(HashMap<String, String> data) throws Exception {
		if (!data.get("RunMode").equalsIgnoreCase("N") && isTestSkipped(data.get("TestName"))) {
			JobsPage jobs = new JobsPage(setNodeReporter(data));
			Reporter.getCurrentTestResult().setAttribute("PageInstance", jobs);
			jobs.launchApplication(data);
			jobs.getLogin(data);
			jobs
				.selectVechileType(data.get("VechileType"))
				.selectJobType(data.get("JobType"));
			jobs
				.selectJobByName("ticketName_xpath", data.get("JobName"))
				.verifyTicketDetails(data.get("TicketDetails"))
				.startJob()
				.completeJob()
				.drawSign();
		} else {
			node = setNodeReporter(data).log(Status.SKIP,
                    "Skipping the test as runMode is NO in the Excel Sheet");
			throw new SkipException("Skipping the test as runMode is NO in the Excel Sheet");
        }
	}
}
