package br.com.dbc.vemser.cinedev.security;

import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

public class GeradorDeSenhas {

    public static void main(String[] args) {

        int cpuCost = (int) Math.pow(2, 14); // factor to increase CPU costs
        int memoryCost = 8;      // increases memory usage
        int parallelization = 1; // currently not supported by Spring Security
        int keyLength = 32;      // key length in bytes
        int saltLength = 64;     // salt length in bytes

        SCryptPasswordEncoder sCryptPasswordEncoder = new SCryptPasswordEncoder(
                cpuCost,
                memoryCost,
                parallelization,
                keyLength,
                saltLength);
//        String encodedPassword = sCryptPasswordEncoder.encode("12345");
//        System.out.println(encodedPassword);
        System.out.println(sCryptPasswordEncoder.matches("12345",
                "$e0801$RoAvgn7wg3Suji1j3ILRBbqaQTTiHt2QGTnKaD4KwZMss47PoanaxDZGnVFx4MGknYRJLWENdKzinIPq3Kmttg==$+UIFWOUr7+SdOZIkG/QD6m1r2/bJi4/5K8Wg84lZc/Y="));
    }
}
