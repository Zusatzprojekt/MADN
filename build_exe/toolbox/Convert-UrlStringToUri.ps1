#Requires -Version 3.0
<#
.SYNOPSIS
    Validates and converts one or more URL strings into System.Uri objects.

.DESCRIPTION
    Parses each provided string as an absolute URI using [System.Uri]::TryCreate.
    If any input is not a valid absolute URI, a terminating error is thrown identifying the offending value.
    Returns a single System.Uri when one URL is provided, or a System.Uri[] when multiple URLs are provided.

.PARAMETER URL
    One or more absolute URL strings to validate and convert (e.g., https://example.com/path).

.EXAMPLE
    PS> .\Convert-UrlStringToUri.ps1 -URL "https://github.com/app/page"
    https://github.com/app/page

.EXAMPLE
    PS> .\Convert-UrlStringToUri.ps1 -URL "https://github.com", "ftp://ftp.example.net/file.txt"
    https://github.com
    ftp://ftp.example.net/file.txt

.OUTPUTS
    System.Uri
    System.Uri[]

.NOTES
    Implementation uses System.Uri.TryCreate with UriKind.Absolute.
    Relative paths and local file paths are rejected.
    Created: 2025-08-13
    Requires: PowerShell 3.0 or later
#>

# ==== Parameter ==========================================================================================================================================================

param (
    [Parameter(Mandatory=$true)]
    [string[]] $URL
)

# ==== Funktionen =========================================================================================================================================================
function Get-UriFromString {
    param (
        [Parameter(Mandatory=$true)]
        [string[]] $URL
    );
    
    $uriList = New-Object System.Collections.ArrayList;

    $URL.ForEach({
        $tempUri = "";
        if (![uri]::TryCreate($_, [System.UriKind]::Absolute, [ref] $tempUri)) {
            throw "Invalid URL: '$_'";
        } else {
            $uriList.Add($tempUri) | Out-Null;
        }
    });

    if ($uriList.Count -gt 1) {
        return $uriList.ToArray()
    } else {
        return $uriList.Item(0)
    }
}

# ==== Hauptprogramm ======================================================================================================================================================

return Get-UriFromString -URL $URL;
