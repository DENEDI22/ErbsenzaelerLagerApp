package com.example.team6mobileapp.network

import com.example.team6mobileapp.model.Artikel
import com.example.team6mobileapp.model.ArtikelCreateRequest
import com.example.team6mobileapp.model.ArtikelUpdateRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet

object DbClient {
    // Azure Postgres JDBC URL. Database name provided: postgres
    private const val DB_URL = "jdbc:postgresql://team6db.postgres.database.azure.com:5432/postgres"
    private const val DB_USER = "team6db"
    private const val DB_PASSWORD = "Start123!"

    private fun getConnection(): Connection {
        try {
            Class.forName("org.postgresql.Driver")
        } catch (e: ClassNotFoundException) {
            android.util.Log.e("DbClient", "Postgres driver not found", e)
        }
        
        android.util.Log.d("DbClient", "Connecting to $DB_URL")
        val props = java.util.Properties()
        props.setProperty("user", DB_USER)
        props.setProperty("password", DB_PASSWORD)
        props.setProperty("sslmode", "require")
        // registrationName must NOT be set to anything to avoid JMX on Android
        // Previous attempt props.setProperty("registrationName", "null") actually set it to string "null"
        
        // Android specific: disable GSSAPI which might also use java.lang.management indirectly or fail on Android
        props.setProperty("allowEncodingException", "true")
        props.setProperty("targetServerType", "any")
        
        return DriverManager.getConnection(DB_URL, props)
    }

    private fun mapArtikel(rs: ResultSet): Artikel = Artikel(
        nr = rs.getInt("nr"),
        name = rs.getString("name"),
        messeinheit = rs.getString("messeinheit"),
        preis = rs.getInt("preis"),
        menge = rs.getInt("menge")
    )

    suspend fun getAllArtikel(): List<Artikel> = withContext(Dispatchers.IO) {
        val list = mutableListOf<Artikel>()
        getConnection().use { conn ->
            conn.createStatement().use { st ->
                st.executeQuery("SELECT nr, name, messeinheit, preis, menge FROM artikel ORDER BY nr").use { rs ->
                    while (rs.next()) {
                        list.add(mapArtikel(rs))
                    }
                }
            }
        }
        list
    }

    suspend fun updateArtikel(nr: Int, req: ArtikelUpdateRequest): Artikel = withContext(Dispatchers.IO) {
        getConnection().use { conn ->
            conn.prepareStatement(
                "UPDATE artikel SET name = ?, messeinheit = ?, preis = ?, menge = ? WHERE nr = ? RETURNING nr, name, messeinheit, preis, menge"
            ).use { ps ->
                ps.setString(1, req.name)
                ps.setString(2, req.messeinheit)
                ps.setInt(3, req.preis)
                ps.setInt(4, req.menge)
                ps.setInt(5, nr)
                ps.executeQuery().use { rs ->
                    if (rs.next()) {
                        return@withContext mapArtikel(rs)
                    } else {
                        throw IllegalStateException("Artikel nicht gefunden")
                    }
                }
            }
        }
    }

    suspend fun createArtikel(req: ArtikelCreateRequest): Artikel = withContext(Dispatchers.IO) {
        getConnection().use { conn ->
            val sql = if (req.nr != null) {
                "INSERT INTO artikel (nr, name, messeinheit, preis, menge) OVERRIDING SYSTEM VALUE VALUES (?, ?, ?, ?, ?) RETURNING nr, name, messeinheit, preis, menge"
            } else {
                "INSERT INTO artikel (name, messeinheit, preis, menge) VALUES (?, ?, ?, ?) RETURNING nr, name, messeinheit, preis, menge"
            }
            conn.prepareStatement(sql).use { ps ->
                if (req.nr != null) {
                    ps.setInt(1, req.nr)
                    ps.setString(2, req.name)
                    ps.setString(3, req.messeinheit)
                    ps.setInt(4, req.preis)
                    ps.setInt(5, req.menge)
                } else {
                    ps.setString(1, req.name)
                    ps.setString(2, req.messeinheit)
                    ps.setInt(3, req.preis)
                    ps.setInt(4, req.menge)
                }
                ps.executeQuery().use { rs ->
                    if (rs.next()) {
                        return@withContext mapArtikel(rs)
                    } else {
                        throw IllegalStateException("Erstellung fehlgeschlagen")
                    }
                }
            }
        }
    }
}