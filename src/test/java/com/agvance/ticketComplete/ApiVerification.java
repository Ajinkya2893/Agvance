package com.agvance.ticketComplete;

import java.util.HashMap;

import org.junit.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobile.base.BaseTest;

import apiHelper.ApiHelper;
import io.restassured.response.Response;


public class ApiVerification extends BaseTest{
	
	public Response response;
	
	@Test(dataProvider = "getApi")
	public void verifyBlendTicket(HashMap<String, String> data) throws Exception {
		if (!data.get("RunMode").equalsIgnoreCase("N") && isTestSkipped(data.get("TestName"))) {
			ApiHelper api = new ApiHelper();
			response = api
				.getBaseUrl(setTestReporter("blendtickets"))
				.addHeader()
				.getRequest("/blendtickets/"+ data.get("ticketNumber"));
			Assert.assertEquals(response.getStatusCode(), 200);
			test.pass(MarkupHelper.createCodeBlock(response.toString(), CodeLanguage.JSON));
			
			JsonNode blendFields = new ObjectMapper().readTree(response.prettyPrint()).findPath("blendFields");
	        if (blendFields.isArray()) {
	            for (JsonNode field : blendFields) {
	                JsonNode dispatchJobStatus = field.findPath("dispatchJobStatus");
	                test.pass(MarkupHelper.createCodeBlock(dispatchJobStatus.toPrettyString(), CodeLanguage.JSON));
	                if (dispatchJobStatus.isTextual()) {
	                    System.out.println("dispatchJobStatus: " + dispatchJobStatus.asText());
	                    api.log("dispatchJobStatus: " + dispatchJobStatus.asText());
	                }
	            }
	        }
		} else {
			node = setNodeReporter(data).log(Status.SKIP,
                    "Skipping the test as runMode is NO in the Excel Sheet");
			throw new SkipException("Skipping the test as runMode is NO in the Excel Sheet");
        }
	}
}
