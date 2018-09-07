import os
import sys
import subprocess

operation = sys.argv[1]
nipkgPath = sys.argv[2]
currentDir = os.getcwd()
nipmAppPath = os.environ["ProgramW6432"]+"\\National Instruments\\NI Package Manager\\nipkg.exe"
nipkgRelPath = currentDir + "\\" + nipkgPath

print("Installing .nipkg file:\n", nipkgPath)
print(f'\'{nipkgRelPath}\'')

try:
	subprocess.run([nipmAppPath, operation, "-y", nipkgRelPath], stderr=subprocess.STDOUT, shell=True, check=True)

except subprocess.CalledProcessError as err:
	print(err.args)
	sys.exit(err.args[0])
