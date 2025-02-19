package com.lulski.aries.dto;

import com.lulski.aries.user.UserControllerResponseDto;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** DTO for UserController */
@SuppressFBWarnings(value = "EI_EXPOSE_REP2")
public class ServerResponse {
  private static final Logger logger = LoggerFactory.getLogger(ServerResponse.class);
  private UserControllerResponseDto item;
  private ServerErrorResponse error;

  public ServerResponse() {}

  public ServerResponse(UserControllerResponseDto userControllerResponseDto) {
    this.item = userControllerResponseDto;
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

  public UserControllerResponseDto getItem() {
    return this.item;
  }

  public void setItem(UserControllerResponseDto item) {
    this.item = item;
  }
}
