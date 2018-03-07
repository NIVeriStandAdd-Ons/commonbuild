def call(buildNumber, buildID, componentID, configurationMap) {
   
   // Update build number for component being built.
   def componentConfiguration = configurationMap.repositories.get(componentID)
   componentConfiguration[buildID] = buildNumber

   // Create updated configuration string and write it back to configuration.toml file. 
   def configurationTOML = ''
   
   configurationMap.repositories.each { repository, properties ->
      configurationTOML = configurationTOML + "[repositories.$repository]\n"
      properties.each { property, value ->
         configurationTOML = configurationTOML + "${property} = '${value}'\n"
      }
      configurationTOML = configurationTOML + "\n"
   }
   
   writeFile file: "commonbuild-configuration\\configuration.toml", text: configurationTOML
}

