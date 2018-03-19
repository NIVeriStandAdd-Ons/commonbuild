def call(buildNumberID, componentName, configurationMap) {

   def buildNumber = 0
   def componentConfiguration = configurationMap.repositories[componentName]

   // If the buildNumberID key exists then read the value and increment it. 
   if(componentConfiguration.containsKey(buildNumberID)) {
      def lastBuild = componentConfiguration[buildNumberID] as Integer
      buildNumber = lastBuild + 1
   }

   // Update build number in configurationMap and return current build number.
   componentConfiguration[buildNumberID] = buildNumber
   return buildNumber
   
}

