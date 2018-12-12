/**
* this groovy script disables all the test cases
* whose name contains the string specified in the
* variable 'testNamePatternToDisable'
**/

//You may change the pattern required
def testNamePatternToDisable = 'negative'

//Get the project object
def project = context.testCase.testSuite.project

//Loop thru the suite lise
project.testSuiteList.each { suite ->  
    //Loop thru the case list
    suite.testCaseList.each { caze ->
           //if test name contains the given pattern, then disable, enable otherwise.
              if (caze.name.toLowerCase().contains(testNamePatternToDisable)) {
                caze.disabled = true
                
                //add temporary_disabled_ to the test case name
                def testCaseName = caze.getName()
                caze.setName("temporary_disabled_" + testCaseName)
            } else {
                caze.disabled = false
            }
	}
}


/**
* this groovy script enables all the test cases
* whose name contains the string specified in the
* variable 'testNamePatternToDisable'
**/

//You may change the pattern required
def testNamePatternToDisable = 'temporary_disabled_'

//Get the project object
def project = context.testCase.testSuite.project

//Loop thru the suite lise
project.testSuiteList.each { suite ->  
    //Loop thru the case list
    suite.testCaseList.each { caze ->
            //if test name contains the given pattern, then enable
      
            if (caze.name.contains(testNamePatternToDisable)) {
                caze.disabled = false
 			 
 			 //remove temporary_disabled_ from test case name
 			 def testCaseName = caze.getName()
 			 def reg = ~/^temporary_disabled_/   //Match 'temporary_disabled_' if it is at the beginning of the String
			 caze.setName(testCaseName - reg)
                	
            } 
       }
}
