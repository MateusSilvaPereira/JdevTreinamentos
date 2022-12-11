package curso.api.rest.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import curso.api.rest.service.ImplementacaoUserDetailsService;

/* Mapear URL, Enderecos, autoriza ou bloqueia acesso de URL*/

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {

	@Autowired
	private ImplementacaoUserDetailsService implementacaoUserDetailsService;

	/* Configura as solicitações por HTTP */
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		/* Ativando a proteção contra usuario que nao estao validados por token */
		http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())

				/* Ativando a permissão para qualquer usuario acessar a pagina inicial /index */
				.disable().authorizeRequests().antMatchers("/").permitAll().antMatchers("/index").permitAll()

				/*
				 * URL de logout Redireciona o usuario deslogado para a pagina inicial do
				 * sistema
				 */
				.anyRequest().authenticated().and().logout().logoutSuccessUrl("/index")

				/* Mapeia URL de logout e invalida o usuario */
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				
				/* Filtra requisicao de login para autenticacao*/
				.and().addFilterBefore(new JWTLoginFilter("/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
				
				/* Filtra demais requisicoes para verificar a presencao do token JWt no Header do HTTP*/
				.addFilterBefore(new JWTApiAutenticacaoFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		/* Servico que ira consultar o usuario no banco de dados */
		auth.userDetailsService(implementacaoUserDetailsService)

				/* Padrao de codificação de usuario */
				.passwordEncoder(new BCryptPasswordEncoder());
	}

}
