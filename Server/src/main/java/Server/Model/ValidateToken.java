package Server.Model;

import io.jsonwebtoken.Claims;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class ValidateToken {
    public static boolean validateToken(String accessToken) throws SQLException {

        //TODO mobe it to application.properties
        Long clientID = 2L;
        Long appSecret = 222222L;

        //dekoduję z otrzymanego tokenu issuedAt, expiration, scopes i subject(userID)
        TokenDecoder tokenDecoder = new TokenDecoder();
        Claims claims = tokenDecoder.decodeToken(accessToken, appSecret.toString());
        String scopes = (String) claims.get("scopes");
        Long userID = Long.parseLong(claims.getSubject());
        // ustawiam format Timestamp issuedAt i expiration
        String date = String.valueOf(claims.getIssuedAt().toInstant()).substring(0, 10);
        String time = String.valueOf(claims.getIssuedAt().toInstant()).substring(11, 19);
        Timestamp issuedAt = Timestamp.valueOf(Timestamp.valueOf(date + " " + time).toLocalDateTime().plusHours(1));
        date = String.valueOf(claims.getExpiration().toInstant()).substring(0, 10);
        time = String.valueOf(claims.getExpiration().toInstant()).substring(11, 19);
        Timestamp expiration = Timestamp.valueOf(Timestamp.valueOf(date + " " + time).toLocalDateTime().plusHours(1));

        // sprawdzam czy expiration nie minął
        if (!expiration.after(Timestamp.valueOf(LocalDateTime.now()))) {
            return false;
        }
        // pobieram z bazy danych accessTokens i szukam przekazanego w params 'accessToken'
//        List<AccessToken> accessTokens = db.getAccessTokensAccessObject().readAll();
//        AccessToken accessTokenFound = accessTokens.stream()
//                .filter(at -> (userID.equals(at.getUser().getId()) && issuedAt.equals(at.getCreatedAt()) || Timestamp.valueOf(issuedAt.toLocalDateTime().plusSeconds(1)).equals(at.getCreatedAt())) && (expiration.equals(at.getExpiresAt()) || Timestamp.valueOf(expiration.toLocalDateTime().plusSeconds(1)).equals(at.getExpiresAt())) && clientID.equals(at.getClientApp().getId()) && scopes.equals(at.getScopes()))
//                .findFirst()
//                .orElse(null);
//
//        if (accessTokenFound != null){
//            {
//                return true;
//            }
//        }
        else {
            return false;
        }
    }
}
