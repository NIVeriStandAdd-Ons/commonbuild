package ni.vsbuild.packages

import groovy.json.JsonSlurperClassic

class Nipkg extends AbstractPackage {

   def stagingPath
   def devXmlPath
   def baseVersion
   def configurationMap
   def buildNumber
   def componentID
   def buildID
   def configurationJsonFile
   
   Nipkg(script, packageInfo, payloadDir) {
      super(script, packageInfo, payloadDir)
      this.stagingPath = packageInfo.get('install_destination')
      this.devXmlPath = packageInfo.get('dev_xml_path')
   }

   void buildPackage(lvVersion) {

      // Get MAJOR.MINOR.PATCH versions from custom device XML file.
      baseVersion = script.getDeviceVersion(devXmlPath)

      // Lookup strings for the build number within configuration.toml. 
      componentID = script.getComponentParts()['repo']
      buildID = lvVersion+'_build_number'
      script.echo "Getting ${buildID} for ${componentID}."
      configurationJsonFile = script.readJSON file: 'configuration.json'
      configurationMap = new JsonSlurperClassic().parseText(configurationJsonFile.toString())
      buildNumber = script.getBuildNumber(componentID, configurationMap)

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

