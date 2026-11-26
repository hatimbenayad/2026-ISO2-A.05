package es.uclm.esi.iso2.common.notifications;

import java.time.LocalDateTime;
import java.util.Objects;

public record SatisfactionSurveyDto(
        String surveyId,
        String patientCip,
        String orderNumber,
        int ratingScore,
        String comments,
        LocalDateTime submittedAt
) {
    public SatisfactionSurveyDto {
        Objects.requireNonNull(surveyId, "surveyId must not be null");
        Objects.requireNonNull(patientCip, "patientCip must not be null");
        if (ratingScore < 1 || ratingScore > 5) {
            throw new IllegalArgumentException("ratingScore must be between 1 and 5");
        }
    }
}
