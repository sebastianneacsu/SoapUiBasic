import net.sf.json.groovy.*

//get data from REST test request
def responseHolderHttp = testRunner.testCase.testSteps["Get First Element"].testRequest.response.responseContent;

//parse the data
def responseHttp = new JsonSlurper().parseText(responseHolderHttp)

//get the data from Database test 
def responseHolder = testRunner.testCase.testSteps["JDBC Request"].testRequest.response.responseContent;

//parse the data
def list = new XmlParser().parseText(responseHolder) 

//get the data from custom variables
def prop = testRunner.testCase.properties['propertyName']

//parse db data for the row with the id we want
def tac = list.ResultSet.'*'.find{ node ->
	node.ID.text() == prop.value
}

//check the REST values against the Database
 try {
  assert tac.ANNEX[0].value()[0] == responseHttp.annex: "DB value = REST value" 
  assert tac.CATEGORY[0].value()[0] == responseHttp.category: "DB value = REST value"
   

} catch (AssertionError e) {
	log.info("Wrong values returned. REST <> DATABASE: " + e.getMessage())
}


//compare number of results from DB to REST
try {
  assert exercise.Row.size() == responseHttp.size(): "DB  count = REST count" 
} catch (AssertionError e) {
	log.info("Wrong number of results: " + e.getMessage())
}

//prompt for data and set value to test suite property
import com.eviware.soapui.support.*
def alert = com.eviware.soapui.support.UISupport
def cookie = alert.prompt("Paste JSESSION ID COOKIE","JSESSION ID COOKIE")
testRunner.testCase.testSuite.setPropertyValue( "session_cookie", cookie)
