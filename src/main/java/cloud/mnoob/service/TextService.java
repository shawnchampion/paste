package cloud.mnoob.service;

import cloud.mnoob.model.PasteException;
import cloud.mnoob.model.record.TextRecord;

public interface TextService {
    void doPaste(TextRecord record) throws PasteException;
    TextRecord doCopy(String code);
}
