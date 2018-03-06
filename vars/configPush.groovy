def call(commitMessage) {
   echo "Pushing to commonbuild-configuration repository."
   bat "git --work-tree=commonbuild-configuration --git-dir=commonbuild-configuration\\.git commit -a -m \"$commitMessage\""
   bat "git --work-tree=commonbuild-configuration --git-dir=commonbuild-configuration\\.git push --set-upstream origin feature/initial-development"
}