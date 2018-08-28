def call(lvVersion){
   echo 'Cloning commonbuild-configuration to workspace.'

   def organization = getComponentParts()['organization']
   def configBranch = env."library.commonbuild-configuration.version"
   cloneRepo("https://github.com/$organization/commonbuild-configuration", configBranch)
   configurationTOMLPath = "commonbuild-configuration\\configuration_${lvVersion}.toml"
   bat "commonbuild\\resources\\buildConfigurationSetup.bat $configurationTOMLPath"
}
