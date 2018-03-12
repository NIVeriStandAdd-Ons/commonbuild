package ni.vsbuild.packages

import groovy.json.JsonSlurperClassic
import groovy.json.JsonOutput

class Nipkg extends AbstractPackage {

   def stagingPath
   def devXmlPath
   def baseVersion
   def configurationMap
   def buildNumber
   def componentName
   def componentBranch
   def buildNumberID
   def configurationJsonFile
   def nipkgInfo
   def releaseBranches
   def componentConfiguration
   
   Nipkg(script, packageInfo, payloadDir) {
      super(script, packageInfo, payloadDir)
      this.stagingPath = packageInfo.get('install_destination')
      this.devXmlPath = packageInfo.get('dev_xml_path')
   }

   void buildPackage(lvVersion) {

      componentName = script.getComponentParts()['repo']
      componentBranch = script.getComponentParts()['branch']
      buildNumberID = lvVersion+'_build_number'

      // Get MAJOR.MINOR.PATCH versions from custom device XML file.
      baseVersion = script.getDeviceVersion(devXmlPath)

      // Read and parse configuration.json file to get next build number. 
      script.echo "Getting ${buildNumberID} for ${componentName}."
      configurationJsonFile = script.readJSON file: 'configuration.json'
      configurationMap = new JsonSlurperClassic().parseText(configurationJsonFile.toString())

      componentConfiguration = configurationMap.repositories.get(componentName)
      buildNumber = script.getBuildNumber(buildNumberID, componentConfiguration, configurationMap)
      componentConfiguration[buildNumberID] = "$buildNumber"

      def updatedConfigurationJson = JsonOutput.prettyPrint(JsonOutput.toJson(configurationMap))
      script.echo updatedConfigurationJson

      def packageInfo = """
         Building package $name from $payloadDir
         Staging path: $stagingPath
         LabVIEW/VeriStand version: $lvVersion
         Custom Device XML Path: $devXmlPath
         Build number: $buildNumber
         """.stripIndent()

      // Build the nipkg. 
      script.echo packageInfo
      nipkgInfo = script.buildNipkg(payloadDir, baseVersion, buildNumber, componentBranch, stagingPath, lvVersion)

      // Update the configuration map, save it to disk, and push to github.com\{your_org}\commonbuild-configuration. 
      script.configUpdate(updatedConfigurationJson)
      releaseBranches = script.getReleaseInfo(componentConfiguration, lvVersion)
      echo "$releaseBranches"
      // script.configPush(buildNumber, componentName, lvVersion) 
      // script.pushRelease(nipkgInfo, payloadDir, releaseBranches, lvVersion)
   }
}

