package br.com.dbc.vemser.cinedev.security;

import br.com.dbc.vemser.cinedev.entity.CargoEntity;
import br.com.dbc.vemser.cinedev.entity.UsuarioEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenService {

    private static final String KEY_CARGOS = "CARGOS";
    @Value("${jwt.expiration}")
    private String expiration;

    @Value("${jwt.expiration}")
    private String expirationChangePassword;

    @Value("${jwt.secret}")
    private String secret;

    public String getToken(UsuarioEntity usuarioEntity) {
        // FIXME por meio do usuário, gerar um token


        LocalDateTime dataAtual = LocalDateTime.now();
        Date now = Date.from(dataAtual.atZone(ZoneId.systemDefault()).toInstant());

        LocalDateTime dataExpiracao = dataAtual.plusDays(Long.parseLong(expiration));
        Date exp = Date.from(dataExpiracao.atZone(ZoneId.systemDefault()).toInstant());

//        Date now = new Date();
//        Date exp = new Date(now.getTime() + 864000000);

        List<String> cargosDoUsuario = usuarioEntity.getCargos().stream()
                .map(CargoEntity::getAuthority)
                .toList();

        String meuToken = Jwts.builder()
                .setIssuer("MATHEUS_GONCALVES")
                .claim(Claims.ID, usuarioEntity.getIdUsuario().toString())
                .claim(KEY_CARGOS, cargosDoUsuario)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        return meuToken;

//        String tokenTexto = usuarioEntity.getLogin() + ";" + usuarioEntity.getSenha();
//        String token = Base64.getEncoder().encodeToString(tokenTexto.getBytes());
//        return token;
    }

    public String getTokenTrocarSenha(UsuarioEntity usuarioEntity) {
        LocalDateTime dataAtual = LocalDateTime.now();
        Date now = Date.from(dataAtual.atZone(ZoneId.systemDefault()).toInstant());

        LocalDateTime dataExpiracao = dataAtual.plusMinutes(Long.parseLong(expirationChangePassword));
        Date exp = Date.from(dataExpiracao.atZone(ZoneId.systemDefault()).toInstant());

//        Date now = new Date();
//        Date exp = new Date(now.getTime() + 864000000);

        List<String> cargosDoUsuario = usuarioEntity.getCargos().stream()
                .map(CargoEntity::getAuthority)
                .toList();

        String meuToken = Jwts.builder()
                .setIssuer("MATHEUS_GONCALVES")
                .claim(Claims.ID, usuarioEntity.getIdUsuario().toString())
                .claim(KEY_CARGOS, cargosDoUsuario)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        return meuToken;
    }

    public UsernamePasswordAuthenticationToken isValid(String token) {
        if (token != null) {
            token = token.replace("Bearer ", "");

            Claims chaves = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token) // validar e retornar as chaves;
                    .getBody();

            //usuário e senha válidos...
//        String idUsuario = chaves.get("jti", String.class);
            String idUsuario = chaves.get(Claims.ID, String.class);
            List<String> cargos = chaves.get(KEY_CARGOS, List.class);

            List<SimpleGrantedAuthority> listaDeCargos = cargos.stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList();

            UsernamePasswordAuthenticationToken dtoDoSpring =
                    new UsernamePasswordAuthenticationToken(idUsuario, null, listaDeCargos);

            return dtoDoSpring;
        }
        return null;
    }
}
