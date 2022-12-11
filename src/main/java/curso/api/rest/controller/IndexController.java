package curso.api.rest.controller;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import curso.api.rest.model.Usuario;
import curso.api.rest.repository.UsuarioRepository;

// Somente este site pode acessar esse controller corre origins 
//@CrossOrigin(origins = "https://young-mountain-67494.herokuapp.com/cadastro")

//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/usuario")
public class IndexController {

    @Autowired
    private UsuarioRepository usuarioRepository;


    @GetMapping("/")
    public ResponseEntity<List<Usuario>> listUser(){

        List<Usuario> userList = (List<Usuario>) usuarioRepository.findAll();
             return new ResponseEntity<List<Usuario>>(userList, HttpStatus.OK);
    }
    

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {
        Optional<Usuario> userId = usuarioRepository.findById(id);

        return new ResponseEntity<Usuario>(userId.get(), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<?> Cadastrar(@RequestBody Usuario usuario){
    	
    	for(int pos= 0; pos < usuario.getTelefones().size(); pos++) {
    		usuario.getTelefones().get(pos).setUsuario(usuario);
    	}
    	
        Usuario user = usuarioRepository.save(usuario);
        return new ResponseEntity<Usuario>(user, HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<?> update(@RequestBody Usuario usuario){

    	for(int pos = 0; pos < usuario.getTelefones().size(); pos ++) {
    		usuario.getTelefones().get(pos).setUsuario(usuario);
    	}
    	
        Usuario userUpdate = usuarioRepository.save(usuario);
        return new ResponseEntity<Usuario>(userUpdate, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id){
    	
    	
        usuarioRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
