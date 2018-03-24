package com.premonition.orator.services;

import org.jline.utils.AttributedString;
import org.springframework.shell.jline.PromptProvider;

public class ConnectedPromptProvider implements PromptProvider {
  private static final String RED = "\u001b[31m";
  private static final String GREEN = "\u001B[32m";
  private static final String DEFAULT = "\u001B[0m";
  private final AudioService audio;

  public ConnectedPromptProvider(AudioService audio) {
    this.audio = audio;
  }

  @Override
  public AttributedString getPrompt() {
    String connected = audio.loggedIn() ? GREEN + "connected" : RED + "disconnected";
    String prompt = "orator (" + connected + DEFAULT + ")" + ":> ";
    return new AttributedString(prompt);
  }
}
