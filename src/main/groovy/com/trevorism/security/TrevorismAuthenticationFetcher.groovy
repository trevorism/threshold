package com.trevorism.security

import com.trevorism.secure.ClaimProperties
import com.trevorism.secure.ClaimsProvider
import com.trevorism.secure.ClasspathBasedPropertiesProvider
import com.trevorism.secure.PropertiesProvider
import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.filters.AuthenticationFetcher
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono

@jakarta.inject.Singleton
class TrevorismAuthenticationFetcher implements AuthenticationFetcher{

    public static final String BEARER_PREFIX = "bearer "

    private PropertiesProvider propertiesProvider

    TrevorismAuthenticationFetcher(){
        propertiesProvider = new ClasspathBasedPropertiesProvider()
    }

    @Override
    Publisher<Authentication> fetchAuthentication(HttpRequest<?> request) {
        def authString = request.getHeaders().get("Authorization")
        if(!authString || !authString.toLowerCase().startsWith(BEARER_PREFIX)){
            return Mono.empty()
        }
        String token = authString.substring(BEARER_PREFIX.length())
        ClaimProperties claimProperties = ClaimsProvider.getClaims(token, propertiesProvider.getProperty("signingKey"))

        Mono.just(Authentication.build(claimProperties.subject, [claimProperties.role], [type: claimProperties.type, iss: claimProperties.issuer, id: claimProperties.id, aud: claimProperties.audience]))
    }
}
