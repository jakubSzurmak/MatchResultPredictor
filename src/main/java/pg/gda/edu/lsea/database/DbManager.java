package pg.gda.edu.lsea.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import pg.gda.edu.lsea.absPerson.implPerson.Player;
import pg.gda.edu.lsea.team.Team;
import pg.gda.edu.lsea.absStatistics.absPlayerStatistics.implPlayerStatistics.fPlayerStatistics;
import pg.gda.edu.lsea.absStatistics.absPlayerStatistics.implPlayerStatistics.gPlayerStatistics;
import pg.gda.edu.lsea.absStatistics.implStatistics.TeamStatistics;
import pg.gda.edu.lsea.match.Match;

import java.util.List;
import java.util.UUID;

public class DbManager {

    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(
            "pg.gda.edu.lsea");

    private static DbManager instance;

    protected DbManager() {

    }

    /**
     * ensures only one instance of DbManager is used
     * @return this instance
     */
    public static DbManager getInstance() {
        if (instance == null) {
            instance = new DbManager();
        }
        return instance;
    }

    /**
     * Sets a custom instance of the DbManager
     * @param customInstance The DbManager instance to set
     */
    public static void setInstance(DbManager customInstance) {
        instance = customInstance;
    }

    /**
     * Saves the given entity to the database.
     *
     * @param entity The object to persist
     */
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

    /**
     * Retrieves a database record by its ID and class type.
     *
     * @param id        The UUID of the entity
     * @param classType The class of the entity to retrieve
     * @return The found entity
     */
    public <C> C getTableById(UUID id, Class<C> classType)  {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return entityManager.find(classType, id);
    }

    /**
     * Retrieves the first entity matching a column value.
     *
     * @param name       Value to match
     * @param classType  Entity type
     * @param columnName Column to search by
     * @return Matching entity or null if not found
     */
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

    /**
     * Executes a SQL SELECT query on the specified table with an optional condition.
     *
     * @param selectionTable  Table name
     * @param conditionColumn Column to filter by
     * @param conditionValue  Filter value or "all" to fetch all rows
     * @return List of result rows or null if no column is specified
     */
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

    /**
     * Retrieves entities using JPQL based on table name and a filter condition.
     *
     * @param selectionTable  Name of the entity (e.g. "players", "teams")
     * @param conditionColumn Column name to filter by
     * @param conditionValue  Value to match or "all" to retrieve all records
     * @return List of matching entities or null if no column is provided
     */
    public Object getFromDBJPQL(String selectionTable, String conditionColumn, String conditionValue) {
        if (!conditionColumn.isEmpty()) {
            EntityManager entityManager = entityManagerFactory.createEntityManager();

            Class<?> entityClass = switch (selectionTable.toLowerCase()) {
                case "players" -> Player.class;
                case "teams" -> Team.class;
                case "fplayerstatistics" -> fPlayerStatistics.class;
                case "gplayerstatistics" -> gPlayerStatistics.class;
                case "teamstats" -> TeamStatistics.class;
                case "matches" -> Match.class;
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


    /**
     * Updates specified column values in a database table based on a condition.
     *
     * @param table           Name of the table to update
     * @param setColumn       Column to be updated
     * @param setValue        New value to set
     * @param conditionColumn Column to filter rows by
     * @param conditionValue  Value to match or "all" to update all rows
     */
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




    /**
     * Updates multiple rows in a single transaction
     * @param table The database table to update
     * @param setColumn The column to update
     * @param baseValue The base value to set (will be appended with row number)
     * @param conditionColumn The column to use in WHERE clause
     * @param conditionValues List of values to update (one per row)
     */
    public void updateMultipleRowsSingleTransaction(String table, String setColumn, String baseValue,
                                                    String conditionColumn, List<String> conditionValues) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            // One transaction for all updates
            entityManager.getTransaction().begin();

            for (int i = 0; i < conditionValues.size(); i++) {
                String query = "UPDATE " + table + " SET " + setColumn + " = '" + baseValue + "_" + i + "' WHERE "
                        + conditionColumn + " = '" + conditionValues.get(i) + "'";
                entityManager.createNativeQuery(query).executeUpdate();
            }

            entityManager.getTransaction().commit();
            System.out.println("Batch transaction completed for " + conditionValues.size() + " rows.");
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            System.out.println("Error in batch transaction: " + e.getMessage());
        } finally {
            entityManager.close();
        }
    }

    /**
     * Updates multiple rows using a separate transaction for each row
     * @param table The database table to update
     * @param setColumn The column to update
     * @param baseValue The base value to set (will be appended with row number)
     * @param conditionColumn The column to use in WHERE clause
     * @param conditionValues List of values to update (one per row)
     */
    public void updateMultipleRowsMultipleTransactions(String table, String setColumn, String baseValue,
                                                       String conditionColumn, List<String> conditionValues) {
        System.out.println("Starting multiple transactions for " + conditionValues.size() + " rows.");

        for (int i = 0; i < conditionValues.size(); i++) {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            try {
                // New transaction for each update
                entityManager.getTransaction().begin();

                String query = "UPDATE " + table + " SET " + setColumn + " = '" + baseValue + "_" + i + "' WHERE "
                        + conditionColumn + " = '" + conditionValues.get(i) + "'";
                entityManager.createNativeQuery(query).executeUpdate();

                entityManager.getTransaction().commit();
            } catch (Exception e) {
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback();
                }
                System.out.println("Error in transaction " + i + ": " + e.getMessage());
            } finally {
                entityManager.close();
            }
        }

        System.out.println("All separate transactions completed.");
    }


    /**
     * Deletes records from the specified table based on a condition,
     * handling related entities and relationships appropriately.
     *
     * @param tableName       Name of the table to delete from
     * @param conditionColumn Column to filter records by
     * @param conditionValue  Value to match or "all" to delete all records
     */
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
                        List<Match> matches = entityManager.createQuery(
                                        "SELECT m FROM Match m WHERE m.homeTeam = :team OR m.awayTeam = :team", Match.class)
                                .setParameter("team", team)
                                .getResultList();
                        for (Match match : matches) {
                            entityManager.remove(match);
                        }

                        TeamStatistics stats = entityManager.find(TeamStatistics.class, team.getId());
                        if (stats != null) {
                            entityManager.remove(stats);
                        }
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
