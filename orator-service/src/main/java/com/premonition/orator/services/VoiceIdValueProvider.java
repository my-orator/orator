package com.premonition.orator.services;

import com.amazonaws.services.polly.model.VoiceId;
import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProvider;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class VoiceIdValueProvider implements ValueProvider {
  private final AudioService audioService;

  public VoiceIdValueProvider(AudioService audioService) {
    this.audioService = audioService;
  }

  @Override
  public boolean supports(MethodParameter parameter, CompletionContext context) {
    return parameter.getParameterType().isAssignableFrom(VoiceId.class);
  }

  @Override
  public List<CompletionProposal> complete(MethodParameter parameter,
                                           CompletionContext context,
                                           String[] hints) {
    return audioService.findAllById(context.currentWordUpToCursor())
        .stream()
        .map(VoiceId::toString)
        .map(CompletionProposal::new)
        .collect(toList());
  }
}
