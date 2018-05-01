def call(devXmlPath, lvVersion) {
   bat "labview-cli --kill --lv-ver $lvVersion \"$WORKSPACE\\commonbuild\\lv\\nipkg\\readCustomDeviceVersionFromXML.vi\" -- \"${WORKSPACE}\" \"${devXmlPath}\""
   def baseVersion = readFile "deviceVersion"
   return baseVersion
}
