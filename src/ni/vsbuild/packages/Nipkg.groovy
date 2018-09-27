package ni.vsbuild.packages

import groovy.json.JsonSlurperClassic
import groovy.json.JsonOutput

class Nipkg extends AbstractPackage {

   def devInstallLoc
   def packageDestination
   def devXmlPath
   def version
   def nipkgStagingPathMap

   Nipkg(script, packageInfo, payloadDir) {
      super(script, packageInfo, payloadDir)
      this.devInstallLoc = packageInfo.get('install_destination')
      this.devXmlPath = packageInfo.get('dev_xml_path')
      this.packageDestination = payloadDir
      this.nipkgStagingPathMap = packageInfo.get('nipkg_staging_paths')
   }

   void buildPackage(lvVersion) {

      version = script.getDeviceVersion(devXmlPath, lvVersion)
      if(devInstallLoc?.trim()) {
         nipkgStagingPathMap = ["${packageDestination}" : devInstallLoc]
      }
      
      def packageInfo = """
         Staging paths: $nipkgStagingPathMap
         .nipkg version: $version
         LabVIEW/VeriStand version: $lvVersion
         Custom Device XML Path: $devXmlPath
         """.stripIndent()
      
      script.echo packageInfo
      script.currentBuild.displayName = "$lvVersion #" + script.nipkg(packageDestination, version, nipkgStagingPathMap, lvVersion)
   }
}
