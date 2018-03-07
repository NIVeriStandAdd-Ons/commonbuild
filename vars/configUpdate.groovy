def call(configuration) {
   
   // Create updated configuration string and write it back to configuration.toml file. 
   def configurationTOML
   
   configuration.repositories.each { repository, properties ->
      configurationTOML.append("[repositories.$repository]\n")
      properties.each { property, value ->
         configurationTOML.append("${property} = '${value}'\n")
      }
      configurationTOML.append("\n")
   }
   
   writeFile file: "commonbuild-configuration\\configuration.toml", text: configurationTOML
}
