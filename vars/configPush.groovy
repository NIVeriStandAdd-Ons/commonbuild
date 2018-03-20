def call(buildNumber, componentID, lvVersion) {

   // Push the updated build configuration back to GitHub configuration repository. 
   echo "Updating build number for ${componentID} (${lvVersion}) to ${buildNumber} in commonbuild-configuration repository."
   def commitMessage = "Updating ${componentID} for VeriStand ${lvVersion} to build number ${buildNumber}."
   bat "git --work-tree=commonbuild-configuration --git-dir=commonbuild-configuration\\.git commit -a -m \"$commitMessage\""
   bat "git --work-tree=commonbuild-configuration --git-dir=commonbuild-configuration\\.git pull origin master"
   bat "git --work-tree=commonbuild-configuration --git-dir=commonbuild-configuration\\.git push --set-upstream origin master"
}
