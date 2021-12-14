<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:include href="identity.xsl"/>
	
	<xsl:output method="xml" indent="yes"/>
	
	<xsl:param name="testSuiteName"/>
	<xsl:param name="testSuiteGuid"/>
	<xsl:param name="rangeFrom"/>
	<xsl:param name="rangeTo"/>
	<xsl:param name="rangeValue"/>
	
	<xsl:template match="/">
		<xsl:message>testSuiteName=<xsl:value-of select="$testSuiteName"
		/>, testSuiteGuid=<xsl:value-of select="$testSuiteGuid"
		/>, rangeFrom=<xsl:value-of select="$rangeFrom"
		/>, rangeTo=<xsl:value-of select="$rangeTo"/></xsl:message>
		<xsl:if test="$testSuiteName = ''">
			<xsl:message terminate="yes">testSuiteName is empty</xsl:message>
		</xsl:if>
		<xsl:if test="$testSuiteGuid = ''">
			<xsl:message terminate="yes">testSuiteGuid is empty</xsl:message>
		</xsl:if>
		<xsl:if test="$rangeFrom = ''">
			<xsl:message terminate="yes">rangeFrom is empty</xsl:message>
		</xsl:if>
		<xsl:if test="$rangeTo = ''">
			<xsl:message terminate="yes">rangeTo is empty</xsl:message>
		</xsl:if>
		<xsl:if test="$rangeValue = ''">
			<xsl:message terminate="yes">rangeValue is empty</xsl:message>
		</xsl:if>
		<xsl:apply-templates/>
	</xsl:template>
	
	<xsl:template match="TestSuiteEntity">
		<xsl:copy>
			<xsl:apply-templates/>
		</xsl:copy>
	</xsl:template>
	
	<xsl:template match="name">
		<xsl:copy><xsl:value-of select="$testSuiteName"
		/>_<xsl:value-of select="$rangeFrom"
		/>-<xsl:value-of select="$rangeTo"
		/></xsl:copy>
	</xsl:template>
	
	<xsl:template match="testSuiteGuid">
		<xsl:copy><xsl:value-of select="$testSuiteGuid"/></xsl:copy>
	</xsl:template>
	
	<xsl:template match="iterationEntity[iterationType='ALL']">
		<xsl:copy>
			<iterationType>RANGE</iterationType>
			<value><xsl:value-of select="$rangeValue"/></value>
		</xsl:copy>
	</xsl:template>
	
</xsl:stylesheet>
