import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable

int start = GlobalVariable.start
int end = GlobalVariable.end

println "start=${start}, end=${end}"

File data = new File("./data.csv")
data.eachLine { line, lineNumber ->
	if (start <= lineNumber && lineNumber <= end) {
		List<String> items = line.split(" ") as List
		WebUI.callTestCase(findTestCase("TC_worker"), ["seq": items[0], "data": items[1]])
	}
}