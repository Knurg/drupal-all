<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:param name="namespace"/>
	<xsl:template match="/">
		<rdf:RDF xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns:skos="http://www.w3.org/2004/02/skos/core#">
			<xsl:for-each select="//Subject">
				<xsl:variable name="thisURI"><xsl:value-of select="$namespace"/><xsl:value-of select="current()/@Subject_ID"/></xsl:variable>
				
				<skos:Concept>
					<xsl:attribute name="rdf:about"><xsl:value-of select="$thisURI"/></xsl:attribute>
					<xsl:call-template name="printLabels">
						<xsl:with-param name="node" select="."/>
					</xsl:call-template>
					
					<xsl:call-template name="printBroaders">
						<xsl:with-param name="node" select="."/>
					</xsl:call-template>
				
				</skos:Concept>
				
			</xsl:for-each>
		</rdf:RDF>		
	</xsl:template>

	
	<xsl:template name="printLabels">
		<xsl:param name="node"/>
		
		<xsl:for-each select="$node/Preferred_Term/Term_Text">
			<skos:prefLabel xmlns:skos="http://www.w3.org/2004/02/skos/core#"><xsl:value-of select="current()/text()"/></skos:prefLabel>
		</xsl:for-each>
		<xsl:for-each select="$node/Non-Preferred_Term/Term_Text">
			<skos:altLabel xmlns:skos="http://www.w3.org/2004/02/skos/core#"><xsl:value-of select="current()/text()"/></skos:altLabel>
		</xsl:for-each>
		
	</xsl:template>

	<xsl:template name="printBroaders">
		<xsl:param name="node"/>
	
		<xsl:for-each select="$node/Parent_Relationships//Parent_Subject_ID">
			<skos:broader xmlns:skos="http://www.w3.org/2004/02/skos/core#">
				<xsl:attribute name="rdf:resource"><xsl:value-of select="$namespace"/><xsl:value-of select="current()/text()"/></xsl:attribute>
			</skos:broader>
		</xsl:for-each>
		
	</xsl:template>
</xsl:stylesheet>