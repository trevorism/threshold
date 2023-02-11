package com.trevorism.security

import com.trevorism.secure.Roles
import com.trevorism.secure.Secure
import com.trevorism.secure.validator.AuthorizationNotValid
import io.micronaut.context.annotation.Replaces
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.core.annotation.Nullable
import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.rules.SecuredAnnotationRule
import io.micronaut.security.rules.SecurityRule
import io.micronaut.security.rules.SecurityRuleResult
import io.micronaut.web.router.MethodBasedRouteMatch
import io.micronaut.web.router.RouteMatch
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono

@jakarta.inject.Singleton
@Replaces(SecuredAnnotationRule)
class TrevorismSecurityRule implements SecurityRule {

    @Override
    int getOrder() {
        return -1000
    }

    @Override
    Publisher<SecurityRuleResult> check(HttpRequest<?> request, @Nullable RouteMatch<?> routeMatch, @Nullable Authentication authentication) {
        if (routeMatch instanceof MethodBasedRouteMatch) {
            MethodBasedRouteMatch methodBasedRouteMatch = (MethodBasedRouteMatch) routeMatch
            if (methodBasedRouteMatch.hasAnnotation(Secure.class)) {
                AnnotationValue<Secure> secureAnnotation = methodBasedRouteMatch.getAnnotation(Secure.class);
                if(validateClaims(secureAnnotation, authentication))
                    return Mono.just(SecurityRuleResult.ALLOWED)
                else
                    return Mono.just(SecurityRuleResult.REJECTED)
            }
        }
        return Mono.just(SecurityRuleResult.ALLOWED)
    }

    boolean validateClaims(AnnotationValue<Secure> annotation, Authentication authentication) {
        try{
            validateInputs(annotation, authentication);
            validateIssuer(authentication)
            validateRole(annotation.stringValue().get(), annotation.booleanValue("allowInternal").asBoolean(), authentication.roles[0])
            return true
        }catch(Exception ignored){
            return false
        }
    }

    private void validateInputs(AnnotationValue<Secure> annotation, Authentication authentication) {
        if (authentication == null || !authentication.roles) {
            throw new AuthorizationNotValid("Unable to parse claim");
        }
        if (annotation == null) {
            throw new AuthorizationNotValid("Unable to validate against a method without the @Secure annotation");
        }
    }
    private void validateIssuer(Authentication authentication) {
        String issuer = authentication.attributes["iss"]
        if ("https://trevorism.com" != issuer) {
            throw new AuthorizationNotValid("Unexpected issuer: ${issuer}")
        }
    }
    static void validateRole(String role, boolean allowInternal, String claimRole) {

        if (claimRole == null) {
            throw new AuthorizationNotValid("Unable to parse claim role");
        }
        if (role.isEmpty()) {
            return;
        }
        if (claimRole.equals(Roles.INTERNAL) && !allowInternal) {
            throw new AuthorizationNotValid("Insufficient access");
        }
        if (role.equals(Roles.ADMIN)) {
            if (!claimRole.equals(Roles.ADMIN)) {
                throw new AuthorizationNotValid("Insufficient access");
            }
        }
        if (role.equals(Roles.SYSTEM)) {
            if (claimRole.equals(Roles.USER)) {
                throw new AuthorizationNotValid("Insufficient access");
            }

        }
    }



}
