
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Newspaper;

@Repository
public interface NewspaperRepository extends JpaRepository<Newspaper, Integer> {

	@Query("select n from Newspaper n where n.draft = false")
	Collection<Newspaper> publishedNewspapers();

	@Query("select ne from Newspaper ne where ne.draft is false and (ne.title LIKE concat('%',?1,'%') or ne.description LIKE concat('%',?1,'%'))")
	Collection<Newspaper> findByKeyWord(String keyWord);

	@Query("select n from Newspaper n where n.title LIKE concat ('%',?1,'%') or n.description LIKE concat('%',?1,'%')")
	Collection<Newspaper> tabooNewspapers(String tabooWord);

	@Query("select n from Newspaper n where n.draft = true")
	Collection<Newspaper> findNotPublished();

	@Query("select n from Newspaper n where n.publisher.id = ?1")
	Collection<Newspaper> myNewspapers(Integer userLoggedId);

	@Query("select n from Newspaper n where n.publisher.id = ?1 and n.draft is false")
	Collection<Newspaper> findNewspapersForVolume(Integer userLoggedId);

	// 7.1

	@Query("select coalesce(avg(u.newspapers.size),0.0)  from User u")
	Double averageNewspapersPerUser();

	@Query("select coalesce(sqrt(sum(t.newspapers.size * t.newspapers.size) / count(t.newspapers.size) - (avg(t.newspapers.size) * avg(t.newspapers.size))),0.0) from User t")
	Double standardDesviationNewspapersPerUser();

	@Query("select n from Newspaper n where n.articles.size > ( select avg(ne.articles.size) + avg(ne.articles.size)*0.1 from Newspaper ne)")
	Collection<Newspaper> newspaperWithMoreArticlesThanAveragePlus10percent();

	@Query("select n from Newspaper n where n.articles.size < ( select avg(ne.articles.size) - avg(ne.articles.size)*0.1 from Newspaper ne)")
	Collection<Newspaper> newspaperWithLessArticlesThanAverageMinus10percent();

	// 24.1

	@Query("select coalesce((count(a)*100)/(select count(b)from Newspaper b where b.isPrivate is true),0.0) from Newspaper a where a.isPrivate is false")
	Double ratioPublicVsPrivateNewspaper();

	@Query("select coalesce(avg(u.articles.size),0.0) from Newspaper u where u.isPrivate is false")
	Double averageArticlesPerPublicNewspaper();

	@Query("select coalesce(avg(u.articles.size),0.0) from Newspaper u where u.isPrivate is true")
	Double averageArticlesPerPrivateNewspaper();

	@Query("select coalesce((100*coalesce(count(a)/(select count(b) from Newspaper b where b.isPrivate is false))/(select count(c) from Customer c)),0.0) from Subscription a")
	Double ratioSuscribersPerPrivateNewspaperVsTotalCostumers();

	@Query("select coalesce((100*coalesce(count(a)/(select count(b) from Newspaper b where b.isPrivate is false))/(select count(c) from User c)),0.0) from Newspaper a where a.isPrivate is true")
	Double ratioPrivateVsPublicNewspapersPerPublisher();

	@Query("select a.newspaper from Advertisement a where a.agent.id= ?1 and a.newspaper.draft is false")
	Collection<Newspaper> findNewspapersWithAdvertisement(int id);

	@Query("select n from Newspaper n where n.draft is false")
	Collection<Newspaper> findNewspapersPublished();

	//2.5.3
	@Query("select coalesce((count(a)*100)/(select count(b)from Newspaper b where b.advertisements is not empty),0.0) from Newspaper a where a.advertisements is empty")
	Double ratioNewspapersWithAdvertisementsVsWithout();

	//2.11.1
	@Query("select coalesce(avg(n.newspapers.size),0.0) from Volume n")
	Double averageNewspapersPerVolume();

	@Query("select coalesce((count(sp)*100)/(select count(sv)from Subscription sv where sv.volume is not null),0.0) from Subscription sp where sp.newspaper is not null")
	Double ratioSubscriptionsToNewspapersVsVolumes();

}
