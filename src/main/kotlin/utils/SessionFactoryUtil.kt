package utils

import models.File
import models.Package
import models.Repository
import org.hibernate.SessionFactory
import org.hibernate.boot.registry.StandardServiceRegistryBuilder
import org.hibernate.cfg.Configuration
import java.util.*

object SessionFactoryUtil {
    private var sessionFactory: SessionFactory? = null

    fun getSessionFactory(id: Int, password: String): SessionFactory? {
        if (sessionFactory == null) {
            try {
                val prop = Properties()
                prop.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/id$id")
                prop.setProperty("dialect", "org.hibernate.dialect.MariaDBDialect")
                prop.setProperty("hibernate.connection.username", "id$id")
                prop.setProperty("hibernate.connection.password", password)
                prop.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver")
                prop.setProperty("show_sql", true.toString())

                val configuration = Configuration().addProperties(prop)
                configuration.addAnnotatedClass(File::class.java)
                configuration.addAnnotatedClass(Package::class.java)
                configuration.addAnnotatedClass(Repository::class.java)
                val builder = StandardServiceRegistryBuilder().applySettings(configuration.properties)
                sessionFactory = configuration.buildSessionFactory(builder.build())
            } catch (e: Exception) {
                println("Exception! $e")
            }

        }
        return sessionFactory
    }
}