#Requires -Version 5.1
#Requires -Modules @{ ModuleName = 'Microsoft.PowerShell.Utility'; ModuleVersion = '3.1.0.0' }

<#
.SYNOPSIS
    Retrieves the download link and checksum for a specific Eclipse Adoptium JDK build
    based on version, OS, architecture, and release type.

.DESCRIPTION
    This script queries the Eclipse Adoptium API to locate the latest or specifically
    matching JDK release that meets the given version and platform criteria.
    It supports both high-level "latest" lookups and granular filtering by
    minor, security, and build versions.

.PARAMETER MajorVersion
    The major Java version (e.g., 8, 11, 17). Required.

.PARAMETER MinorVersion
    Optional minor version number. Default: -1 (ignored unless specified).

.PARAMETER SecurityVersion
    Optional security version number. Default: -1 (ignored unless specified).
    Requires MinorVersion to be set.

.PARAMETER BuildVersion
    Optional build version number. Default: -1 (ignored unless specified).
    Requires both MinorVersion and SecurityVersion to be set.

.PARAMETER OS
    Target operating system. Must be one of:
    linux, windows, mac, solaris, aix, alpine-linux.

.PARAMETER Arch
    Target system architecture. Must be one of:
    x64, x86, x32, ppc64, ppc64le, s390x, aarch64, arm, sparcv9, riscv64.

.PARAMETER ReleaseType
    The type of release artifact to download.
    Possible values: package, installer. Default: package.

.OUTPUTS
    [hashtable]  
    Returns a hashtable containing:
        link     - [string] The download URL of the matching JDK artifact.
        checksum - [hashtable] The checksum(s) for the artifact.

.EXAMPLE
    PS> .\Get-AdoptiumJdkVersion.ps1 -MajorVersion 17 -OS linux -Arch x64
    Returns the latest JDK 17 package download link and checksum for Linux x64.

.EXAMPLE
    PS> .\Get-AdoptiumJdkVersion.ps1 -MajorVersion 11 -MinorVersion 0 -SecurityVersion 20 -BuildVersion 8 -OS windows -Arch x64 -ReleaseType installer
    Returns the matching JDK installer link and checksum for Windows x64.

.NOTES
    - External Dependency: Internet access to https://api.adoptium.net
    Created: 2025-08-14
    Requires: PowerShell 5.1 or later
#>

# ==== Parameter ==========================================================================================================================================================

param (
    # Java major version
    [Parameter(Mandatory=$true)]
    [UInt32] $MajorVersion,
    
    # Java minor version
    [int] $MinorVersion = -1,
    
    # Java security version
    [int] $SecurityVersion = -1,

    # Java build version
    [int] $BuildVersion = -1,

    # Operating system
    [Parameter(Mandatory=$true)]
    [ValidateSet('linux', 'windows', 'mac', 'solaris', 'aix', 'alpine-linux')]
    [string] $OS,

    # System architecture
    [Parameter(Mandatory=$true)]
    [ValidateSet('x64', 'x86', 'x32', 'ppc64', 'ppc64le', 's390x', 'aarch64', 'arm', 'sparcv9', 'riscv64')]
    [string] $Arch,

    # Release Type
    [ValidateSet('package', 'installer')]
    [string] $ReleaseType = 'package'
);

function Get-MatchingVersion {
    param (
        [Object[]] $Versions
    )
    
    $mostMatching = $Versions.Where({ $_.version_data.minor -eq $MinorVersion });

    if ($SecurityVersion -ge 0) {
        $mostMatching = $mostMatching.Where({ $_.version_data.security -eq $SecurityVersion });
    }

    if ($BuildVersion -ge 0) {
        $mostMatching = $mostMatching.Where({ $_.version_data.build -eq $BuildVersion });
    }

    if ($mostMatching.Count -le 0) {
        throw "No matching version found."
    }

    return $mostMatching.Item(0);
}




if ($SecurityVersion -ge 0 -and $MinorVersion -lt 0) {
    throw "When -SecurityVersion is specified, -MinorVersion is also requiered";
} elseif ($BuildVersion -ge 0 -and ($SecurityVersion -lt 0 -or $SecurityVersion -lt 0)) {
    throw "When -BuildVersion is specified, -SecurityVersion and -MinorVersion are also requiered";
}

$oldProgressPreference = $ProgressPreference;
$ProgressPreference = 'SilentlyContinue';

if ($MinorVersion -lt 0 -and $SecurityVersion -lt 0 -and $BuildVersion -lt 0) {
    $jsonVersions = Invoke-RestMethod -Uri "https://api.adoptium.net/v3/assets/latest/$MajorVersion/hotspot?architecture=$Arch&image_type=jdk&os=$OS&vendor=eclipse" -Method Get -Headers @{'accept' = 'application/json'} -ErrorAction Stop;
    if ($jsonVersions.Count -le 0) {
        throw "No matching version found";
    }
    $latestMatch = $jsonVersions.Item(0);

} else {
    $jsonVersions = New-Object System.Collections.ArrayList;
    $currentUrl = "https://api.adoptium.net/v3/assets/feature_releases/$MajorVersion/ga?architecture=$Arch&os=$OS&page=0&page_size=10&project=jdk&sort_method=DEFAULT&sort_order=DESC&vendor=eclipse";
    
    do {
        $webRequest = Invoke-WebRequest -Uri $currentUrl -Method Get -Headers @{'accept' = 'application/json'} -ErrorAction Stop;
        $jsonVersions.AddRange( @(ConvertFrom-Json -InputObject $webRequest.Content -ErrorAction Stop) );
        if ($webRequest.Headers.link) {
            $currentUrl = (Select-String -InputObject $webRequest.Headers.link -Pattern '(?<=<).*(?=>)').Matches.Value;
        }

    } while ($webRequest.Headers.link)
    
    if ($jsonVersions.Count -le 0) {
        throw "No matching version found";
    }
    $latestMatch = Get-MatchingVersion -Versions $jsonVersions;
}

$ProgressPreference = $oldProgressPreference;

if ($latestMatch.binaries) {
    $matchedBinary = $latestMatch.binaries.Where({ $_.image_type -eq 'jdk' })[0];
} elseif ($latestMatch.binary) {
    $matchedBinary = $latestMatch.binary;
} else {
    throw "Version list missing attribute 'binary' or 'binaries'";
}

if ($matchedBinary.$ReleaseType) {
    $downloadLink = $matchedBinary.$ReleaseType.link;
    $releaseName = $latestMatch.release_name;
    $checksum = @{sha256 = $matchedBinary.$ReleaseType.checksum};
} else {
    throw "Release type '$ReleaseType' for OS '$OS' for version '$($latestMatch.release_name)' not found";
}

if (!$downloadLink) {
    throw "No matching Version found";
} elseif (!$checksum.sha256) {
    throw "No checksum found for '$downloadLink'";
}

return @{name = $releaseName; link = $downloadLink; checksum = $checksum};
