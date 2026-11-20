# Lab S.04.02: Test Reports & Automation Guide

> **Course:** Software Engineering II (ISO2) · Academic Year 2026/2027  
> **Degree:** Bachelor's Degree in Computer Engineering · School of Computer Science of Ciudad Real (UCLM)  
> **Instructors:** Ismael Caballero, Moisés Rodríguez, Carmen Ormeño  
> **Version:** Notes v1.15  

---

## 1. Schedule & Important Milestones

| Week | Date | Lab / Milestone Title | Key Deliverable & Goals |
| :---: | :---: | :--- | :--- |
| **W6** | 09/11 | Test Management & JUnit Intro | Introduction to JUnit 6, Project supervision, **PUD Submission 1 (13/11)**. |
| **W7** | **16/11** | **Test Report Automation with Maven** | **Current Lab:** Surefire, JaCoCo, Maven Site, Multi-module staging, Wiki "Test Reports". |
| **W8** | 23/11 | Maintenance Management | Maintenance report automation with Maven, Project supervision. |
| **W9** | 30/11 | Final Project Supervision | Pre-submission project review. |
| **W10** | 07/12 | Project Supervision | Supervision session (Group B2 only). |
| **W11** | **14/12 & 15/12** | **Progress Test & Final Submission** | **Progress Test (Mon 14/12)** & **Testing Submission 2 / Final Lab Submission (Tue 15/12)**. |

---

## 2. Core Objectives of Lab S.04.02

1. **Report Plugins in Parent `pom.xml`:** Configure `maven-surefire-plugin`, `maven-site-plugin`, `jacoco-maven-plugin`, and `<reporting>` plugins in the aggregator POM.
2. **Multi-Module Report Staging:** Run `mvn clean install site site:stage` to merge reports from all submodules (`common-contracts`, `server`, `client`) into `target/staging/index.html`.
3. **GitHub Wiki "Test Reports" Page:** Publish a live table and report screenshots tracking tests, failures, line coverage ($\ge 75\%$), and branch coverage.
4. **Time Tracking:** Maintain continuous logging of engineering hours in the official time-tracking Excel.

---

## 3. JUnit 6 Best Practices (Exceptions & Parameterized Tests)

### A. Exception Testing with `assertThrows`
In modern JUnit (Jupiter / JUnit 6), exception testing must use `assertThrows` with a lambda expression containing **strictly the single throwing statement**:

```java
@Test
void noSePuedeRealizarDosVeces() {
    SolicitudPrueba s = new SolicitudPrueba(2);
    s.registrarRealizacion();
    
    // assertThrows checks exception type and returns the instance to inspect its message
    IllegalStateException e = assertThrows(
        IllegalStateException.class,
        () -> s.registrarRealizacion() // Lambda contains ONLY the throwing line
    );
    
    assertEquals("Ya realizada", e.getMessage());
}
```

> [!WARNING]
> **Deprecated JUnit 4 Syntax:** Never use `@Test(expected = ...)` or `@Rule ExpectedException`. They are obsolete and non-compliant in JUnit 5/6.

### B. Parameterized Tests with `@ValueSource`
Parameterized tests fit the **Equivalence Partitioning (EP)** and **Boundary Value Analysis (BVA)** design tables 1:1:

```java
@ParameterizedTest(name = "prioridad {0} es válida")
@ValueSource(ints = {1, 3, 5})
void prioridadValida(int p) {
    assertDoesNotThrow(() -> new SolicitudPrueba(p));
}

@ParameterizedTest
@ValueSource(ints = {0, -1, 6})
void prioridadNoValida(int p) {
    assertThrows(IllegalArgumentException.class,
        () -> new SolicitudPrueba(p));
}
```

### C. Multi-Parameter Tests with `@CsvSource`
When testing combinations or expected outputs, use `@CsvSource`:

```java
@ParameterizedTest(name = "{0} + {1} = {2}")
@CsvSource({
    "2, 3, 5",
    "0, 0, 0",
    "-1, 1, 0"
})
void suma(int a, int b, int esperado) {
    assertEquals(esperado, calculadora.suma(a, b));
}
```

---

## 4. Maven `pom.xml` Report Architecture

### Step 1: Surefire & Site Plugin in `<build><plugins>`
```xml
<build>
  <plugins>
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-surefire-plugin</artifactId>
      <version>3.5.6</version>
    </plugin>
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-site-plugin</artifactId>
      <version>3.22.0</version>
    </plugin>
  </plugins>
</build>
```

