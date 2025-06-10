package pl.edu.streamfinder.exceptions;


public record ErrorResponseDTO(String error, String message, int errorCode) {
}
