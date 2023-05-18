@echo off
cd /d D:\Battleship\Battleship\src\com\almasb\battleship\sounds
for %%i in (*.mp3) do (
    ffmpeg -i "%%i" "%%~ni.wav"
)
