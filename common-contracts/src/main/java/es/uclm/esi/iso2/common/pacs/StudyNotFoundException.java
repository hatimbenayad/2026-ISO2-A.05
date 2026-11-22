package es.uclm.esi.iso2.common.pacs;

import java.util.NoSuchElementException;

public class StudyNotFoundException extends NoSuchElementException {
    public StudyNotFoundException(String message) {
        super(message);
    }
}
