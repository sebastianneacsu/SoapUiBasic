// Try-catch block to handle exceptions
try {
// 1. Create a "SoapUIResults" folder in the project path 
      // Retrieve the project root folder
      def projectPath = new com.eviware.soapui.support.GroovyUtils(context).projectPath
      // Specify a folder inside project root to store the results
      String folderPath = projectPath + "/SoapUIResults";
      // Create a File object for the specified path
      def resultFolder = new File(folderPath);
      // Check for existence of folder and create a folder
      if(!resultFolder.exists())
      {
        resultFolder.mkdirs();
      }
/* ------------------------------------------------------------------------------- */
// 2. Create a subfolder (with timestamp) to store the request-response local copy 
      // Retrieve the latest execution date-time
      Date d = new Date();
      //Added HH_mm_ss for a better precision, and to fix the reporting of multiple tests in a single csv file
      def executionDate = d.format("dd-MMM-yyyy HH_mm_ss");
      //Get the first characters from test case name
      String rule = testRunner.testCase.name;
      def ruleId = rule.substring(0,18);
	 //get the suite name to add to the folder
	 String suite = testRunner.testCase.testSuite.name;
	 def suiteName = suite.substring(0,14);
      // Specify the subfolder path with name Request-Response_CurrentTimeStamp
      String subfolderPath1 = folderPath+ "/TestCase "+suiteName+" "+ruleId+" "+executionDate;
      // Create this sub-folder
      new File(subfolderPath1).mkdirs();
/* ------------------------------------------------------------------------------- */
// 3. Create another subfolder "CSV Reports" to store the reports file 
      // Specify the subfolder path with name CSV Reports
      String subfolderPath2 = folderPath+ "/CSV Reports";
      // Create this sub-folder
      new File(subfolderPath2).mkdirs();
/* ------------------------------------------------------------------------------- */
// 4. Create a Report.csv file inside the CSV Reports folder 
      // Create a File object for Report csv file (with timestamp)
      def reportFile = new File(subfolderPath2, "Report_"+executionDate+".csv");
      // Check for existence of report file and create a file
      if(!reportFile.exists())
      {
        reportFile.createNewFile();
        // Create required column names in the report file
        reportFile.write('"Test Suite","Test Case","Test Step","Step Type","Step Status",'
                        +'"Result message","Execution Date"');
      }
/* ------------------------------------------------------------------------------- */
// 5. Inserting data in the file
      // Iterate over all the test steps results
  for(stepResult in testRunner.getResults())
  {
    // Retrieve Test Suite name
   def testSuite = testRunner.testCase.testSuite.name;
   // Retrieve Test Case name
   def testCase = testRunner.testCase.name;
   // Retrieve Test Step
   def testStep = stepResult.getTestStep();
   // Retrieve Test Step name
   def testStepName = testStep.name
   // Retrieve Test Step type
   def type = testStep.config.type
   // Retrieve Test Step status
   def status = stepResult.getStatus()
   // Creating new line in report file
   reportFile.append('\n');
   // Write all the necessary information in the file
   reportFile.append('"' + testSuite + '",');
   reportFile.append('"' + testCase + '",');
   reportFile.append('"' + testStepName + '",');
   reportFile.append('"' + type + '",');
   reportFile.append('"' + status + '",');
   // Retrieve the test result messages
   reportFile.append('"');
   for(resMessage in stepResult.getMessages())
   {
     // Write messages and separate multiple messages by new line
     reportFile.append('Message:' + resMessage + '\n');
   }
   reportFile.append('",');
   //Write executionDate in the file
   reportFile.append('"' + executionDate + '",');
/* ------------------------------------------------------------------------------- */
// 6. Extract the request and response and save it to external file
      // Verify if the test step type is request: SOAP project or restrequest: REST project
      if((type=="request").or(type=="restrequest"))
        {
          // Extract the request from the test step
      def extRequest = testStep.properties["RawRequest"].value;    
      // Create a file in the reports folder and write the request
      // Naming convention: request_TestSuiteName_TestCaseName_TestStepName.txt
     // def requestFile=subfolderPath1+"/request_"+testSuite+"_"+testCase+"_"+testStepName+".txt";

     // Naming convention for extra long Test Case Names
      def requestFile=subfolderPath1+"/Request_"+executionDate+".xml";
      
      def rqfile = new File(requestFile);
      rqfile.write(extRequest, "UTF-8");
      // Extract the response from the test step
      def extResponse = stepResult.getResponseContent();    
      // Create a file in the reports folder and write the response
      // Naming convention: response_TestSuiteName_TestCaseName_TestStepName.txt
      //def responseFile=subfolderPath1+"/response_"+testSuite+"_"+testCase+"_"+testStepName+".txt";

      // Naming convention for extra long Test Case Names
      def responseFile=subfolderPath1+"/Response_"+executionDate+".xml";
      
      def rsfile = new File(responseFile);
      rsfile.write(extResponse, "UTF-8");
     }
     else if (type == "mockresponse"){
     	def validationResponse = testStep.properties["Request"].value;    
		def validationFile=subfolderPath1+"/Request_validation_"+executionDate+".xml";
		def rsvfile = new File(validationFile);
		rsvfile.write(validationResponse, "UTF-8");	
     }
   }
 }
catch(exc)
{
   log.error("Exception happened: " + exc.toString());
}
