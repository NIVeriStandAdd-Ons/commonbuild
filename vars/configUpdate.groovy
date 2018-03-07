def call(configuration) {

   def configurationTOMLFile = new File("${WORKSPACE}\\commonbuild-configuration\\configuration.toml")
   def configurationTOML = configurationTOMLFile.newWriter()
   
   configuration.repositories.each { repository, properties ->
      configurationTOML << "[repositories.$repository]\n"
      properties.each { property, value ->
         configurationTOML << "${property} = '${value}'\n"
      }
      configurationTOML << "\n"
   }
   
   configurationTOML.flush()
   configurationTOML.close()
}
