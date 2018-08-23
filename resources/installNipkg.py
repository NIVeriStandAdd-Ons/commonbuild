import os
import sys
import subprocess

nipkgPath = sys.argv[1]
currentDir = os.getcwd()
nipmAppPath = os.environ["ProgramW6432"]+"\\National Instruments\\NI Package Manager\\nipkg.exe"
nipkgRelPath = currentDir + "\\" + nipkgPath

print("Installing .nipkg file:\n", nipkgPath)
print(f'\'{nipkgRelPath}\'')

try:
	subprocess.run([nipmAppPath, "install", "-y", nipkgRelPath], stderr=subprocess.STDOUT, shell=True, check=True)

except subprocess.CalledProcessError as err:
	print(err.args)
	sys.exit(err.args[0])

	
