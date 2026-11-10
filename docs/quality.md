# Code Quality

## Tools

| Tool | Purpose |
|---|---|
| **ScalaTest** | Unit tests, written test-first |
| **scoverage** / **Coveralls** | Code coverage |
| **SonarQube Cloud** | Static analysis and quality gate |
| **Stryker4s** | Mutation testing |
| **ArchUnit** | Architecture rules (see [architecture.md](architecture.md)) |
| **GitHub Actions** | Runs all checks automatically |

## Continuous Integration

Every push and pull request to `main` and `develop` runs the tests with coverage and uploads the results to Coveralls. Pull requests and `main` are additionally analyzed by SonarQube.

- Third-party actions are pinned to a commit hash.
- The workflow token only has read permission.
- The build only requires sbt and a JDK. All versions are defined in the project.

## Quality Rules

**Tests**
- All logic is covered by tests.
- Code that only starts the application or performs console input and output is excluded from coverage (`Main`, `Tui.run`, `Tui.update`).

**Static analysis**
- The SonarQube quality gate must pass before a pull request is merged.

**Mutation testing**
- The total mutation score must be at least **80 %**. The build fails below this threshold.
- Mutants that only change the wording of messages, such as console output or exception messages, do not have to be killed. Tests verify the behavior, not the exact text.

## Running the Checks Locally

```
sbt clean coverage test coverageReport
sbt stryker
```