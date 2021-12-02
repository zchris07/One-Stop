package Functionality;

import com.google.api.gax.longrunning.OperationFuture;
import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class DetectTextSpeechGcs {

    /**
     * Performs non-blocking speech recognition on raw PCM audio and prints the transcription. Note
     * that transcription is limited to 60 seconds audio.
     *
     * @param fileName the path to a PCM audio file to transcribe.
     */
    public static String syncRecognizeFile(String fileName) throws Exception {
        try (SpeechClient speech = SpeechClient.create()) {
            Path path = Paths.get(fileName);
            byte[] data = Files.readAllBytes(path);
            ByteString audioBytes = ByteString.copyFrom(data);

            // Configure request with local raw PCM audio
            RecognitionConfig config =
                    RecognitionConfig.newBuilder()
                            .setEncoding(RecognitionConfig.AudioEncoding.FLAC)
                            .setLanguageCode("en-US")
                            .setSampleRateHertz(48000)
                            .build();
            RecognitionAudio audio = RecognitionAudio.newBuilder().setContent(audioBytes).build();

            // Use blocking call to get audio transcript
            RecognizeResponse response = speech.recognize(config, audio);
            List<SpeechRecognitionResult> results = response.getResultsList();
            // System.out.printf(results.toString());
            for (SpeechRecognitionResult result : results) {
                // There can be several alternative transcripts for a given chunk of speech. Just use the
                // first (most likely) one here.
                SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
            //    System.out.printf("Transcription: %s%n", alternative.getTranscript());
            }
            return results.get(0).getAlternativesList().get(0).getTranscript();
        }

    }


}
