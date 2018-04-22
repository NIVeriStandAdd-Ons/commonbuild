def call(devXmlPath, lvVersion) {
   bat "labview-cli --kill --lv-ver $lvVersion \"$WORKSPACE\\commonbuild\\lv\\nipkg\\readCustomDeviceVersionFromXML.vi\" -- \"${WORKSPACE}"\ \"devXmlPath\""
   baseVersion = readFile "deviceVersion"
   echo baseVersion
   return baseVersion
}

