def call(configurationJson) {

   writeFile file: 'configuration.json', text: configurationJson
   bat "commonbuild\\resources\\saveConfig.bat"

}

