def call(buildNumberID, componentName, configurationMap) {

   def buildNumber = 0

   // Read last stored build number from configurationMap. If a value exists, increment it. 
   if(configurationMap.repositories.containsKey(componentName.buildNumberID)) {
      def lastBuild = configurationMap.get(repositories.componentName.buildNumberID) as Integer
      buildNumber = lastBuild + 1
      return buildNumber

   //  If the component doesn't exist, add it to the map. 
   } else {
      configurationMap.repositories[componentName.buildNumberID] = buildNumber
      return buildNumber
   }
}

