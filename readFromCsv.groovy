def groovyUtils = new com.eviware.soapui.support.GroovyUtils(context)

//get the csv test file with 
def csvFilePath = "C:\\test_data_file_name.csv"
context.fileReader = new BufferedReader(new FileReader(csvFilePath))

//parse the csv file line by line
rowsData = context.fileReader.readLines()
int rowsize = rowsData.size()
for(int i = 1;  i < rowsize;  i++)
{

    rowdata = rowsData[i]
    String[] data = rowdata.split(",")
    
    //get the values from records
    def value_from_first_column = data[0]	
    def value_from_second_column = data[1]

   //assign the values to test properties
   testRunner.testCase.setPropertyValue('test_case_variable',value_from_first_column);	
   testRunner.testCase.testSuite.project.setPropertyValue('test_suite_variable',value_from_second_column);
   
   // EXECUTE REQUEST FOR EACH VALUE IN CSV
   testRunner.runTestStepByName( "HTTP Request test step name")  
   //timeout between iterations
   sleep(500)
}
