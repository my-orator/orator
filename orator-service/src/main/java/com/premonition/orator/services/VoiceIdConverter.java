package com.premonition.orator.services;

import com.amazonaws.services.polly.model.VoiceId;
import org.springframework.core.convert.converter.Converter;

public class VoiceIdConverter implements Converter<String, VoiceId> {

  private final AudioService audio;

  public VoiceIdConverter(AudioService audio) {
    this.audio = audio;
  }

  @Override
  public VoiceId convert(String voiceId) {
    return audio.findById(voiceId);
  }
}
