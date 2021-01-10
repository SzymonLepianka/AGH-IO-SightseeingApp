package Server.Model;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.sql.SQLException;

public class Authorization {

    public static String Authorize(HttpServletResponse httpServletResponse) throws ResponseStatusException, SQLException {
        String appSecret = "222222";

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        var accessTokenCookie = WebUtils.getCookie(request, "AccessToken");
        if(accessTokenCookie == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "accessTokenCookie is null");
        }
        else {
            var accessToken = accessTokenCookie.getValue();
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(DatatypeConverter.parseBase64Binary(appSecret))
                        .parseClaimsJws(accessToken).getBody();
            }
            catch(Exception e) {
                 throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "thrown in Authorization, claims error");
            }
        }
        return accessTokenCookie.getValue();
    }
}
