def call(buildNumberID, componentName, configurationMap) {

   def buildNumber = 0
   def componentConfiguration = configurationMap.repositories[componentName]

   // Read last stored build number from component configuration. If a value exists, increment it. 
   if(componentConfiguration.containsKey(buildNumberID)) {
      def lastBuild = componentConfiguration[buildNumberID] as Integer
      buildNumber = lastBuild + 1
      return buildNumber

   //  If the component doesn't exist, add it to the map. 
   } else {
      componentConfiguration[buildNumberID] = buildNumber
      return buildNumber
   }
}

