package com.premonition.orator.commands;

import com.premonition.orator.services.AudioService;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import javax.validation.constraints.NotBlank;

import static org.springframework.shell.Availability.available;
import static org.springframework.shell.Availability.unavailable;

@ShellComponent
public class AuthenticationCommands {

  private final AudioService audio;

  public AuthenticationCommands(AudioService audio) {
    this.audio = audio;
  }

  @ShellMethod("Allows you to login")
  public String login(@NotBlank String accessKey,
                      @NotBlank String secret) {
    this.audio.login(accessKey, secret);
    return "You are logged in. You can start playing text";
  }

  @ShellMethod("Allows you to logout")
  public String logout() {
    this.audio.logout();
    return "Logged out successfully!";
  }

  @SuppressWarnings("unused")
  Availability logoutAvailability() {
    return !audio.loggedIn() ? unavailable("you are not logged in") : available();
  }

  @SuppressWarnings("unused")
  Availability loginAvailability() {
    return audio.loggedIn() ? unavailable("you are already logged in") : available();
  }

}
