def call(packageName, baseVersion, buildNumber, payloadDir) {

   // Add all files in payloadDir to a GitHub release.
   
   def branch = getComponentParts()['branch']
   def org = getComponentParts()['organization']
   def repo = getComponentParts()['repo']
   def versionTag = "${baseVersion}.${buildNumber}"
   def releaseName = "${packageName}-${versionTag}"
   def description = "$releaseName built from branch $branch."

   if(branch == 'master') {
      bat "github-release release --user $org --repo $repo --name $releaseName --tag $versionTag --description \"${description}\""
   }
   if(branch == 'develop') {
      bat "github-release release --user $org --repo $repo --name $releaseName --tag $versionTag --description \"${description}\" --pre-release"
   }
}