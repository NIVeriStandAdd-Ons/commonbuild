def call(){
   echo 'Cloning commonbuild steps to workspace.'
   
   def organization = getComponentParts()['organization']
   def branch = env."library.vs-common-build.version"
   def configBranch = env."library.commonbuild-configuration.version"
   def commonbuildDir = 'commonbuild'
   cloneRepo("https://github.com/$organization/commonbuild", branch)
   cloneRepo("https://github.com/$organization/commonbuild-configuration", configBranch)
   return commonbuildDir
}
