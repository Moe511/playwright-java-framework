<#
.SYNOPSIS
    Run tests then optionally generate an Allure HTML report.

.PARAMETER Suite
    Which suite to run: all (default), ui, api, bdd

.PARAMETER Headed
    Run browsers in headed (visible) mode.

.PARAMETER Browser
    Browser engine: chromium (default), firefox, webkit.

.EXAMPLE
    .\run.ps1
    .\run.ps1 -Suite bdd
    .\run.ps1 -Suite ui -Headed
    .\run.ps1 -Suite api -Browser firefox
#>
param(
    [ValidateSet("all","ui","api","bdd")]
    [string]$Suite = "all",
    [switch]$Headed,
    [ValidateSet("","chromium","firefox","webkit")]
    [string]$Browser = ""
)

# ── Build mvn arguments ───────────────────────────────────────────────────────
$mvnArgs = @("-B", "test")
if ($Suite -ne "all") { $mvnArgs += "-P$Suite" }
if ($Headed)          { $mvnArgs += "-Dheadless=false" }
if ($Browser)         { $mvnArgs += "-Dbrowser=$Browser" }

# ── Run tests ─────────────────────────────────────────────────────────────────
Write-Host ""
Write-Host "=================================================" -ForegroundColor Cyan
Write-Host "  Suite : $($Suite.ToUpper())" -ForegroundColor Cyan
Write-Host "=================================================" -ForegroundColor Cyan
Write-Host ""

& mvn @mvnArgs
$exitCode = $LASTEXITCODE

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
