def call(componentConfig, lvVersion) {
   
   def releaseBranches = [:]
   def releaseBranchInfoKey = "$lvVersion_release_branches"

   // Read the release branch info from configuration.json. The list of branches determines whether to push releases or not.
   if(componentConfig.containsKey(releaseBranchInfoKey)) {
      releaseBranches = componentConfig.get(releaseBranchInfoKey)
      return releaseBranches
   }
   // Return null if the key isn't found: don't push any releases.
   return null
}

   