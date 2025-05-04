package pg.gda.edu.lsea.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class DbManager {

    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(
            "pg.gda.edu.lsea");


    public void saveToDb(Object entity) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try{
            entityManager.getTransaction().begin();
            entityManager.persist(entity);
            entityManager.getTransaction().commit();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            entityManager.close();
        }
    }

    public Object getFromDB(String selectionTable, String conditionColumn, String conditionValue) {
        if(!conditionColumn.isEmpty()){
            EntityManager entityManager = entityManagerFactory.createEntityManager();
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
