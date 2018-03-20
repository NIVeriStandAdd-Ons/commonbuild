def call(configurationMap, lvVersion) {

   def configurationJsonFileName = "configuration_${lvVersion}.json"   
   def configurationJSON = new.net.sf.json.JSONObject()
   configurationJSON.putAll(configurationMap)

   // Write configuration to JSON file and then convert it back to TOML. 
   writeJSON file: configurationJsonFileName, json: configurationJSON, pretty: 4
   bat "commonbuild\\resources\\configUpdate.bat $configurationJsonFileName"

}

