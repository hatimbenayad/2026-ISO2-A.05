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
**Decision:** Add a `common-contracts` module that `server` and `client` depend on, keeping the names `server` and `client`. Code is organised by component and layer under `es.uclm.esi.iso2`: `common.access` (Role, User, AuthSession and their DTOs), `common.patients` (Patient, its DTOs and the `IPatientRegistryService` contract of CU19) and `server.patients` (repository and service). The Physician → Patient generalization (D-007) is kept in `Role.includes`, not with class inheritance. Until the database is chosen (A08), patients are stored in memory.  
**Alternatives:** Renaming the modules to `servidor`/`cliente-web` (it would break the merged build and the CI); putting everything in `server` (the client would not have the contract).  
**Reason:** The client only depends on interfaces and DTOs ("the Engineer designs the Interfaces; the AI implements the Contracts"), and the existing build changes as little as possible.  
**Consequences:** `mvn verify` builds `common-contracts` first. New components follow the same packages, and their mock adapters go in `server`. The design diagrams are made in Visual Paradigm (D-005) and stored in `docs/diagrams/design/` as the project file and its exported image: `It1-baseline-design.vpp` and `.png` (classes) and `CU19-register-patient-communication.vpp` and `.png` (communication of CU19).
**Reason:** The client only depends on interfaces and DTOs ("the Engineer designs the Interfaces; the AI implements the Contracts"), and the existing build changes as little as possible.  
**Consequences:** `mvn verify` builds `common-contracts` first. New components follow the same packages, and their mock adapters go in `server`. Design diagrams in `docs/diagrams/design/`: `It1-baseline-design` (classes) and `CU19-register-patient-communication`.

