def call(lvVersion){
   echo 'Cloning commonbuild-configuration to workspace.'

   // Clone the build configuration repository to the workspace.
   // This is only used if building an unofficial .nipkg.
   def organization = getComponentParts()['organization']
   def configBranch = env."library.commonbuild-configuration.version"
   cloneRepo("https://github.com/$organization/commonbuild-configuration", configBranch)

   // Convert the build configuration file to JSON using batch file which invokes Python script.
   configurationTOMLPath = "commonbuild-configuration\\configuration_${lvVersion}.toml"
   bat "commonbuild\\resources\\buildConfigurationSetup.bat $configurationTOMLPath"
}
