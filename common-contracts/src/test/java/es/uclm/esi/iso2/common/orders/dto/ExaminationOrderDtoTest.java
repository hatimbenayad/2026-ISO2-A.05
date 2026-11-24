package es.uclm.esi.iso2.common.orders.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import es.uclm.esi.iso2.common.orders.domain.ExaminationOrder;
import es.uclm.esi.iso2.common.orders.domain.OrderStatus;
import es.uclm.esi.iso2.common.orders.domain.Priority;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Tests of {@link ExaminationOrderDto}: the client sees the order as it is, cancellation included. */
@DisplayName("ExaminationOrderDto")
class ExaminationOrderDtoTest {

    @Test
    @DisplayName("from keeps the data and the cancellation of the order")
    void fromKeepsTheCancellation() {
        // Arrange
        LocalDateTime cancelled = LocalDateTime.of(2026, 11, 11, 12, 30);
        ExaminationOrder order = new ExaminationOrder("ORD-0001", "PAT-0001", "mlopez", "MRI", "Knee pain",
                Priority.ROUTINE);
        order.cancel(cancelled);

        // Act
        ExaminationOrderDto dto = ExaminationOrderDto.from(order);

        // Assert
        assertEquals(new ExaminationOrderDto("ORD-0001", "PAT-0001", "mlopez", "MRI", "Knee pain", Priority.ROUTINE,
                OrderStatus.CANCELLED, cancelled), dto);
    }
}
