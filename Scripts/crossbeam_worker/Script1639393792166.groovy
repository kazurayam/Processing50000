import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import javax.xml.transform.Result
import javax.xml.transform.Source
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

import com.kms.katalon.core.configuration.RunConfiguration

/**
 * 
 */
assert testSuiteName != null
assert testSuiteGuid != null
assert rangeFrom != 0
assert rangeTo != 0
assert outputDirRelativePath != null

rangeValue = rangeFrom + "-" + rangeTo         //    -> "1-99"

rangeFrom = String.format("%05d", Integer.valueOf(rangeFrom))   //  1 -> "00001"
rangeTo   = String.format("%05d", Integer.valueOf(rangeTo))     // 99 -> "00099"

Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path outputDir = projectDir.resolve(outputDirRelativePath)
Files.createDirectories(outputDir)


/// copy *.groovy file
Path sourceGroovy = projectDir.resolve("Test Suites").resolve("${testSuiteName}.groovy")
Path targetGroovy = outputDir.resolve("${testSuiteName}_${rangeFrom}-${rangeTo}.groovy")
Files.copy(sourceGroovy, targetGroovy)


/// generate *.ts file by XSLT

// prepare the transformer
Path stylesheet = projectDir.resolve("Include/scripts/xslt/crossbeam.xsl")
TransformerFactory tFactory = TransformerFactory.newInstance("net.sf.saxon.TransformerFactoryImpl",null);
Transformer transformer = createTransformer(tFactory, stylesheet, 
	[	"testSuiteName": testSuiteName,
		"testSuiteGuid": testSuiteGuid,
		"rangeFrom": rangeFrom,
		"rangeTo": rangeTo,
		"rangeValue": rangeValue
		])

// transform the document
Path inputXml = projectDir.resolve("Test Suites/${testSuiteName}.ts")
Path outputXml = outputDir.resolve(
	"${testSuiteName}_${rangeFrom}-${rangeTo}.ts")
doTransform(transformer, inputXml, outputXml)
assert Files.exists(outputXml)
println "outputXml=${outputXml}"

Transformer createTransformer(TransformerFactory tFactory, Path stylesheet, Map<String, String> parameters) {
	Source xsltSource = getSourceOf(stylesheet)
	Transformer tr = tFactory.newTransformer(xsltSource)
	parameters.each { key, value ->
		tr.setParameter(key, value)
	}
	return tr
}

/**
 * 
 */
def doTransform(Transformer transformer, Path inputXml, Path outputXml) {
	Source tsSource = getSourceOf(inputXml)
	Files.createDirectories(outputXml.getParent())
	Result copyResult = getResultOf(outputXml)
	transformer.transform(tsSource, copyResult)
}

/**
 * 
 */
Source getSourceOf(Path p) {
	Source source = new StreamSource(new InputStreamReader(new FileInputStream(p.toFile()), "UTF-8"))
	source.setSystemId(p.toString())
	return source
}

/**
 * 
 */
Result getResultOf(Path p) {
	return new StreamResult(new OutputStreamWriter(new FileOutputStream(p.toFile()),"UTF-8"))
}
