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

// IO
Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path outputDir = projectDir.resolve(outputDirRelativePath)

// prepare the transformer
Path stylesheet = projectDir.resolve("Include/scripts/xslt/crossbeam.xsl")
TransformerFactory tFactory = TransformerFactory.newInstance("net.sf.saxon.TransformerFactoryImpl",null);
Transformer transformer = createTransformer(tFactory, stylesheet, 
	[	"testSuiteName": testSuiteName,
		"testSuiteGuid": testSuiteGuid,
		"rangeFrom": rangeFrom,
		"rangeTo": rangeTo])

// transform the document
Path inputXml = projectDir.resolve("Test Suites/${testSuiteName}.ts")
Path outputXml = outputDir.resolve(
	"${testSuiteName}_${String.format("%05d", rangeFrom)}-${String.format("%05d", rangeTo)}.ts")
doTransform(transformer, inputXml, outputXml)
assert Files.exists(outputXml)

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
