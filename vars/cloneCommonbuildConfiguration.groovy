def call(){
   echo 'Cloning commonbuild-configuration to workspace.'

   def organization = getComponentParts()['organization']
   def configBranch = env."library.commonbuild-configuration.version"
   cloneRepo("https://github.com/$organization/commonbuild-configuration", configBranch)
}
