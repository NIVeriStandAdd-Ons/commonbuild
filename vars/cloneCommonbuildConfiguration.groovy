def call(){
   echo 'Cloning commonbuild configuration to workspace.'
   
   def organization = getComponentParts()['organization']
   def branch = env."library.vs-common-build-configuration.version"
   commonbuildConfigDir = cloneRepo("https://github.com/$organization/commonbuild-configuration", branch)
   return commonbuildConfigDir
}
