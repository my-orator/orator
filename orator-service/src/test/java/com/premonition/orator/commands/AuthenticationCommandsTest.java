package com.premonition.orator.commands;

import com.premonition.orator.services.AudioService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.shell.Availability.available;
import static org.springframework.shell.Availability.unavailable;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationCommandsTest {

  private static final String ACCESS_KEY = "accessKey";
  private static final String SECRET = "secret";
  @Mock
  private AudioService service;
  private AuthenticationCommands commands;

  @Before
  public void setUp() {
    commands = new AuthenticationCommands(service);
  }

  @Test
  public void shouldLoginWithAnAccessKeyAndSecret() {
    commands.login(ACCESS_KEY, SECRET);

    verify(service).login(ACCESS_KEY, SECRET);
  }

  @Test
  public void shouldLogout() {
    commands.logout();
    verify(service).logout();
  }

  @Test
  public void loginShouldNotBeAvailableIfLoggedIn() {
    when(service.loggedIn()).thenReturn(true);
    assertThat(commands.loginAvailability()).isEqualToComparingFieldByField(unavailable("you are already logged in"));
  }

  @Test
  public void logoutShouldNotBeAvailableIfNotLoggedIn() {
    when(service.loggedIn()).thenReturn(false);
    assertThat(commands.logoutAvailability()).isEqualToComparingFieldByField(unavailable("you are not logged in"));
  }

  @Test
  public void logoutShouldBeAvailableIfLoggedIn() {
    when(service.loggedIn()).thenReturn(true);
    assertThat(commands.logoutAvailability()).isEqualToComparingFieldByField(available());
  }

  @Test
  public void loginShouldNotBeAvailableIfLoggedOut() {
    when(service.loggedIn()).thenReturn(false);
    assertThat(commands.loginAvailability()).isEqualToComparingFieldByField(available());
  }
}