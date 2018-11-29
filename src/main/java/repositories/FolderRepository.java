
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Folder;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Integer> {

	@Query("select distinct f from Actor a join a.folders f where a.id=?1")
	Collection<Folder> findByActorId(int explorerId);

	@Query("select f from Folder f where f.actor.id=?1 and f.fatherFolder is null")
	Collection<Folder> primerNivel(int actorId);

	@Query("select f from Folder f where f.actor.id=?1 and f.fatherFolder is null and f.name like 'in box'")
	Folder inbox(int actorId);

	@Query("select f from Folder f where f.actor.id=?1 and f.fatherFolder is null and f.name like 'notification box'")
	Folder notification(int actorid);

	@Query("select f from Folder f where f.actor.id=?1 and f.fatherFolder is null and f.name like 'trash box'")
	Folder trashBox(int actorid);

}
