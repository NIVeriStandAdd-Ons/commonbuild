import groovy.json.JsonSlurperClassic
import groovy.json.JsonOutput

def call(componentName, lvVersion) {

   def buildNumber = 0
   def configurationJsonFile = readJSON file: "configuration_${lvVersion}.json"
   def configurationMap = new JsonSlurperClassic().parseText(configurationJsonFile.toString())

   if(configurationMap.repositories.containsKey(componentName)) {
     def componentConfiguration = configurationMap.repositories[componentName]
     if(componentConfiguration.containsKey('build_number')) {
       buildNumber = 1 + componentConfiguration['build_number'] as Integer
       componentConfiguration['build_number'] = buildNumber
       echo "Next build number for ${componentName}: ${buildNumber}"
     } else {
       echo "Did not find build configuration for ${componentName}. Starting at Build # 0."
       configurationMap.repositories[componentName] = ['build_number': buildNumber]}
   }

   def configurationJSON = readJSON text: JsonOutput.toJson(configurationMap)
   def configurationJsonFileName = "configuration_${lvVersion}.json"

   // Write configuration to JSON file and then convert it back to TOML.
   writeJSON file: configurationJsonFileName, json: configurationJSON, pretty: 4
   bat "commonbuild\\resources\\configUpdate.bat $configurationJsonFileName"

   return buildNumber
}
