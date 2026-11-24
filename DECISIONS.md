# DECISIONS

Log of the project's decisions: requirements, analysis, design and architecture decisions, assumptions, changes to the plan, and AI tools and models. One entry per decision, written when the decision is taken and linked to the meeting minutes where it was agreed. Code details are not logged here; they are in the commits. Team organisation is on the wiki page Team and roles, and work assignments are GitHub issues.

Each entry starts with its date, iteration, minutes (with the meeting record link) and, when there is one, the GitHub issue that tracks it. Then come its context, decision, alternatives, reason and consequences. A decision that is later changed is not rewritten: a Status line points to the decision that replaces or updates it.

This file is also context for the AI agent: it must follow these decisions and never change them on its own.

---

### D-001 · Division of functional requirements and use case diagrams by actor (Mina, Ayça, Hatim, Felipe)
**Date:** 19/10/2026 · **Iteration:** 0 · **Minutes:** [Meeting 01](https://github.com/hatimbenayad/2026-ISO2-A.05/wiki/Minutes-of-meetings) · **Issues:** [#6](https://github.com/hatimbenayad/2026-ISO2-A.05/issues/6), [#7](https://github.com/hatimbenayad/2026-ISO2-A.05/issues/7), [#8](https://github.com/hatimbenayad/2026-ISO2-A.05/issues/8), [#9](https://github.com/hatimbenayad/2026-ISO2-A.05/issues/9), [#10](https://github.com/hatimbenayad/2026-ISO2-A.05/issues/10)  
**Context:** The project statement specifies a complex healthcare system with 32 functional requirements across 4 user profiles (Patient, Physician, Admissions/Administrative, and System Administrator). Reviewing all requirements and modeling all use cases collectively in synchronous group meetings would create scheduling bottlenecks and slow down delivery.  
**Decision:** In the first meeting, the team agreed to divide both the functional requirements review and the creation of the individual use case diagrams among four team members according to actor:
- **Mina**: Patient requirements (RF01–RF09) and Patient use case diagram ([Issue #7](https://github.com/hatimbenayad/2026-ISO2-A.05/issues/7)).
- **Ayça**: Physician requirements (RF10–RF18) and Physician use case diagram ([Issue #8](https://github.com/hatimbenayad/2026-ISO2-A.05/issues/8)).
- **Hatim**: Administrative/Admissions requirements (RF19–RF26) and Administrative use case diagram ([Issue #9](https://github.com/hatimbenayad/2026-ISO2-A.05/issues/9)).
- **Felipe**: System Administrator requirements (RF27–RF32) and System Administrator use case diagram ([Issue #10](https://github.com/hatimbenayad/2026-ISO2-A.05/issues/10)).  
**Alternatives:** Reviewing all 32 requirements and drawing all diagrams synchronously together in whole-group calls; or assigning requirements randomly without grouping by actor.  
**Reason:** Dividing the work by actor allows parallel execution so the team finishes both functional requirements validation and use case diagrams significantly faster, accommodating different university schedules and establishing clear individual ownership.  
**Consequences:** Each member became the designated domain specialist for their actor, authoring their requirements review table and independent `.vpp` diagram in parallel. Cross-actor questions were resolved in team discussions, and individual outputs were consolidated into the shared functional requirements table and general client diagram.

---

### D-002 · Assembly of general client diagram from individual actor diagrams
**Date:** 19/10/2026 · **Iteration:** 0 · **Minutes:** [Meeting 01](https://github.com/hatimbenayad/2026-ISO2-A.05/wiki/Minutes-of-meetings) & [Meeting 2](https://github.com/hatimbenayad/2026-ISO2-A.05/wiki/Minutes-of-meetings) · **Issues:** [#6](https://github.com/hatimbenayad/2026-ISO2-A.05/issues/6), [#13](https://github.com/hatimbenayad/2026-ISO2-A.05/issues/13), PR [#20](https://github.com/hatimbenayad/2026-ISO2-A.05/pull/20)  
**Context:** Visual Paradigm binary project files (`.vpp`) cannot be merged automatically via Git text diffs. Team members needed a reliable method to combine their four actor models into a single coherent client diagram without overwriting each other's work.  
**Decision:** Each member creates an independent `.vpp` and exported `.png` diagram for their assigned actor. Hatim (Project Coordinator) is responsible for assembling the four individual diagrams into a master `GeneralClientDiagram.vpp` and publishing exported PNGs to GitHub.  
**Alternatives:** Passing a single master `.vpp` file around sequentially from person to person.  
**Reason:** Sequential editing causes severe team waiting bottlenecks; parallel modeling followed by a single designated assembly step is far faster and prevents binary file corruption.  
**Consequences:** Individual client diagrams (Patient, Physician, Admin, SysAdmin) were submitted via separate PRs (#19, #20, #21, #30). Hatim merges them into the unified client diagram before Elaboration begins.

---

### D-003 · Development IDE: Visual Studio / Visual Studio Code as primary IDE
**Date:** 19/10/2026 · **Iteration:** 0 · **Minutes:** [Meeting 01](https://github.com/hatimbenayad/2026-ISO2-A.05/wiki/Minutes-of-meetings)  
**Context:** The team needed a standardized, cross-platform development environment for Java coding, Maven build execution, Git version control, Markdown wiki authoring, and AI assistant integration. While course documents mention Eclipse as a reference tool, team members work across different operating systems (Windows, macOS, Linux) and sought a modern, extensible workflow.  
**Decision:** Standardize on **Visual Studio / Visual Studio Code** as the primary IDE across team members, equipped with the Java Extension Pack (Language Support for Java by Red Hat, Maven for Java, Project Manager for Java, Test Runner for Java) and GitHub extensions.  
**Alternatives:** Eclipse IDE; IntelliJ IDEA.  
**Reason:** Visual Studio provides lightweight performance, native cross-platform support, seamless Git and GitHub integration, superior Markdown editing for project wiki documentation, and built-in first-class compatibility with GitHub Copilot.  
**Consequences:** All team members configure their local development environments in Visual Studio / VS Code, ensuring consistent formatting, terminal workflows, and extension setups across the team.

---

### D-004 · AI Agent: GitHub Copilot as official AI coding assistant
**Date:** 19/10/2026 · **Iteration:** 0 · **Minutes:** [Meeting 01](https://github.com/hatimbenayad/2026-ISO2-A.05/wiki/Minutes-of-meetings)  
**Context:** The course encourages utilizing generative AI to accelerate software engineering tasks while requiring strict human accountability, traceability, and adherence to requirements.  
**Decision:** Adopt **GitHub Copilot** as the team's official AI assistant/agent for code autocompletion, boilerplate generation, unit test scaffolding, and documentation assistance.  
**Alternatives:** Manual development without AI assistance; using unvetted web chatbots without codebase context.  
**Reason:** GitHub Copilot is accessible to all team members via GitHub Student Developer Packs, integrates directly inside Visual Studio / VS Code, understands multi-module Maven project context, and significantly accelerates development velocity while keeping human engineers in full control.  
**Consequences:** All Copilot suggestions must be reviewed, tested, and validated by a human team member before committing. The AI agent is not permitted to commit or merge code directly to remote branches.

---

### D-005 · Visual Paradigm as the official UML modeling tool
**Date:** 23/10/2026 · **Iteration:** 0 · **Minutes:** [Meeting 2](https://github.com/hatimbenayad/2026-ISO2-A.05/wiki/Minutes-of-meetings)  
**Context:** Multiple UML diagramming tools were available (PlantUML, draw.io, Visual Paradigm, StarUML). The team needed an officially supported tool for academic evaluation.  
**Decision:** Standardize on **Visual Paradigm** (using the academic license provided by UCLM) for all official UML diagrams (use case, class, analysis, and sequence diagrams). All project commits must include both the editable `.vpp` file and an exported `.png` image in `docs/diagrams/`.  
**Alternatives:** PlantUML text files rendered via Markdown; Draw.io XML files.  
**Reason:** Visual Paradigm is the mandatory academic tool evaluated by the course instructor and provides automated model consistency checks.  
**Consequences:** Every team member maintains an active Visual Paradigm installation. Binary `.vpp` files are stored in `docs/diagrams/` alongside high-resolution exported PNGs.

---

### D-006 · Separate client and server use case diagrams
**Date:** 23/10/2026 · **Iteration:** 0 · **Minutes:** [Meeting 2](https://github.com/hatimbenayad/2026-ISO2-A.05/wiki/Minutes-of-meetings) · **Issues:** [#17](https://github.com/hatimbenayad/2026-ISO2-A.05/issues/17), [#32](https://github.com/hatimbenayad/2026-ISO2-A.05/issues/32)  
**Context:** The laboratory specification distinguishes between interactions initiated by human actors on client interfaces and server-side processing orchestrated with external hospital services.  
**Decision:** Maintain two distinct sets of use case diagrams: (1) Client Use Case Diagram, modeling human actor interactions (Patient, Physician, Admin, SysAdmin) within the client UI; and (2) Server Use Case Diagram, modeling backend services (`servidor`) interacting with the client and external healthcare systems.  
**Alternatives:** A single combined use case diagram containing all human actors, client UI flows, and external hospital backends.  
**Reason:** A single diagram would have over 40 use cases and 8 actors, becoming illegible and violating separation of concerns between client and server tiers.  
**Consequences:** Berke owns the server use case diagram (`ServerUseCaseDiagram.vpp`, PR #32), while client diagrams remain partitioned by actor.

---

### D-007 · Actor inheritance: Physician specializes Patient
**Date:** 23/10/2026 · **Iteration:** 0 · **Minutes:** [Meeting 2](https://github.com/hatimbenayad/2026-ISO2-A.05/wiki/Minutes-of-meetings) · **Issues:** [#15](https://github.com/hatimbenayad/2026-ISO2-A.05/issues/15), [#28](https://github.com/hatimbenayad/2026-ISO2-A.05/issues/28)  
**Context:** Physicians need to consult patient medical histories and diagnostic reports (RF12, RF15), which mirrors the Patient's own capability to view their records (RF02, RF03).  
**Decision:** In the client use case model, the `Physician` actor inherits from (specializes) the `Patient` actor. This grants physicians inherent access to patient record consultation without duplicating use case definitions.  
**Alternatives:** Creating duplicate use cases under Physician with identical logic; creating an abstract `HealthcareUser` superclass.  
**Reason:** Avoids visual clutter and functional duplication in the use case model; mirrors real-world healthcare privilege inheritance.  
**Consequences:** Handled in Issue #28. Mina and Ayça collaborate on the boundary connection in `GeneralClientDiagram.vpp`.  
**Status:** Clarified by D-018 (exact CU01–CU09 inheritance scope, own-care boundary, and clinical exclusions; corrects RF02/RF03 reference to RF01/RF06).

---

### D-008 · Server system boundary and external system actors (E01–E04)
**Date:** 23/10/2026 · **Iteration:** 0 · **Minutes:** [Meeting 2](https://github.com/hatimbenayad/2026-ISO2-A.05/wiki/Minutes-of-meetings) · **Issues:** [#17](https://github.com/hatimbenayad/2026-ISO2-A.05/issues/17), PR [#32](https://github.com/hatimbenayad/2026-ISO2-A.05/pull/32)  
**Context:** The server backend must interact with 4 distinct external hospital systems specified in the course statement.  
**Decision:** The server use case diagram defines a boundary titled `SESCAM Radiodiagnostics Server`. The Client Application acts as the primary calling actor, while four external systems are modeled as secondary actors: E01 (Corporate SSO), E02 (Electronic Health Record - EHR), E03 (Patient Admissions), and E04 (RIS/PACS). A Timer actor is introduced for scheduled background jobs (SCU05).  
**Alternatives:** Modeling external hospital systems inside the application boundary.  
**Reason:** External hospital systems are autonomous legacy enterprise systems outside the control of our project codebase.  
**Consequences:** External systems will be integrated via simulated mock adapter interfaces in the `common-contracts` module (SCU01–SCU13).

---

### D-009 · Peer-review matrix and Git Flow pull request approvals
**Date:** 23/10/2026 · **Iteration:** 0 · **Minutes:** [Meeting 2](https://github.com/hatimbenayad/2026-ISO2-A.05/wiki/Minutes-of-meetings) · **Issues:** [#12](https://github.com/hatimbenayad/2026-ISO2-A.05/issues/12)  
**Context:** Git Flow requires protected branches (`develop` and `main`). The team needed an explicit review assignment protocol to prevent PR stagnation.  
**Decision:** Establish a cross-review matrix:
- Mina (Patient) ↔ Felipe (SysAdmin) review each other's deliverables.
- Hatim (Admin) ↔ Ayça (Physician) review each other's deliverables.
- Berke (Git/Server) and Balota (Planning/Constraints) review cross-cutting architecture and planning.
- Direct pushes to `develop` and `main` are blocked. Every PR requires at least 1 approving peer review and green CI checks before Hatim merges.  
**Alternatives:** Allowing any random member to review whenever available; allowing authors to merge their own PRs.  
**Reason:** Guarantees balanced workload, prevents review bottlenecks, and enforces academic software quality assurance.  
**Consequences:** PRs cannot be merged without documented review approval on GitHub. Traceability is maintained between issues, branches, and PRs.

---

### D-010 · Planning governance: Balota leads constraints and availability, Berke leads SCM and release calendar
**Date:** 23/10/2026 · **Iteration:** 0 · **Minutes:** [Meeting 2](https://github.com/hatimbenayad/2026-ISO2-A.05/wiki/Minutes-of-meetings) · **Issues:** [#18](https://github.com/hatimbenayad/2026-ISO2-A.05/issues/18), [#31](https://github.com/hatimbenayad/2026-ISO2-A.05/issues/31)  
**Context:** The Planification page on the wiki requires collecting extensive constraints, student availability hours, and calendar milestones.  
**Decision:** Divide planning responsibilities: Balota leads collecting team availability, drafting constraints/assumptions (§2, §3), and risk matrices (§12). Berke leads SCM policies (§4), release calendar milestones (§11), and RADIT effort estimation baselines (§6, §7, §8).  
**Alternatives:** Leaving all project planning exclusively to the Coordinator.  
**Reason:** The course instructor specified that project planning must be an active collaborative team effort rather than a solo task.  
**Consequences:** Both Balota and Berke work directly on the wiki `Planification.md` page in parallel.

---

### D-011 · Multi-module Maven architecture: root aggregator with 4 decoupled submodules
**Date:** 27/10/2026 · **Iteration:** 0 · **Minutes:** [Meeting 2](https://github.com/hatimbenayad/2026-ISO2-A.05/wiki/Minutes-of-meetings) · **Issues:** [#33](https://github.com/hatimbenayad/2026-ISO2-A.05/issues/33), [#35](https://github.com/hatimbenayad/2026-ISO2-A.05/issues/35)  
**Context:** Course specifications require a multi-module Maven project in Java. The team must partition components to avoid circular dependencies and ensure clean layer decoupling.  
**Decision:** Create a root Maven aggregator `pom.xml` (`groupId: es.uclm.sescam`, `packaging: pom`) with four submodules:
1. `common-contracts`: Shared interfaces, POJO domain models, and DTOs (zero framework dependencies).
2. `servidor`: Backend business logic, persistence services, and mock external adapters (depends on `common-contracts`).
3. `cliente-web`: Web MVC controllers, diagnostic appointment booking, and DICOM viewing UI (depends on `common-contracts`).
4. `cliente-movil`: Mobile client interfaces and notification receivers (depends on `common-contracts`).  
**Alternatives:** Single monolithic Maven project; two separate repositories for client and server.  
**Reason:** Allows independent compilation and testing while sharing a unified build lifecycle (`mvn clean verify`). Ensures client and server share identical contract definitions without code duplication.  
**Consequences:** Hatim scaffolds the root `pom.xml` and submodule directory structures in Elaboration (Milestone 2).  
**Status:** Updated by D-017: the build has `common-contracts`, `server` and `client`.

---

### D-012 · Simulation of external healthcare platforms (SSO, EHR, Admissions, PACS)
**Date:** 27/10/2026 · **Iteration:** 0 · **Minutes:** [Meeting 2](https://github.com/hatimbenayad/2026-ISO2-A.05/wiki/Minutes-of-meetings)  
**Context:** The SESCAM specification requires integration with external hospital systems (SSO, EHR, Admissions, RIS/PACS) using HL7/FHIR and DICOM protocols. Live SESCAM production APIs are not provided.  
**Decision:** External hospital platforms are simulated behind strict Java interfaces in `common-contracts/src/main/java/es/uclm/sescam/contracts/adapters/`. Mock adapter implementations are provided in `servidor/src/main/java/es/uclm/sescam/server/adapters/mock/` returning synthetic clinical test data.  
**Alternatives:** Implementing real live network connections to external public healthcare sandboxes.  
**Reason:** Live healthcare integrations require specialized hospital network credentials and are outside the academic scope of the course. Simulated interfaces allow reproducible automated unit testing.  
**Consequences:** Testing does not require network connectivity; test suites run hermetically in Maven build pipelines.

---

### D-013 · Direct self-service editing for GitHub Wiki vs Git Flow for repository code
**Date:** 06/11/2026 · **Iteration:** 0 · **Minutes:** [Meeting 2](https://github.com/hatimbenayad/2026-ISO2-A.05/wiki/Minutes-of-meetings) · **Issues:** [#18](https://github.com/hatimbenayad/2026-ISO2-A.05/issues/18), [#31](https://github.com/hatimbenayad/2026-ISO2-A.05/issues/31)  
**Context:** The team needed to clarify whether documentation on the GitHub Wiki must follow the same Git Flow PR approval process as Java source code.  
**Decision:** Establish a two-tiered contribution rule:
1. **GitHub Wiki Documentation:** Direct self-service editing. Authors (Balota, Berke, Felipe, Hatim, Mina, Ayça) edit and publish Wiki pages directly in the browser or via git push to the wiki repository without creating PRs or waiting for coordinator merge approval.
2. **Repository Code & Build Artifacts:** Strict Git Flow. All modifications to `pom.xml`, Java source files, diagrams, and CI workflows must go through dedicated feature branches (`feature/...`), peer review, and coordinator merge into `develop`.  
**Alternatives:** Forcing wiki pages through repository pull requests via git submodule.  
**Reason:** Eliminates documentation bottlenecks; allows rapid planning updates and meeting minute publication while keeping code strictly protected.  
**Consequences:** Wiki pages (like `Planification.md` and `Minutes-of-meetings`) are updated and published in real time.

---

### D-014 · AI agent governance: Spec-Driven Development and human-in-the-loop accountability
**Date:** 06/11/2026 · **Iteration:** 0 · **Minutes:** [Meeting 01](https://github.com/hatimbenayad/2026-ISO2-A.05/wiki/Minutes-of-meetings) & [Meeting 2](https://github.com/hatimbenayad/2026-ISO2-A.05/wiki/Minutes-of-meetings)  
**Context:** The team utilizes GitHub Copilot to assist in software engineering tasks while mandating that human engineers maintain ultimate accountability and full ownership of all code.  
**Decision:** Adopt the principle: *"The Engineer designs the Interfaces; the AI implements the Contracts."*
- GitHub Copilot proposes code snippets, implementations, and unit tests, but is never permitted to commit or merge code directly.
- All AI-suggested code must be reviewed, understood, and tested by a human team member.
- Latency for human review (~35–45% of task duration) is explicitly budgeted in all planning estimates.  
**Alternatives:** Disallowing AI tools entirely; allowing automated agents to commit directly to branches without human review.  
**Reason:** Complies with course guidelines while accelerating implementation velocity.  
**Consequences:** Every pull request carries human verification evidence and passing JUnit tests.

---

### D-015 · Clinical records: deactivation and cancellation instead of destructive deletion
**Date:** 06/11/2026 · **Iteration:** 0 · **Minutes:** [Meeting 2](https://github.com/hatimbenayad/2026-ISO2-A.05/wiki/Minutes-of-meetings)  
**Context:** Medical domain specifications require that patient records, diagnostic orders, and appointments must never disappear from the system audit log.  
**Decision:** No use case performs destructive database `DELETE` operations on clinical entities. Cancelling an appointment marks its status as `CANCELLED`; discharging a patient marks their status as `INACTIVE`; removing a diagnostic test deactivates the test type.  
**Alternatives:** Cascading hard delete of patient records and associated orders.  
**Reason:** Spanish and European healthcare regulations (LOPD/GDPR, SESCAM clinical compliance) mandate strict traceability and permanent clinical audit logging.  
**Consequences:** Domain entities include `status` and `deactivatedAt` fields; repository queries filter by active status by default.

---

### D-016 · Release calendar alignment with academic deadlines and Santa Tecla holiday
**Date:** 06/11/2026 · **Iteration:** 0 · **Minutes:** [Meeting 2](https://github.com/hatimbenayad/2026-ISO2-A.05/wiki/Minutes-of-meetings) · **Issues:** [#18](https://github.com/hatimbenayad/2026-ISO2-A.05/issues/18), [#31](https://github.com/hatimbenayad/2026-ISO2-A.05/issues/31)  
**Context:** Project schedules must account for university academic deadlines and official faculty non-working periods.  
**Decision:** Lock key project milestones to academic deadlines:
- Inception Phase (v0.0.1): October 19 – November 6, 2026.
- Elaboration Phase (v0.1.0): November 7 – November 13, 2026 (concluding with Moodle submission of Theoretical Assignment 1 on November 13).
- Construction Phase (v0.2.0–v0.8.0): November 14 – December 8, 2026.
- Non-working holiday break: Santa Tecla week (November 23–27, 2026) has 0 billable hours.
- Transition Phase (v1.0.0): December 9 – December 15, 2026 (concluding with Final Lab Deliverable submission on December 15).  
**Alternatives:** Scheduling uniform weekly hours without accounting for holidays or exams.  
**Reason:** Avoids unrealistic sprint plans and prevents delivery crunches during exam periods.  
**Consequences:** Formalized in `Planification.md` §11 and reflected in GitHub Milestones M0 through M5.

---

### D-017 · `common-contracts` module and core domain of the architectural baseline
**Date:** 09/11/2026 · **Iteration:** 1 · **Minutes:** [Meeting 4](https://github.com/hatimbenayad/2026-ISO2-A.05/wiki/Minutes-of-meetings) · **Issues:** [#44](https://github.com/hatimbenayad/2026-ISO2-A.05/issues/44), [#45](https://github.com/hatimbenayad/2026-ISO2-A.05/issues/45)  
**Context:** D-011 planned `common-contracts`, `servidor`, `cliente-web` and `cliente-movil`, but the build merged in PR #49 has `es.uclm.esi.iso2:radiodiagnostics` with `server` and `client`. Client and server need a shared contract for the core entities of It1.  
**Decision:** Add a `common-contracts` module that `server` and `client` depend on, keeping the names `server` and `client`. Code is organised by component and layer under `es.uclm.esi.iso2`: `common.access` (Role, User, AuthSession and their DTOs), `common.patients` (Patient, MedicalRecord, their DTOs and the `IPatientRegistryService` contract of CU19), `common.orders` (ExaminationOrder with its Priority and OrderStatus, and its DTO) and `server.patients` (repository and service). The Physician → Patient generalization (D-007) is kept in `Role.includes`, not with class inheritance. Until the database is chosen (A08), patients are stored in memory.  
**Alternatives:** Renaming the modules to `servidor`/`cliente-web` (it would break the merged build and the CI); putting everything in `server` (the client would not have the contract).  
**Reason:** The client only depends on interfaces and DTOs ("the Engineer designs the Interfaces; the AI implements the Contracts"), and the existing build changes as little as possible.  
**Consequences:** `mvn verify` builds `common-contracts` first. New components follow the same packages, and their mock adapters go in `server`. The design diagrams are made in Visual Paradigm (D-005) and stored in `docs/diagrams/design/` as the project file and its exported image: `It1-baseline-design.vpp` and `.png` (classes) and `CU19-register-patient-communication.vpp` and `.png` (communication of CU19).


---

### D-018 · Scope and clinical exclusions of Physician–Patient actor generalization
**Date:** 10/11/2026 · **Iteration:** 1 · **Minutes:** [Meeting 3](https://github.com/hatimbenayad/2026-ISO2-A.05/wiki/Minutes-of-meetings) · **Issues:** [#28](https://github.com/hatimbenayad/2026-ISO2-A.05/issues/28), [#36](https://github.com/hatimbenayad/2026-ISO2-A.05/issues/36), PR [#51](https://github.com/hatimbenayad/2026-ISO2-A.05/pull/51)  
**Context:** While reviewing the combined Patient–Physician client use-case diagram (`D-007`), the team analyzed the specification clause granting physicians *"all applicable Patient features"* (Spec p. 2). Pure UML actor generalization (`Physician ──▷ Patient`) structurally inherits all ovals (`CU01–CU09`), whereas in clinical practice certain patient interactions are inapplicable to a physician acting professionally. Additionally, Issue #36 listed an unmapped task (`CU18 (Emergency Override) «extend» CU12 (Consult Medical History)`), whereas the team's approved RF/CU catalogue defines `CU12` as *Manage procedure orders* and `CU18` as *Consult order metrics*.  
**Decision:** Retain the `Physician ──▷ Patient` generalization connector in `patient-physician-use-cases.vpp` (PR #51) and enforce the following formal semantics and exclusions:
1. **Inherited scope (Own Care vs. Clinical Role):** Under UML generalization, `CU01–CU09` apply strictly to the physician's **own personal appointments and care as a patient** (plus shared notifications `CU05` and follow-up guidelines `CU09`), **never** to view or alter other patients' records. Access to assigned patients' clinical data and orders is provided exclusively via `CU10–CU18` (`CU12–CU15`).
2. **Clinical role exclusions & mappings across `CU01–CU09`:**
   - `CU01` (*Consult scheduled appointments*): Own agenda/appointments inherited; staff execution schedules accessed via `CU13`.
   - `CU02` (*Confirm appointment*), `CU03` (*Request rescheduling*), `CU04` (*Cancel appointment*): Excluded from clinical role (patient-only personal actions; physicians cancel clinical *orders* via `CU12`).
   - `CU05` (*Receive notifications & reminders*): Shared notification delivery for own care / clinical alerts.
   - `CU06` (*Consult procedure history*): Own history inherited; assigned patients' reports and medical images accessed via `CU14`/`CU15`.
   - `CU07` (*Submit incident report*): Excluded from clinical workflow and replaced by `CU17` (*Document incident*).
   - `CU08` (*Complete satisfaction survey*): Excluded from clinical role (strictly patient experience feedback).
   - `CU09` (*Consult follow-up information & guidelines*): Inherited and applied alongside follow-up protocol ordering (`CU11`).
3. **Correction to D-007 references:** Patient appointment consultation and procedure history map to `RF01`/`CU01` and `RF06`/`CU06` (not `RF02`/`RF03`).
4. **Exclusion of `CU18 «extend» CU12` from #36:** Rejected because no "Emergency Override" or "Consult Medical History" use case exists in `RF01–RF32`.  
**Alternatives:** Keeping Physician and Patient completely disconnected in the diagram; creating duplicate ovals; adding unmapped features outside `RF01–RF32`.  
**Reason:** Preserves clean UML generalization (`D-007`), prevents scope creep, documents the exact meaning of *"applicable"*, and establishes clear authorization boundaries (`Role.includes`) for implementation.  
**Consequences:** Authorization rules on server services must scope `CU01–CU09` to self-ownership (`userId == targetPatientAccount`) and require `PHYSICIAN` role privileges for assigned patient data in `CU10–CU18`.

---

### D-019 · Multi-platform build standardization via Maven Wrapper and CI
**Date:** 10/11/2026 · **Iteration:** 1 · **Minutes:** [Meeting 3](https://github.com/hatimbenayad/2026-ISO2-A.05/wiki/Minutes-of-meetings) · **Issues:** [#42](https://github.com/hatimbenayad/2026-ISO2-A.05/issues/42), [#43](https://github.com/hatimbenayad/2026-ISO2-A.05/issues/43), PR [#49](https://github.com/hatimbenayad/2026-ISO2-A.05/pull/49), PR [#53](https://github.com/hatimbenayad/2026-ISO2-A.05/pull/53)  
**Context:** Following setup of the multi-module Maven build and GitHub Actions CI workflow (`.github/workflows/maven.yml` running `mvn -B verify` under JDK 21), team members on Windows, macOS, and Linux needed deterministic local builds without manual global Maven installation and mismatched versions.  
**Decision:** Standardize project compilation and JUnit 6.1.3 test execution on **Maven Wrapper 3.3.4 pinned to Maven 3.9.16** (`only-script` distribution via `./mvnw` and `.\mvnw.cmd`). Enforce Unix LF (`mvnw`) and Windows CRLF (`*.cmd`) line endings via `.gitattributes`, ignore local IDE folders (`.vscode/`, `.idea/`) and Maven `target/` outputs in `.gitignore`, and gate merges to `develop`/`main` on a passing GitHub Actions `mvn -B verify` check.  
**Alternatives:** Requiring each member to manually install matching Maven binaries; committing wrapper `.jar` binaries directly to Git.  
**Reason:** Guarantees zero build discrepancy across developer OSes, keeps binary artifacts out of Git, and satisfies Issue #43 verification requirements.  
**Consequences:** All members only require **JDK 21** locally and run `./mvnw clean verify` (Linux/macOS) or `.\mvnw.cmd clean verify` (Windows) before pushing commits.

---

### D-020 · Semantic Versioning 2.0.0 release progression and Git Flow branch preservation
**Date:** 10/11/2026 · **Iteration:** 1 · **Minutes:** [Meeting 4](https://github.com/hatimbenayad/2026-ISO2-A.05/wiki/Minutes-of-meetings) · **Issues:** [#31](https://github.com/hatimbenayad/2026-ISO2-A.05/issues/31), [#41](https://github.com/hatimbenayad/2026-ISO2-A.05/issues/41), [#42](https://github.com/hatimbenayad/2026-ISO2-A.05/issues/42)  
**Context:** Per Git & GitHub Slides 4–6, 19–23 and Kickoff Slide 9, the project tracks both overall system release tags on `main` and independent Maven submodule versions (`common-contracts`, `server`, `client`), while preserving complete branch history in GitHub Network/Insights for course evaluation. Additionally, D-016 listed a wide version gap (`v0.2.0–v0.8.0`) across three Construction iterations.  
**Decision:** Establish a monotonic Semantic Versioning 2.0.0 release cadence and explicit branch preservation policy:
1. **System Releases (`main` annotated tags via `release/X.Y.Z`):** `v0.0.1` (Inception setup patch) -> `v0.1.0` (Elaboration architectural baseline) -> `v0.2.0` (Construction I) -> `v0.3.0` (Construction II) -> `v0.4.0` (Construction III) -> `v1.0.0` (Transition production release on 15/12/2026). Critical post-release fixes branch via `hotfix/X.Y.Z`, bump `PATCH` (e.g., `v0.1.1`), and sync back to `develop`. When bumping the root `pom.xml` version in `release/X.Y.Z`, update `<parent><version>` identically in every submodule `pom.xml` (`server`, `client`, and `common-contracts`).
2. **Submodule Versions & Use-Case Branches (`pom.xml`):** Modules start at `1.0.0-SNAPSHOT` and advance `MINOR` upon delivering a backward-compatible use case (`1.1.0`, `1.2.0`, ...) or `PATCH` for fixes (`1.0.1`). Starting in Construction (`It2`), each use-case parent issue tracks per-module sub-issues and feature branches (e.g., `feature/CUxx-server`, `feature/CUxx-client`).
3. **Git Flow Branch Preservation (Kickoff Slide 9):** Feature (`feature/*`), bugfix (`bugfix/*`), and release (`release/*`) branches are **never deleted after merging** so complete branching history stays auditable in GitHub Insights. Feature testing fixes remain on their active `feature/*` branch (`fix(CUxx): ...`), whereas post-merge fixes on `develop` use `bugfix/*`.  
**Alternatives:** Deleting feature branches after PR merge; skipping intermediate MINOR tags.  
**Reason:** Aligns `Planification.md`, `pom.xml`, and SCM governance 100% with the instructor's rubric (Kickoff Slide 9 & 20).  
**Consequences:** Updates `D-016`'s Construction version targets to `v0.2.0–v0.4.0`; Release Coordinator tags `main` via `git tag -a vX.Y.Z` on milestone closure without deleting branches.

---

### D-021 · Time-Tracking Excel Workbook and Weekly RADIT Allocation Logging
**Date:** 17/11/2026 · **Iteration:** 1 · **Minutes:** [Meeting 05](https://github.com/hatimbenayad/2026-ISO2-A.05/wiki/Minutes-of-meetings) · **Issues:** [#80](https://github.com/hatimbenayad/2026-ISO2-A.05/issues/80)  
**Context:** Lab session S.04.02 (Slide 22) mandates: *"Always log today's hours in the time-tracking Excel"*. Estimating engineering effort in the Unified Process requires empirical reconciliation between planned effort (RADIT model) and actual person-hours dedicated by each team member across iterations.  
**Decision:** Standardize on a versioned Excel workbook located at `docs/management/Time_Tracking_ISO2_A05.xlsx`. Every team member logs their individual hours weekly, categorized by Unified Process discipline (Requirements, Analysis, Design, Implementation, Testing) and project phase. Berke and Balota oversee workbook reconciliation prior to milestone reviews.  
**Alternatives:** Relying solely on GitHub issue timestamps; using untracked personal notes; logging hours only at the end of the course.  
**Reason:** A versioned, centralized spreadsheet ensures auditability, prevents retrospective guesswork, and enables direct comparison between the initial RADIT plan (Planification §8) and actual engineering investment.  
**Consequences:** `docs/management/Time_Tracking_ISO2_A05.xlsx` is tracked in Git; hours are updated weekly and summarized in milestone assessment deliverables.

---

### D-022 · Delivery 2 Cross-Testing Architecture and Independent Verification (P1, P2, P3)
**Date:** 20/11/2026 · **Iteration:** 1 · **Minutes:** [Meeting 06](https://github.com/hatimbenayad/2026-ISO2-A.05/wiki/Minutes-of-meetings) · **Issues:** [#88](https://github.com/hatimbenayad/2026-ISO2-A.05/issues/88), [#89](https://github.com/hatimbenayad/2026-ISO2-A.05/issues/89), [#90](https://github.com/hatimbenayad/2026-ISO2-A.05/issues/90)  
**Context:** The second major project delivery requires three rotating pairs to solve three distinct software problems (P1: Parking Fee Calculator, P2: Scholarship Eligibility, P3: Smart Greenhouse Irrigation) while enforcing the **Independent Verification Rule** (*"Developers responsible for writing the code should not perform the testing"*).  
**Decision:** Scaffold three independent Maven repositories under the naming standard `ISO2-2026-GrupoA05-Testing-PX` (P1, P2, P3), linked directly from the main repository wiki under a dedicated section titled **"Testing Part"**. The workload rotates strictly per Table 1:
- **Pair 1 (Hatim & Felipe):** Step 1 (Code on `develop`) for P1; Step 3 (JUnit on `hotfix`) for P2; Step 2 (Theory PDF) for P3.
- **Pair 2 (Mina & Ayça):** Step 2 (Theory PDF) for P1; Step 1 (Code on `develop`) for P2; Step 3 (JUnit on `hotfix`) for P3.
- **Pair 3 (Berke & Beyza):** Step 3 (JUnit on `hotfix`) for P1; Step 2 (Theory PDF) for P2; Step 1 (Code on `develop`) for P3.  
All three repositories enforce Java 21, Maven 3.9+, JUnit 5, and a minimum **>= 80% JaCoCo Line Coverage Gate**.  
**Alternatives:** Housing all three problems in subfolders of the main repository; allowing developers to test their own code.  
**Reason:** Standalone repositories mirror real-world client segregation and satisfy the exact submission requirements of the practical instructor.  
**Consequences:** Repositories `ISO2-2026-GrupoA05-Testing-P1/P2/P3` created; permissions granted to all members and instructor `IsmaelCaballero`; code authored on `develop` and test suites restricted to `hotfix`.

---

### D-023 · Formal Requirements Traceability Matrix (RTM) across Unified Process Workflows
**Date:** 17/11/2026 · **Iteration:** 1 · **Minutes:** [Meeting 05](https://github.com/hatimbenayad/2026-ISO2-A.05/wiki/Minutes-of-meetings) · **Issues:** [#81](https://github.com/hatimbenayad/2026-ISO2-A.05/issues/81)  
**Context:** In the lab session of 17/11/2026, the instructor emphasized that listing requirements by actor or iteration is insufficient without demonstrating complete lifecycle traceability. An engineering project requires rigorous proof that each customer requirement maps cleanly into analysis models, software design, implementation classes, automated tests, and release milestones.  
**Decision:** Establish and maintain a formal **Requirements Traceability Matrix (RTM)** within the project wiki (`Requirements-and-use-cases.md`). Every requirement (RF01–RF32) is mapped forward and backward through six dimensions:
1. **Functional Requirement ID (RFxx)** & Title.
2. **Analysis Artifact:** Use Case (CUxx / SCUxx) & Communication Diagram.
3. **Design Artifact:** Contract DTO / Interface in `common-contracts` or Service in `server`.
4. **Implementation:** Target Java Package and Class.
5. **Verification & Testing:** Corresponding JUnit Test Suite.
6. **Milestone / Release:** Target SemVer milestone (`v0.1.0`, `v0.2.0`, `v0.3.0`, `v1.0.0`).  
**Alternatives:** Leaving requirements mapped only to actors; relying solely on GitHub issue closing keywords.  
**Reason:** Provides end-to-end auditability, guarantees zero orphan requirements, and demonstrates full compliance with ISO/IEC 12207 and Unified Process principles during course evaluations.  
**Consequences:** The wiki page `Requirements and Use Cases` is expanded with the master RTM; each subsequent code PR must verify that its affected requirement row is updated.

---

### D-024 · AI Token Accounting, Empirical Heuristic Modeling and Usage Reconciliation
**Date:** 17/11/2026 · **Iteration:** 1 · **Minutes:** [Meeting 05](https://github.com/hatimbenayad/2026-ISO2-A.05/wiki/Minutes-of-meetings) · **Issues:** [#82](https://github.com/hatimbenayad/2026-ISO2-A.05/issues/82)  
**Context:** The instructor queried the team on AI token accounting to validate that initial AI cost/effort estimates match real-world consumption. Because IDE AI assistants (such as GitHub Copilot) operate as background extension services without exposing a real-time per-request token telemetry counter to student accounts, raw token counts cannot be logged directly via an API dashboard.  
**Decision:** Adopt an **Empirical AI Token Accounting Model** based on industry standard tokenization heuristics:
- **Input / Context Tokens:** Estimated at ~400 tokens/prompt + (Active Files in Context * 1,200 tokens).
- **Output / Completion Tokens:** Estimated at ~25 tokens per generated LOC.  
Team members log AI-assisted engineering sessions in the project time-tracking workbook (`docs/management/Time_Tracking_ISO2_A05.xlsx`) under an "AI Telemetry" log, recording the AI tool used (GitHub Copilot, Gemini CLI), task/issue ID, estimated token volume, and corresponding human verification time (HITL).  
**Alternatives:** Claiming inability to track tokens due to IDE limitations; inventing arbitrary token counts without a defined mathematical model.  
**Reason:** Provides an intellectually honest, defensible engineering methodology that reconciles initial planning estimates with empirical reality.  
**Consequences:** AI token expenditure is transparently documented alongside person-hours, validating the economic and productivity assumptions made in Planning §11.

---

### D-025 · Optimistic Feature Branch Governance and Protected Trunk Perimeter
**Date:** 17/11/2026 · **Iteration:** 1 · **Minutes:** [Meeting 05](https://github.com/hatimbenayad/2026-ISO2-A.05/wiki/Minutes-of-meetings) · **Issues:** [#83](https://github.com/hatimbenayad/2026-ISO2-A.05/issues/83)  
**Context:** In the lab session of 17/11/2026, the instructor questioned why all team members possess repository `write` permissions and why individual `feature/*` branches lack restrictive GitHub rulesets.  
**Decision:** Formally ratify an **Optimistic Feature Branch Model with a Hardened Trunk Perimeter**:
1. **Hardened Perimeter:** The shared integration branches (`main` and `develop`) are strictly protected via GitHub Ruleset `24678480` (enforcing deletion block, force-push block, linear history, and required pull requests with green CI). No code can enter the product baseline unreviewed.
2. **Optimistic Feature Space:** Feature branches (`feature/*`) remain free of administrative locks to maximize developer agility and prevent bottlenecking during active coding sessions.
3. **Social Contract & Branch Ownership:** Feature branches are strictly single-owner, corresponding 1-to-1 with assigned GitHub issues. The team adheres to an explicit non-negotiable policy: **Remote branches are NEVER deleted** (Kickoff Slide 9 branch preservation rule), and **no member pushes to another member's feature branch** without prior explicit coordination.  
**Alternatives:** Creating restrictive rulesets for every individual feature branch; restricting repository permissions to read-only forks.  
**Reason:** Enforcing branch rulesets on short-lived feature branches adds excessive administrative friction without providing tangible security, as the protected `develop` branch already acts as an unbreakable quality firewall.  
**Consequences:** Developers retain autonomy within their designated feature branches, while baseline stability and historical branch preservation are 100% guaranteed.

---

### D-026 · Centralized Project Management and Issue Lifecycle Governance (Coordinator Ownership)
**Date:** 17/11/2026 · **Iteration:** 1 · **Minutes:** [Meeting 05](https://github.com/hatimbenayad/2026-ISO2-A.05/wiki/Minutes-of-meetings) · **Issues:** [#84](https://github.com/hatimbenayad/2026-ISO2-A.05/issues/84)  
**Context:** Effective execution in a 6-person team requires strict alignment between the project backlog, Unified Process milestones, and GitHub Project boards. Uncontrolled issue authoring leads to scope drift, mismatched branch names, and orphaned backlog items.  
**Decision:** The Project Coordinator (Hatim Ben Ayad) acts as the single point of governance for the project backlog and board operations:
1. **Issue Creation:** All GitHub issues (Epics, Features, Analysis, Design, Testing) are authored or vetted by the coordinator to ensure uniform acceptance criteria, branch naming instructions, and milestone tagging.
2. **Board Transitions:** The coordinator transitions cards across the GitHub Project board (`To Do` -> `In Progress` -> `In Review` -> `Done`).
3. **Issue Resolution:** Issues are closed automatically when their corresponding Pull Request is merged into `develop` using GitHub linking keywords (`closes #X`), or closed manually by the coordinator upon verifying Definition of Done compliance.  
**Alternatives:** Allowing any team member to freely create unlinked issues; managing project status without a synchronized GitHub Project board.  
**Reason:** Enforces milestone discipline, ensures 100% issue-to-branch consistency, and provides instructors with a transparent, professional project management dashboard.  
**Consequences:** Clear task allocation; no duplicate or abandoned issues; accurate burndown metrics for course evaluations.

---

### D-027 · Repository Administrator Ruleset Bypass and Coordinator Pull Request Approval Protocol
**Date:** 22/11/2026 · **Iteration:** 1 · **Minutes:** [Meeting 06](https://github.com/hatimbenayad/2026-ISO2-A.05/wiki/Minutes-of-meetings) · **Issues:** PR [#4](https://github.com/hatimbenayad/ISO2-2026-GrupoA05-Testing-P1/pull/4), PR [#101](https://github.com/hatimbenayad/2026-ISO2-A.05/pull/101)  
**Context:** Under Decision D-009, Pull Requests targeting `develop` require peer approval. However, during critical synchronization windows, emergency merge conflict resolutions (e.g., binary `.png` conflicts in PR #101), or administrative scaffolding, waiting for peer availability can stall team velocity. The Project Coordinator discussed this workflow with practical instructor **Ismael Caballero**, who confirmed that administrator bypass is fully acceptable provided the team works responsibly and maintains rigorous oversight.  
**Decision:** Enable `Repository admin` bypass on GitHub Ruleset `24678480` for the Project Coordinator (`@hatimbenayad`). The coordinator is authorized to approve and merge pull requests when:
1. The PR contains emergency build/merge conflict resolutions required to unblock teammates.
2. The PR involves baseline administrative documentation, scaffolding, or project tracking artifacts.
3. The assigned peer reviewer is temporarily unavailable due to academic constraints, and the PR has successfully passed all automated GitHub Actions CI checks (`mvn verify`).  
Standard feature PRs continue to require peer review whenever possible. The coordinator assumes full architectural accountability for all merged PRs.  
**Alternatives:** Halting all pipeline merges whenever a peer reviewer is unavailable; entirely disabling branch protection rulesets.  
**Reason:** Formally validated with Professor Ismael Caballero. Balances academic peer review standards with pragmatic project momentum, preventing pipeline deadlocks while maintaining a fully auditable Git trail.  
**Consequences:** Eliminates integration bottlenecks; all admin bypass merges are transparently logged in GitHub PR audit trails; satisfies both academic standards and team operational requirements.
