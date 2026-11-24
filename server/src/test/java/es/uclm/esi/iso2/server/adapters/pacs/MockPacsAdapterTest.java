package es.uclm.esi.iso2.server.adapters.pacs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

/** Tests of {@link MockPacsAdapter}: synthetic slices and validation. */
@DisplayName("MockPacsAdapter")
class MockPacsAdapterTest {
    private MockPacsAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new MockPacsAdapter();
    }

    @Test
    @DisplayName("An order gets valid slices numbered from one")
    void orderGetsValidSlices() {
        // Arrange
        String orderId = "ORD-0001";

        // Act
        List<DicomSliceMetadata> result = adapter.fetchSlices(orderId);

        // Assert
        assertTrue(result.size() >= 3);
        assertTrue(result.size() <= 6);
        assertTrue(result.stream().allMatch(slice ->
                slice.studyInstanceUid().startsWith("2.25.")
                        && slice.seriesInstanceUid().startsWith(slice.studyInstanceUid())
                        && Arrays.asList("CT", "MR", "CR", "US").contains(slice.modality())));
        for (int i = 0; i < result.size(); i++) {
            DicomSliceMetadata slice = result.get(i);
            int number = i + 1;
            assertEquals(number, slice.sliceNumber());
            assertEquals(slice.seriesInstanceUid() + "." + number, slice.sopInstanceUid());
            assertEquals(result.get(0).studyInstanceUid(), slice.studyInstanceUid());
            assertEquals(result.get(0).seriesInstanceUid(), slice.seriesInstanceUid());
            assertEquals(result.get(0).modality(), slice.modality());
        }
    }

    @Test
    @DisplayName("The same order always gets the same slices")
    void slicesAreDeterministicAcrossCallsAndAdapters() {
        // Arrange
        MockPacsAdapter anotherAdapter = new MockPacsAdapter(42L);

        // Act
        List<DicomSliceMetadata> firstCall = adapter.fetchSlices("ORD-0001");
        List<DicomSliceMetadata> secondCall = adapter.fetchSlices("ORD-0001");
        List<DicomSliceMetadata> otherAdapter = anotherAdapter.fetchSlices("ORD-0001");

        // Assert
        assertEquals(firstCall, secondCall);
        assertEquals(firstCall, otherAdapter);
    }

    @Test
    @DisplayName("Different orders get different study UIDs")
    void differentOrdersGetDifferentStudyUids() {
        // Act
        String firstStudyUid = adapter.fetchSlices("ORD-0001").get(0).studyInstanceUid();
        String secondStudyUid = adapter.fetchSlices("ORD-0002").get(0).studyInstanceUid();

        // Assert
        assertNotEquals(firstStudyUid, secondStudyUid);
    }

    @Test
    @DisplayName("The returned slice list cannot be changed")
    void returnedListIsUnmodifiable() {
        // Arrange
        List<DicomSliceMetadata> result = adapter.fetchSlices("ORD-0001");

        // Act and Assert
        assertThrows(UnsupportedOperationException.class, () -> result.clear());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("A null or blank order id is rejected")
    void invalidOrderIdsAreRejected(String orderId) {
        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> adapter.fetchSlices(orderId),
                "The order id is empty");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4})
    @DisplayName("DICOM slice metadata rejects each null object field")
    void dicomSliceMetadataRejectsNullObjectFields(int nullField) {
        // Arrange
        String studyInstanceUid = nullField == 0 ? null : "2.25.123";
        String seriesInstanceUid = nullField == 1 ? null : "2.25.123.1";
        String sopInstanceUid = nullField == 2 ? null : "2.25.123.1.1";
        String modality = nullField == 3 ? null : "CT";
        LocalDateTime acquiredAt =
                nullField == 4 ? null : LocalDateTime.of(2026, 1, 1, 8, 0);

        // Act and Assert
        assertThrows(NullPointerException.class,
                () -> new DicomSliceMetadata(
                        studyInstanceUid,
                        seriesInstanceUid,
                        sopInstanceUid,
                        modality,
                        1,
                        acquiredAt));
    }

    @Test
    @DisplayName("A slice number starts at one")
    void sliceNumberCannotBeZero() {
        // Act and Assert
        assertThrows(IllegalArgumentException.class,
                () -> new DicomSliceMetadata(
                        "2.25.123",
                        "2.25.123.1",
                        "2.25.123.1.1",
                        "CT",
                        0,
                        LocalDateTime.of(2026, 1, 1, 8, 0)),
                "The slice number starts at 1");
    }
}
