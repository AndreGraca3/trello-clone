package pt.isel.ls.sql

import org.junit.Before
import org.junit.BeforeClass
//import org.junit.jupiter.api.Test
import org.postgresql.ds.PGSimpleDataSource
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import kotlin.test.*

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

    /** runs before each test **/
//    @Before
//    @Test
//    fun initial_state(){
//        val cmdDelete =
//            "delete from dbo.students;" +
//            "delete from dbo.courses;"
//
//
//        dataSource.getConnection().use {
//            it.autoCommit = false
//            /** Delete students and courses **/
//            val stmDelete = it.prepareStatement(cmdDelete)
//            stmDelete.executeUpdate()
//        }
//    }

    /** runs before all tests but only once **/
    @BeforeTest
    @Test
    fun testConnection(){
        assertNotNull(dataSource)
    }

    @Test
    fun `insert and select students`() {
        val cmdInsert =
            "insert into dbo.courses(cid, name) values (1,'LEIM');\n" +
            "insert into dbo.students(course, number, name) values (1, 12345, 'Jorge');\n" +
            "insert into dbo.students(course, number, name) select cid as course, 12346 as number, 'Bob' as name from dbo.courses where name = 'LEIM';"

        dataSource.getConnection().use {
            it.autoCommit = false  //needed for test effects

            val cmdDelete =
                "delete from dbo.students;" +
                        "delete from dbo.courses;"
            val stmDelete = it.prepareStatement(cmdDelete)
            stmDelete.executeUpdate()

            /** Update students and courses **/
            val stmInsert = it.prepareStatement(cmdInsert)
            stmInsert.executeUpdate()

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
        }

    }

    @Test
    fun `update students`(){
        val cmdInsert = "insert into dbo.courses(cid, name) values (1,'LEIM');\n" +
                "insert into dbo.students(course, number, name) values (1, 12345, 'Jorge');\n" +
                "insert into dbo.students(course, number, name) select cid as course, 12346 as number, 'Bob' as name from dbo.courses where name = 'LEIM';"

        val cmdUpdate = "update dbo.students set name = 'Oscar' where number = 12345;"

        val cmdSelect = "Select * from dbo.students where number = 12345;"

        dataSource.getConnection().use{
            it.autoCommit = false

            val cmdDelete =
                "delete from dbo.students;" +
                        "delete from dbo.courses;"
            val stmDelete = it.prepareStatement(cmdDelete)
            stmDelete.executeUpdate()

            val stmInsert = it.prepareStatement(cmdInsert)
            stmInsert.executeUpdate()

            val stmUpdate = it.prepareStatement(cmdUpdate)
            stmUpdate.executeUpdate()

            val stmSelectS = it.prepareStatement(cmdSelect)
            val rsStudents = stmSelectS.executeQuery()

            while (rsStudents.next()){
                assertEquals(rsStudents.getString("name"),"Oscar")
            }
        }
    }

    @Test
    fun `delete students`(){
        val cmdInsert = "insert into dbo.courses(cid, name) values (1,'LEIM');\n" +
                "insert into dbo.students(course, number, name) values (1, 12345, 'Jorge');\n" +
                "insert into dbo.students(course, number, name) select cid as course, 12346 as number, 'Bob' as name from dbo.courses where name = 'LEIM';"

        val cmdDelete = "delete from dbo.students where number = 12345;"

        val cmdSelect = "Select * from dbo.students where number = 12345;"

        dataSource.getConnection().use {
            it.autoCommit = false

            val cmdDelete =
                "delete from dbo.students;" +
                        "delete from dbo.courses;"
            val stmDelete = it.prepareStatement(cmdDelete)
            stmDelete.executeUpdate()

            val stmInsert = it.prepareStatement(cmdInsert)
            stmInsert.executeUpdate()

            val stmUpdate = it.prepareStatement(cmdDelete)
            stmUpdate.executeUpdate()

            val stmSelectS = it.prepareStatement(cmdSelect)
            val rsStudents = stmSelectS.executeQuery()

            while(rsStudents.next()){
                assertEquals(rsStudents.getString("name"),null)
                //assertEquals(rsStudents.getInt("course"),null)
                //assertEquals(rsStudents.getInt("number"),null)
            }
        }

    }
    @AfterMethod
    fun rollback() {
        dataSource.getConnection().use{
            it.prepareStatement("ROLLBACK;").executeUpdate()
        }
    }
}



