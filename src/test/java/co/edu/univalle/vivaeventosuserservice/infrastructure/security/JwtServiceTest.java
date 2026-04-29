package co.edu.univalle.vivaeventosuserservice.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private final String TEST_SECRET = "aVerySecretKeyThatIsAtLeast256BitsLongAndShouldBeStoredSecurely";

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(TEST_SECRET);
    }

    @Test
    void generateToken_ShouldReturnValidToken() {
        // Arrange
        String email = "test@example.com";

        // Act
        String token = jwtService.generateToken(email);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());

        // Verify token claims
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(TEST_SECRET.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals(email, claims.getSubject());
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
        assertTrue(claims.getExpiration().after(claims.getIssuedAt()));
    }

    @Test
    void generateToken_ShouldExpireInOneHour() {
        // Arrange
        String email = "test@example.com";

        // Act
        String token = jwtService.generateToken(email);

        // Assert
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(TEST_SECRET.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();

        long expectedExpirationTime = claims.getIssuedAt().getTime() + (1000 * 60 * 60);
        assertEquals(expectedExpirationTime, claims.getExpiration().getTime(), 1000); // Allow for 1 second tolerance
    }
}
