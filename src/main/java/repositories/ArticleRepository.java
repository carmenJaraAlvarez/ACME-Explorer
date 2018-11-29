
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {

	@Query("select a from Article a where a.newspaper = (select n from Newspaper n where n.id = ?1 and n.draft = false) and a.draft = false")
	Collection<Article> articlesPublishedOfNewspaperId(int newspaperId);

	@Query("select a from Article a where a.title LIKE concat('%',?1,'%') or a.body LIKE concat('%',?1,'%') or a.summary LIKE concat('%',?1,'%')")
	Collection<Article> tabooArticle(String tabooWord);

	@Query("select ar from Article ar where ar.newspaper.draft is false and ar.draft is false and (ar.title LIKE concat('%',?1,'%') or ar.body LIKE concat('%',?1,'%') or ar.summary LIKE concat('%',?1,'%'))")
	Collection<Article> findByKeyWord(String keyWord);

	@Query("select ar from Article ar where ar.writer.id=?1 and ar.newspaper.draft is false and ar.draft is false")
	Collection<Article> findFinalAndPublished(int id);

	//7.1

	@Query("select  coalesce(avg(u.articles.size),0.0) from User u")
	Double averageArticlesPerUser();

	@Query("select   coalesce (sqrt(sum(u.articles.size * u.articles.size) / count(u.articles.size) - (avg(u.articles.size) * avg(u.articles.size))),0.0) from User u")
	Double standardDeviationArticlesPerUser();

	@Query("select coalesce(avg(n.articles.size),0.0) from Newspaper n")
	Double averageArticlesPerNewspaper();

	@Query("select coalesce(sqrt(sum(n.articles.size * n.articles.size) / count(n.articles.size) - (avg(n.articles.size) * avg(n.articles.size))),0.0) from Newspaper n")
	Double standardDeviationArticlesPerNewspaper();

	//17.1

	@Query("select  coalesce(avg(a.followUps.size),0.0) from Article a")
	Double averageFollowUpsPerArticle();

	@Query("select coalesce((select count(b) from Article b where b.father.id is not null and b.draft is false and datediff(b.newspaper.pubDate, b.pubMoment) <= 7)/count(a)*100,0.0) from Article a where a.draft is false")
	Double averageFollowUpsPerArticle1WeekAfter();

	@Query("select coalesce((select count(b) from Article b where b.father.id is not null and b.draft is false and datediff(b.newspaper.pubDate, b.pubMoment) <= 14)/count(a)*100,0.0) from Article a where a.draft is false")
	Double averageFollowUpsPerArticle2WeekAfter();

	@Query("select a from Article a where a.father.id=?1 and a.draft is false and a.newspaper.draft is false")
	Collection<Article> filterFollowUps(int id);

}
