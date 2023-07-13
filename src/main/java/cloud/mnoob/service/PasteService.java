package cloud.mnoob.service;

import cloud.mnoob.model.PasteException;
import cloud.mnoob.model.PasteRecord;

public interface PasteService {
    void doPaste(PasteRecord record) throws PasteException;
    PasteRecord doCopy(String code);
}
