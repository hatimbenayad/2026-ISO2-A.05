package es.uclm.esi.iso2.client.presentation;

import es.uclm.esi.iso2.common.orders.contracts.IExaminationOrderService;
import es.uclm.esi.iso2.common.orders.domain.OrderStatus;
import es.uclm.esi.iso2.common.orders.dto.ExaminationOrderDto;
import es.uclm.esi.iso2.common.orders.dto.ExaminationOrderRequest;
import java.util.Collections;
import java.util.List;

final class NoOpOrderService implements IExaminationOrderService {
    @Override
    public ExaminationOrderDto createOrder(ExaminationOrderRequest request) {
        throw new IllegalStateException("No order service has been configured");
    }

    @Override
    public List<ExaminationOrderDto> listOrders(OrderStatus status) {
        return Collections.emptyList();
    }

    @Override
    public ExaminationOrderDto cancelOrder(String orderId) {
        throw new IllegalStateException("No order service has been configured");
    }
}
