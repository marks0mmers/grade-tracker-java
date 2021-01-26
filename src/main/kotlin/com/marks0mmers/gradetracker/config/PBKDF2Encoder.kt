package com.marks0mmers.gradetracker.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import java.util.*
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

@Component
class PBKDF2Encoder : PasswordEncoder {
    @Value("\${springbootwebfluxjjwt.password.encoder.secret}")
    private val secret: String? = null

    @Value("\${springbootwebfluxjjwt.password.encoder.iteration}")
    private val iteration: Int? = null

    @Value("\${springbootwebfluxjjwt.password.encoder.keylength}")
    private val keylength: Int? = null

    @Throws(InvalidKeySpecException::class, NoSuchAlgorithmException::class)
    override fun encode(cs: CharSequence?): String {
        return try {
            val result = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512")
                    .generateSecret(PBEKeySpec(cs.toString().toCharArray(), secret?.toByteArray(), iteration ?: 0, keylength ?: 0))
                    .encoded
            Base64.getEncoder().encodeToString(result)
        }
        catch (ex: NoSuchAlgorithmException) { throw ex }
        catch (ex: InvalidKeySpecException) { throw ex }
    }

    override fun matches(cs: CharSequence?, pw: String?) = encode(cs) == pw

}

