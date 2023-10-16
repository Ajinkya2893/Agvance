package utility;

public class Constants {

	public static final int PORT = 4723;
	public static final String URL = "127.0.0.1";

	// OutLook EMAIL DETAILS
	public static final String SENDTO = "";
	public static final String SENDFROM = "";
	public static final String EMAILPASSWORD = "";

	// OutLook EMAIL DETAILS
	public static final String SENDTO_GMAIL = "";
	public static final String SENDFROM_GMAIL = "";
	public static final String EMAILPASSWORD_GMAIL = "";

	// Browser Stack // divyani Engagement
	public static final String BSTACK_USERNAME = "divyani_VXApBE";
	public static final String BSTACK_ACCESS_KEY = "DqSbspEJ9EYw1QYLxgyz";
	public static final String BSTACK_URL = "https://" + BSTACK_USERNAME + ":" + BSTACK_ACCESS_KEY
			+ "@hub-cloud.browserstack.com/wd/hub";

	// Sauce Labs
	public static final String SAUCE_USERNAME = "";
	public static final String SAUCE_ACCESS_KEY = "";
	public static final String SAUCE_URL = "https://" + SAUCE_USERNAME + ":" + SAUCE_ACCESS_KEY
			+ "@ondemand.eu-central-1.saucelabs.com:443/wd/hub";

	// Perfecto
	public static final String PERFECTO_HOST = "";
	public static final String PERFECTO_TOKEN = "";
	public static final String PERFECTO_URL = "https://" + PERFECTO_HOST + "/nexperience/perfectomobile/wd/hub/fast";

	/*** Docker hub url ***********/
	public static final String HUB_URL = "http://192.168.28.94:4444";

	/*** Browser stack flags and values **/
	public static final Long BSTACK_IDLE_TIMEOUT = 120L;
	public static final Long BSTACK_IDLE_AUTOWAIT = 20L;
	public static final Long TIMEOUT = 60L;
	public static final Long POOLTIME = 4000L;

	public static final boolean isEmailReport = false;

	// Path
	public static final String REPORT_PATH = System.getProperty("user.dir") + "/ExtentReport/";
	public static final String DATAFILE = System.getProperty("user.dir") + "//src//test//resources//dataRepository//${var}.xlsx";
	public static String PROPERTERYFILE = System.getProperty("user.dir")+ "//src//test//resources//properties//${var}.properties";


	// All the Excel Related Values
	public static final String TESTCASES_SHEET = "TestCases";
	public static final String TCID_COL = "TCID";
	public static final String RESULTS_COL = "Result";
	public static final String ACTUALRESULTS_COL = "Actual Result";
	public static final String RUNMODE_COL = "RunMode";
	public static final String DESCRIPTION_COL = "Description";

	// API Values
	public static final String API_URI = "https://covid-193.p.rapidapi.com";
	public static final String GOOGLEAPI = "https://google-translate1.p.rapidapi.com";
	public static final String RapidAPI_KEY = "59102f52fdmsh89e61c9ff7a9cdfp123964jsn4497fab810ed";
	public static final String RapidAPI_HOST = "covid-193.p.rapidapi.com";
	public static final String RapidAPI_METHOD = "GET";

	public static String filePath;

}
