def call(nipkgInfo, payloadDir, lvVersion) {

   // Add all files in payloadDir to a GitHub release.
   def nipkgVersion = nipkgInfo['version']
   def nipkgName = nipkgInfo['name']
   def branch = getComponentParts()['branch']
   def org = getComponentParts()['organization']
   def repo = getComponentParts()['repo']
   def tagString = "${lvVersion}-${nipkgVersion}"
   def releaseName = "${nipkgName}_${nipkgVersion}"
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