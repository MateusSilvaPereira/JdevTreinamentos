package curso.api.rest.security;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import curso.api.rest.ApplicationContextLoad;
import curso.api.rest.model.Usuario;
import curso.api.rest.repository.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.IOException;

@Service
@Component
public class JWTTokenAutenticacaoService {
	
	/* Tempo de validade do Token de 2 dias*/
	private static final long EXPIRATION_TIME = 172800000;
	
	/* Uma senha unica para compor a autenticacao e ajudar na seguranca  */
	private static final String SECRET = "SenhaExtremamenteSecreta";

	/* Prefixo padrao de token*/
	private static final String TOKEN_PREFIX = "Bearer";

	private static final String HEADER_STRING = "Authorization";
	
	
	public void addAuthentication(HttpServletResponse response, String userName) throws IOException, java.io.IOException {
		
		@SuppressWarnings("deprecation")
		/* Montagem do token*/
		String JWT = Jwts.builder() /* Chama o gerador de token*/
				.setSubject(userName)/* Adiciona o usuario*/
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) /* Tempo de expirção*/
				 .signWith(SignatureAlgorithm.HS512, SECRET).compact();/* Compactação e algoritimo de geração de senha*/
	
		/* Junta o token com o PREFIX*/
		String token = TOKEN_PREFIX + " " + JWT; /* Bearer ohfhuiwehfghuihb438t93qyhfvhuoi */
				
		/* Adiciona no cabecalho Http*/
		response.addHeader(HEADER_STRING, token);/* |Exemplo|    Authorization: Bearer ohfhuiwehfghuihb438t93qyhfvhuoi */
		
		
		/*Liberando resposta para portas diferentes que usam a API ou caso clientes web*/
		 //liberacaoCors(response);
		
		/* Escreve o token como resposta no corpo do Http*/
		response.getWriter().write("{\"Authorization\": \"" + token + "\"}");// formato json
		
		
	}
	
	/* Retorna o usuario valido com o token ou caso não seja válido retorna null */
	public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {
		
		/*Pega o token enviado no cabeçalho http*/
		String token = request.getHeader(HEADER_STRING);
		
		if(token != null) {
			
			/* Faz a válidacao do token do usuario na requisicao*/
			@SuppressWarnings("deprecation")
			String user = Jwts.parser().setSigningKey(SECRET) /* Bearer ohfhuiwehfghuihb438t93qyhfvhuoi */
					.parseClaimsJws(token.replace(TOKEN_PREFIX, ""))/*ohfhuiwehfghuihb438t93qyhfvhuoi */
					.getBody().getSubject();
			
			if(user != null) {
				
				Usuario usuario = ApplicationContextLoad.getApplicationContext()
				        .getBean(UsuarioRepository.class).findUseByLogin(user);
				
			if(usuario != null) {
				return new UsernamePasswordAuthenticationToken(
						usuario.getLogin(),
						usuario.getSenha(), 
						usuario.getAuthorities());
			}
			
		}
		
	}
		return null; // Usuario não autorizado
}
}

