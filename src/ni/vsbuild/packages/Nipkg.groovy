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
   def nipkgName
   
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

      // Read and parse configuration.json file to get next build number. 
      script.echo "Getting ${buildID} for ${componentID}."
      configurationJsonFile = script.readJSON file: 'configuration.json'
      configurationMap = new JsonSlurperClassic().parseText(configurationJsonFile.toString())
      buildNumber = script.getBuildNumber(buildID, componentID, configurationMap)

      def packageInfo = """
         Building package $name from $payloadDir
         Staging path: $stagingPath
         LabVIEW/VeriStand version: $lvVersion
         Custom Device XML Path: $devXmlPath
         Build number: $buildNumber
         """.stripIndent()

      // Build the nipkg. 
      script.echo packageInfo
      nipkgName = script.buildNipkg(payloadDir, baseVersion, buildNumber, stagingPath, lvVersion)

      // Update the configuration map, save it to disk, and push to github.com\{your_org}\commonbuild-configuration. 
      script.configUpdate(buildNumber, buildID, componentID, configurationMap)
      script.configPush(buildNumber, componentID, lvVersion) 
      script.pushRelease(nipkgName, baseVersion, buildNumber, payloadDir)
   }
}

