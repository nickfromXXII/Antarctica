@file:Suppress("SyntaxError")

package dao

import models.File
import org.hibernate.NonUniqueResultException
import utils.SessionFactoryUtil
import utils.UIUtil
import javax.persistence.NoResultException

class FileDao(private val userID: Int, private val password: String) : Dao<File>() {

    override val all: List<File>?
        get() = try {
            SessionFactoryUtil.getSessionFactory(userID, password)
                    ?.openSession()?.createQuery("from File")?.list() as List<File>
        } catch (e: Exception) {
            when (e) {
                is org.hibernate.HibernateException, is javax.persistence.PersistenceException -> {
                    UIUtil.showHibernateError(e.toString())
                } else -> { e.printStackTrace() }
            }
            null
        }

    override fun findById(id: Int): File? {
        return try {
            SessionFactoryUtil.getSessionFactory(userID, password)!!.openSession()?.get(File::class.java, id)
        } catch (e: Exception) {
            when (e) {
                is org.hibernate.HibernateException, is javax.persistence.PersistenceException -> {
                    UIUtil.showHibernateError(e.toString())
                } else -> { e.printStackTrace() }
            }
            null
        }
    }

    fun findByNamePath(name: String, path: String): File? {
        val session = SessionFactoryUtil.getSessionFactory(userID, password)!!.openSession()
        val criteriaQuery = session?.criteriaBuilder?.createQuery(File::class.java)
        val root = criteriaQuery?.from(File::class.java)
        criteriaQuery?.multiselect(root?.get<File>("id"), root?.get<File>("name"), root?.get<File>("path"), root?.get<File>("content"))
        criteriaQuery?.where(
                session.criteriaBuilder?.equal(root?.get<File>("name"), name),
                session.criteriaBuilder?.equal(root?.get<File>("path"), path)
        )

        var result: File? = null
        try {
            result = session?.createQuery(criteriaQuery)?.singleResult
        } catch (e: NonUniqueResultException) {
            e.printStackTrace()
        } catch (e: Exception) {
            when (e) {
                is org.hibernate.HibernateException, is javax.persistence.PersistenceException -> {
                    UIUtil.showHibernateError(e.toString())
                } else -> { e.printStackTrace() }
            }
        }
        catch (ignored: NoResultException) {
        } finally {
            session.close()
        }

        return result
    }

    override fun save(obj: File) {
        val session = SessionFactoryUtil.getSessionFactory(userID, password)!!.openSession()
        val tx = session?.beginTransaction()
        session?.save(obj)
        tx?.commit()
        session?.close()
    }

    override fun update(obj: File) {
        val session = SessionFactoryUtil.getSessionFactory(userID, password)!!.openSession()
        val tx = session?.beginTransaction()
        session?.update(obj)
        tx?.commit()
        session?.close()
    }

    override fun delete(obj: File) {
        val session = SessionFactoryUtil.getSessionFactory(userID, password)!!.openSession()
        val tx = session?.beginTransaction()
        session?.delete(obj)
        tx?.commit()
        session?.close()
    }

}
