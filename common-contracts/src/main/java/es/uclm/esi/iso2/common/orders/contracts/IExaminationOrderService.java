package es.uclm.esi.iso2.common.orders.contracts;

import es.uclm.esi.iso2.common.orders.domain.OrderStatus;
import es.uclm.esi.iso2.common.orders.dto.ExaminationOrderDto;
import es.uclm.esi.iso2.common.orders.dto.ExaminationOrderRequest;
import java.util.List;

/** Contract for creating, listing and cancelling examination orders. */
public interface IExaminationOrderService {
    ExaminationOrderDto createOrder(ExaminationOrderRequest request);

    List<ExaminationOrderDto> listOrders(OrderStatus status);

    ExaminationOrderDto cancelOrder(String orderId);

    default ExaminationOrderDto create(ExaminationOrderRequest request) {
        return createOrder(request);
    }

    default List<ExaminationOrderDto> getOrders(OrderStatus status) {
        return listOrders(status);
    }

    default ExaminationOrderDto cancel(String orderId) {
        return cancelOrder(orderId);
    }

    default ExaminationOrderDto createExaminationOrder(ExaminationOrderRequest request) {
        return createOrder(request);
    }

    default List<ExaminationOrderDto> getExaminationOrders(OrderStatus status) {
        return listOrders(status);
    }

    default ExaminationOrderDto softCancelOrder(String orderId) {
        return cancelOrder(orderId);
    }
}
