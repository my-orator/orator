package com.premonition.orator.commands;

import com.premonition.orator.services.AudioService;
import com.premonition.orator.services.TextExtractorService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.URL;

import static com.amazonaws.services.polly.model.VoiceId.Amy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.shell.Availability.available;
import static org.springframework.shell.Availability.unavailable;

@RunWith(MockitoJUnitRunner.class)
public class SpeechCommandsTest {

  private static final String TEXT_TO_PLAY = "This is awesome";
  @Mock
  private AudioService audio;
  @Mock
  private TextExtractorService text;
  private SpeechCommands commands;

  @Before
  public void setUp() {
    commands = new SpeechCommands(audio, text);
  }

  @Test
  public void shouldPlayText() {
    commands.playText(TEXT_TO_PLAY, Amy);

    verify(audio).play(TEXT_TO_PLAY, Amy);
    verifyZeroInteractions(text);
  }

  @Test
  public void shouldPlayFromUrl() throws Exception {
    URL url = new URL("http://localhost");
    when(text.getText(url)).thenReturn(TEXT_TO_PLAY);
    commands.playUrl(url, Amy);

    verify(audio).play(TEXT_TO_PLAY, Amy);
  }

  @Test
  public void playShouldNotBeAvailableIfNotLoggedIn() {
    when(audio.loggedIn()).thenReturn(false);
    assertThat(commands.playAvailability()).isEqualToComparingFieldByField(unavailable("you are not logged in"));
  }

  @Test
  public void playShouldBeAvailableIfLoggedIn() {
    when(audio.loggedIn()).thenReturn(true);
    assertThat(commands.playAvailability()).isEqualToComparingFieldByField(available());
  }
}