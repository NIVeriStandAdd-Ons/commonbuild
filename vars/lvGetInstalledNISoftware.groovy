def call(lvVersion){
   echo "Getting list of installed NI Software using System Configuration API."
   bat "labview-cli --kill --lv-ver $lvVersion \"$WORKSPACE\\commonbuild\\lv\\build\\lvGetInstalledNISoftware.vi\" -- \"$WORKSPACE\""
}
