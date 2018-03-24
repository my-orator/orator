package com.premonition.orator.commands;

import com.amazonaws.services.polly.model.VoiceId;
import com.premonition.orator.services.AudioService;
import com.premonition.orator.services.TextExtractorService;
import com.premonition.orator.services.VoiceIdValueProvider;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;

import java.net.URL;
import java.util.Arrays;

@ShellComponent
public class SpeechCommands {

  private static final String JOANNA = "Joanna";
  private final AudioService audio;
  private final TextExtractorService text;

  public SpeechCommands(AudioService audio, TextExtractorService text) {
    this.audio = audio;
    this.text = text;
  }

  @ShellMethod("plays a text sample")
  @ShellMethodAvailability("playAvailability")
  public void playText(String text,
                       @ShellOption(defaultValue = JOANNA, valueProvider = VoiceIdValueProvider.class)
                           VoiceId voiceId) {
    audio.play(text, voiceId);
  }

  @ShellMethod("plays text from a url")
  @ShellMethodAvailability("playAvailability")
  public void playUrl(URL url,
                      @ShellOption(defaultValue = JOANNA, valueProvider = VoiceIdValueProvider.class)
                          VoiceId voiceId) {
    String text = this.text.getText(url);
    Arrays.stream(text.split("\n")).forEach(l -> audio.play(l, voiceId));

  }

  @SuppressWarnings("unused")
  Availability playAvailability() {
    return audio.loggedIn() ? Availability.available() : Availability.unavailable("you are not logged in");
  }
}
