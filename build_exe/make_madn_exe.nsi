!define EXE_VERSION "1.0.0.0"

OutFile ${OUTFILE}
Icon ${ICONFILE}

VIProductVersion ${EXE_VERSION}

; English language
VIAddVersionKey /LANG=1033 "FileDescription" "A digital version of the game 'Mensch ärgere Dich nicht'. Playable from 2 to 4 Players with offline multiplayer."
VIAddVersionKey /LANG=1033 "ProductName" "Mensch ärgere Dich nicht"
VIAddVersionKey /LANG=1033 "Link" "https://github.com/Zusatzprojekt/MADN"
VIAddVersionKey /LANG=1033 "LegalCopyright" ""
VIAddVersionKey /LANG=1033 "FileVersion" ${EXE_VERSION}

; German language
VIAddVersionKey /LANG=1031 "FileDescription" "Eine digitale Version von 'Mensch ärgere Dich nicht'. Spielbar mit 2 - 4 Spielern im Offline-Multiplayer."
VIAddVersionKey /LANG=1031 "ProductName" "Mensch ärgere Dich nicht"
VIAddVersionKey /LANG=1031 "Link" "https://github.com/Zusatzprojekt/MADN"
VIAddVersionKey /LANG=1031 "LegalCopyright" ""
VIAddVersionKey /LANG=1031 "FileVersion" ${EXE_VERSION}

RequestExecutionLevel user
SetCompressor zlib

SilentInstall silent
ShowInstDetails nevershow
AutoCloseWindow true

Var COUNTER

Function .onInit
    StrCpy $COUNTER 0

loop:
    System::Call "kernel32::GetTickCount()i .r0"
    StrCpy $INSTDIR "$TEMP\madn-$0-$COUNTER"

    IfFileExists "$INSTDIR\*" 0 name_ok
        IntOp $COUNTER $COUNTER + 1
        Goto loop

name_ok:
FunctionEnd

Section "Extract and Run" S1
    CreateDirectory $INSTDIR

    SetOutPath "$INSTDIR"
    File /r "${APPDIR}\bin"
    File /r "${APPDIR}\conf"
    File /r "${APPDIR}\legal"
    File /r "${APPDIR}\lib"
    File "${APPDIR}\release"

    nsExec::Exec '"$INSTDIR\bin\app.bat" $CMDLINE'
SectionEnd

Function .onInstSuccess
    SetOutPath $TEMP
    RMDir /r "$INSTDIR"
FunctionEnd

Function .onInstFailed
    SetOutPath $TEMP
    RMDir /r "$INSTDIR"
FunctionEnd
