package com.login.app.controller;

import java.security.Key;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.login.app.DTO.TokenDTO;
import com.login.app.service.JWTService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
public class HelloController {

    @Value("${token.key}")
    private String secretKey;

    //autoriza o de acesso ao endpoint para qualquer usuario com role admin ou user
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/hello")
    public String hello(){
        return "Acesso para hasAnyRole('ADMIN', 'USER')";
    }

    //autoriza o de acesso ao endpoint para qualquer usuario com role admin ou user
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/hello-2")
    public String helloUsr(){
        return "Acesso para hasAnyRole('USER')";
    }

    //tem acesso apenas para o usuario com role admin
    @GetMapping("validacao")
    public String validar(@RequestBody TokenDTO request){
        
        String token = request.body.trim();
        System.out.println(token);

        Claims claim  = Jwts.parser()
            .setSigningKey(secretKey)  // ou sua chave HMAC/Key object
            .build()
            .parseSignedClaims(token)
            .getBody();

        System.out.println(claim.toString());    

        //gerar key
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        System.out.println(Encoders.BASE64.encode(key.getEncoded()));
        return token;
    }

    @GetMapping("/test")
    //testar a role no token
    public String test(Authentication auth) {
        return auth.getAuthorities().toString();
}

}
