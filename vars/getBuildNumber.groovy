def call(componentID, configurationMap) {

   def buildNumber = 0

   // Read last stored build number from configurationMap. If a value exists, increment it. 
   def componentConfig = configurationMap.repositories.get(componentID)
   def lastBuild = componentConfig.get(buildID) as Integer
   if(lastBuild) {
      buildNumber = lastBuild + 1
   }
   return buildNumber

}
