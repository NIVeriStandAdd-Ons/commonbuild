def call(devXmlPath) {
   def devXmlText = readFile devXmlPath
   def devXml = new XmlSlurper().parseText(devXmlText)
   def baseVersion = devXml.Version.text()

   return baseVersion
}

