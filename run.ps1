<#
.SYNOPSIS
    Run tests then optionally generate an Allure HTML report.

.PARAMETER Suite
    Which suite to run: all (default), ui, api, bdd

.PARAMETER Headed
    Run browsers in headed (visible) mode.

.PARAMETER Browser
    Browser engine: chromium (default), firefox, webkit.

.PARAMETER Tag
    Run only BDD scenarios with this tag, e.g. smoke, blocker, "@smoke and @ui".
    Automatically selects the BDD suite.

.PARAMETER Feature
    Run only the named feature file, e.g. login, booking, cart.
    Automatically selects the BDD suite.

.EXAMPLE
    .\run.ps1
    .\run.ps1 -Suite bdd
    .\run.ps1 -Suite ui -Headed
    .\run.ps1 -Tag smoke
    .\run.ps1 -Tag blocker
    .\run.ps1 -Feature login
    .\run.ps1 -Feature booking -Tag critical
#>
param(
    [ValidateSet("all","ui","api","bdd")]
    [string]$Suite = "all",
    [switch]$Headed,
    [ValidateSet("","chromium","firefox","webkit")]
    [string]$Browser = "",
    [string]$Tag = "",
    [string]$Feature = ""
)

# ── Tag / Feature imply BDD suite ─────────────────────────────────────────────
if ($Tag -or $Feature) { $Suite = "bdd" }

# ── Shared extra flags ────────────────────────────────────────────────────────
$extraArgs = @()
if ($Headed)  { $extraArgs += "-Dheadless=false" }
if ($Browser) { $extraArgs += "-Dbrowser=$Browser" }

if ($Tag) {
    $tagValue = if ($Tag -match "@") { $Tag } else { "@$Tag" }
    $extraArgs += "-Dcucumber.filter.tags=$tagValue"
}

if ($Feature) {
    $found = Get-ChildItem -Path (Join-Path $PSScriptRoot "src\test\resources\features") `
                           -Filter "*$Feature*.feature" -Recurse
    if ($found.Count -eq 0) {
        Write-Host "No feature file matching '$Feature' found." -ForegroundColor Red
        exit 1
    }
    if ($found.Count -gt 1) {
        Write-Host "Multiple matches for '$Feature' — be more specific:" -ForegroundColor Yellow
        $found | ForEach-Object { Write-Host "  $($_.Name)" }
        exit 1
    }
    $rel = $found[0].FullName.Replace((Join-Path $PSScriptRoot "src\test\resources\"), "").Replace("\", "/")
    $extraArgs += "-Dcucumber.features=classpath:$rel"
}

# ── Run tests ─────────────────────────────────────────────────────────────────
Write-Host ""
Write-Host "=================================================" -ForegroundColor Cyan
Write-Host "  Suite : $($Suite.ToUpper())" -ForegroundColor Cyan
Write-Host "=================================================" -ForegroundColor Cyan
Write-Host ""

if ($Suite -eq "all") {
    Write-Host "── JUnit tests ──────────────────────────────────" -ForegroundColor DarkCyan
    & mvn -B test @extraArgs
    $junitExit = $LASTEXITCODE

    Write-Host ""
    Write-Host "── BDD scenarios ────────────────────────────────" -ForegroundColor DarkCyan
    & mvn -B test -Pbdd @extraArgs
    $bddExit = $LASTEXITCODE

    $exitCode = if ($junitExit -ne 0 -or $bddExit -ne 0) { 1 } else { 0 }
} else {
    & mvn -B test "-P$Suite" @extraArgs
    $exitCode = $LASTEXITCODE
}

# ── Result banner ─────────────────────────────────────────────────────────────
Write-Host ""
Write-Host "=================================================" -ForegroundColor Cyan
if ($exitCode -eq 0) {
    Write-Host "  RESULT : All tests passed " -ForegroundColor Green
} else {
    Write-Host "  RESULT : Tests finished with failures" -ForegroundColor Red
}
Write-Host "=================================================" -ForegroundColor Cyan
Write-Host ""

# ── Prompt: generate report ───────────────────────────────────────────────────
$gen = Read-Host "Generate HTML Allure report? [Y/n]"
if ([string]::IsNullOrWhiteSpace($gen) -or $gen -match "^[Yy]") {

    Write-Host ""
    Write-Host "Generating report..." -ForegroundColor Cyan
    & mvn -q allure:report

    $report = Join-Path $PSScriptRoot "target\allure-report\index.html"

    if (Test-Path $report) {
        Write-Host "Report ready: $report" -ForegroundColor Green
        Write-Host ""

        $open = Read-Host "Open in browser? [Y/n]"
        if ([string]::IsNullOrWhiteSpace($open) -or $open -match "^[Yy]") {
            Start-Process $report
        }
    } else {
        Write-Host "Could not find report — ensure tests ran and produced allure-results." -ForegroundColor Yellow
    }
}

exit $exitCode
