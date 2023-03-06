package pt.isel.ls.sql

import org.junit.BeforeClass
import kotlin.test.Test
//import org.junit.jupiter.api.Test
import org.postgresql.ds.PGSimpleDataSource
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class SqlTest {

    companion object{

        lateinit var dataSource : PGSimpleDataSource

        @JvmStatic
        @BeforeClass
        fun setup()  {
            dataSource = PGSimpleDataSource()
            val jdbcDatabaseURL = System.getenv("JDBC_DATABASE_URL")
            //dataSource.setURL(jdbcDatabaseURL)
            dataSource.setURL("jdbc:postgresql://localhost:5432/?user=postgres&password=isel")
        }

    }

    @BeforeTest
    fun testConnection(){
        assertNotNull(dataSource)
        //assert<Throwable> { dataSource.getConnection() }
    }

    @Test
    fun `clear insert read students`() {
        val cmdDelete =
            "delete from dbo.students;" +
            "delete from dbo.courses;"

        val cmdUpdate =
            "insert into dbo.courses(name) values ('LEIM');\n" +
            "insert into dbo.students(course, number, name) values (1, 12345, 'Jorge');\n" +
            "insert into dbo.students(course, number, name) select cid as course, 12346 as number, 'Bob' as name from dbo.courses where name = 'LEIM';"

        dataSource.getConnection().use {
            val stmDelete = it.prepareStatement(cmdDelete)
            stmDelete.executeUpdate()

            val stmUpdate = it.prepareStatement(cmdUpdate)
            stmUpdate.executeUpdate()
            val stmSelectS = it.prepareStatement("select * from dbo.students where name='Jorge'")
            val stmSelectC = it.prepareStatement("select * from dbo.courses where cid=1")

            val rsStudents = stmSelectS.executeQuery()
            val rsCourses = stmSelectC.executeQuery()

            while (rsStudents.next()) {
                assertEquals(rsStudents.getInt("number"), 12345)
                assertEquals(rsStudents.getString("name"), "Jorge")
                assertEquals(rsStudents.getInt("course"), 1)
            }
            while (rsCourses.next()){
                assertEquals(rsStudents.getInt("cid"), 1)
                assertEquals(rsStudents.getString("name"), "LEIM")
            }
            it.prepareStatement("ROLLBACK;").executeUpdate()
        }
    }
}



