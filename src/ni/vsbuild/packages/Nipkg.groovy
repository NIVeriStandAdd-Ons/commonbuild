package ni.vsbuild.packages

import groovy.json.JsonSlurperClassic
import groovy.json.JsonOutput

class Nipkg extends AbstractPackage {

   def stagingPath
   def devXmlPath
   def baseVersion
   def configurationMap
   def configurationJSON
   def componentName
   def componentBranch
   def configurationJsonFile
   def nipkgInfo
   def releaseBranches
   def buildNumber
   
   Nipkg(script, packageInfo, payloadDir) {
      super(script, packageInfo, payloadDir)
      this.stagingPath = packageInfo.get('install_destination')
      this.devXmlPath = packageInfo.get('dev_xml_path')
      this.buildNumber = 0
   }

   void buildPackage(lvVersion) {

      componentName = script.getComponentParts()['repo']
      componentBranch = script.getComponentParts()['branch']

      // Get MAJOR.MINOR.PATCH versions from custom device XML file.
      baseVersion = script.getDeviceVersion(devXmlPath)

      // Read and parse configuration.json file to get next build number. 
      script.echo "Getting 'build_number' for ${componentName}."
      configurationJsonFile = script.readJSON file: "configuration_${lvVersion}.json"
      configurationMap = new JsonSlurperClassic().parseText(configurationJsonFile.toString())

      if(configurationMap.repositories.containsKey(componentName)) {
         buildNumber = script.getBuildNumber(componentName, configurationMap)
         script.echo "Next build number: $buildNumber"
      } else { 
         configurationMap.repositories[componentName] = ['build_number': buildNumber] 
      }

      configurationJSON = script.readJSON text: JsonOutput.toJson(configurationMap)
 
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
      script.configUpdate(configurationJSON, lvVersion)
      releaseBranches = script.getReleaseInfo(componentName, configurationMap, lvVersion)
      script.configPush(buildNumber, componentName, lvVersion) 
      script.pushRelease(nipkgInfo, payloadDir, releaseBranches, lvVersion)
   }
}

