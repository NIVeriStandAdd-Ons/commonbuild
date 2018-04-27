@echo on

SET nipkgFilePath=%1
SET nipmAppPath="C:\\Program Files\\National Instruments\\NI Package Manager\\nipkg.exe"

%nipmAppPath% install -y %nipkgFilePath%

EXIT
