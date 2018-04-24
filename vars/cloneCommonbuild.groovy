def call(){
   echo 'Cloning commonbuild steps to workspace.'
   
   def organization = getComponentParts()['organization']
   def branch = env."library.vs-common-build.version"
   def configBranch = env."library.commonbuild-configuration.version"
   commonbuildDir = cloneRepo("https://github.com/$organization/commonbuild", branch)
   commonbuildConfigDir = cloneRepo("https://github.com/$organization/commonbuild-configuration", configBranch)
   return commonbuildDir
}
