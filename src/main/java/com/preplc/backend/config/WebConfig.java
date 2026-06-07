package com.preplc.backend.config;

// CORS is fully handled by Spring Security's CorsConfigurationSource in SecurityConfig.java
// Do NOT add MVC-level CORS here — it conflicts with Spring Security CORS and breaks
// preflight requests for endpoints that send custom headers (e.g., Authorization).
