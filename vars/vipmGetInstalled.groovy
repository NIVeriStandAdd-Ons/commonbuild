def call(lvVersion){
   echo "Getting list of installed VI Packages."
   bat "(labview-cli --kill --lv-ver $lvVersion \"$WORKSPACE\\commonbuild\\lv\\vipm\\vipmGetInstalled.vi\" -- \"$WORKSPACE\") || ((echo \"Failed to get list of installed VI Packages. Continuing.\") && (SET ERRORLEVEL=0))"
}
