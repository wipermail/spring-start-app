package io.ipmen.starter.application.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.ipmen.starter.application.common.TimeProvider;
import io.ipmen.starter.application.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class TokenHelper {

  @Value("${app.name}")
  private String APP_NAME;

  @Value("${jwt.secret}")
  public String SECRET;

  @Value("${jwt.expires_in}")
  private int EXPIRES_IN;

  @Value("${jwt.mobile_expires_in}")
  private int MOBILE_EXPIRES_IN;

  @Value("${jwt.header}")
  private String AUTH_HEADER;

  private static final String AUDIENCE_UNKNOWN = "unknown";
  private static final String AUDIENCE_WEB = "web";
  private static final String AUDIENCE_MOBILE = "mobile";
  private static final String AUDIENCE_TABLET = "tablet";

  private final TimeProvider timeProvider;

  private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

  @Autowired
  public TokenHelper(TimeProvider timeProvider) {
    this.timeProvider = timeProvider;
  }

  public String getUsernameFromToken(String token) {
    String username;
    try {
      final Claims claims = this.getAllClaimsFromToken(token);
      username = claims.getSubject();
    } catch (Exception e) {
      username = null;
    }
    return username;
  }

  public Date getIssuedAtDateFromToken(String token) {
    Date issueAt;
    try {
      final Claims claims = this.getAllClaimsFromToken(token);
      issueAt = claims.getIssuedAt();
    } catch (Exception e) {
      issueAt = null;
    }
    return issueAt;
  }

  public String getAudienceFromToken(String token) {
    String audience;
    try {
      final Claims claims = this.getAllClaimsFromToken(token);
      audience = claims.getAudience();
    } catch (Exception e) {
      audience = null;
    }
    return audience;
  }

  public String refreshToken(String token) {
    String refreshedToken;
    Date a = timeProvider.now();
    try {
      final Claims claims = this.getAllClaimsFromToken(token);
      claims.setIssuedAt(a);
      refreshedToken = Jwts.builder()
          .setClaims(claims)
          .setExpiration(this.generateExpirationDate())
          .signWith(SIGNATURE_ALGORITHM, SECRET)
          .compact();
    } catch (Exception e) {
      refreshedToken = null;
    }
    return refreshedToken;
  }

  public String generateToken(String username) {
    String audience = this.generateAudience();
    return Jwts.builder()
        .setIssuer(APP_NAME)
        .setSubject(username)
        .setAudience(audience)
        .setIssuedAt(timeProvider.now())
        .setExpiration(generateExpirationDate())
        .signWith(SIGNATURE_ALGORITHM, SECRET)
        .compact();
  }

  private String generateAudience() {
    return AUDIENCE_UNKNOWN;
  }

  private Claims getAllClaimsFromToken(String token) {
    Claims claims;
    try {
      claims = Jwts.parser()
          .setSigningKey(SECRET)
          .parseClaimsJws(token)
          .getBody();
    } catch (Exception e) {
      claims = null;
    }
    return claims;
  }

  private Date generateExpirationDate() {
    long expiresIn = EXPIRES_IN;
    return new Date(timeProvider.now().getTime() + expiresIn * 1000);
  }

  public int getExpiredIn() {
    return EXPIRES_IN;
  }

  public Boolean validateToken(String token, UserDetails userDetails) {
    User user = (User) userDetails;
    final String username = this.getUsernameFromToken(token);
    final Date created = this.getIssuedAtDateFromToken(token);
    return (
        username != null &&
            username.equals(user.getUsername()) &&
            !this.isCreatedBeforeLastPasswordReset(created, user.getLastPasswordResetDate())
    );
  }

  private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
    return (lastPasswordReset != null && created.before(lastPasswordReset));
  }

  public String getToken(HttpServletRequest request) {
    /*
       Getting the token from Authentication header
       e.g Bearer your_token
     */
    String authHeader = this.getAuthHeaderFromHeader(request);
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      return authHeader.substring(7);
    }

    return null;
  }

  public String getAuthHeaderFromHeader(HttpServletRequest request) {
    return request.getHeader(AUTH_HEADER);
  }

}