# Software Engineering II: 2nd Theoretical Exercise & Testing Practicum

> **Course:** Software Engineering II (ISO2) · Academic Year 2026/2027  
> **Faculty:** School of Computer Science of Ciudad Real (UCLM)  
> **Submission Deadline:** **Tuesday 15/12/2026** (ZIP upload to Moodle)  
> **Grading Scale:** 0 to 10 points (3.333 points per problem)  

---

## 1. Executive Summary & General Concerns (ET.02.00)

The first part of the lab project simulated the execution of the Unified Development Process (UDP). In this second phase, the focus shifts to real-world **Verification & Validation**:
1. **Theoretical Component:** Rigorous reasoning and derivation of test cases (Equivalence Classes, Boundary Values, Error Guessing, Combinatorial, Pairwise/PICT, Decision Coverage, MC/DC).
2. **Practical / Lab Component:** Implementing those test cases in JUnit 6 within a Maven project, achieving **$\ge 80\%$ statement coverage** via JaCoCo and Surefire reports.

### The Independent Verification Rule:
> **"Developers responsible for writing the code should NOT perform the testing!"**

To mirror industry practices, the 6-member team divides into **3 pairs** that rotate through all 3 stages across 3 problems:
* **Step 1 (Program Implementation):** Pair A writes the Java program in Maven on branch `develop`.
* **Step 2 (Test Design - Theory PDF):** Pair B designs the test cases and writes the formal 10-question report (`ISO2-2025-GrupoA05-Testing-PM.pdf`).
* **Step 3 (Test Implementation - Lab JUnit):** Pair C implements the JUnit 6 tests on a dedicated **`hotfix`** branch, verifying $\ge 80\%$ JaCoCo coverage.

---

## 2. Work Assignment Matrix (Table 1)

For **Group A.05**, the team splits into 3 pairs based on existing Decision D-009 pairings:
* **Pair 1 (P1):** Hatim Ben Ayad & Ayça Beril Gümüş
* **Pair 2 (P2):** Mina Alimohammadi & Felipe Dionisio
* **Pair 3 (P3):** Berke Taha Özdemir & Beyza Nur Topuz

| Problem & Repository | Step 1: Program Code (Maven) | Step 2: Test Design (Theory PDF) | Step 3: JUnit 6 & JaCoCo &ge;80% (Lab) |
| :--- | :---: | :---: | :---: |
| **Problem 1: Parking Fee**<br>`ISO2-2025-GrupoA05-Testing-P1` | **Pair 1** (Hatim & Ayça)<br>Branch: `develop` | **Pair 2** (Mina & Felipe)<br>PDF Report | **Pair 3** (Berke & Beyza)<br>Branch: `hotfix` |
| **Problem 2: Scholarships**<br>`ISO2-2025-GrupoA05-Testing-P2` | **Pair 2** (Mina & Felipe)<br>Branch: `develop` | **Pair 3** (Berke & Beyza)<br>PDF Report | **Pair 1** (Hatim & Ayça)<br>Branch: `hotfix` |
| **Problem 3: Greenhouse Irrigation**<br>`ISO2-2025-GrupoA05-Testing-P3` | **Pair 3** (Berke & Beyza)<br>Branch: `develop` | **Pair 1** (Hatim & Ayça)<br>PDF Report | **Pair 2** (Mina & Felipe)<br>Branch: `hotfix` |

---

## 3. The 3 Problem Specifications

### Problem 1: Parking Fee Calculator (ET.02.02)
* **Goal:** Calculate total parking stay cost from entry and exit Date/Time ticket objects.
* **Encapsulation & Validation:** 
  * If `exitTime < entryTime`, or if null objects are passed, throw an appropriate exception.
  * Validate at constructor level with proper encapsulation.
* **Pricing Rules:**
  * First **30 minutes**: completely **free of charge** (€0.00).
  * Thereafter: **€0.05 per minute**.
  * Daily limit: capped at a strict maximum of **€20.00 per day**.
