// Copies the config file specifying all of the VeriStand assemblies
// and versions, then replaces the versions based on the lvVersion
def call(projectPath, lvVersion){
   echo "Copying configuration file for $projectPath"
   def configFileName = "$projectPath" + ".config"
   
   def defaultVersion = ["$lvVersion": "${lvVersion}.0.0.0"]
   def assemblyVersions = readProperties defaults: defaultVersion, file: "commonbuild/resources/assemblyVersions.properties"

   def newAssemblyVersion = assemblyVersions."$lvVersion"
   
   def fileContent = readFile "commonbuild/resources/LabVIEW.exe.config"
   
   fileContent = fileContent.replaceAll("(newVersion=\")[^\"]+","\$1$newAssemblyVersion")
   
   writeFile file: configFileName, text: fileContent
}
