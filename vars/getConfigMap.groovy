import groovy.json.JsonSlurperClassic

def call() {
   // Get build number from configuration.json, increment it, and update the configuration map. 
   def configurationJsonFile = script.readJSON file: 'configuration.json'
   def configurationMap = new JsonSlurperClassic().parseText(configurationJsonFile.toString())
   return configurationMap
}