package Server.Model;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.time.Instant;
import java.util.Date;

public class Authorization {

    public String Authorize(String username, HttpServletResponse httpServletResponse) throws ResponseStatusException {
        String appSecret = "222222";

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        var accessTokenCookie = WebUtils.getCookie(request, "AccessToken");
        if (accessTokenCookie == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "accessTokenCookie is null");
        } else {
            var accessToken = accessTokenCookie.getValue();
            Claims claims;
            try {
                claims = Jwts.parser()
                        .setSigningKey(DatatypeConverter.parseBase64Binary(appSecret))
                        .parseClaimsJws(accessToken).getBody();
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "thrown in Authorization, accessToken is invalid");
            }
            String username1 = (String) claims.get("username");
            if (!username1.equals(username)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "username does not match");
            }
            if (claims.getExpiration().before(Date.from(Instant.now()))) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "accessToken is expired");
            }
        }
        return accessTokenCookie.getValue();
    }
}
