package ni.vsbuild.packages

import groovy.json.JsonSlurperClassic
import groovy.json.JsonOutput

class VsStepsPackage extends AbstractPackage {

   def nipkgVersion
   def nipkgInfo
   def typesVersion
   def tsVersions
   def vsVersion
   def configurationMap
   def configurationJson
   def configurationJsonFile
   def componentName
   def componentBranch
   def releaseBranches
   def buildNumber
   def paddedBuildNumber

   VsStepsPackage(script, packageInfo, payloadDir) {
      super(script, packageInfo, payloadDir)
      this.typesVersion = packageInfo.get('types_version')
      this.tsVersions = packageInfo.get('teststand_versions')
      this.buildNumber = 0
   }

   void buildPackage(lvVersion) {
      def packageInfo = """
         Building package $name from $payloadDir
         Package version: $nipkgVersion
         TestStand versions: $tsVersions
         """.stripIndent()

      componentName = script.getComponentParts()['repo']
      componentBranch = script.getComponentParts()['branch']

      // Read and parse configuration.json file to get next build number. 
      script.echo "Getting 'build_number' for ${componentName}."
      configurationJsonFile = script.readJSON file: "configuration_${lvVersion}.json"
      configurationMap = new JsonSlurperClassic().parseText(configurationJsonFile.toString())

     if(configurationMap.repositories.containsKey(componentName)) {
         buildNumber = script.getBuildNumber(componentName, configurationMap)
         script.echo "Next build number: $buildNumber"
      } else { 
         configurationMap.repositories[componentName] = ['build_number': 0] 
      }
      
      configurationJson = script.readJSON text: JsonOutput.toJson(configurationMap)
      
      paddedBuildNumber = "$buildNumber".padLeft(3, '0')
      nipkgVersion = typesVersion+"+$paddedBuildNumber"
      vsVersion = lvVersion
      script.echo packageInfo
      nipkgInfo = script.vsStepsPackage(nipkgVersion, vsVersion)

      script.configUpdate(configurationJson, lvVersion)
      script.vipmGetInstalled(lvVersion)
      releaseBranches = script.getReleaseInfo(componentName, configurationMap, lvVersion)
      script.configPush(buildNumber, componentName, lvVersion) 
      script.pushRelease(nipkgInfo, payloadDir, releaseBranches, lvVersion)
   }
}
