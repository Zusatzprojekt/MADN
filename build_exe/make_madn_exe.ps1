$oldErrorAction = $ErrorActionPreference;
$ErrorActionPreference = 'Stop';

try {
$NSIS_PROJECT_LINK = "https://sourceforge.net/projects/nsis";
$ADOPTIUM_API_LINK = "https://api.adoptium.net";
$ADOPTIUM_DWN_LINK = "https://github.com/adoptium/";

$BASE_DIR = "$(Split-Path -Path $PSScriptRoot -Parent)";
$TOOL_DIR = $PSScriptRoot;
$INTELLIJ_PROJ_INFO = "$BASE_DIR\.idea\misc.xml";
$NSIS_SCRIPT_LOCATION = "$TOOL_DIR\make_madn_exe.nsi";
$NSIS_EXE_OUTFILE = "$BASE_DIR\target\madn.exe";
$NSIS_EXE_ICON = "$TOOL_DIR\madn.ico";
$JLINK_APP_DIR = "$BASE_DIR\target\app";

$uris = & "$TOOL_DIR\toolbox\Convert-UrlStringToUri.ps1" -URL $NSIS_PROJECT_LINK, $ADOPTIUM_API_LINK, $ADOPTIUM_DWN_LINK;

$ae = [char]0x00E4;
$ue = [char]0x00FC;
$ue_gr = [char]0x00DC;

$JDK_FOLDER_NAME = "jdk";
$NSIS_FOLDER_NAME = "nsis";

if ($null -eq (Get-Command -Name curl.exe -ErrorAction SilentlyContinue)) {
    throw "'curl.exe' muss vorhanden und in 'Path' hinterlegt sein."
}

if ($null -eq (Get-Command -Name tar.exe -ErrorAction SilentlyContinue)) {
    throw "'tar.exe' muss vorhanden und in 'Path' hinterlegt sein."
}

Write-Host "==== Starte Build =====".PadRight([System.Console]::WindowWidth, '=');
Set-Location -Path $BASE_DIR;
Write-Host "";


Write-Host "---- Starte Verbindungstest ----".PadRight([System.Console]::WindowWidth, '-');
Write-Host "Teste Verbindung zu $(($uris | ForEach-Object { $_.Host }) -join ', ') ...";
& "$TOOL_DIR\toolbox\Test-UrlConnections.ps1" -URL ($uris | ForEach-Object { $_.Host }) -TimeoutMillis 10000 | Out-Null;
Write-Host "Verbindungen erfolgreich!";
Write-Host "---- Verbindungstest abgeschlossen ----".PadRight([System.Console]::WindowWidth, '-');
Write-Host "";


Write-Host "---- Bereite Build-Umgebung vor ----".PadRight([System.Console]::WindowWidth, '-');
Write-Host "Teste Ordner 'programme' ...";
if (Test-Path -Path "$BASE_DIR\target" -PathType Container) {
    Write-Host "Ordner 'target' existiert bereits. Bereinige ...";
    Remove-Item -Path "$BASE_DIR\target\*" -Recurse | Out-Null;
    Write-Host "Ordner 'target' bereinigt";
} else {
    Write-Host "Erzeuge Ordner 'target' ...";
    New-Item -Path "$BASE_DIR\" -Name 'target' -ItemType Directory | Out-Null;
    Write-Host "Ordner 'target' erzeugt";
}

if (Test-Path -Path "$TOOL_DIR\programs" -PathType Container) {
    Write-Host "Ordner 'programs' existiert bereits. Bereinige ...";
    Remove-Item -Path "$TOOL_DIR\programs\*" -Recurse | Out-Null;
    Write-Host "Ordner 'programs' bereinigt";
} else {
    Write-Host "Erzeuge Ordner 'programs' ...";
    New-Item -Path "$TOOL_DIR\" -Name 'programs' -ItemType Directory | Out-Null;
    Write-Host "Ordner 'programs' erzeugt";
}

Write-Host "---- Build-Umgebung vorbereitet ----".PadRight([System.Console]::WindowWidth, '-');
Write-Host "";


Write-Host "---- Suche Abh${ae}ngigkeiten ----".PadRight([System.Console]::WindowWidth, '-');
Write-Host "Erkenne Projekt JDK-Version ...";
$jdk_version_xml = [xml](Get-Content -Path $INTELLIJ_PROJ_INFO);
$jdk_version_str = ($jdk_version_xml.project.component | Where-Object { $_.languageLevel }).languageLevel;
$jdk_version = [UInt32]($jdk_version_str | Select-String -Pattern '(?<=^JDK_\d_)\d+|\d{2,}').Matches[0].Value;
Write-Host "Version '$jdk_version_str' erkannt!";

Write-Host "Suche Adoptium JDK Version $jdk_version ...";
$java = & "$TOOL_DIR\toolbox\Get-AdoptiumJdkVersion.ps1" -MajorVersion $jdk_version -OS windows -Arch x64 -ReleaseType package;
Write-Host "Version '$($java.name)' gefunden!";

Write-Host "Suche aktuellste NSIS-Version ...";
$nsis = & "$TOOL_DIR\toolbox\Get-SourceforgeRelease.ps1" -URI $uris[0] -ReleaseType 'application/zip' -MinFileSize 1500000 -TitleRegex '^(?!.*?Pre-{0,1}release).*';
Write-Host "Version '$($nsis.name)' gefunden!";
Write-Host "---- Abh${ae}ngigkeiten-Suche abgeschlossen ----".PadRight([System.Console]::WindowWidth, '-');
Write-Host "";


Write-Host "---- Starte Downloads ----".PadRight([System.Console]::WindowWidth, '-');
Write-Host "Lade '$($java.name)' herunter ...";
curl.exe -L $java.link -o "$TOOL_DIR\programs\$JDK_FOLDER_NAME.zip" -#;
if ($LASTEXITCODE -ne 0) {
    throw "curl.exe failed with exit code $LASTEXITCODE";
}
[System.Console]::SetCursorPosition(0, [System.Console]::CursorTop - 1);
Write-Host "'$($java.name)' heruntergeladen!".PadRight([System.Console]::WindowWidth);

Write-Host "${ue_gr}berpr${ue}fe checksumme von '$($java.name)' ...";
$java_checksum = (Get-FileHash -Path "$TOOL_DIR\programs\$JDK_FOLDER_NAME.zip" -Algorithm "$($java.checksum.Keys[0])").Hash;
if ($java_checksum.ToLower() -ne $java.checksum[$java.checksum.Keys[0]].ToLower()) {
    throw "Checksum of '$($java.name)' does not match";
}
Write-Host "Checksumme ${ue}berpr${ue}ft!";

Write-Host "Lade '$($nsis.name)' herunter ...";
curl.exe -L $nsis.link -o "$TOOL_DIR\programs\$NSIS_FOLDER_NAME.zip" -#;
if ($LASTEXITCODE -ne 0) {
    throw "curl.exe failed with exit code $LASTEXITCODE";
}
[System.Console]::SetCursorPosition(0, [System.Console]::CursorTop - 1);
Write-Host "'$($nsis.name)' heruntergeladen!".PadRight([System.Console]::WindowWidth);

Write-Host "${ue_gr}berpr${ue}fe checksumme von '$($nsis.name)' ...";
$nsis_checksum = (Get-FileHash -Path "$TOOL_DIR\programs\$NSIS_FOLDER_NAME.zip" -Algorithm "$($nsis.checksum.Keys[0])").Hash;
if ($nsis_checksum.ToLower() -ne $nsis.checksum[$nsis.checksum.Keys[0]].ToLower()) {
    throw "Checksum of '$($nsis.name)' does not match";
}
Write-Host "Checksumme ${ue}berpr${ue}ft!";
Write-Host "---- Downloads abgeschlossen ----".PadRight([System.Console]::WindowWidth, '-');
Write-Host "";


Write-Host "---- Extrahiere Downloads ----".PadRight([System.Console]::WindowWidth, '-');
Write-Host "Erstelle Ziel-Ordner '$JDK_FOLDER_NAME' ...";
New-Item -Path "$TOOL_DIR\programs\" -Name "$JDK_FOLDER_NAME" -ItemType Directory | Out-Null;
Write-Host "Ziel-Ordener '$JDK_FOLDER_NAME' erstellt!";

Write-Host "Extrahiere '$JDK_FOLDER_NAME.zip' into '$JDK_FOLDER_NAME' ...";
tar.exe -xf "$TOOL_DIR\programs\$JDK_FOLDER_NAME.zip" -C "$TOOL_DIR\programs\$JDK_FOLDER_NAME\";
if ($LASTEXITCODE -ne 0) {
    throw "tar.exe failed with exit code $LASTEXITCODE";
}
Write-Host "'$JDK_FOLDER_NAME.zip' extrahiert!";

Write-Host "Entferne Archiv '$JDK_FOLDER_NAME.zip' ...";
Remove-Item -Path "$TOOL_DIR\programs\$JDK_FOLDER_NAME.zip" -Force | Out-Null;
Write-Host "Archiv '$JDK_FOLDER_NAME.zip' entfernt!";

Write-Host "Erstelle Ziel-Ordner '$NSIS_FOLDER_NAME' ...";
New-Item -Path "$TOOL_DIR\programs\" -Name "$NSIS_FOLDER_NAME" -ItemType Directory | Out-Null;
Write-Host "Ziel-Ordener '$NSIS_FOLDER_NAME' erstellt!";

Write-Host "Extrahiere '$NSIS_FOLDER_NAME.zip' into '$NSIS_FOLDER_NAME' ...";
tar.exe -xf "$TOOL_DIR\programs\$NSIS_FOLDER_NAME.zip" -C "$TOOL_DIR\programs\$NSIS_FOLDER_NAME\";
if ($LASTEXITCODE -ne 0) {
    throw "tar.exe failed with exit code $LASTEXITCODE";
}
Write-Host "'$NSIS_FOLDER_NAME.zip' extrahiert!";

Write-Host "Entferne Archiv '$NSIS_FOLDER_NAME.zip' ...";
Remove-Item -Path "$TOOL_DIR\programs\$NSIS_FOLDER_NAME.zip" -Force | Out-Null;
Write-Host "Archiv '$NSIS_FOLDER_NAME.zip' entfernt!";
Write-Host "---- Extrahieren abgeschlossen ----".PadRight([System.Console]::WindowWidth, '-');
Write-Host "";


Write-Host "---- Setze Umgebungsvariablen ----".PadRight([System.Console]::WindowWidth, '-');
Write-Host "Setze 'JAVA_HOME' auf '$($java.name)' ...";
$jdk_path = (Get-ChildItem -Path "$TOOL_DIR\programs\$JDK_FOLDER_NAME\" -Directory)[0].FullName;
$env:JAVA_HOME = $jdk_path;
Write-Host "'JAVA_HOME' gesetzt!";

Write-Host "F${ue}ge '$($java.name)' zu 'Path' hinzu ...";
$env:Path = "$jdk_path\bin;" + $env:Path;
Write-Host "'$($java.name)' zu 'Path' hinzuge${ue}gt!";

Write-Host "F${ue}ge '$($nsis.name)' zu 'Path' hinzu ...";
$nsis_path = (Get-ChildItem -Path "$TOOL_DIR\programs\$NSIS_FOLDER_NAME\" -Directory)[0].FullName;
$env:Path = "$nsis_path;" + $env:Path;
Write-Host "'$($nsis.name)' zu 'Path' hinzugef${ue}gt!";
Write-Host "---- Umgebungsvariablen gesetzt ----".PadRight([System.Console]::WindowWidth, '-');
Write-Host "";


Write-Host "---- Starte Maven-Build ----".PadRight([System.Console]::WindowWidth, '-');
& "$BASE_DIR\mvnw.cmd" "-ntp" "javafx:jlink";
if ($LASTEXITCODE -ne 0) {
    throw "mvnw.cmd failed with exit code $LASTEXITCODE";
}
Write-Host "---- Maven-Build abgeschlossen ----".PadRight([System.Console]::WindowWidth, '-');
Write-Host "";


Write-Host "---- Erzeuge ausf${ue}hrbare Datei (exe) ----".PadRight([System.Console]::WindowWidth, '-');
makensis.exe /V3 /DOUTFILE=$NSIS_EXE_OUTFILE /DICONFILE=$NSIS_EXE_ICON /DAPPDIR=$JLINK_APP_DIR "$NSIS_SCRIPT_LOCATION";
if ($LASTEXITCODE -ne 0) {
    throw "makensis.exe failed with exit code $LASTEXITCODE";
}
Write-Host "---- Ausf${ue}hrbare Datei (exe) erzeugt ----".PadRight([System.Console]::WindowWidth, '-');
Write-Host "";


Write-Host "---- Build-Umgebung bereinigen ----".PadRight([System.Console]::WindowWidth, '-');

if (Test-Path -Path "$TOOL_DIR\programs" -PathType Container) {
    Write-Host "Entferne Build-Abh${ae}ngigkeiten ...";
    Remove-Item -Path "$TOOL_DIR\programs" -Recurse | Out-Null;
    Write-Host "Build-Abh${ae}ngigkeiten entfernt!";
}

if (Test-Path -Path "$BASE_DIR\target" -PathType Container) {
    Write-Host "Entferne jlink-Output ...";
    Remove-Item -Path "$BASE_DIR\target\*" -Recurse -Exclude '*.exe' | Out-Null;
    Write-Host "jlink-Output entfernt!";
}

Write-Host "---- Build-Umgebung bereinigt ----".PadRight([System.Console]::WindowWidth, '-');
Write-Host "";

Write-Host "==== Build abgeschlossen ====".PadRight([System.Console]::WindowWidth, '=');
Write-Host ""
Write-Host "Die Ausf${ue}hrbare Datei (exe) liegt hier: $(Split-Path -Path $NSIS_EXE_OUTFILE -Parent)";
Write-Host ""
Write-Host "========".PadRight([System.Console]::WindowWidth, '=');

} finally {
    $ErrorActionPreference = $oldErrorAction;
}




