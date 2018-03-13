def call(componentConfig, lvVersion) {
   
   def releaseBranches = [:]
   def releaseBranchInfoKey = '${lvVersion}_release_branches'
   echo "$releaseBranchInfoKey"

   // Read the release branch info from configuration.json. 
   if(componentConfig.containsKey(releaseBranchInfoKey)) {
      releaseBranches = componentConfig.get(releaseBranchInfoKey)
      echo "Branches configured for release: $releaseBranches"
      return releaseBranches
   } else {
      echo "No branches configured for GitHub releases."
   // Return null if the key isn't found: don't push any releases.
      return null
   }
}

   