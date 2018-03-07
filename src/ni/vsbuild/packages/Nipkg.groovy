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

      def componentID = script.getComponentParts()['repo']
      def buildID = lvVersion+'_build_version'
      script.echo "BuildID: $buildID"

      script.echo "Getting build version number for ${componentID}."
      def configurationJsonFile = script.readJSON file: 'configuration.json'
      def configurationMap = new JsonSlurperClassic().parseText(configurationJsonFile.toString())
      def componentConfig = configurationMap.repositories.get(componentID)
      def buildNumber = componentConfig.get(buildID) as Integer
      buildNumber = buildNumber + 1
      def commitMessage = "Updating ${componentID} for VeriStand ${lvVersion} to build number ${buildNumber}."
      componentConfig << [$buildID:buildNumber]

      script.configUpdate(configurationMap)
      def baseVersion = script.getDeviceVersion(devXmlPath)
      script.buildNipkg(payloadDir, baseVersion, buildNumber, stagingPath, lvVersion)
      script.configPush(commitMessage) //Push updated build number to commonbuild-configuration repository.
      
   }
}

