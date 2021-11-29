import subprocess
import sys

if sys.platform == "win32":
    EXE = ".\\gradlew.bat"
else:
    EXE = "./gradlew"

ARGS = [
    EXE,
    "--daemon",
    "ktlintFormat"
]

proc = subprocess.run(ARGS, stdout=sys.stdout, stderr=sys.stderr)
sys.exit(proc.returncode)
