package com.premonition.orator;

import com.amazonaws.services.polly.AmazonPollyClientBuilder;
import com.kohlschutter.boilerpipe.extractors.ArticleExtractor;
import com.premonition.orator.services.AudioService;
import com.premonition.orator.services.ConnectedPromptProvider;
import com.premonition.orator.services.TextExtractorService;
import com.premonition.orator.services.VoiceIdConverter;
import com.premonition.orator.services.VoiceIdValueProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.shell.jline.PromptProvider;

import static com.amazonaws.regions.Regions.US_EAST_1;

@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public AudioService audioService() {
    return new AudioService(clientBuilder());
  }

  @Bean
  public PromptProvider promptProvider() {
    return new ConnectedPromptProvider(audioService());
  }

  @Bean
  public VoiceIdValueProvider voiceIdValueProvider() {
    return new VoiceIdValueProvider(audioService());
  }

  @Bean
  public VoiceIdConverter voiceIdConverter() {
    return new VoiceIdConverter(audioService());
  }

  @Bean
  public TextExtractorService textExtractorService() {
    return new TextExtractorService(ArticleExtractor.INSTANCE);
  }

  @Bean
  public AmazonPollyClientBuilder clientBuilder() {
    return AmazonPollyClientBuilder.standard().withRegion(US_EAST_1);
  }
}

