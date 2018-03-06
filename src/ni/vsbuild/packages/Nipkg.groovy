package ni.vsbuild.packages

import groovy.json.JsonSlurperClassic

class Nipkg extends AbstractPackage {

   def stagingPath
   def devXmlPath
   
   Nipkg(script, packageInfo, payloadDir) {
      super(script, packageInfo, payloadDir)
      this.stagingPath = packageInfo.get('install_destination')
      this.devXmlPath = packageInfo.get('dev_xml_path')
   }

   void buildPackage(lvVersion) {
      def packageInfo = """
         Building package $name from $payloadDir
         Staging path: $stagingPath
         LabVIEW/VeriStand version: $lvVersion
         Custom Device XML Path: $devXmlPath
         """.stripIndent()
      script.echo packageInfo
      
      script.cloneCommonbuildConfiguration()
      script.configSetup()

      def componentConfigJsonFile = script.readJSON file: 'configuration.json'
      def componentConfigStringMap = new JsonSlurperClassic().parseText(componentConfigJsonFile.toString())
      def repo = script.getComponentParts()['repo']
      def branch = script.getComponentParts()['branch']
      def componentID = repo+'-'+branch

      script.echo "Getting build version number for ${componentID}."
      
      def componentConfig = componentConfigStringMap.repositories.get(componentID)
      def buildNumber = componentConfig.get('build') as Integer
      buildNumber = buildNumber + 1
      def commitMessage = "updating ${componentID} to build number ${buildNumber}."
      componentConfig << [build:buildNumber]

      script.configUpdate(componentID, componentConfigStringMap)
      baseVersion = script.getDeviceVersion(devXmlPath)
    //script.configPush(commitMessage)
      script.buildNipkg(payloadDir, baseVersion, buildNumber, stagingPath, lvVersion)
      
   }
}

