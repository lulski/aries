package com.lulski.aries.user;

import java.util.List;

public record UserControllerResponseDto(List<UserDto> items, String message) {}
