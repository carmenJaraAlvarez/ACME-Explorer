
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Advertisement;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Integer> {

	@Query("select a from Advertisement a where a.title LIKE concat ('%',?1,'%')")
	Collection<Advertisement> tabooAdvertisement(String tabooWord);

	@Query("select ne from Advertisement ne where (ne.title LIKE concat('%',?1,'%'))")
	Collection<Advertisement> findByKeyWord(String keyWord);
}
