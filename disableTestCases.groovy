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
            } else {
                caze.disabled = false
            }
	}
}

/**
* this groovy script enables all the test cases
* except whose name contains the string specified in the
* variable 'testNamePatternToDisable'
**/

//You may change the pattern required
def testNamePatternToDisable = 'disabled'

//Get the project object
def project = context.testCase.testSuite.project

//Loop thru the suite lise
project.testSuiteList.each { suite ->  
    //Loop thru the case list
    suite.testCaseList.each { caze ->
            //if test name contains the given pattern, then enable
      
            if (caze.name.contains(testNamePatternToDisable) == false) {
                caze.disabled = false	
            } 
       }
}
