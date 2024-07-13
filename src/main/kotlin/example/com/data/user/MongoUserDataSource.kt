package example.com.data.user

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull

class MongoUserDataSource(
   db:MongoDatabase
):UserDataSource {
    private val users = db.getCollection("Users",User::class.java)
    override suspend fun getUserByUserName(userName: String): User? {
        return users.withDocumentClass<User>().find(Filters.eq("userName",userName)).firstOrNull()
    }

    override suspend fun insertUser(user: User): Boolean {
        return users.insertOne(user).wasAcknowledged()
    }
}