### Step 2: JaCoCo Code Coverage Configuration
```xml
<plugin>
  <groupId>org.jacoco</groupId>
  <artifactId>jacoco-maven-plugin</artifactId>
  <version>0.8.16</version>
  <executions>
    <execution>
      <id>preparar</id>
      <goals><goal>prepare-agent</goal></goals>
    </execution>
    <execution>
      <id>informe</id>
      <goals><goal>report</goal></goals>
    </execution>
    <execution>
      <id>comprobar</id>
      <goals><goal>check</goal></goals>
      <configuration>
        <haltOnFailure>false</haltOnFailure> <!-- Set to true once >= 75% is achieved -->
        <rules>
          <rule>
            <element>BUNDLE</element>
            <limits>
              <limit>
                <counter>LINE</counter>
                <value>COVEREDRATIO</value>
                <minimum>0.75</minimum>
              </limit>
            </limits>
          </rule>
        </rules>
      </configuration>
    </execution>
  </executions>
</plugin>
```

### Step 3: The `<reporting>` Section (Placed Outside `<build>`)
```xml
<reporting>
  <plugins>
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-project-info-reports-plugin</artifactId>
      <version>3.9.0</version>
    </plugin>
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-surefire-report-plugin</artifactId>
      <version>3.5.6</version>
    </plugin>
    <plugin>
      <groupId>org.jacoco</groupId>
      <artifactId>jacoco-maven-plugin</artifactId>
      <reportSets>
        <reportSet>
          <reports>
            <report>report</report>
          </reports>
        </reportSet>
      </reportSets>
    </plugin>
  </plugins>
</reporting>
```

### Step 4: Staging Configuration for Multi-Module Reports
To merge reports across `common-contracts`, `server`, and `client`, add `<distributionManagement>` and `<url>` to the root `pom.xml`:

```xml
<url>https://example.org/radiodiagnostico</url>
<distributionManagement>
  <site>
    <id>local</id>
    <url>file:///tmp/radiodiagnostico-sitio</url>
  </site>
</distributionManagement>
```

**Generate Staged Site:**
```bash
mvn clean install site site:stage
```

> [!CAUTION]
> **Do NOT open `target/site/index.html`!**  
> In multi-module projects, links between submodules are broken in `target/site`. Always open **`target/staging/index.html`**, which merges all submodules cleanly.

---

## 5. Reading JaCoCo & Surefire Reports

### A. JaCoCo Coverage Metrics
* **Green:** Line executed by test cases.
* **Yellow:** Partial branch coverage (e.g. `if` condition evaluated to true, but never false).
* **Red:** Line never reached by any test case.
* **Instructions:** Statement coverage (the official $\ge 75\%$ threshold).
* **Branches:** Decision/Branch coverage.

### B. Surefire Reports
* Shows Total Tests, Errors, Failures, Skipped, Success Rate, and Execution Time.
* **Rule:** An unexpected exception or failed assertion must be logged in its own dedicated GitHub issue.

---

## 6. AI-Assisted Coverage Protocol (Prompting Workflow)

Use Copilot / Cursor / ChatGPT in a strict 3-prompt sequence:

* **Prompt 1 (Ask Mode — Identify Gaps):**
  > *"Using `#file:server/target/site/jacoco/jacoco.csv`, tell me which classes and methods have the lowest line and branch coverage. Do not write code."*
* **Prompt 2 (Ask Mode — Test Design):**
  > *"For `<Class>`, which test cases are missing to cover those branches? Give them to me in a table with the technique for each one (equivalence class, boundary value or error guessing). Do not write code."*
* **Prompt 3 (Agent Mode — Implementation):**
  > *"Add the cases from this table to `<Class>Test`: `[paste reviewed table]`. Use `assertThrows` for exceptions and `@ParameterizedTest` if there are several values of the same class. Do not modify the production code. Then run `mvn verify`."*

> [!IMPORTANT]
> **Strict Anti-Cheat Rule:** No tests without assertions just to raise coverage numbers artificially. Tests without valid oracles will be detected and penalized during grading.

---

## 7. The GitHub Wiki Page: "Test Reports"

For **Testing Submission 2 (Dec 15)**, a dedicated Wiki page named **"Test Reports"** must be updated in every iteration showing:
1. Date and commit / pull request hash.
2. Table of tests per module: Total, Failures, Errors.
3. Statement Coverage (%) and Branch Coverage (%) per module.
4. Screenshots of the staged Surefire and JaCoCo reports.
5. Hyperlinks to GitHub issues for any failing tests.

---

## 8. "Before You Leave" Lab Checklist

- [ ] Plugins configured in root `pom.xml`.
- [ ] Pull Request opened and merged to `develop` with CI green.
- [ ] GitHub Wiki page "Test Reports" created with current iteration results.
- [ ] **Log today's engineering hours in the Time-Tracking Excel spreadsheet.**
