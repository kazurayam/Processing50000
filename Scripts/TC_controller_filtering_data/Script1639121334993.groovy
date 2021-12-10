import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable

String pattern = GlobalVariable.DataStartsWithPattern
println "pattern=${pattern}"

File data = new File("./data.csv")
int count = 0
data.eachLine { line, lineNumber ->
	List<String> items = line.split(" ") as List
	if (items[1].toUpperCase().startsWith(pattern.toUpperCase())) {
		WebUI.callTestCase(findTestCase("TC_worker"), ["seq": items[0], "data": items[1]])
		count += 1
	}	
}
println "count=${count}"