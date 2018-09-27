def call(lvVersion){

  // Use the VIPM LabVIEW API to get a list of installed VI Packages.
  // This is broken in VIPM for LV 2018 hence the conditional error handling.
   echo "Getting list of installed VI Packages."
   bat "labview-cli --kill --lv-ver $lvVersion \"$WORKSPACE\\commonbuild\\lv\\vipm\\vipmGetInstalled.vi\" -- \"$WORKSPACE\""
}
