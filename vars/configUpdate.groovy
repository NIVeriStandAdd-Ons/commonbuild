def call(configurationJson) {

   writeFile file: 'configuration.json', text: configurationJson
}

