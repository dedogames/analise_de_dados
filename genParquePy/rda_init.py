import subprocess
import psycopg2

# Database connection details (replace with your actual values)
HOST = "postgres"  # Assuming the PostgreSQL container name is "postgres"
PORT = 5432
DATABASE = "my_company"
USERNAME = "postgres"
PASSWORD = "root"  # Replace with a strong password

# Path to the SQL script within the container
SQL_SCRIPT_PATH = "/scripts/summary_by_regions.sql"

def create_user_and_database():
    """Creates the 'postgres' user and 'my_company' database."""
    try:
        # Connect to the default database (usually 'postgres') without a password
        conn = psycopg2.connect(host=HOST, port=PORT)
        cursor = conn.cursor()

        # Create the 'postgres' user with the provided password
        cursor.execute(f"CREATE USER {USERNAME} WITH PASSWORD '{PASSWORD}';")

        # Create the 'my_company' database
        cursor.execute(f"CREATE DATABASE {DATABASE};")

        conn.commit()
        print("User and database created successfully!")

    except (Exception, psycopg2.Error) as error:
        print("Error while creating user and database:", error)

    finally:
        if conn:
            cursor.close()
            conn.close()

def execute_sql_script():
    """Executes the SQL script located at 'SQL_SCRIPT_PATH'."""
    try:
        # Connect to the 'my_company' database as the 'postgres' user
        conn = psycopg2.connect(host=HOST, port=PORT, database=DATABASE, user=USERNAME, password=PASSWORD)
        cursor = conn.cursor()

        # Execute the SQL script from the specified path
        with open(SQL_SCRIPT_PATH, "r") as script_file:
            sql_script = script_file.read()
            cursor.execute(sql_script)

        conn.commit()
        print("SQL script executed successfully!")

    except (Exception, psycopg2.Error) as error:
        print("Error while executing SQL script:", error)

    finally:
        if conn:
            cursor.close()
            conn.close()

def list_tables():
    """Lists all tables in the connected database."""
    try:
        # Connect to the 'my_company' database as the 'postgres' user
        conn = psycopg2.connect(host=HOST, port=PORT, database=DATABASE, user=USERNAME, password=PASSWORD)
        cursor = conn.cursor()

        # Get all tables
        cursor.execute("SELECT table_name FROM information_schema.tables;")
        tables = cursor.fetchall()

        if tables:
            print("Tables:")
            for table in tables:
                print(table[0])
        else:
            print("No tables found in the database.")

    except (Exception, psycopg2.Error) as error:
        print("Error while listing tables:", error)

    finally:
        if conn:
            cursor.close()
            conn.close()

# Create the user and database (optional, uncomment if needed)
create_user_and_database()

# Execute the SQL script
execute_sql_script()

# List tables (optional, uncomment if needed)
list_tables()