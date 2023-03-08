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
            dataSource.setURL(jdbcDatabaseURL)
        }

    }

    @BeforeTest
    fun testConnection(){
        assertNotNull(dataSource)
        //assert<Throwable> { dataSource.getConnection() } //TODO PSQLException(pwd error)
    }

    @Test
    fun `clear insert read students`() {
        val cmdDelete =
            "delete from dbo.students;" +
            "delete from dbo.courses;"

        val cmdUpdate =
            "insert into dbo.courses(cid, name) values (1,'LEIM');\n" +
            "insert into dbo.students(course, number, name) values (1, 12345, 'Jorge');\n" +
            "insert into dbo.students(course, number, name) select cid as course, 12346 as number, 'Bob' as name from dbo.courses where name = 'LEIM';"

        dataSource.getConnection().use {
            it.autoCommit = false  //needed for test effects

            /** Delete students and courses **/
            val stmDelete = it.prepareStatement(cmdDelete)
            stmDelete.executeUpdate()

            /** Update students and courses **/
            val stmUpdate = it.prepareStatement(cmdUpdate)
            stmUpdate.executeUpdate()

            /** Select students **/
            val stmSelectS = it.prepareStatement("select * from dbo.students where name='Jorge'")
            val rsStudents = stmSelectS.executeQuery()

            /** Select courses **/
            val stmSelectC = it.prepareStatement("select * from dbo.courses where cid=1")
            val rsCourses = stmSelectC.executeQuery()

            /** Iterate students ResultSet **/
            while (rsStudents.next()) {
                assertEquals(rsStudents.getInt("number"), 12345)
                assertEquals(rsStudents.getString("name"), "Jorge")
                assertEquals(rsStudents.getInt("course"), 1)
            }

            /** Iterate courses ResultSet **/
            while (rsCourses.next()){
                assertEquals(rsCourses.getInt("cid"), 1)
                assertEquals(rsCourses.getString("name"), "LEIM")
            }
            it.prepareStatement("ROLLBACK;").executeUpdate()
        }
    }
}



