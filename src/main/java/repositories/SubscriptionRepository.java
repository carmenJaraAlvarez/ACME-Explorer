
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Newspaper;
import domain.Subscription;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {

	@Query("select s.newspaper from Subscription s where s.customer.id=?1 and s.newspaper.id=?2")
	Collection<Newspaper> findNewspaperEq(int customerId, int newspaperId);

	@Query("select s from Subscription s where s.customer.id=?1 and s.volume.id=?2")
	Collection<Subscription> findVolumeEq(int customerId, int volumeId);

	@Query("select sub.volume.newspapers from Subscription sub where sub.customer.id=?1 and sub.newspaper=null")
	Collection<Newspaper> findAllNewspapersOfVolumeByCustomer(int customerId);

	@Query("select sub.newspaper from Subscription sub where sub.customer.id=?1 and sub.volume=null")
	Collection<Newspaper> findAllNewspaperByCustomer(int customerId);

	@Query("select sub from Subscription sub where sub.newspaper.id=?1 and sub.volume=null")
	Collection<Subscription> findSubscriptionByNewspaper(int newspaperId);

}
