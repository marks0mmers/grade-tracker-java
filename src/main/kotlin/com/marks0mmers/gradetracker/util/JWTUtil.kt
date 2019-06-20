package com.marks0mmers.gradetracker.util

import com.marks0mmers.gradetracker.persistent.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.Serializable
import java.util.*
import kotlin.collections.HashMap

@Component
class JWTUtil : Serializable {
    @Value("\${springbootwebfluxjjwt.jjwt.secret}")
    private val secret: String? = null

    @Value("\${springbootwebfluxjjwt.jjwt.expiration}")
    private val expirationTime: String? = null

    fun getAllClaimsFromToken(token: String): Claims = Jwts
            .parser()
            .setSigningKey(Base64.getEncoder().encodeToString(secret?.toByteArray()))
            .parseClaimsJws(token)
            .body

    fun getUsernameFromToken(token: String): String = getAllClaimsFromToken(token).subject

    fun getExpirationDateFromToken(token: String): Date = getAllClaimsFromToken(token).expiration

    fun isTokenExpired(token: String) = getExpirationDateFromToken(token).before(Date())

    fun generateToken(user: User): String {
        val claims = HashMap<String, Any>()
        claims["role"] = user.roles
        return doGenerateToken(claims, user.username)
    }

    @Suppress("DEPRECATION")
    private fun doGenerateToken(claims: Map<String, Any>, username: String): String {
        val expirationTimeLong = expirationTime?.toLongOrNull() ?: 0
        val createdDate = Date()
        val expirationDate = Date(createdDate.time + expirationTimeLong * 1000)
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encodeToString(secret?.toByteArray()))
                .compact()
    }

    fun validateToken(token: String) = !isTokenExpired(token)
}
