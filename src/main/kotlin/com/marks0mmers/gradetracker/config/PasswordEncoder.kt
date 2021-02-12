package com.marks0mmers.gradetracker.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

@Component
@ConfigurationProperties(prefix = "json-web-token.password.encoder")
class PasswordEncoder : PasswordEncoder {
    var secret: String = ""
    var iteration: Int = 0
    var keylength: Int = 0

    override fun encode(cs: CharSequence?): String {
        return try {
            val result = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512")
                .generateSecret(
                    PBEKeySpec(
                        cs.toString().toCharArray(),
                        secret.toByteArray(),
                        iteration,
                        keylength
                    )
                )
                .encoded
            Base64.getEncoder().encodeToString(result)
        } catch (e: Exception) {
            throw e
        }
    }

    override fun matches(cs: CharSequence?, pw: String?): Boolean {
        return encode(cs) == pw
    }

}

