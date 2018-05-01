package ni.vsbuild.packages

import groovy.json.JsonSlurperClassic
import groovy.json.JsonOutput

class Nipkg extends AbstractPackage {

   def stagingPath
   def devXmlPath
   def baseVersion
   def configurationMap
   def configurationJSON
   def componentName
   def componentBranch
   def configurationJsonFile
   def nipkgInfo
   def releaseBranches
   def buildNumber
   
   Nipkg(script, packageInfo, payloadDir) {
      super(script, packageInfo, payloadDir)
      this.stagingPath = packageInfo.get('install_destination')
      this.devXmlPath = packageInfo.get('dev_xml_path')
      this.buildNumber = 0
   }

   void buildPackage(lvVersion) {
 
      def packageInfo = """
         Building package $name from $payloadDir
         Staging path: $stagingPath
         LabVIEW/VeriStand version: $lvVersion
         Custom Device XML Path: $devXmlPath
         Build number: $buildNumber
         """.stripIndent()

      script.nipkg(payloadDir, baseVersion, buildNumber, componentBranch, stagingPath, lvVersion)

   }
}
