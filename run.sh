#!/bin/bash
echo "[SHELL] compile C"
gcc out.c -o app
chmod +x app
./app