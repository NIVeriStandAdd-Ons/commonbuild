def call(configurationJson, lvVersion) {

   def configurationJsonFileName = "configuration_${lvVersion}.json"   

   // Write configuration to JSON file and then convert it back to TOML. 
   writeFile file: configurationJsonFileName, text: configurationJson
   bat "commonbuild\\resources\\configUpdate.bat $configurationJsonFileName"

}

