package es.uclm.esi.iso2.server.adapters.pacs;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** Supplies fixed deterministic synthetic DICOM slices for orders. */
public class MockPacsAdapter implements IPacsAdapter {
    private static final String UID_ROOT = "2.25.";
    private static final String[] MODALITIES = {"CT", "MR", "CR", "US"};
    private static final LocalDateTime FIRST_ACQUISITION =
            LocalDateTime.of(2026, 1, 1, 8, 0);
    private static final int MIN_SLICES = 3;
    private static final int MAX_EXTRA_SLICES = 4;
    private static final long DEFAULT_SEED = 42L;

    private final long seed;

    /** Creates an adapter using the default deterministic seed. */
    public MockPacsAdapter() {
        this(DEFAULT_SEED);
    }

    /**
     * Creates an adapter using the supplied deterministic seed.
     *
     * @param seed the seed used to generate synthetic slices
     */
    public MockPacsAdapter(long seed) {
        this.seed = seed;
    }

    /**
     * Generates deterministic synthetic slices for an order.
     *
     * @param orderId the order identifier
     * @return an unmodifiable list of slices sorted by slice number
     * @throws IllegalArgumentException if the order id is null or blank
     */
    @Override
    public List<DicomSliceMetadata> fetchSlices(String orderId) {
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("The order id is empty");
        }

        Random random = new Random(seed ^ orderId.hashCode());
        String studyUid = UID_ROOT + Integer.toUnsignedString(orderId.hashCode());
        String seriesUid = studyUid + ".1";
        String modality = MODALITIES[random.nextInt(MODALITIES.length)];
        LocalDateTime acquiredAt =
                FIRST_ACQUISITION.plusMinutes(random.nextInt(60 * 24 * 300));
        int sliceCount = MIN_SLICES + random.nextInt(MAX_EXTRA_SLICES);

        List<DicomSliceMetadata> slices = new ArrayList<>(sliceCount);
        for (int number = 1; number <= sliceCount; number++) {
            slices.add(new DicomSliceMetadata(
                    studyUid,
                    seriesUid,
                    seriesUid + "." + number,
                    modality,
                    number,
                    acquiredAt.plusSeconds(number)));
        }
        return List.copyOf(slices);
    }
}
