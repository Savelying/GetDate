package ru.savelying.getdate.service.bundle;

import lombok.Getter;

import java.util.Locale;

public class WordBundleRu extends WordBundle {
    @Getter
    private final static WordBundleRu instance = new WordBundleRu();

    private WordBundleRu() {
        super(Locale.of("ru"));
    }
}
