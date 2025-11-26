#!/usr/bin/env sh
# Run the transmitter using the bundled Linux x64 JRE
# Usage: ./run-transmitter-linux_x64 path/to/properties.file

set -eu

SCRIPT_DIR="$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)"

if [ "$#" -ne 1 ]; then
  echo "Usage: $(basename "$0") path/to/properties.file" >&2
  exit 1
fi
PROPS="$1"

# Find the platform-specific JRE directory (versioned folder matching *-linux_x64)
JRE_DIR=""
for d in "$SCRIPT_DIR"/*-linux_x64; do
  if [ -d "$d" ]; then
    JRE_DIR="$d"
    break
  fi
done
if [ -z "$JRE_DIR" ]; then
  echo "Bundled JRE for linux_x64 not found next to this script." >&2
  exit 3
fi
JAVA_BIN="$JRE_DIR/bin/java"
if [ ! -x "$JAVA_BIN" ]; then
  echo "java binary not executable at: $JAVA_BIN" >&2
  exit 4
fi

# Locate the fat JAR
JAR_FILE=""
for f in "$SCRIPT_DIR"/*-jar-with-dependencies.jar; do
  if [ -f "$f" ]; then
    JAR_FILE="$f"
    break
  fi
done
if [ -z "$JAR_FILE" ]; then
  echo "Fat JAR (*-jar-with-dependencies.jar) not found next to this script." >&2
  exit 5
fi

exec "$JAVA_BIN" -jar "$JAR_FILE" --properties="$PROPS"
