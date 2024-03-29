package pl.school.register.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterListener;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.util.Collection;

/*
    Not really a view as it isn't displayed (or rather it shouldn't).
    I just need to redirect the user after login based on their role somehow
    and this seems to be the most sane way to do so.
 */

@Route(value = "/")
@PermitAll
@AnonymousAllowed
public class AfterLoginRedirectView extends VerticalLayout implements BeforeEnterObserver {
    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        if (principal instanceof AnonymousAuthenticationToken){
            beforeEnterEvent.forwardTo(LoginLayout.class);
            return;
        }
        UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) principal;
        Collection<? extends GrantedAuthority> authorityCollection = userDetails.getAuthorities();
        if (authorityCollection.contains(new SimpleGrantedAuthority("ROLE_STUDENT"))){
            beforeEnterEvent.forwardTo(StudentScheduleView.class);
        }else if (authorityCollection.contains(new SimpleGrantedAuthority("ROLE_TEACHER"))){
            beforeEnterEvent.forwardTo(TeacherLayout.class);
        }else if (authorityCollection.contains(new SimpleGrantedAuthority("ROLE_PARENT"))){
            beforeEnterEvent.forwardTo(ParentChildrenLayout.class);
        }
    }
}
