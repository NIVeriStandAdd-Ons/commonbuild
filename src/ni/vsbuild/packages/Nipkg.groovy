package ni.vsbuild.packages

import groovy.json.JsonSlurperClassic
import groovy.json.JsonOutput

class Nipkg extends AbstractPackage {

   def stagingPath
   def devXmlPath
   
   Nipkg(script, packageInfo, payloadDir) {
      super(script, packageInfo, payloadDir)
      this.stagingPath = packageInfo.get('install_destination')
      this.devXmlPath = packageInfo.get('dev_xml_path')
   }

   void buildPackage(lvVersion) {
 
      def packageInfo = """
         Building package $name from $payloadDir
         Staging path: $stagingPath
         LabVIEW/VeriStand version: $lvVersion
         Custom Device XML Path: $devXmlPath
         """.stripIndent()

      script.nipkg(payloadDir, devXmlPath, stagingPath, lvVersion)

   }
}
