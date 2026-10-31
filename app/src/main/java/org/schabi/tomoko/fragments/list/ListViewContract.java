package org.coffe.tomoko.fragments.list;

import org.coffe.tomoko.fragments.ViewContract;

public interface ListViewContract<I, N> extends ViewContract<I> {
    void showListFooter(boolean show);

    void handleNextItems(N result);
}
