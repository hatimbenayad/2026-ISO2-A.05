# IT2 PACS Viewing and Reporting Black-Box Test Design

Issue: **#75**

Use cases covered: **CU14 PACS viewing**, **CU15 diagnostic reporting**, **CU17 clinical incident reporting**.

## Equivalence partitions

| Partition ID | Input / state class | Representative values | Expected result |
|---|---|---|---|
| EP-PACS-01 | Valid pre-seeded PACS study UID | `1.2.840.113619.2.55`, `STUDY-1.2.840.10008` | Non-empty, deterministic DICOM series list |
| EP-PACS-02 | Unknown study UID | `NON_EXISTENT_UID` | `StudyNotFoundException` |
| EP-PACS-03 | Missing study UID | `null`, `""`, whitespace | `IllegalArgumentException` |
| EP-REP-01 | Valid draft report data | Normal findings and impression | Report created as `DRAFT`, `signedAt = null` |
| EP-INC-01 | Severe adverse reaction | Contrast extravasation | Incident recorded and patient emergency alert set |
| EP-INC-02 | Non-severe incident | Mild nausea | Incident recorded without emergency alert |

## Boundary-value analysis

| Partition ID | Boundary / state | Representative values | Expected result |
|---|---|---|---|
| BVA-REP-01 | First valid signature transition | `DRAFT` report with matching physician and token | Status becomes `REPORTED`; signing date is set |
| BVA-REP-02 | Final-state modification | `DRAFT` update succeeds; `REPORTED` update fails | Draft changes are stored; finalized report cannot change |

## Additional validation partitions

Mandatory string fields reject `null`, empty, and blank values. A report must exist before update or signature operations.
Signature credentials require both nonblank values and a physician ID matching the report owner. Duplicate or archived
signature attempts are rejected with `IllegalStateException`. Incident logging requires matching patient and incident IDs,
and stores an immutable snapshot of the incident history.

## Test classes

- `PacsIntegrationServiceTest`
- `DiagnosticReportServiceTest`
