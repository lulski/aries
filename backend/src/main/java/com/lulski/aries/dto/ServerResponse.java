package com.lulski.aries.dto;

import com.lulski.aries.user.UserResponseDto;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** DTO for UserController */
@SuppressFBWarnings(value = "EI_EXPOSE_REP2")
public class ServerResponse {
  private static final Logger logger = LoggerFactory.getLogger(ServerResponse.class);
  private UserResponseDto item;
  private ServerErrorResponse error;

  public ServerResponse() {}

  public ServerResponse(UserResponseDto userResponseDto) {
    this.item = userResponseDto;
  }

  public ServerResponse(ServerErrorResponse errorResponse) {
    this.error = errorResponse;
  }

  /**
   * SuppressFBWarnings because there is a serialization process that went haywire if errorCopy is
   * used
   *
   * @return
   */
  @SuppressFBWarnings
  public ServerErrorResponse getError() {
    // var errorCopy = new ServerErrorResponse(null, null, 0);
    // if (this.error != null) {
    // errorCopy.setDetails(this.error.getDetails());
    // errorCopy.setErrorCode(this.error.getErrorCode());
    // errorCopy.setMessage(this.error.getMessage());
    // logger.info("errorCopy: " + errorCopy.getErrorCode());
    // }
    // return errorCopy;
    logger.info("getError is called ");
    return error;
  }

  public void setError(ServerErrorResponse error) {
    this.error = error;
  }

  public UserResponseDto getItem() {
    return this.item;
  }

  public void setItem(UserResponseDto item) {
    this.item = item;
  }
}
