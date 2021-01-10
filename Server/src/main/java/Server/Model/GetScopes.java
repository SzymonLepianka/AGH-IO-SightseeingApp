package Server.Model;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.xml.bind.DatatypeConverter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetScopes {
    public static List<String> getScopes(String accessToken) {
        String appSecret = "222222";
        //TODO move appsecret to application properties
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(appSecret))
                .parseClaimsJws(accessToken).getBody();

        String scopesRaw = (String) claims.get("scopes");

        List<String> scopesSeparated = new ArrayList<>();
        String[] scopesArray;
        if (scopesRaw.contains(",")) {
            scopesArray = scopesRaw.split(",");
            scopesSeparated.addAll(Arrays.asList(scopesArray));
        } else {
            scopesSeparated.add(scopesRaw);
        }
        System.out.println(scopesSeparated);
        return scopesSeparated;
    }
}
