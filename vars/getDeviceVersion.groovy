def call(devXmlPath, lvVersion) {
   baseVersion = sh(returnStdout: true, script: "labview-cli --kill --lv-ver $lvVersion \"$WORKSPACE\\commonbuild\\lv\\nipkg\\readCustomDeviceVersionFromXML.vi\" --\"devXmlPath\""
   echo baseVersion
   return baseVersion
}

