package com.lulski.aries.security;

import com.lulski.aries.user.User;
import com.lulski.aries.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AriesUserDetailsServiceTest {

    @Mock
    UserRepository userRepository;

    @Test
    void findByUsername_returnsUserDetails_whenUserExists() {
        User mockUser = mock(User.class);
        when(mockUser.getUsername()).thenReturn("alice");
        when(mockUser.getPassword()).thenReturn("secret");
        when(mockUser.getEmail()).thenReturn("alice@example.com");
        when(mockUser.getFirstName()).thenReturn("Alice");
        when(mockUser.getLastName()).thenReturn("Wonder");
        when(mockUser.getAuthoritiesNames()).thenReturn(Set.of("ROLE_USER", "ROLE_ADMIN"));

        when(userRepository.findTopByUsername("alice")).thenReturn(Mono.just(mockUser));

        AriesUserDetailsService svc = new AriesUserDetailsService(userRepository);

        StepVerifier.create(svc.findByUsername("alice"))
                .assertNext((UserDetails ud) -> {
                    assertEquals("alice", ud.getUsername());
                    assertEquals("secret", ud.getPassword());
                    assertTrue(ud.getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
                })
                .verifyComplete();
    }

    @Test
    void findByUsername_returnsEmpty_whenUserNotFound() {
        when(userRepository.findTopByUsername("bob")).thenReturn(Mono.empty());

        AriesUserDetailsService svc = new AriesUserDetailsService(userRepository);

        StepVerifier.create(svc.findByUsername("bob"))
                .verifyComplete();
    }
}