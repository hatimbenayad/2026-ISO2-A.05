package es.uclm.esi.iso2.common.notifications;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Notifications Contracts Tests")
class NotificationsContractsTest {

    @Test
    void testNotificationTypeEnum() {
        assertEquals(4, NotificationType.values().length);
        assertEquals(NotificationType.REPORT_PUBLISHED, NotificationType.valueOf("REPORT_PUBLISHED"));
    }

    @Test
    void testNotificationDto() {
        LocalDateTime now = LocalDateTime.now();
        NotificationDto dto = new NotificationDto("NOTIF-1", "CIP123", NotificationType.REMINDER, "Your exam is tomorrow", now, false);

        assertAll(
                () -> assertEquals("NOTIF-1", dto.notificationId()),
                () -> assertEquals("CIP123", dto.recipientCip()),
                () -> assertEquals(NotificationType.REMINDER, dto.type()),
                () -> assertEquals("Your exam is tomorrow", dto.message()),
                () -> assertEquals(now, dto.sentAt()),
                () -> assertFalse(dto.isRead())
        );

        assertThrows(NullPointerException.class, () -> new NotificationDto(null, "CIP", NotificationType.REMINDER, "msg", now, false));
    }

    @Test
    void testClinicalHistoryRecordDto() {
        LocalDateTime procDate = LocalDateTime.of(2026, 11, 15, 10, 30);
        ClinicalHistoryRecordDto dto = new ClinicalHistoryRecordDto("HIST-1", "CIP123", "ORD-1", "Chest X-Ray", procDate, "Normal findings", "Dr. Smith");

        assertAll(
                () -> assertEquals("HIST-1", dto.recordId()),
                () -> assertEquals("CIP123", dto.patientCip()),
                () -> assertEquals("ORD-1", dto.orderNumber()),
                () -> assertEquals("Chest X-Ray", dto.procedureName()),
                () -> assertEquals(procDate, dto.procedureDate()),
                () -> assertEquals("Normal findings", dto.diagnosticSummary()),
                () -> assertEquals("Dr. Smith", dto.radiologistName())
        );

        assertThrows(NullPointerException.class, () -> new ClinicalHistoryRecordDto(null, "CIP", "ORD", "Name", procDate, null, null));
    }

    @Test
    void testSatisfactionSurveyDto() {
        LocalDateTime now = LocalDateTime.now();
        SatisfactionSurveyDto survey = new SatisfactionSurveyDto("SURV-1", "CIP123", "ORD-1", 5, "Excellent service", now);

        assertAll(
                () -> assertEquals("SURV-1", survey.surveyId()),
                () -> assertEquals("CIP123", survey.patientCip()),
                () -> assertEquals("ORD-1", survey.orderNumber()),
                () -> assertEquals(5, survey.ratingScore()),
                () -> assertEquals("Excellent service", survey.comments()),
                () -> assertEquals(now, survey.submittedAt())
        );

        assertThrows(IllegalArgumentException.class, () -> new SatisfactionSurveyDto("S1", "CIP", "O1", 0, "Bad", now));
        assertThrows(IllegalArgumentException.class, () -> new SatisfactionSurveyDto("S1", "CIP", "O1", 6, "Super", now));
        assertThrows(NullPointerException.class, () -> new SatisfactionSurveyDto(null, "CIP", "O1", 3, "Ok", now));
    }

    @Test
    void testPreparationGuidelineDto() {
        List<String> list = new ArrayList<>();
        list.add("Do not eat 6h before");
        PreparationGuidelineDto dto = new PreparationGuidelineDto("PROC-MRI", "Abdominal MRI Prep", list, true, 6);

        list.add("Extra step");
        assertEquals(1, dto.instructions().size());
        assertEquals("Do not eat 6h before", dto.instructions().get(0));

        assertThrows(NullPointerException.class, () -> new PreparationGuidelineDto(null, "Title", List.of(), false, 0));
        assertThrows(NullPointerException.class, () -> new PreparationGuidelineDto("P", null, List.of(), false, 0));
    }
}
