#/bin/sh

ls -td logs/gc-* | tail -n+5 | xargs rm 2>/dev/null
