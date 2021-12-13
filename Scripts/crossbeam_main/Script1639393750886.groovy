import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

Path projectDir = Paths.get(RunConfiguration.getProjectDir())

String outputDirRelativePath = "out/crossbeam"

Path outputDir = projectDir.resolve(outputDirRelativePath)
if (Files.exists(outputDir)) {
	outputDir.toFile().deleteDir()
}

int numberOfDataRows = 50000
int threads = 8

int rowsPerBand = (numberOfDataRows / threads) + 1
//println rowsPerBand

for (int band = 1; band <= threads; band++) {
	int rangeFrom = 1 + rowsPerBand * (band - 1)
	int to = rangeFrom + rowsPerBand - 1
	int rangeTo = (to > numberOfDataRows) ? numberOfDataRows : to   
	WebUI.callTestCase(findTestCase("crossbeam_worker"), 
		[	"testSuiteName": "TSx",
			"testSuiteGuid": UUID.randomUUID().toString(), 
			"rangeFrom": rangeFrom,
			"rangeTo": rangeTo,
			"outputDirRelativePath": "out/crossbeam"
		])
}