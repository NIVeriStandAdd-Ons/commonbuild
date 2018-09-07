package ni.vsbuild.packages

import groovy.json.JsonSlurperClassic
import groovy.json.JsonOutput

class Nipkg extends AbstractPackage {

   def devInstallLoc
   def packageDestination
   def devXmlPath
   def version

   Nipkg(script, packageInfo, payloadDir) {
      super(script, packageInfo, payloadDir)
      this.devInstallLoc = packageInfo.get('install_destination')
      this.devXmlPath = packageInfo.get('dev_xml_path')
      this.packageDestination = payloadDir
   }

   void buildPackage(lvVersion) {

      def packageInfo = """
         Building package from $payloadDir
         Staging path: $devInstallLoc
         LabVIEW/VeriStand version: $lvVersion
         Custom Device XML Path: $devXmlPath
         """.stripIndent()

      version = script.getDeviceVersion(devXmlPath, lvVersion)
      def stagingPathMap = ["${packageDestination}" : devInstallLoc]
      script.currentBuild.displayName = "$lvVersion #" + script.nipkg(packageDestination, version, stagingPathMap, lvVersion)
   }
}
