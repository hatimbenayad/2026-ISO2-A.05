package es.uclm.esi.iso2.server.adapters.pacs;

import java.util.List;

/** Provides access to image slices in the RIS/PACS platform (E04). */
public interface IPacsAdapter {

    /**
     * Gets the image slices of the study made for the order, sorted by slice number.
     *
     * @param orderId the order identifier
     * @return the study slices
     * @throws IllegalArgumentException if the order id is null or blank
     */
    List<DicomSliceMetadata> fetchSlices(String orderId);
}
