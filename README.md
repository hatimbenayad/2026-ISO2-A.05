# 2026-ISO2-A.05

## Build and test
- Only JDK 21 has to be installed. The Maven Wrapper downloads Maven 3.9.16 the first time it runs and keeps it in
  `~/.m2/wrapper/`, so every member builds with the same Maven version (D-011, #43).
- Linux (bash) and macOS (zsh): `./mvnw clean compile`. Windows (PowerShell): `.\mvnw.cmd clean compile`. With
  `verify` instead of `compile`, the tests run as well.
- GitHub Actions (`.github/workflows/maven.yml`) builds and tests every push and pull request to `main` and `develop`.
