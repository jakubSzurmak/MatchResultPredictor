package pg.gda.edu.lsea.database;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class DbManager {

    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(
            "pg.gda.edu.lsea.database-1.3");
    private static final EntityManager entityManager = entityManagerFactory.createEntityManager();


    public void saveToDb(Object entity) {
        entityManager.getTransaction().begin();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public Object getFromDB(String selectionTable, String conditionColumn, String conditionValue) {
        if(!conditionColumn.isEmpty()){

            if(conditionValue == "all"){
                return entityManager.createNativeQuery("SELECT * FROM " + selectionTable)
                        .getResultList();
            }
            else if(!conditionValue.isEmpty()){
                return entityManager.createNativeQuery("SELECT * FROM " + selectionTable + " WHERE "
                                + conditionColumn + " LIKE '%" + conditionValue + "%'")
                        .getResultList();
            }
            else{
                return entityManager.createNativeQuery("SELECT * FROM " + selectionTable + " WHERE "
                                + conditionColumn + " LIKE *")
                        .getResultList();
            }
        }else{
            return null;
        }
    }
}
