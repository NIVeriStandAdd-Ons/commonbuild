package ni.vsbuild.packages

import groovy.json.JsonSlurperClassic

class Nipkg extends AbstractPackage {

   def stagingPath
   def devXmlPath
   def buildNumber
   
   Nipkg(script, packageInfo, payloadDir) {
      super(script, packageInfo, payloadDir)
      this.stagingPath = packageInfo.get('install_destination')
      this.devXmlPath = packageInfo.get('dev_xml_path')
      this.buildNumber = 0 // Temporary until this is available with the rest of the config.
   }

   void buildPackage(lvVersion) {

      // Get MAJOR.MINOR.PATCH versions from custom device XML file.
      def baseVersion = script.getDeviceVersion(devXmlPath)

      // Lookup strings for the build number within configuration.toml. 
      def componentID = script.getComponentParts()['repo']
      def buildID = lvVersion+'_build_number'

      script.echo "Getting ${buildID} for ${componentID}."

      // Get build number from configuration.json, increment it, and update the configuration map. 
      def configurationJsonFile = script.readJSON file: 'configuration.json'
      def configurationMap = new JsonSlurperClassic().parseText(configurationJsonFile.toString())
      def componentConfig = configurationMap.repositories.get(componentID)
      buildNumber = componentConfig.get(buildID) as Integer
      buildNumber = buildNumber + 1
      componentConfig[buildID] = buildNumber

      // Build the nipkg. 
      def packageInfo = """
         Building package $name from $payloadDir
         Staging path: $stagingPath
         LabVIEW/VeriStand version: $lvVersion
         Custom Device XML Path: $devXmlPath
         Build number: $buildNumber
         """.stripIndent()
      script.echo packageInfo
      script.buildNipkg(payloadDir, baseVersion, buildNumber, stagingPath, lvVersion)

      // Update the configuration map, save it to disk, and push to github.com\{your_org}\commonbuild-configuration. 
      def commitMessage = "Updating ${componentID} for VeriStand ${lvVersion} to build number ${buildNumber}."
      script.configUpdate(configurationMap)
      script.configPush(commitMessage) 
      
   }
}

