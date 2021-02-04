package com.marks0mmers.gradetracker.models.persistent

import com.marks0mmers.gradetracker.models.constants.Role
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Document
data class User(
    private var username: String,
    private var password: String,
    var firstName: String,
    var lastName: String,
    var enabled: Boolean,
    var roles: List<Role>
) : UserDetails {
    @Id var id: String? = null
    override fun isEnabled() = enabled
    override fun getUsername() = username
    override fun isCredentialsNonExpired() = false
    override fun getPassword() = password
    override fun isAccountNonExpired() = false
    override fun isAccountNonLocked() = false
    override fun getAuthorities() = roles.map { SimpleGrantedAuthority(it.name) }.toMutableList()
}