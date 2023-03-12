package pt.isel.ls

class Services(private val dataMem: DataMem) {

    fun createUser(name : String, email : String) : User {
        val newUser = dataMem.createUser(name,email)
        return User(newUser.second,email,name,newUser.first)
    }
}