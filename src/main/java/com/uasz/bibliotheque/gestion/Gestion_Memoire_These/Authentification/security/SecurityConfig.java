package com.uasz.bibliotheque.gestion.Gestion_Memoire_These.Authentification.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final String[] FOR_RESPONSABLE = {"/Admin/**"}; // Routes accessibles aux Admin
    private static final String[] FOR_STAGER = {"/User/**"}; // Routes accessibles aux User

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Encodage des mots de passe avec BCrypt
    }
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Désactiver CSRF si vous testez en local, à activer en production
                .authorizeHttpRequests(authorize -> authorize
                        // Autoriser l'accès aux ressources statiques
                        .requestMatchers("/js/**", "/css/**", "/img/**").permitAll()

                        // Autoriser les pages publiques
                        .requestMatchers("/login", "/logout","/resultatsRecherche", "/register", "/error","/logs","/sessions","/Responsable").permitAll()

                        // Autoriser l'accès à H2 console (pour le développement uniquement)
                        .requestMatchers("/h2/**").permitAll()

                        // Restrictions basées sur les rôles
                        .requestMatchers(FOR_RESPONSABLE).hasRole("Admin")
                        .requestMatchers(FOR_STAGER).hasRole("User")

                        // Toute autre requête nécessite une authentification
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login") // Page de connexion personnalisée
                        .usernameParameter("username") // Nom du champ pour le nom d'utilisateur
                        .passwordParameter("password") // Nom du champ pour le mot de passe
                        .defaultSuccessUrl("/") // Redirection après connexion réussie
                        .permitAll() // Permettre l'accès à la page de connexion
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL pour la déconnexion
                        .logoutSuccessUrl("/login?logout=true") // Redirection après déconnexion
                        .invalidateHttpSession(true) // Invalider la session après déconnexion
                        .deleteCookies("JSESSIONID") // Supprimer le cookie de session
                        .permitAll() // Autoriser la déconnexion pour tous
                );


        return http.build();
    }
}
