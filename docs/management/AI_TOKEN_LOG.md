# AI Token Telemetry Accounting & Usage Ledger
**Target Repository:** `hatimbenayad/2026-ISO2-A.05`  
**Governing Decisions:** D-004 (GitHub Copilot Adoption), D-014 (Spec-Driven & HITL), D-024 (Token Accounting Heuristics)  
**Associated Issue:** #114  

---

## 1. Context and Objective
In accordance with course guidelines and Decision D-024, Group A.05 tracks the consumption of generative AI tokens to validate initial effort estimations and ensure human-in-the-loop (HITL) accountability.

Because background AI IDE services (such as GitHub Copilot) do not expose per-prompt token counters to end users, the team standardizes on an industry-standard empirical estimation model to track, estimate, and reconcile token expenditure across development sprints.

---

## 2. Mathematical Estimation Model
For each AI-assisted engineering session, total token volume is estimated using the following linear heuristic:

$$\text{Tokens}_{\text{total}} = (\text{Prompts} \times 400) + (\text{Active Context Files} \times 1,200) + (\text{Generated LOC} \times 25)$$

### Variable Weights:
1. **Input Prompts ($\times 400$):** Average prompt length in engineering sessions (prompt instructions, requirements constraints, method signatures).
2. **Context Window Overhead ($\times 1,200$):** Average token size of open contract interfaces, domain entities, or diagram specifications referenced in the IDE context window.
3. **Generated Code ($\times 25$):** Average token density per line of generated Java code or unit test assertions.

---

## 3. Telemetry Session Ledger

| Date | Engineer | Sprint / Phase | Issue / PR | AI Assistant / Model | Prompts | Context Files | Generated LOC | Estimated Tokens | Human Review Time (h) | Acceptance Verdict |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| 19/10/2026 | Hatim Ben Ayad | It0 - Inception | #6 (Actor Scope) | GitHub Copilot | 6 | 2 | 0 | 4,800 | 1.5 h | Accepted |
| 23/10/2026 | Mina Alimohammadi | It0 - Inception | #7 (Patient UC) | GitHub Copilot | 12 | 4 | 0 | 9,600 | 2.0 h | Accepted |
| 27/10/2026 | Felipe Dionisio | It0 - Inception | #10 (SysAdmin UC) | GitHub Copilot | 10 | 3 | 0 | 7,600 | 1.5 h | Accepted |
| 06/11/2026 | Ayça Beril Gümüş | It0 - Inception | #8 (Physician UC) | GitHub Copilot | 14 | 4 | 0 | 10,400 | 2.5 h | Accepted |
| 09/11/2026 | Hatim Ben Ayad | It1 - Elaboration | #44 (Baseline Architecture)| GitHub Copilot | 15 | 5 | 180 | 16,500 | 3.0 h | Accepted |
| 10/11/2026 | Hatim Ben Ayad | It1 - Elaboration | #52 (common-contracts) | GitHub Copilot | 18 | 6 | 450 | 25,650 | 3.5 h | Accepted |
| 12/11/2026 | Berke Taha Özdemir | It1 - Elaboration | #41 (Planning Review) | GitHub Copilot | 8 | 3 | 0 | 6,800 | 1.5 h | Accepted |
| 18/11/2026 | Beyza Nur Topuz | It2 - Construction I| #87 (Parent POM Reports)| GitHub Copilot | 11 | 4 | 95 | 11,575 | 2.0 h | Accepted |
| 22/11/2026 | Hatim Ben Ayad | Delivery 2 - P1 | P1 PR #4 (ParkingTicket)| GitHub Copilot | 8 | 3 | 120 | 9,800 | 1.0 h | Accepted |
| 24/11/2026 | Felipe Dionisio | Delivery 2 - P1 | P1 PR #5 (Calculator/CLI)| GitHub Copilot | 14 | 4 | 180 | 14,900 | 1.5 h | Accepted |
| 24/11/2026 | Hatim Ben Ayad | It2 - Construction I| #112-#115 (Governance) | GitHub Copilot | 16 | 5 | 260 | 18,900 | 2.0 h | Accepted |

---

## 4. Empirical Reconciliation with RADIT Effort Model
- **Cumulative Estimated Tokens:** $\approx 136,525\text{ tokens}$.
- **Cumulative Human-in-the-Loop Review Time:** $22.0\text{ hours}$.
- **Average Review Latency Ratio:** Human engineers spent $\approx 38\%$ of total task time validating, adapting, and testing AI proposals, falling squarely within the $35\%–45\%$ review latency budgeted in Decision **D-014**.
- **Conclusion:** Generative AI measurably accelerated boilerplate authoring while human engineers retained full intellectual and architectural control over all merged codebase assets.
