#!/usr/bin/env bash
# Run tests then optionally generate an Allure HTML report.
#
# Usage:
#   ./run.sh [all|ui|api|bdd] [--headed] [--browser chromium|firefox|webkit]
#
# Examples:
#   ./run.sh
#   ./run.sh bdd
#   ./run.sh ui --headed
#   ./run.sh api --browser firefox

SUITE="${1:-all}"
shift || true
EXTRA=()

while [[ $# -gt 0 ]]; do
    case "$1" in
        --headed)  EXTRA+=("-Dheadless=false"); shift ;;
        --browser) EXTRA+=("-Dbrowser=$2");     shift 2 ;;
        *)         shift ;;
    esac
done

SUITE_LABEL=$(echo "$SUITE" | tr '[:lower:]' '[:upper:]')

# ── Run tests ─────────────────────────────────────────────────────────────────
echo ""
echo "================================================="
echo "  Suite : $SUITE_LABEL"
echo "================================================="
echo ""

set +e
if [[ "$SUITE" == "all" ]]; then
    echo "── JUnit tests ──────────────────────────────────"
    mvn -B test "${EXTRA[@]}"
    JUNIT_EXIT=$?

    echo ""
    echo "── BDD scenarios ────────────────────────────────"
    mvn -B test -Pbdd "${EXTRA[@]}"
    BDD_EXIT=$?

    [[ $JUNIT_EXIT -ne 0 || $BDD_EXIT -ne 0 ]] && EXIT=1 || EXIT=0
else
    mvn -B test "-P$SUITE" "${EXTRA[@]}"
    EXIT=$?
fi
set -e

# ── Result banner ─────────────────────────────────────────────────────────────
echo ""
echo "================================================="
if [[ $EXIT -eq 0 ]]; then
    echo "  RESULT : All tests passed"
else
    echo "  RESULT : Tests finished with failures"
fi
echo "================================================="
echo ""

# ── Prompt: generate report ───────────────────────────────────────────────────
read -rp "Generate HTML Allure report? [Y/n] " GEN
GEN="${GEN:-Y}"

if [[ "$GEN" =~ ^[Yy] ]]; then
    echo ""
    echo "Generating report..."
    mvn -q allure:report

    REPORT="$(cd "$(dirname "$0")" && pwd)/target/allure-report/index.html"

    if [[ -f "$REPORT" ]]; then
        echo "Report ready: $REPORT"
        echo ""
        read -rp "Open in browser? [Y/n] " OPEN
        OPEN="${OPEN:-Y}"
        if [[ "$OPEN" =~ ^[Yy] ]]; then
            if   command -v xdg-open &>/dev/null; then xdg-open "$REPORT"   # Linux
            elif command -v open     &>/dev/null; then open     "$REPORT"   # macOS
            elif command -v start    &>/dev/null; then start    "$REPORT"   # Git Bash / Windows
            else echo "Open manually: $REPORT"
            fi
        fi
    else
        echo "Could not find report — ensure tests ran and produced allure-results."
    fi
fi

exit $EXIT
