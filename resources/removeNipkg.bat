@echo on

SET nipkgFilePath=%1
SET nipmAppPath="C:\\Program Files\\National Instruments\\NI Package Manager\\nipkg.exe"

%nipmAppPath% remove -y %nipkgFilePath%

EXIT
