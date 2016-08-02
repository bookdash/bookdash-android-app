package org.bookdash.android.presentation.base;

public interface BaseView<T> {

    void setPresenter(T presenter);

    boolean isActive();

}