def call(packageName, baseVersion, buildNumber, payloadDir) {

   // Add all files in payloadDir to a GitHub release.
   
   def branch = getComponentParts()['branch']
   def org = getComponentParts()['organization']
   def repo = getComponentParts()['repo']
   def nipkgVersion = "${baseVersion}.${buildNumber}"
   def releaseName = "${packageName}_${nipkgVersion}"
   def description = "$releaseName built from branch $branch."
   def nipkgPath = "${payloadDir}\\${releaseName}_windows_x64.nipkg"

   if(branch == 'master') {
      bat "github-release release --user $org --repo $repo --name $releaseName --tag $nipkgVersion --description \"${description}\""
   }
   if(branch == 'develop') {
      bat "github-release release --user $org --repo $repo --name $releaseName --tag $nipkgVersion --description \"${description}\" --pre-release"
   }
   
   bat "github-release upload --user $org --repo $repo --name $packageName --tag $nipkgVersion --file \"${nipkgPath}\""
}