#Requires -Version 5.1
<#
.SYNOPSIS
    Returns the first SourceForge release download URL and md5 checksum from a project's RSS feed that matches the given filters.

.DESCRIPTION
    This script takes a SourceForge project URL, fetches its RSS feed of releases, applies various filters (file type, title regex, link regex, size range), and returns the download link and MD5 checksum for the most recent match.

.PARAMETER URI
    Absolute SourceForge project URL (e.g., https://sourceforge.net/projects/MY_PROJECT). Must be well-formed.

.PARAMETER ReleaseType
    File content type to match in the RSS item's content.type (e.g., application/zip). Default: application/zip.

.PARAMETER TitleRegex
    Regular expression applied to the RSS item's <title>.

.PARAMETER DownloadLinkRegex
    Regular expression applied to the RSS item's <link>.

.PARAMETER MinFileSize
    Minimum file size in bytes.

.PARAMETER MaxFileSize
    Maximum file size in bytes.

.OUTPUTS
    [hashtable]  
    Returns a hashtable containing:
        link     - [string] The download URL of the matching file.
        checksum - [hashtable] The checksum(s) for the file.

.EXAMPLE
    .\Get-SourceforgeReleaseDownloadLink.ps1 -URI 'https://sourceforge.net/projects/MY_PROJECT' -ReleaseType 'application/zip' -TitleRegex '^(?!.*beta).*' -DownloadLinkRegex '\.zip$' -MinFileSize 1048576 -MaxFileSize 1073741824
    Returns the first ZIP release that is not a beta, between 1 MB and 1 GB.

.EXAMPLE
    PS> .\Get-SFRelease.ps1 -URI 'https://sourceforge.net/projects/MY_OTHER_PROJECT/' -ReleaseType 'application/vnd.microsoft.portable-executable'

    Returns the latest EXE release from the given SourceForge project.

.NOTES
    - Uses the project's RSS feed located at <project>/rss.
    - Throws if the URL is invalid, the feed has no <item> tags, or no version matches the filters.
    Created: 2025-08-14
    Requires: PowerShell 5.1 or later
#>

# ==== Parameter ==========================================================================================================================================================

param (
    # Repository-Url
    [Parameter(Mandatory=$true)]
    [uri] $URI,

    # Release type
    [string] $ReleaseType = 'application/zip',

    # Title must match this regex for the file to be downloaded
    [regex] $TitleRegex = '.*',

    # Download link must match this regex for the file to be downloaded
    [regex] $DownloadLinkRegex = '.*',

    # Minimal size in bytes of the file to be downloaded
    [UInt64] $MinFileSize = [UInt64]::MinValue,

    # Maximal size in bytes of the file to be downloaded
    [UInt64] $MaxFileSize = [UInt64]::MaxValue
);

function Get-FirstOccurrenceInArray {
    param (
        [Parameter(Mandatory=$true)]
        [string[]] $StringArray,
        
        [Parameter(Mandatory=$true)]
        [string] $SearchString
    )
    
    foreach ($string in $StringArray) {
        if ($string.ToLower().Contains($SearchString.ToLower())) {
            return $StringArray.IndexOf($string);
        }
    }

    return -1;
}

if (![uri]::IsWellFormedUriString($URI, [System.UriKind]::Absolute)) {
    throw "Invalid URL: '$($URI.OriginalString)'.`nPattern: 'https://sourceforge.net/projects/MY_PROJECT'";
}

$rssPath = "$($URI.Segments[0..((Get-FirstOccurrenceInArray -StringArray $URI.Segments -SearchString 'project') + 1)] -join '')/rss".Replace("//", "/");

$rssUrl = "$($URI.Scheme)://$($URI.Host)$rssPath";

$oldProgressPreference = $ProgressPreference;
$ProgressPreference = 'SilentlyContinue';

$rssVersions = Invoke-RestMethod -Uri $rssUrl -Method Get

$ProgressPreference = $oldProgressPreference;

if ($rssVersions.Count -le 0) {
    throw "No xml tag 'item' found in rss feed";
}

$matchedVersions = $rssVersions. `
    Where({ $_.content.type.Contains($ReleaseType) }). `
    Where({ $_.title.'#cdata-section' -match $TitleRegex }). `
    Where({ $_.link -match $DownloadLinkRegex }). `
    Where({ [UInt64]$_.content.filesize -ge $MinFileSize }). `
    Where({ [UInt64]$_.content.filesize -le $MaxFileSize });

if ($matchedVersions.Count -le 0) {
    throw "No version found with give filters.";
}

$latestMatch = $matchedVersions.Item(0);
$releaseName = (($latestMatch.title.'#cdata-section').Split("/")[-1].Split(".") | Select-Object -SkipLast 1) -join ".";

return @{name = $releaseName; link = $latestMatch.link; checksum = @{md5 = $latestMatch.content.hash.'#text'}};

# .\toolbox\Get-SourceforgeRelease.ps1 -URI "https://sourceforge.net/projects/nsis" -MinFileSize 1500000 -TitleRegex '^(?!.*?Pre-{0,1}release).*' # Matches latest no-pre-release version of nsis
