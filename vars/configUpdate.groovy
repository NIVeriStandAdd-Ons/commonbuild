def call(configuration) {
   
   // Create updated configuration string and write it back to configuration.toml file. 
   def configurationTOML = ''
   
   configuration.repositories.each { repository, properties ->
      configurationTOML + "[repositories.$repository]\n"
      properties.each { property, value ->
         configurationTOML + "${property} = '${value}'\n"
      }
      configurationTOML + "\n"
   }
   
   writeFile file: "commonbuild-configuration\\configuration.toml", text: configurationTOML
}
