def call(packageName, baseVersion, buildNumber, payloadDir, lvVersion) {

   // Add all files in payloadDir to a GitHub release.
   
   def branch = getComponentParts()['branch']
   def org = getComponentParts()['organization']
   def repo = getComponentParts()['repo']
   def nipkgVersion = "${baseVersion}.${buildNumber}"
   def tagString = "${lvVersion}-${nipkgVersion}-$branch"
   def releaseName = "${packageName}_${nipkgVersion}"
   def description = "$releaseName built from branch $branch."
   def nipkgPath = "${payloadDir}\\${releaseName}_windows_x64.nipkg"

   if(branch == 'master') {
      bat "github-release release --user $org --repo $repo --target $branch --name $releaseName --tag $tagString --description \"${description}\""
   }
   if(branch == 'develop') {
      bat "github-release release --user $org --repo $repo --target $branch --name $releaseName --tag $tagString --description \"${description}\" --pre-release"
   }
   
   bat "github-release upload --user $org --repo $repo --name \"${releaseName}_windows_x64.nipkg\" --tag $tagString --file \"${nipkgPath}\""
}