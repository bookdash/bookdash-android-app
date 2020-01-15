package org.bookdash.android.data.database.firebase

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.bookdash.android.data.utils.firebase.FirebaseObservableListeners
import org.bookdash.android.domain.model.firebase.FireBookDetails
import org.bookdash.android.domain.model.firebase.FireContributor
import org.bookdash.android.domain.model.firebase.FireLanguage
import org.bookdash.android.domain.model.firebase.FireRole
import rx.Observable
import rx.functions.Func1
import java.util.*


class FirebaseBookDatabase(firebaseDatabase: FirebaseDatabase,
                           private val firebaseObservableListeners: FirebaseObservableListeners) : BookDatabase {
    private val booksTable: DatabaseReference = firebaseDatabase.getReference(FireBookDetails.TABLE_NAME)
    private val languagesTable: DatabaseReference = firebaseDatabase.getReference(FireLanguage.TABLE_NAME)
    private val contributorsTable: DatabaseReference = firebaseDatabase.getReference(FireContributor.TABLE_NAME)
    private val roleTable: DatabaseReference = firebaseDatabase.getReference(FireRole.TABLE_NAME)

    override fun getLanguages(): Observable<List<FireLanguage>> {
        return firebaseObservableListeners.listenToValueEvents(languagesTable, asLanguages())
    }

    override fun getBooks(): Observable<List<FireBookDetails>> {
        return firebaseObservableListeners
                .listenToValueEvents(booksTable.orderByChild(FireBookDetails.BOOK_COLUMN_CREATED_DATE), asBooks())
    }

    override fun getContributorById(contributorId: String): Observable<FireContributor> {
        return firebaseObservableListeners
                .listenToSingleValueEvents(contributorsTable.child(contributorId), asContributor())
    }

    override fun getRoleById(roleId: String): Observable<FireRole> {
        return firebaseObservableListeners.listenToSingleValueEvents(roleTable.child(roleId), asRole())
    }

    override fun getBooksByLanguage(fireLanguage: FireLanguage): Observable<List<FireBookDetails>> {
        return firebaseObservableListeners.listenToSingleValueEvents(
                booksTable.orderByChild(FireBookDetails.BOOK_LANGUAGE_FIELD).equalTo(fireLanguage.id), asBooks())
    }


    private fun asRole(): Func1<DataSnapshot, FireRole> {
        return Func1 { dataSnapshot -> dataSnapshot.getValue(FireRole::class.java) }
    }

    private fun asContributor(): Func1<DataSnapshot, FireContributor> {
        return Func1 { dataSnapshot ->
            val contributor = dataSnapshot.getValue(FireContributor::class.java)
            contributor?.setId(dataSnapshot.key)
            Log.d(TAG, "Contributor:" + contributor?.name + ", " + contributor?.avatar)
            val keys = ArrayList<String?>()
            if (dataSnapshot.child(FireContributor.ROLES_SECTION).hasChildren()) {
                val children = dataSnapshot.child(FireContributor.ROLES_SECTION).children
                children.mapTo(keys) { it.key }
            }
            contributor?.roleIds = keys
            contributor
        }
    }

    private fun asBooks(): Func1<DataSnapshot, List<FireBookDetails>> {
        return Func1 { dataSnapshot ->
            val fireBookDetails = ArrayList<FireBookDetails>()
            for (snap in dataSnapshot.children) {
                val bookDetails = snap.getValue(FireBookDetails::class.java)
                Log.d(TAG, "Book Details:" + bookDetails?.bookTitle + ". Book URL:" + bookDetails
                        ?.bookCoverPageUrl)
                bookDetails?.bookId = snap.key
                val keys = ArrayList<String?>()
                if (snap.child(FireBookDetails.CONTRIBUTORS_ITEM_NAME).hasChildren()) {
                    val children = snap.child(FireBookDetails.CONTRIBUTORS_ITEM_NAME)
                            .children
                    children.mapTo(keys) { it.key }
                }
                bookDetails?.setContributorsIndexedKeys(keys)
                fireBookDetails.add(bookDetails!!)

            }
            Collections.sort(fireBookDetails, FireBookDetails.COMPARATOR)
            fireBookDetails
        }
    }

    private fun asLanguages(): Func1<DataSnapshot, List<FireLanguage>> {
        return Func1 { dataSnapshot ->
            val fireLanguages = ArrayList<FireLanguage>()
            for (snap in dataSnapshot.children) {
                val language = snap.getValue(FireLanguage::class.java)
                language?.id = snap.key
                fireLanguages.add(language!!)
            }
            fireLanguages
        }
    }

    companion object {

        private const val TAG = "FirebaseBookDatabase"
    }

}
