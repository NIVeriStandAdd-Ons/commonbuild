def call(packageName, baseVersion, buildNumber, payloadDir) {

   // Add all files in payloadDir to a GitHub release.
   
   def branch = getComponentParts()['branch']
   def org = getComponentParts()['organization']
   def repo = getComponentParts()['repo']
   def nipkgVersion = "${baseVersion}.${buildNumber}"
   def releaseName = "${packageName}_${nipkgVersion}"
   def description = "$releaseName built from branch $branch."
   def nipkgPath = "${payloadDir}\\${releaseName}.nipkg"

   if(branch == 'master') {
      bat "github-release release --user $org --repo $repo --name $releaseName --tag $versionTag --description \"${description}\""
   }
   if(branch == 'develop') {
      bat "github-release release --user $org --repo $repo --name $releaseName --tag $versionTag --description \"${description}\" --pre-release"
   }
   
   bat "github-release upload --user $org --repo $repo --name $packageName --tag $versionTag --file \"${nipkgPath}\""
}