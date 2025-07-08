package ru.savelying.getdate.service.bundle;

import lombok.Getter;

import java.util.Locale;

public class WordBundleEn extends WordBundle {
    @Getter
    private final static WordBundleEn instance = new WordBundleEn();

    private WordBundleEn() {
        super(Locale.ENGLISH);
    }
}
