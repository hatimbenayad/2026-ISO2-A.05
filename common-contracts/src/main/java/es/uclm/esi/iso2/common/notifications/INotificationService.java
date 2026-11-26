package es.uclm.esi.iso2.common.notifications;

import java.util.List;

public interface INotificationService {
    void sendNotification(NotificationDto notification);
    List<NotificationDto> getUnreadNotifications(String patientCip);
    List<ClinicalHistoryRecordDto> getPatientHistory(String patientCip);
    void submitSurvey(SatisfactionSurveyDto survey);
    PreparationGuidelineDto getPreparationGuideline(String procedureCode);
}
