<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" omit-xml-declaration="no" indent="yes"/>
    <xsl:param name="project"/>
    <xsl:template match="/">
        <html>
            <title>Groups</title>
            <body>
                <h1>Groups in the project</h1>
                <table border="1">
                    <tbody>
                        <tr><th>name of group</th><th>status</th></tr>
                            <xsl:for-each
                                    select="/*[name()='Payload']/*[name()='Projects']/*[name()='Project']/*[name()='Title' and text()[. = $project]]
                                    /following-sibling::*[name()='Groups']/*[name()='Group']">
                                <tr>
                                    <td><xsl:value-of select="text()"/></td>
                                    <td><xsl:value-of select="@status"/></td>
                                </tr>
                            </xsl:for-each>
                    </tbody>
                </table>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>