# Release Flow Guide

## Branch Strategy

This project uses the following branch model:

- `main` → production-ready code
- `develop` → integration branch
- `feature/*` → feature or phase implementation
- `release/*` → release preparation and final validation

---

## Feature Flow

1. Create a feature branch from `develop`
2. Implement the required changes
3. Push the branch
4. Open a pull request to `develop`
5. Run CI validation
6. Merge into `develop`

Example:

- `feature/runtime-profiles`
- `feature/cicd-base`
- `feature/sonarqube-integration`

---

## Release Flow

1. Create a release branch from `develop`
2. Perform final validation
3. Apply only release fixes if needed
4. Merge release branch into `main`
5. Create version tag
6. Merge release branch back into `develop`

Example release branch:

- `release/0.1.0`

Example tag:

- `v0.1.0`

---

## Rules

### develop
- integrates completed features
- should remain stable
- receives pull requests from `feature/*`

### release/*
- created only when a release is being prepared
- should not receive new features
- only validation, fixes, and release adjustments are allowed

### main
- production branch
- receives validated code only from `release/*`
- triggers Docker image publication workflow

---

## Pull Request Policy

### To develop
- required for all feature branches
- must pass CI
- should be reviewed before merge

### To main
- should come only from `release/*`
- must pass CI
- should represent a validated release candidate

---

## Versioning

Recommended versioning format:

- `0.1.0`
- `0.2.0`
- `1.0.0`

Tag format:

- `v0.1.0`
- `v0.2.0`

---

## Example End-to-End Flow

1. Start from `develop`
2. Create `feature/some-feature`
3. Merge into `develop`
4. Create `release/0.1.0`
5. Validate build, tests, SonarQube, Docker image
6. Merge into `main`
7. Create tag `v0.1.0`
8. Docker publish workflow releases the image
9. Merge release branch back into `develop`

---

## Current Project Direction

This release flow supports:

- controlled integration
- cleaner release preparation
- reproducible versioning
- automated image publication from `main`

---

## CI/CD Tooling

The project uses GitHub Actions as the primary CI/CD pipeline.

A complementary `Jenkinsfile` is also included to simulate an enterprise-oriented pipeline with:

- build
- test
- SonarQube analysis
- Docker image build
- Docker image publication from `main`
