package example.com.security.hashing

// we convert the actual password into salted password(we attach the randomly generated string of length 32 which we call salt and convert it to hex
// then we attach hexSalt to end of actual password and then convert it by hashing

interface HashingService {
    fun generateSaltedHash(value: String,saltLength:Int=32): SaltedHash
    fun verify(value: String,saltedHash: SaltedHash):Boolean
}