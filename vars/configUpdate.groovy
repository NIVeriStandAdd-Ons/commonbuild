def call(configurationMap, lvVersion) {

   def configurationJsonFileName = "configuration_${lvVersion}.json"   

   // Write configuration to JSON file and then convert it back to TOML. 
   writeJSON file: configurationJsonFileName, json: configurationMap, pretty: 4
   bat "commonbuild\\resources\\configUpdate.bat $configurationJsonFileName"

}

