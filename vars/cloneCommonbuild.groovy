def call(){
   echo 'Cloning commonbuild steps to workspace.'

   def organization = getComponentParts()['organization']
   def branch = env."library.vs-common-build.version"
   def commonbuildDir = 'commonbuild'
   cloneRepo("https://github.com/$organization/commonbuild", branch)
   return commonbuildDir
}
