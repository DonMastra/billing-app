
package com.bolsadeideas.springboot.app.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.app.models.entity.Usuario;

//May extend from JpaRepository (!= methods)
public interface IUsuarioDao extends CrudRepository<Usuario, Long>{
	
	//QueryMethodName execute JPQL query "select u from Usuario u where u.username=?1"
	public Usuario findByUsername(String username);

}
