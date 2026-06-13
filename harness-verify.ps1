$ErrorActionPreference = 'Stop'

$errors = 0
$checks = 0

function Check-File {
  param([string]$Path)
  $script:checks++
  if (-not (Test-Path -Path $Path -PathType Leaf)) {
    Write-Host "❌ Missing file: $Path" -ForegroundColor Red
    $script:errors++
  } else {
    Write-Host "✅ $Path" -ForegroundColor Green
  }
}

function Check-Dir {
  param([string]$Path)
  $script:checks++
  if (-not (Test-Path -Path $Path -PathType Container)) {
    Write-Host "❌ Missing directory: $Path" -ForegroundColor Red
    $script:errors++
  } else {
    Write-Host "✅ $Path/" -ForegroundColor Green
  }
}

Write-Host ""
Write-Host "=================================================="
Write-Host "  Harness Template Verification (PowerShell)"
Write-Host "=================================================="
Write-Host ""

# 1. Claude Code Steering
Write-Host "[1/6] Claude Code Steering File" -ForegroundColor Yellow
Check-File ".claude/CLAUDE.md"
Write-Host ""

# 2. Claude Code Hooks
Write-Host "[2/6] Claude Code Hooks" -ForegroundColor Yellow
Check-File ".claude/settings.local.json"
Check-File ".claude/hooks/harness-reminder.ps1"
Write-Host ""

# 3. Maven config
Write-Host "[3/6] Maven Build Config" -ForegroundColor Yellow
Check-File "pom.xml"
Check-File "config/checkstyle/checkstyle.xml"
Check-File "config/checkstyle/checkstyle-strict.xml"
Check-File "config/spotbugs/exclude.xml"
Write-Host ""

# 4. CI config
Write-Host "[4/6] CI Config" -ForegroundColor Yellow
Check-File ".github/workflows/ci-verify.yml"
Write-Host ""

# 5. harness-collab docs
Write-Host "[5/6] harness-collab Docs" -ForegroundColor Yellow
Check-File "harness-collab/README.md"
Check-File "harness-collab/AGENTS.md"
Check-File "harness-collab/func.md"
Check-Dir "harness-collab/01-product-specs/templates"
Check-File "harness-collab/01-product-specs/templates/product-spec-template.md"
Check-Dir "harness-collab/02-design-docs/templates"
Check-File "harness-collab/02-design-docs/templates/design-doc-template.md"
Check-Dir "harness-collab/03-exec-plans/templates"
Check-File "harness-collab/03-exec-plans/templates/exec-plan-template.md"
Check-Dir "harness-collab/04-api-docs/templates"
Check-File "harness-collab/04-api-docs/templates/api-doc-template.md"
Check-File "harness-collab/05-methodology/architecture-constraints.md"
Check-File "harness-collab/05-methodology/dev-workflow.md"
Check-File "harness-collab/05-methodology/ai-delivery-playbook.md"
Check-File "harness-collab/06-adapters/bootstrap-guide.md"
Check-File "harness-collab/06-adapters/retrofit-guide.md"
Write-Host ""

# 6. root docs
Write-Host "[6/6] Root Docs" -ForegroundColor Yellow
Check-File "README.md"
Check-File "AGENTS.md"
Write-Host ""

Write-Host "=================================================="
if ($errors -eq 0) {
  Write-Host "✅ Verification passed! Completed $checks checks." -ForegroundColor Green
  Write-Host ""
  Write-Host "Harness template is ready to use in Claude Code."
  exit 0
}

Write-Host "❌ Verification failed! Completed $checks checks, found $errors issues." -ForegroundColor Red
Write-Host "Please fix missing files/directories and run again."
exit 1
