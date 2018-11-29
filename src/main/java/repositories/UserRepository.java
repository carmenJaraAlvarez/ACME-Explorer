
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Article;
import domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	//Para encontrar un user a traves de su userAccount
	@Query("select a from User a where a.userAccount.id=?1")
	User findByUserAccount(int userAccountId);

	//Para que te devuelva la lista de articulos de un user que estan publicados
	@Query("select ar from User a join a.articles ar where a.id=?1 and ar.pubMoment != null")
	Collection<Article> articlesPublished(int userId);

	//Para que te devuelva la lista de usuarios que siguen a un usuario
	@Query("select u.followers from User u where u.id=?1")
	Collection<User> findFollowers(int userId);

	//Para que te devuelva la lista de usuarios que sigue un usuario
	@Query("select u.following from User u where u.id=?1")
	Collection<User> findFollowing(int userId);

	// 7.1

	@Query("select coalesce((count(a)*100)/(select count(b)from User b),0.0) from User a where a.newspapers is not empty")
	Double ratioCreatorsNewspaper();

	@Query("select coalesce((count(a)*100)/(select count(b)from User b),0.0) from User a where a.articles is not empty")
	Double ratioCreatorsArticle();

	// 17.1

	@Query("select coalesce((count(a)*100)/(select count(b)from User b),0.0) from User a where a.chirps.size > 0.75*(select avg(u.chirps.size) from User u)")
	Double ratioUsersChirpsAbove75Average();

}
