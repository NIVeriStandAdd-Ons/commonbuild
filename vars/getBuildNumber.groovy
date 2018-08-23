import groovy.json.JsonSlurperClassic
import groovy.json.JsonOutput

def call(componentName, lvVersion) {

   def buildNumber = 0
   def configurationJsonFile = readJSON file: "configuration_${lvVersion}.json"
   def configurationMap = new JsonSlurperClassic().parseText(configurationJsonFile.toStriong())
   def componentConfiguration = configurationMap.repositories[componentName]

   if(configurationMap.repositories.containsKey(componentName)) {
     if(componentConfiguration.containsKey('build_number')) {
        buildNumber = 1 + componentConfiguration['build_number'] as Integer
     }
   }
   // Update build number in configurationMap and return current build number.
   componentConfiguration['build_number'] = buildNumber
   def configurationJSON = readJSON text: JsonOutput.toJson(configurationMap)
   def configurationJsonFileName = "configuration_${lvVersion}.json"

   // Write configuration to JSON file and then convert it back to TOML.
   writeJSON file: configurationJsonFileName, json: configurationJSON, pretty: 4
   bat "commonbuild\\resources\\configUpdate.bat $configurationJsonFileName"

   return buildNumber

}
