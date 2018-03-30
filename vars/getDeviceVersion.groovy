def call(devXmlPath) {
   def devXmlText = readFile devXmlPath
   devXmlText = devXmlText.trim().replaceFirst("^([\\W]+)<","<")
   def devXml = new XmlSlurper().parseText(devXmlText)
   def baseVersion = devXml.Version.text()

   return baseVersion
}

