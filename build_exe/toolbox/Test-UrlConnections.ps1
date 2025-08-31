#Requires -Version 4.0
#Requires -Modules NetTCPIP
<#
.SYNOPSIS
    Tests connectivity to one or more URLs in parallel, with an overall timeout.

.DESCRIPTION
    This script starts background jobs to run HTTP connectivity tests (`Test-NetConnection`)
    against the specified URLs. It waits until all jobs complete or the overall timeout is reached.
    If all URLs respond successfully, it returns `$true`.
    If any fail, an error is thrown listing the failed URLs.
    If the timeout is exceeded, an error is thrown listing any URLs that did not finish.

.PARAMETER URLs
    One or more hostnames or URLs to test. This parameter is mandatory.

.PARAMETER TimeoutMillis
    Total time allowed (in milliseconds) for all tests combined.
    Defaults to 5000 ms (5 seconds) if not specified.

.EXAMPLE
    PS> .\Test-UrlConnections.ps1 -URLs example.com, github.com -TimeoutMillis 8000
    Tests connectivity to both hosts, allowing up to 8 seconds total.

.EXAMPLE
    PS> .\Test-UrlConnections.ps1 -URLs "intranet.local"
    Tests intranet.local with the default 5 second total timeout.

.OUTPUTS
    System.Boolean
    Returns `$true` if all URLs succeed.
    Throws a terminating error if any URL fails or if the timeout is exceeded.

.NOTES
    Created: 2025-08-13
    Requires: PowerShell 3.0 or later
#>

# ==== Parameter ==========================================================================================================================================================

param (
    # URL's die getestet werden
    [Parameter(Mandatory=$true)]
    [string[]] $URL,

    # Timeout für alle URL's zusammen
    [UInt32] $TimeoutMillis = 5000
);

# ==== Funktionen =========================================================================================================================================================

function handleAllFinished {
    param (
        $Jobs
    )
    
    if ($Jobs.Where({ (Receive-Job -Job $_ -Keep) -eq $false }).Count -eq 0) {
        Remove-Job -Job $Jobs -Force;
        return $true;
    } else {
        throwErrorMsg -FormatErrorMessage "Connection to {0} failed." -Jobs $Jobs -WhereCondition { (Receive-Job -Job $_ -Keep) -eq $false };
    }
}

function throwErrorMsg {
    param (
        [Parameter(Mandatory=$true)]
        [string] $FormatErrorMessage,

        [Parameter(Mandatory=$true)]
        [System.Collections.ArrayList] $Jobs,
        
        [Parameter(Mandatory=$true)]
        [scriptblock] $WhereCondition
    )
    
    $errorLinks = "'";

    $a = $Jobs.Where($WhereCondition);
    $a.ForEach({
        $errorLinks += $_.Name + "', '";
    });
    $errorLinks = $errorLinks.Substring(0, $errorLinks.Length - 3);

    Remove-Job -Job $Jobs -Force;

    $errorMsg = $FormatErrorMessage -f $errorLinks;
    throw $errorMsg;
}

# ==== Variablen ==========================================================================================================================================================

$jobs = New-Object System.Collections.ArrayList;
$startTime = Get-Date;

# ==== Hauptprogramm ======================================================================================================================================================

foreach ($u in $URL) {
    $jobs.Add( (Start-Job -Name $u -ScriptBlock {
        param($url);
        $ProgressPreference = 'SilentlyContinue';
        $result = Test-NetConnection -ComputerName $url -InformationLevel Quiet -CommonTCPPort HTTP;
        return $result;
    } -ArgumentList $u) ) | Out-Null;
}


while ( ((Get-Date) - $startTime).TotalMilliseconds -lt $TimeoutMillis ) {
    Start-Sleep -Milliseconds 100;

    $allFinished = $jobs.Where({ $_.State -ne 'Completed' }, 'First').Count -eq 0;

    if ($allFinished) {
        return handleAllFinished -Jobs $jobs;
    }
}

throwErrorMsg -FormatErrorMessage "Timeout. Connection to {0} took longer than $TimeoutMillis milliseconds." -Jobs $jobs -WhereCondition { $_.State -ne 'Completed' };