* **Architecture:** Command-line UI component separated from domain logic.

---

### Problem 2: University Scholarship Eligibility (ET.02.03)
* **Goal:** Determine the single matching scholarship category for a university student.
* **Profile Variables:** GPA (0.0 to 10.0), Family Income (€), Distance to Campus (km), Degree of Disability (%), Number of Siblings.
* **Categories & Thresholds:**
  1. **Excellence Scholarship:** GPA $\ge 8.5$ AND Family Income $< €30,000$ $\rightarrow$ Full tuition waiver + €1,000 stipend.
  2. **Residence Scholarship:** GPA between $6.5$ and $8.4$ AND Family Income $< €20,000$ AND Distance $> 50\text{ km}$ $\rightarrow$ Full tuition waiver + housing allowance.
  3. **Large Family Scholarship:** GPA $\ge 5.0$ AND Family Income strictly $< €15,000$ AND Siblings $\ge 3$ $\rightarrow$ Full tuition waiver.
  4. **Inclusion Scholarship:** Certified Disability $\ge 33\%$ AND GPA $\ge 5.0$ $\rightarrow$ Full tuition waiver + assistive technology funding.
* **Zero-Ambiguity Rule:** Document explicit assumptions and exceptions for unhandled boundary conditions.

---

### Problem 3: Agricultural Greenhouse Smart Irrigation (ET.02.04)
* **Goal:** Output irrigation decisions based on sensor inputs: Soil Moisture (%), Ambient Temperature (°C), Rain Forecast, and Time of Day.
* **Decision Rules:**
  1. **Heavy Watering:** Moisture $< 20\%$ AND Temp $> 25^\circ\text{C}$ AND No rain forecasted within 24h.
  2. **Light Watering:** Moisture between $20\%$ and $40\%$ AND Temp $> 25^\circ\text{C}$ AND No rain forecasted.
  3. **Do Not Water:** Moisture $< 40\%$ AND imminent rain forecasted.
  4. **Emergency Drainage:** Moisture $> 60\%$ AND Temp $< 5^\circ\text{C}$ (prevents root freeze damage).
  5. **Peak Solar Radiation Rule (12:00 PM – 4:00 PM):** Postpone standard watering cycles, UNLESS Soil Moisture $< 10\%$, which triggers **Critical Watering**.

---

## 4. The 10 Theoretical Work Questions (TW.02.01)

For each assigned problem, the designated pair must answer all 10 questions in their PDF:
1. Write pseudocode of identified methods.
2. Identify test units (classes and methods).
3. Identify input/state variables for testing.
4. Derive test values using Equivalence Partitioning (EP), Boundary Value Analysis (BVA), and Error Guessing.
5. Calculate theoretical combinatorial maximum of test cases.
6. Define minimal Each-Choice test suite (each value at least once).
7. Define 2-way Pairwise test suite (verified using PICT tool).
8. Propose test cases guaranteeing 100% Decision Coverage.
9. Propose test cases achieving MC/DC (Modified Condition/Decision Coverage).
10. Analytical commentary evaluating test suite sizes, coverage efficiency, and code testability impact.

---

## 5. Submission & Repository Guidelines

1. **3 GitHub Repositories:**
   * `ISO2-2025-GrupoA05-Testing-P1`
   * `ISO2-2025-GrupoA05-Testing-P2`
   * `ISO2-2025-GrupoA05-Testing-P3`
2. **Main Wiki Linkage:** The team's main Wiki (`2026-ISO2-A.05`) must have a section titled **"Testing Part"** linking to all 3 repositories *(mandatory rule: missing links = 0 points)*.
3. **Branching Standard:**
   * Code implementation: `develop`
   * Test implementation: dedicated **`hotfix`** branch from `develop`.
4. **Final Submission:**
   * Coordinator compiles the 3 PDFs into a single ZIP file and uploads it to Moodle/Virtual Campus before **Tuesday 15/12/2026**.
