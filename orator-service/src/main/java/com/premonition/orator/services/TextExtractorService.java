package com.premonition.orator.services;

import com.kohlschutter.boilerpipe.BoilerpipeProcessingException;
import com.kohlschutter.boilerpipe.extractors.ArticleExtractor;

import java.net.URL;

public class TextExtractorService {
  private final ArticleExtractor extractor;


  public TextExtractorService(ArticleExtractor extractor) {
    this.extractor = extractor;
  }

  public String getText(URL url) {
    try {
      return extractor.getText(url);
    } catch (BoilerpipeProcessingException e) {
      throw new IllegalArgumentException("Failed to parse text from url: " + url, e);
    }
  }
}
