def call(configuration) {
   
   // Create updated configuration string and write it back to configuration.toml file. 
   def configurationTOML = ''
   
   configuration.repositories.each { repository, properties ->
      configurationTOML = configurationTOML + "[repositories.$repository]\n"
      properties.each { property, value ->
         configurationTOML = configurationTOML + "${property} = '${value}'\n"
      }
      configurationTOML = configurationTOML + "\n"
   }
   
   writeFile file: "commonbuild-configuration\\configuration.toml", text: configurationTOML
}
