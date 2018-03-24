package com.premonition.orator.services;

import org.jline.utils.AttributedString;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ConnectedPromptProviderTest {

  @Mock
  private AudioService service;
  private ConnectedPromptProvider provider;

  @Before
  public void setUp() {
    provider = new ConnectedPromptProvider(service);
  }

  @Test
  public void shouldSetPromptWhenLoggedIn() {
    Mockito.when(service.loggedIn()).thenReturn(true);

    AttributedString prompt = provider.getPrompt();

    assertThat(prompt.toAnsi()).contains("connected")
        .doesNotContain("disconnected");
  }

  @Test
  public void shouldSetPromptWhenLoggedOut() {
    Mockito.when(service.loggedIn()).thenReturn(false);

    AttributedString prompt = provider.getPrompt();

    assertThat(prompt.toAnsi()).contains("disconnected");
  }
}