
package utilities;

public class QueryBuildering {

	public static void main(String[] args) {

		//	seleccionarArticles("article1", "article2");
		System.out.print("fin");

	}

	//
	//	private static void seleccionarArticles(String s1, String s2) {
	//		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Acme-Newspaper");
	//		EntityManager em = emf.createEntityManager();
	//		try {
	//			String sql = "select p from Article p ";
	//			if (s1 != null || s2 != null) {
	//				sql += " where ";
	//			}
	//			if (s1 != null) {
	//
	//				sql += " title='" + s1 + "'";
	//			}
	//			if (s2 != null) {
	//
	//				if (s1 != null) {
	//					sql += " or  title='" + s2 + "'";
	//				} else {
	//
	//					sql += "  title='" + s2 + "'";
	//				}
	//
	//			}
	//
	//			TypedQuery<Article> consulta = em.createQuery(sql, Article.class);
	//
	//			List<Article> lista = consulta.getResultList();
	//			for (Article a : lista)
	//
	//				System.out.println(a.getTitle());
	//
	//		} catch (Exception e) {
	//
	//			e.printStackTrace();
	//		} finally {
	//			em.close();
	//
	//		}
	//	}
	//

}
