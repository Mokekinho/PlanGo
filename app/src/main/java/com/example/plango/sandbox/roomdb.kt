package com.example.plango.sandbox

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Entity(
    //aqui daria pra mudar o nome da tabela e um monte de outras coisas que nao sao tao importantes
) //isso fala que essa dataclass é uma tabela
data class Contact(
    @PrimaryKey(autoGenerate = true) // isso vai dizer para o Room que o id é a chave primaria e que a gente quer que ela seja auto gerada
    val id: Int = 0, //tem que colocar um valor padrao pra enganar o compilador, mas na pratica o room ele vai substitur esse 0 pelo valor id correto

    val firstName : String,
    val lastName: String,
    val phoneNumber: String,

    )

@Dao //Data Access Object (Objeto de Acesso a Dados)
//Você não escreve o código SQL manualmente — o Room gera tudo pra você durante a compilação.
//Basicamente uma DAO é criar uma camada de metodos que interage com o banco de dados, fazendo busca, adição, e muito mais
//aqui a gente ta fazendo o contrato, o Room vai criar a implementação dos metodos em SQL de verdade
interface ContactDao{

    //@Insert // é o padrao, mas nesse caso se existir um objeto com a mesmo id teria que definir uma forma de contornar isso, com upsert ele atualiza o objeto pelo novo
    @Upsert
    suspend fun upsertContact(
        contact: Contact
    )

    @Delete
    suspend fun deleteContact(
        contact: Contact
    )

    @Query(
        "SELECT * FROM Contact ORDER BY firstName ASC" // busca ordenada, estudar mais sobre SQL
    )// basicamente da pra fazer tudo com essa anotação mas aqui a gente vai ter que fazer SQL manual
    fun getContactOrderByFirstName(): Flow<List<Contact>> // Esse Flow poderia ser substituido por LiveData<>, mas basicamente ele faz com que a busca seja em tempo real, entao em vez de buscar uma vez so, toda vez que o dado é adicionado ele faz uma nova busca e atualiza a Lista

    //Nao usar suspend fun com Flow pq ele ja é assincorno
    @Query(
        "SELECT * FROM Contact ORDER BY lastName ASC"
    )
    fun getContactOrderByLastName(): Flow<List<Contact>>

    @Query(
        "SELECT * FROM Contact ORDER BY phoneNumber ASC"
    )
    fun getContactOrderByPhoneNumber(): Flow<List<Contact>>

}


@Database(
    entities = [Contact::class],
    version = 1
)
abstract class ContactDataBase() : RoomDatabase(){
    //abstract val dao: ContactDao
    abstract fun contactDao(): ContactDao // aqui é uuma funçao que retorna o dao, mais recomendavel do que criar uma variavel pq desse jeito é como a documentaçã ta falando rpa fazer

}