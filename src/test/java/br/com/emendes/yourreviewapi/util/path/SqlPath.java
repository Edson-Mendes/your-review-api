package br.com.emendes.yourreviewapi.util.path;

/**
 * Classe com constantes que indicam o path para arquivo SQL usados em testes automatizados.
 */
public final class SqlPath {

  private SqlPath() {
  }

  public static final String CREATE_DATABASE_TABLES_SQL_PATH = "/sql/database/create_database_tables.sql";
  public static final String DROP_DATABASE_TABLES_SQL_PATH = "/sql/database/drop_database_tables.sql";

  public static final String INSERT_REVIEW_SQL_PATH = "/sql/review/insert_review_with_user_and_movie_votes.sql";
  public static final String INSERT_TWO_REVIEWS_FOR_SAME_MOVIE_SQL_PATH = "/sql/review/insert_two_reviews_for_same_movie.sql";

}
