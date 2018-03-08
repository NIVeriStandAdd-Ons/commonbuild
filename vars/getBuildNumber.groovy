def call(buildID, componentID, configurationMap) {

   def buildNumber = 0

   // Read last stored build number from configurationMap. If a value exists, increment it. 
   if(configurationMap.repositories.containsKey(componentID)) {
      def componentConfig = configurationMap.repositories.get(componentID)
	  if(componentConfig.containsKey(buildID)) {
	     def lastBuild = componentConfig.get(buildID) as Integer
		 buildNumber = lastBuild + 1
	  }
	   return buildNumber

   //  If the component doesn't exist, add it to the map. 
   } else {
      configurationMap.repositories[componentID] = [buildID: buildNumber]
      return buildNumber
   }
}

