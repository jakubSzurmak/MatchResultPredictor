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

    public void updateInDb(String table, String setColumn, String setValue, String conditionColumn, String conditionValue) {
        if (!conditionColumn.isEmpty()) {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            try {
                entityManager.getTransaction().begin();
                String query;
                if (conditionValue.equals("all")) {
                    query = "UPDATE " + table + " SET " + setColumn + " = '" + setValue + "'";
                } else if (!conditionValue.isEmpty()) {
                    query = "UPDATE " + table + " SET " + setColumn + " = '" + setValue + "' WHERE "
                            + conditionColumn + " LIKE '%" + conditionValue + "%'";
                } else {
                    query = "UPDATE " + table + " SET " + setColumn + " = '" + setValue + "' WHERE "
                            + conditionColumn + " LIKE '*'";
                }
                entityManager.createNativeQuery(query).executeUpdate();
                entityManager.getTransaction().commit();
                System.out.println("Update completed successfully.");
            } catch (Exception e) {
                System.out.println("Something went wrong during update: " + e.getMessage());
            } finally {
                entityManager.close();
            }
        }
    }


    public void deleteFromDb(String table, String conditionColumn, String conditionValue) {
        if (!conditionColumn.isEmpty()) {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            try {
                entityManager.getTransaction().begin();
                String query;
                if (conditionValue.equals("all")) {
                    query = "DELETE FROM " + table;
                } else if (!conditionValue.isEmpty()) {
                    query = "DELETE FROM " + table + " WHERE " + conditionColumn + " LIKE '%" + conditionValue + "%'";
                } else {
                    query = "DELETE FROM " + table + " WHERE " + conditionColumn + " LIKE '*'";
                }
                entityManager.createNativeQuery(query).executeUpdate();
                entityManager.getTransaction().commit();
                System.out.println("Delete completed successfully.");
            } catch (Exception e) {
                System.out.println("Something went wrong during delete: " + e.getMessage());
            } finally {
                entityManager.close();
            }
        }
    }
}
