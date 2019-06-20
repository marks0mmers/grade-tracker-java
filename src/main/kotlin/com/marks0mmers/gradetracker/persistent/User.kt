package com.marks0mmers.gradetracker.persistent

import com.marks0mmers.gradetracker.constants.Role
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Document
data class User(
        @Id
        var id: String? = null,
        private var username: String,
        private var password: String,
        var firstName: String,
        var lastName: String,
        var enabled: Boolean,
        var roles: List<Role>
) : UserDetails {
    override fun isEnabled() = enabled
    override fun getUsername() = username
    override fun isCredentialsNonExpired() = false
    override fun getPassword() = password
    override fun isAccountNonExpired() = false
    override fun isAccountNonLocked() = false
    override fun getAuthorities() = roles.map { SimpleGrantedAuthority(it.name)}.toMutableList()
}