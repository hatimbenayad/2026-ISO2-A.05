package es.uclm.esi.iso2.common.orders.domain;

/** The steps of an examination order, in the order they happen. */
public enum OrderStatus {
    ORDERED,
    SCHEDULED,
    PERFORMED,
    REPORTED,
    /** The order stays stored with all its data (D-015). */
    CANCELLED
}
