package com.example.aletheia.pretest.security;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.aletheia.pretest.models.Participante;
import com.example.aletheia.pretest.repository.ParticipanteRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
public class JwtUserDetailsService implements UserDetailsService {

    private final ParticipanteRepo participanteRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {

        Participante participante = participanteRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + correo));

        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority(participante.getRol().name())
        );

        return new CustomUserDetails(
                participante.getId(),
                participante.getCorreo(),
                participante.getContrasenaHash(),
                participante.getNombres(),
                participante.getRol(),
                authorities
        );
    }
}