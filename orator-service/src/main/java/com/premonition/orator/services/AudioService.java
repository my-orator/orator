package com.premonition.orator.services;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.polly.AmazonPolly;
import com.amazonaws.services.polly.AmazonPollyClientBuilder;
import com.amazonaws.services.polly.model.DescribeVoicesRequest;
import com.amazonaws.services.polly.model.DescribeVoicesResult;
import com.amazonaws.services.polly.model.OutputFormat;
import com.amazonaws.services.polly.model.SynthesizeSpeechRequest;
import com.amazonaws.services.polly.model.SynthesizeSpeechResult;
import com.amazonaws.services.polly.model.Voice;
import com.amazonaws.services.polly.model.VoiceId;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.amazonaws.services.polly.model.LanguageCode.EnUS;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

// TODO: Amazon's builders are final. Cannot unit test without more encapsulation
public class AudioService {
  private boolean loggedIn;
  private AmazonPolly client;
  private List<VoiceId> voices = new ArrayList<>();
  private final AmazonPollyClientBuilder builder;

  public AudioService(AmazonPollyClientBuilder builder) {
    this.builder = builder;
  }

  public void login(String accessKey, String secret) {
    this.client = builder
        .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secret)))
        .build();

    DescribeVoicesRequest request = new DescribeVoicesRequest();
    request.setLanguageCode(EnUS);

    DescribeVoicesResult result = client.describeVoices(request);
    voices = result.getVoices()
        .stream()
        .filter(v -> Arrays.stream(VoiceId.values())
            .map(VoiceId::toString)
            .collect(toSet()).contains(v.getId()))
        .map(Voice::getId)
        .map(VoiceId::fromValue)
        .collect(toList());
    loggedIn = true;
  }

  public boolean loggedIn() {
    return loggedIn;
  }

  public void logout() {
    client.shutdown();
    client = null;
    loggedIn = false;
  }

  public void play(String text, VoiceId voiceId) {
    SynthesizeSpeechRequest request = new SynthesizeSpeechRequest()
        .withOutputFormat(OutputFormat.Mp3)
        .withText(text)
        .withVoiceId(voiceId);
    SynthesizeSpeechResult result = client.synthesizeSpeech(request);
    InputStream stream = result.getAudioStream();

    play(stream);
  }

  private void play(InputStream stream) {
    try {
      AdvancedPlayer player = new AdvancedPlayer(stream);
      player.play();
    } catch (JavaLayerException e) {
      throw new IllegalStateException("Failed to play stream!", e);
    }
  }

  public VoiceId findById(String voiceId) {
    return voices.stream()
        .filter(v -> v.toString().equalsIgnoreCase(voiceId))
        .findFirst()
        .orElse(voices.isEmpty() ? null : voices.get(0));
  }

  public Collection<VoiceId> findAllById(String voiceId) {
    return voices.stream()
        .filter(v -> v.toString().toLowerCase().contains(voiceId.toLowerCase()))
        .collect(toList());
  }
}
