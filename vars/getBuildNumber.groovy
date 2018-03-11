def call(buildNumberID, componentConfiguration, configurationMap) {

   def buildNumber = 0

   // Read last stored build number from configurationMap. If a value exists, increment it. 
   if(componentConfiguration.containsKey(buildNumberID)) {
      def lastBuild = componentConfig.get(buildID) as Integer
      buildNumber = lastBuild + 1
      return buildNumber

   //  If the component doesn't exist, add it to the map. 
   } else {
      componentConfiguration[buildNumberID] = [buildNumberID: buildNumber]
      return buildNumber
   }
}

