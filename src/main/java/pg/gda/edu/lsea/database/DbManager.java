package pg.gda.edu.lsea.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import pg.gda.edu.lsea.team.Team;
import pg.gda.edu.lsea.absPerson.implPerson.Player;

import java.util.List;
import java.util.UUID;

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

    public <C> C getTableById(UUID id, Class<C> classType)  {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return entityManager.find(classType, id);
    }

    public <C> C getValueFromColumn(String name, Class<C> classType, String columnName) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<C> query = cb.createQuery(classType);
        Root<C> root = query.from(classType);
        query.where(cb.equal(root.get(columnName), name));
        List<C> resultList = entityManager.createQuery(query).getResultList();

        if (!resultList.isEmpty()) {
            return resultList.get(0);
        }
        return null;
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

    public Object getFromDBJPQL(String selectionTable, String conditionColumn, String conditionValue) {
        if (!conditionColumn.isEmpty()) {
            EntityManager entityManager = entityManagerFactory.createEntityManager();

            Class<?> entityClass = switch (selectionTable.toLowerCase()) {
                case "players" -> Player.class;
                case "teams" -> Team.class;
                default -> throw new IllegalArgumentException("Unknown table: " + selectionTable);
            };

            String entityName = entityClass.getSimpleName();

            try {
                if (conditionValue.equals("all")) {
                    return entityManager.createQuery("SELECT e FROM " + entityName + " e", entityClass)
                            .getResultList();
                } else if (!conditionValue.isEmpty()) {
                    return entityManager.createQuery(
                                    "SELECT e FROM " + entityName + " e WHERE e." + conditionColumn + " LIKE :value", entityClass)
                            .setParameter("value", "%" + conditionValue + "%")
                            .getResultList();
                } else {
                    return entityManager.createQuery(
                                    "SELECT e FROM " + entityName + " e WHERE e." + conditionColumn + " LIKE '*'", entityClass)
                            .getResultList();
                }
            } finally {
                entityManager.close();
            }
        } else {
            return null;
        }
    }


    public void updateInDb(String table, String setColumn, String setValue, String conditionColumn, String conditionValue) {
        if (!conditionColumn.isEmpty()) {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            try {
                entityManager.getTransaction().begin();
                String query;


                boolean isNumeric = setValue.matches("-?\\d+(\\.\\d+)?");

                String formattedValue = isNumeric ? setValue : "'" + setValue + "'";

                if (conditionValue.equals("all")) {
                    query = "UPDATE " + table + " SET " + setColumn + " = " + formattedValue;
                } else if (!conditionValue.isEmpty()) {
                    query = "UPDATE " + table + " SET " + setColumn + " = " + formattedValue +
                            " WHERE " + conditionColumn + " LIKE '%" + conditionValue + "%'";
                } else {
                    query = "UPDATE " + table + " SET " + setColumn + " = " + formattedValue +
                            " WHERE " + conditionColumn + " LIKE '*'";
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



    public void deleteFromDb(String tableName, String conditionColumn, String conditionValue) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();

            Object result = getFromDBJPQL(tableName, conditionColumn, conditionValue);

            if (result instanceof List<?> list && !list.isEmpty()) {
                for (Object obj : list) {
                    if (obj instanceof Player player) {
                        player.getTeamSet().clear();
                        entityManager.merge(player);

                        entityManager.remove(entityManager.contains(player) ? player : entityManager.merge(player));

                    } else if (obj instanceof Team team) {
                        team.getPlayerSet().clear();
                        entityManager.merge(team);

                        entityManager.remove(entityManager.contains(team) ? team : entityManager.merge(team));

                    } else {
                        entityManager.remove(entityManager.contains(obj) ? obj : entityManager.merge(obj));
                    }
                }
            } else {
                System.out.println("No object found to delete.");
            }

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            System.err.println("Something went wrong during delete: " + e.getMessage());
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }



}
