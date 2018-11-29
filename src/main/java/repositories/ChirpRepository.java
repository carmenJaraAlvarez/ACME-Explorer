
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Chirp;

@Repository
public interface ChirpRepository extends JpaRepository<Chirp, Integer> {

	@Query("select c from Chirp c where c.title LIKE concat ('%',?1,'%') or c.description LIKE concat ('%',?1,'%')")
	Collection<Chirp> tabooChirps(String tabooWord);

	@Query("select  coalesce(avg(u.chirps.size),0.0) from User u")
	Double averageChirpsPerUser();

	@Query("select  coalesce(sqrt(sum(u.chirps.size * u.chirps.size) / count(u.chirps.size) - (avg(u.chirps.size) * avg(u.chirps.size))),0.0) from User u")
	Double standardDeviationChirpsPerUser();

	@Query("select ne from Chirp ne where (ne.title LIKE concat('%',?1,'%') or ne.description LIKE concat('%',?1,'%'))")
	Collection<Chirp> findByKeyWord(String keyWord);
}
