package org.coffe.tomoko.util.debounce;

import org.coffe.tomoko.error.ErrorInfo;

public interface DebounceSavable {

    /**
     * Execute operations to save the data. <br>
     * Must set {@link DebounceSaver#setIsModified(boolean)} false in this method manually
     * after the data has been saved.
     */
    void saveImmediate();

    void showError(ErrorInfo errorInfo);
}
