package es.uclm.esi.iso2.common.orders.domain;

/** From the most to the least urgent, so orders can be sorted by priority. */
public enum Priority {
    URGENT,
    PREFERENTIAL,
    ROUTINE
}
